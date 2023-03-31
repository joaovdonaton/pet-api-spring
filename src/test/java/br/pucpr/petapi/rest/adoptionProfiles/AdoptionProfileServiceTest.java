package br.pucpr.petapi.rest.adoptionProfiles;

import br.pucpr.petapi.lib.database.TestDataSettings;
import br.pucpr.petapi.lib.error.ResourceDoesNotExistException;
import br.pucpr.petapi.lib.location.LocationUtils;
import br.pucpr.petapi.lib.location.dto.response.CEPDataResponse;
import br.pucpr.petapi.lib.location.dto.response.geocoding.CoordinatesDTO;
import br.pucpr.petapi.rest.users.User;
import br.pucpr.petapi.rest.users.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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

    @BeforeEach
    public void setup(){
        service = new AdoptionProfileService(usersService, locationUtils, adoptionProfileRepository);
    }

    @Test
    public void shouldCompleteProfileLocationData(){
        AdoptionProfile ap = new AdoptionProfile();

        when(locationUtils.getCEPData(any())).thenReturn(new CEPDataResponse(
                "80250-220",
                "Avenida Visconde de Guarapuava",
                "de 3337/3338 a 4379/4380",
                "Centro",
                "Curitiba",
                "PR",
                "4106902",
                "",
                "41",
                "7535"
        ));

        when(locationUtils.getCoordinates(any())).thenReturn(new CoordinatesDTO(
                "-25.437242700000",
                "-49.269966500000"
        ));

        AdoptionProfile expectedAp = new AdoptionProfile(null,
                "80250-220",
                null,
                false,
                "PR",
                "Curitiba",
                "Centro",
                new BigDecimal("-25.437242700000"),
                new BigDecimal("-49.269966500000"),
                null,
                null
                );

         AdoptionProfile result = service.completeLocationData(ap, "80250-220");

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
}
