package br.pucpr.petapi.lib.security;

import br.pucpr.petapi.lib.location.LocationUtils;
import br.pucpr.petapi.petTypes.PetType;
import br.pucpr.petapi.petTypes.PetTypeRepository;
import br.pucpr.petapi.roles.Role;
import br.pucpr.petapi.roles.RolesRepository;
import br.pucpr.petapi.users.User;
import br.pucpr.petapi.users.UsersRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DatabaseBootstrap implements CommandLineRunner {
    private RolesRepository rolesRepository;
    private UsersRepository usersRepository;
    private PetTypeRepository petTypeRepository;
    private PasswordEncoder encoder;
    private Logger logger = LoggerFactory.getLogger(DatabaseBootstrap.class);
    private LocationUtils locationUtils;

    public DatabaseBootstrap(RolesRepository rolesRepository, UsersRepository usersRepository, PetTypeRepository petTypeRepository, PasswordEncoder encoder, LocationUtils locationUtils) {
        this.rolesRepository = rolesRepository;
        this.usersRepository = usersRepository;
        this.petTypeRepository = petTypeRepository;
        this.encoder = encoder;
        this.locationUtils = locationUtils;
    }

    @Transactional
    private void createRoles(){
        logger.info("Creating Roles...");

        if(!rolesRepository.existsByName("USER")) rolesRepository.save(new Role("USER"));
        if(!rolesRepository.existsByName("ADMIN")) rolesRepository.save(new Role("ADMIN"));

        logger.info("Roles successfully created!");
    }

    @Transactional
    private void createUsers(){
        logger.info("Creating users...");


        Role USER_ROLE = rolesRepository.findByName("USER");
        Role ADMIN_ROLE = rolesRepository.findByName("ADMIN");

        var usernames = List.of(
                "georgie1984",
                "gustavo2012",
                "ricardo66",
                "fumantepassivo",
                "baratavoadora",
                "dragaorosa3");
        var passwords = List.of(
                "12345678",
                "12345678",
                "12345678",
                "12345678",
                "12345678",
                "12345678");
        var names = List.of(
                "George F. Richards",
                "Gustavo Pepe",
                "Ricardo da Costa",
                "Pedro Mendonça Arantes",
                "Emanuelly Rodrigues",
                "Layla de Souza");

        for(int i = 0; i < usernames.size(); i++){
            if(!usersRepository.existsByUsername(usernames.get(i))) {
                User u = new User(
                        usernames.get(i),
                        encoder.encode(passwords.get(i)),
                        names.get(i));
                u.addRole(USER_ROLE);

                usersRepository.save(u);
                logger.info("Test user '"+usernames.get(i)+"' created");
            }
            else {
                logger.info("Test user '" + usernames.get(i) + "' already exists");
            }
        }

        logger.info("Users successfully created!");
    }

    @Transactional
    private void createPetTypes() {
        logger.info("Creating pet types...");

        var types = List.of("dog", "cat");

        for(var type: types){
            if(!petTypeRepository.existsByName(type)) petTypeRepository.save(new PetType(type));
        }

        logger.info("Pet Types successfully created!");
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading initial data...");
        createRoles();
        createUsers();
        createPetTypes();

        logger.info("Initial data loading complete!");
    }
}