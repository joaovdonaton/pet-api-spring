package br.pucpr.petapi.users;

import br.pucpr.petapi.error.InvalidCredentialsException;
import br.pucpr.petapi.error.UsernameAlreadyExistsException;
import br.pucpr.petapi.roles.RolesService;
import br.pucpr.petapi.security.JWT;
import br.pucpr.petapi.users.dto.UserCredentialsDTO;
import br.pucpr.petapi.users.dto.UserInfoDTO;
import br.pucpr.petapi.users.dto.UserRegisterDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private UsersRepository repository;
    private PasswordEncoder encoder;
    private final RolesService rolesService;
    private JWT jwt;

    public UsersService(UsersRepository repository, PasswordEncoder encoder,
                        RolesService rolesService, JWT jwt) {
        this.repository = repository;
        this.encoder = encoder;
        this.rolesService = rolesService;
        this.jwt = jwt;
    }

    @Transactional
    public User createUser(UserRegisterDTO userRegisterDTO){
        if(repository.existsByUsername(userRegisterDTO.getUsername()))
            throw new UsernameAlreadyExistsException("Username ["+userRegisterDTO.getUsername()+"] is already in use.");

        User u = new User(userRegisterDTO.getUsername(),
                encoder.encode(userRegisterDTO.getPassword()),
                userRegisterDTO.getName());

        u.addRole(rolesService.findByName("USER"));

        return repository.save(u);
    }

    public String authenticate(UserCredentialsDTO userCredentialsDTO) {
        var u = repository.findByUsername(userCredentialsDTO.getUsername()).orElseThrow(InvalidCredentialsException::new);

        if(!encoder.matches(userCredentialsDTO.getPassword(), u.getPassword()))
            throw new InvalidCredentialsException();

        return jwt.createToken(extractUserInfo(u));
    }

    public UserInfoDTO extractUserInfo(User u){
        return new UserInfoDTO(u.getId(), u.getName(), u.getUsername(),
                u.getRoleNames());
    }
}
