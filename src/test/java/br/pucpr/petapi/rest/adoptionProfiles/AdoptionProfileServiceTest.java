package br.pucpr.petapi.rest.adoptionProfiles;

import br.pucpr.petapi.TestDataLoader;
import br.pucpr.petapi.lib.error.exceptions.ResourceDoesNotExistException;
import br.pucpr.petapi.lib.location.LocationUtils;
import br.pucpr.petapi.rest.adoptionProfiles.dto.AdoptionProfileUpdateDTO;
import br.pucpr.petapi.rest.users.User;
import br.pucpr.petapi.rest.users.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AdoptionProfileServiceTest {
    private AdoptionProfileService service;
    @Mock
    private UsersService usersService;
    @Mock
    private LocationUtils locationUtils;
    @Mock
    private AdoptionProfileRepository adoptionProfileRepository;
    private final TestDataLoader testDataLoader = new TestDataLoader();

    @BeforeEach
    public void setup(){
        service = new AdoptionProfileService(usersService, locationUtils, adoptionProfileRepository);
    }

    @Test
    public void shouldCompleteProfileLocationData(){
        AdoptionProfile ap = new AdoptionProfile();

        var cepData = testDataLoader.getCepDataResponses().get(0);
        when(locationUtils.getCEPData(any())).thenReturn(cepData);

        var coordinateData = testDataLoader.getCoordinatesDTOs().get(0);
        when(locationUtils.getCoordinates(any())).thenReturn(coordinateData);

        AdoptionProfile expectedAp = new AdoptionProfile(null,
                cepData.getCep(),
                null,
                false,
                cepData.getUf(),
                cepData.getLocalidade(),
                cepData.getBairro(),
                new BigDecimal(coordinateData.getLat()),
                new BigDecimal(coordinateData.getLng()),
                null,
                null
                );

         AdoptionProfile result = service.completeLocationData(ap, cepData.getCep());

         assertAll(
                 () -> assertEquals(expectedAp.getCep(), result.getCep()),
                 () -> assertEquals(expectedAp.getState(), result.getState()),
                 () -> assertEquals(expectedAp.getCity(), result.getCity()),
                 () -> assertEquals(expectedAp.getDistrict(), result.getDistrict()),
                 () -> assertEquals(expectedAp.getLatitude(), result.getLatitude()),
                 () -> assertEquals(expectedAp.getLongitude(), result.getLongitude())
         );
    }

    @Test
    public void shouldThrowResourceDoesNotExistException_IfUserExistsButDoesntHaveAProfile(){
        var user = mock(User.class);

        when(usersService.findByUsername(any())).thenReturn(user);
        when(user.getAdoptionProfile()).thenReturn(null);

        assertThrows(ResourceDoesNotExistException.class, () -> service.findAdoptionProfileByUsername("ricardo66"));
    }

    @Test
    public void shouldUpdateProfileLocationData_IfCepHasBeenUpdated(){
        //retornar perfil de adoção antigo
        var user = mock(User.class);
        when(usersService.getCurrentAuth()).thenReturn(user);

        AdoptionProfile oldProfile = testDataLoader.getAdoptionProfiles().get(0);
        when(user.getAdoptionProfile()).thenReturn(oldProfile);

        // chamada do método para atualizar dados de licalização
        var newCepData = testDataLoader.getCepDataResponses().get(1);
        var newCoordinateData = testDataLoader.getCoordinatesDTOs().get(1);

        when(locationUtils.getCEPData(any())).thenReturn(newCepData);
        when(locationUtils.getCoordinates(any())).thenReturn(newCoordinateData);

        service.updateAdoptionProfile(new AdoptionProfileUpdateDTO(newCepData.getCep(),
                null,
                oldProfile.isNewPetOwner(),
                null));

        //validar se os dados foram atualizados
        assertAll(
                () -> assertEquals(newCepData.getCep(), oldProfile.getCep()),
                () -> assertEquals(newCepData.getUf(), oldProfile.getState()),
                () -> assertEquals(newCepData.getLocalidade(), oldProfile.getCity()),
                () -> assertEquals(newCepData.getBairro(), oldProfile.getDistrict()),
                () -> assertEquals(new BigDecimal(newCoordinateData.getLat()), oldProfile.getLatitude()),
                () -> assertEquals(new BigDecimal(newCoordinateData.getLng()), oldProfile.getLongitude())
        );
    }
}
