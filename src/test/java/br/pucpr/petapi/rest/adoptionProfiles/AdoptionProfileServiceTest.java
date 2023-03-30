package br.pucpr.petapi.rest.adoptionProfiles;

import br.pucpr.petapi.lib.location.LocationUtils;
import br.pucpr.petapi.rest.users.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

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

    }
}
