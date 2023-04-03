package br.pucpr.petapi.lib.database;

import br.pucpr.petapi.lib.error.MessageSettings;
import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfile;
import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfileRepository;
import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfileService;
import br.pucpr.petapi.lib.location.LocationUtils;
import br.pucpr.petapi.rest.adoptionRequests.AdoptionRequest;
import br.pucpr.petapi.rest.adoptionRequests.AdoptionRequestsRepository;
import br.pucpr.petapi.rest.adoptionRequests.enums.Status;
import br.pucpr.petapi.rest.petTypes.PetType;
import br.pucpr.petapi.rest.petTypes.PetTypeRepository;
import br.pucpr.petapi.rest.petTypes.PetTypeService;
import br.pucpr.petapi.rest.pets.Pet;
import br.pucpr.petapi.rest.pets.PetsRepository;
import br.pucpr.petapi.rest.pets.PetsService;
import br.pucpr.petapi.rest.roles.Role;
import br.pucpr.petapi.rest.roles.RolesRepository;
import br.pucpr.petapi.rest.users.User;
import br.pucpr.petapi.rest.users.UsersRepository;
import br.pucpr.petapi.rest.users.UsersService;
import br.pucpr.petapi.rest.users.dto.UserCredentialsDTO;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DatabaseBootstrap implements CommandLineRunner {
    private final RolesRepository rolesRepository;
    private final UsersRepository usersRepository;
    private final PetTypeRepository petTypeRepository;
    private final PasswordEncoder encoder;
    private final Logger logger = LoggerFactory.getLogger(DatabaseBootstrap.class);
    private final LocationUtils locationUtils;
    private final UsersService usersService;
    private final PetsRepository petsRepository;
    private final BootstrapSettings settings;
    private final PetTypeService petTypeService;
    private final PetsService petsService;
    private final AdoptionProfileRepository adoptionProfileRepository;
    private final AdoptionProfileService adoptionProfileService;
    private final AdoptionRequestsRepository adoptionRequestsRepository;
    private final MessageSettings messageSettings;

    public DatabaseBootstrap(RolesRepository rolesRepository, UsersRepository usersRepository, PetTypeRepository petTypeRepository, PasswordEncoder encoder, LocationUtils locationUtils, UsersService usersService, PetsRepository petsRepository, BootstrapSettings settings, PetTypeService petTypeService, PetsService petsService, AdoptionProfileRepository adoptionProfileRepository, AdoptionProfileService adoptionProfileService, AdoptionRequestsRepository adoptionRequestsRepository, MessageSettings messageSettings) {
        this.rolesRepository = rolesRepository;
        this.usersRepository = usersRepository;
        this.petTypeRepository = petTypeRepository;
        this.encoder = encoder;
        this.locationUtils = locationUtils;
        this.usersService = usersService;
        this.petsRepository = petsRepository;
        this.settings = settings;
        this.petTypeService = petTypeService;
        this.petsService = petsService;
        this.adoptionProfileRepository = adoptionProfileRepository;
        this.adoptionProfileService = adoptionProfileService;
        this.adoptionRequestsRepository = adoptionRequestsRepository;
        this.messageSettings = messageSettings;
    }

    @Transactional
    private void createRoles(){
        logger.info("Creating Roles...");

        for(String roleName: settings.getRoles()){
            if(!rolesRepository.existsByName(roleName)) rolesRepository.save(new Role(roleName));
        }

        logger.info("Roles successfully created!");
    }

    @Transactional
    private void createUsers(){
        logger.info("Creating users...");


        Role USER_ROLE = rolesRepository.findByName("USER");

        var usernames = settings.getUsernames();
        var password = settings.getDefaultPassword();
        var names = settings.getNames();

        for(int i = 0; i < usernames.size(); i++){
            if(!usersRepository.existsByUsername(usernames.get(i))) {
                User u = new User(
                        usernames.get(i),
                        encoder.encode(password),
                        names.get(i));
                u.addRole(USER_ROLE);

                usersRepository.save(u);
                logger.info("Test user '"+usernames.get(i)+"' created");
            }
            else {
                logger.info("Test user '" + usernames.get(i) + "' already exists");
            }
            logger.info("Token: " + usersService.authenticate(new UserCredentialsDTO(usernames.get(i), password)));
        }

        logger.info("Users successfully created!");
    }

    @Transactional
    private void createPetTypes() {
        logger.info("Creating pet types...");

        for(var type: settings.getPetTypes()){
            if(!petTypeRepository.existsByName(type)) petTypeRepository.save(new PetType(type));
        }

        logger.info("Pet Types successfully created!");
    }

    @Transactional
    private void createPets(){
        logger.info("Creating pets...");

        var petNames = settings.getPetNames();
        var petNicknames = settings.getPetNicknames();
        var petAges = settings.getPetAges();
        var ownerUsernames = settings.getOwnerUsernames();
        var petTypeNames = settings.getPetAnimalTypes();
        var defaultDescription = settings.getPetDefaultDescription();

        for(int i = 0; i < petNames.size(); i++){
            if(petsService.existsByName(petNames.get(i))) {
                logger.info("Test pet named " + petNames.get(i) + " already exists");
                continue;
            }
            var u = usersService.findByUsername(ownerUsernames.get(i));
            var pet = new Pet(petNames.get(i), petNicknames.get(i), petAges.get(i), defaultDescription, u
                    , LocalDateTime.now(),
                    petTypeService.findPetTypeByName(petTypeNames.get(i)));

            pet = petsRepository.save(pet);

            u.addPet(pet);

        }

        logger.info("Pets sucessfully created");
    }

    @Transactional
    private void createProfiles(){
        logger.info("Creating adoption profiles...");

        var users = settings.getProfileUsers();
        var newPetOwners = settings.getProfileNewPetOwner();
        var ceps = settings.getProfileCeps();
        var description = settings.getProfileDefaultDescription();
        var preferredTypes = settings.getProfileDefaultPreferredTypes();

        for (int i = 0; i < users.size(); i++){
            if(usersService.findByUsername(users.get(i)).getAdoptionProfile() != null){
                logger.info("Adoption profile for " + users.get(i) + " already exists");
                continue;
            }
            var profile = new AdoptionProfile(
                    usersService.findByUsername(users.get(i)),
                    ceps.get(i),
                    description,
                    Boolean.parseBoolean(newPetOwners.get(i)),
                    Set.of(),
                    new HashSet<>(preferredTypes)
            );
            profile = adoptionProfileService.completeLocationData(profile, ceps.get(i));

            adoptionProfileRepository.save(profile);
        }

        logger.info("Adoption profiles successfully created.");
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading initial data...");
        createRoles();
        createUsers();
        createPetTypes();
        createPets();
        createProfiles();

        AdoptionRequest ar = new AdoptionRequest(
                usersService.findByUsername("ricardo66").getPets().stream().findFirst().get(),
                usersService.findByUsername("ricardo66"),
                usersService.findByUsername("baratavoadora"),
                "I like your dog",
                "I really like that cute liitle doggy of yours, and I want it",
                Status.PENDING
        );

        adoptionRequestsRepository.save(ar);

        logger.info("Initial data loading complete!");
    }
}
