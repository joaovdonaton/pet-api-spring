package br.pucpr.petapi.rest.users;

import br.pucpr.petapi.lib.error.InvalidCredentialsException;
import br.pucpr.petapi.lib.error.InvalidUUIDException;
import br.pucpr.petapi.lib.error.ResourceAlreadyExistsException;
import br.pucpr.petapi.lib.error.ResourceDoesNotExistException;
import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfile;
import br.pucpr.petapi.rest.roles.RolesService;
import br.pucpr.petapi.lib.security.JWT;
import br.pucpr.petapi.rest.users.dto.UserCredentialsDTO;
import br.pucpr.petapi.rest.users.dto.UserInfoDTO;
import br.pucpr.petapi.rest.users.dto.UserRegisterDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public User findById(UUID id){
        return repository.findById(id).orElseThrow(() -> new InvalidUUIDException("User id [" + id + "] not found",
                HttpStatus.NOT_FOUND));
    }

    public User findByUsername(String username){
        return repository.findByUsername(username).orElseThrow(() -> new ResourceDoesNotExistException("Username ["+username+"] not found",
                HttpStatus.NOT_FOUND));
    }

    @Transactional
    public User createUser(UserRegisterDTO userRegisterDTO){
        if(repository.existsByUsername(userRegisterDTO.getUsername()))
            throw new ResourceAlreadyExistsException("Username ["+userRegisterDTO.getUsername()+"] is already in use.",
                    HttpStatus.BAD_REQUEST);

        User u = new User(userRegisterDTO.getUsername(),
                encoder.encode(userRegisterDTO.getPassword()),
                userRegisterDTO.getName());

        u.addRole(rolesService.findByName("USER"));

        return repository.save(u);
    }

    public String authenticate(UserCredentialsDTO userCredentialsDTO) {
        var u = repository.findByUsername(userCredentialsDTO.getUsername()).orElseThrow(
                () -> new InvalidCredentialsException("Invalid Credentials", HttpStatus.UNAUTHORIZED)
        );

        if(!encoder.matches(userCredentialsDTO.getPassword(), u.getPassword()))
            throw new InvalidCredentialsException("Invalid Credentials", HttpStatus.UNAUTHORIZED);

        return jwt.createToken(extractUserInfo(u));
    }

    public UserInfoDTO extractUserInfo(User u){
        return new UserInfoDTO(u.getId(), u.getName(), u.getUsername(),
                u.getRoleNames());
    }

    /**
     * @return o user atualmente autenticado
     */
    public User getCurrentAuth(){
        return findByUsername(
                ((UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUsername()
        );
    }

    /**
     * @return o perfil do usuário atualmente autenticado.
     * Útil para verificar caso o usuário atualmente logado tem um profile.
     */
    public AdoptionProfile getCurrentProfile(){
        var p = getCurrentAuth().getAdoptionProfile();

        if(p == null)
            throw new ResourceDoesNotExistException("User does not have an AdoptionProfile", HttpStatus.NOT_FOUND);

        return p;
    }
}
