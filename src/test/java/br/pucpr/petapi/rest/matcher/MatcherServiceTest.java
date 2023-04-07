package br.pucpr.petapi.rest.matcher;

import br.pucpr.petapi.TestDataLoader;
import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfile;
import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfileService;
import br.pucpr.petapi.rest.matcher.dto.MatchingResultDTO;
import br.pucpr.petapi.rest.petTypes.PetTypeService;
import br.pucpr.petapi.rest.pets.PetsService;
import br.pucpr.petapi.rest.users.User;
import br.pucpr.petapi.rest.users.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatcherServiceTest {
    private MatcherService matcherService;

    @Mock
    private UsersService usersService;

    @Mock
    private PetsService petsService;

    @Mock
    private AdoptionProfileService adoptionProfileService;

    @Mock
    private PetTypeService petTypeService;

    private final TestDataLoader testDataLoader = new TestDataLoader();

    @BeforeEach
    public void setup(){
        matcherService = new MatcherService(usersService, petsService, petTypeService, adoptionProfileService);
    }

    @Test
    public void shouldClearViewedHistory(){
        var profile = testDataLoader.getAdoptionProfiles().get(0);
        var initialIds = new HashSet<UUID>();
        initialIds.add(UUID.randomUUID());
        profile.setViewedPetIds(initialIds);

        when(usersService.getCurrentProfile()).thenReturn(profile);

        matcherService.clearViewedHistory();

        assertTrue(profile.getViewedPetIds().isEmpty());
    }

    @Test
    public void shouldReturnThreeOrderedMatches_IfUserThatPreferesDogs(){
        var profile = Mockito.mock(AdoptionProfile.class);
        when(profile.getPreferredPetTypes()).thenReturn(Set.of("dog"));
        when(usersService.getCurrentProfile()).thenReturn(profile);

        stubFindPetsSortByDistance(3);

        when(petTypeService.getAllPetTypeNames()).thenReturn(Set.of("dog", "cat"));

        var result = matcherService.getNextMatches(3);

        assertAll(
                () -> assertEquals(result.get(0).getName(), "Timmy"),
                () -> assertEquals(result.get(1).getName(), "Jimmy"),
                () -> assertEquals(result.get(2).getName(), "Limmy")
        );
    }

    @Test
    public void shouldReturnThreeOrderedMatches_IfUserThatHasNoPreference(){
        var profile = Mockito.mock(AdoptionProfile.class);
        when(profile.getPreferredPetTypes()).thenReturn(Set.of()); // sem preferencias
        when(usersService.getCurrentProfile()).thenReturn(profile);

        stubFindPetsSortByDistance(3);

        var result = matcherService.getNextMatches(3);

        assertAll(
                () -> assertEquals(result.get(0).getName(), "Jimmy"),
                () -> assertEquals(result.get(1).getName(), "Timmy"),
                () -> assertEquals(result.get(2).getName(), "Limmy")
        );
    }

    @Test
    public void shouldReturnEmptyList_IfUserHasViewedAllPossibleMatches(){
        var profile = Mockito.mock(AdoptionProfile.class);
        when(usersService.getCurrentProfile()).thenReturn(profile);

        when(petsService.findPetsSortByDistance(any(), anyInt(), anyBoolean())).thenReturn(
            List.of()
        );

        var result = matcherService.getNextMatches(3);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnSingleMatchBasedOnDistance_IfLimitIs1(){
        var profile = Mockito.mock(AdoptionProfile.class);
        when(usersService.getCurrentProfile()).thenReturn(profile);

        stubFindPetsSortByDistance(1);

        var result = matcherService.getNextMatches(1);

        assertEquals(result.size(), 1);
    }

    private void stubFindPetsSortByDistance(int limit){
        var findResult = testDataLoader.getPetsWithDistance().stream().limit(limit)
                .peek(pwd -> {
                    // preencher com dados aleatorios para evitar nullpointer
                    var u = new User("", "", "");
                    u.setAdoptionProfile(testDataLoader.getAdoptionProfiles().get(0));
                    pwd.setUser(u);
                }).toList();

        when(petsService.findPetsSortByDistance(any(), anyInt(), anyBoolean())).thenReturn(
                findResult
        );
    }
}
