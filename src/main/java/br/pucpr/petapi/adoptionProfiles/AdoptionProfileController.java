package br.pucpr.petapi.adoptionProfiles;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("adoption/profile")
public class AdoptionProfileController {
    private final AdoptionProfileService service;

    public AdoptionProfileController(AdoptionProfileService service) {
        this.service = service;
    }
//
//    @PostMapping("/")
//    public AdoptionProfile create(@RequestBody @Valid AdoptionProfile adoptionProfile){
//        return service.createAdoptionProfile();
//    }
}
