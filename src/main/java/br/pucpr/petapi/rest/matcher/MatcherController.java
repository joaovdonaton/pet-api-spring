package br.pucpr.petapi.rest.matcher;

import br.pucpr.petapi.rest.matcher.dto.MatchingResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
            description = "Returns an array of matches for the user. The first priority for sorting is distance," +
                    "then sorting is done by type preference. Pets that have already been viewed are excluded from the" +
                    "matcher. Every pet returned by the Matcher is added to a list of already viewed pets, and will" +
                    "not show up again"
    )
    @Tag(name = "Matcher")
    public List<MatchingResultDTO> nextMatch(@RequestParam(defaultValue = "1") Integer limit){
        return service.getNextMatches(limit);
    }

    @DeleteMapping("/")
    @RolesAllowed("USER")
    @SecurityRequirement(name = "auth")
    @Operation(
            summary = "clears the currently authenticated user's matcher viewed history"
    )
    @Tag(name = "Matcher")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearHistory(){
        service.clearViewedHistory();
    }
}
