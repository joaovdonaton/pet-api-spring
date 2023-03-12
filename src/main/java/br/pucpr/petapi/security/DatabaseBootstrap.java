package br.pucpr.petapi.security;

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
    private PasswordEncoder encoder;
    private Logger logger = LoggerFactory.getLogger(DatabaseBootstrap.class);

    public DatabaseBootstrap(RolesRepository rolesRepository, UsersRepository usersRepository, PasswordEncoder encoder) {
        this.rolesRepository = rolesRepository;
        this.usersRepository = usersRepository;
        this.encoder = encoder;
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
            logger.info("Test user '"+usernames.get(i)+"' already exists");
        }

        logger.info("Users successfully created!");
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading initial data...");
        createRoles();
        createUsers();

        logger.info("Initial data loading complete!");
    }
}