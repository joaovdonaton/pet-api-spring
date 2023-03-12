package br.pucpr.petapi.users;

import br.pucpr.petapi.users.dto.TokenDTO;
import br.pucpr.petapi.users.dto.UserCredentialsDTO;
import br.pucpr.petapi.users.dto.UserInfoDTO;
import br.pucpr.petapi.users.dto.UserRegisterDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
    private UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @Operation(summary = "create new user", tags = {"Auth"})
    @PostMapping("/")
    public UserInfoDTO createUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO){
        return service.extractUserInfo(service.createUser(userRegisterDTO));
    }

    @Operation(summary = "login and generate jwt token", tags = {"Auth"})
    @PostMapping("/login")
    public TokenDTO createUser(@RequestBody @Valid UserCredentialsDTO userCredentialsDTO){
        return new TokenDTO(service.authenticate(userCredentialsDTO));
    }
}
