package br.pucpr.petapi.matcher;

import br.pucpr.petapi.matcher.dto.MatchingResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adoption/matcher")
public class MatcherController {
    private final MatcherService service;

    public MatcherController(MatcherService service) {
        this.service = service;
    }

    @GetMapping("/")
    @RolesAllowed("USER")
    @SecurityRequirement(name = "auth")
    @Operation(
            summary = "get next matches for currently authenticated user",
            description = ""
    )
    @Tag(name = "Matcher")
    public List<MatchingResultDTO> nextMatch(@RequestParam(defaultValue = "1") Integer limit){
        return service.getNextMatches(limit);
    }
}
