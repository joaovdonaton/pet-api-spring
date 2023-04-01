package br.pucpr.petapi.rest.AdoptionRequests;

import br.pucpr.petapi.TestDataLoader;
import br.pucpr.petapi.lib.error.BadRequest;
import br.pucpr.petapi.lib.error.ResourceAlreadyExistsException;
import br.pucpr.petapi.rest.adoptionRequests.AdoptionRequest;
import br.pucpr.petapi.rest.adoptionRequests.AdoptionRequestsRepository;
import br.pucpr.petapi.rest.adoptionRequests.AdoptionRequestsService;
import br.pucpr.petapi.rest.adoptionRequests.dto.AdoptionRequestRegister;
import br.pucpr.petapi.rest.pets.Pet;
import br.pucpr.petapi.rest.pets.PetsService;
import br.pucpr.petapi.rest.users.User;
import br.pucpr.petapi.rest.users.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdoptionRequestsServiceTest {
    private AdoptionRequestsService service;
    @Mock
    private AdoptionRequestsRepository adoptionRequestsRepository;
    @Mock
    private PetsService petsService;
    @Mock
    private UsersService usersService;
    private final TestDataLoader testDataLoader = new TestDataLoader();

    @BeforeEach
    public void setup(){
        service = new AdoptionRequestsService(adoptionRequestsRepository, petsService, usersService);
    }

    @Test
    public void shouldThrowResourceAlreadyExists_IfUserAlreadyHasRequestForPet(){
        UUID senderId = UUID.randomUUID();

        var user = Mockito.mock(User.class);
//        user.setId(senderId); acho que não da pra setar os fields de um objeto mock
        when(user.getId()).thenReturn(senderId);
        when(usersService.getCurrentAuth()).thenReturn(user);

        var pet = Mockito.mock(Pet.class);
        when(petsService.findById(any())).thenReturn(pet);

        var existingRequest = new AdoptionRequest();
        existingRequest.setUserSender(user);
        when(pet.getAdoptionRequests()).thenReturn(Set.of(existingRequest));

        var userReceiver = Mockito.mock(User.class);
        when(pet.getUser()).thenReturn(userReceiver);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            service.createAdoptionRequest(
                new AdoptionRequestRegister(pet.getId(),
                        "A".repeat(20),
                        "A".repeat(75)));
        });
    }

    @Test
    public void shouldThrowBadRequest_IfUserIsBothSenderAndReceiver(){
        UUID id = UUID.randomUUID();

        var user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(id);
        when(usersService.getCurrentAuth()).thenReturn(user);

        var pet = Mockito.mock(Pet.class);
        when(petsService.findById(any())).thenReturn(pet);

        var userReceiver = Mockito.mock(User.class);
        when(userReceiver.getId()).thenReturn(id);
        when(pet.getUser()).thenReturn(userReceiver);

        assertThrows(BadRequest.class, () ->
            service.createAdoptionRequest(new AdoptionRequestRegister(
                    pet.getId(),
                    "A".repeat(20),
                    "A".repeat(75))
            )
        );
    }
}
