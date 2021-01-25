package id.java.personal.project.controller.user_controller;

import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.user_service.DiscoverPeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class DiscoverPeopleController {

    private final DiscoverPeopleService discoverPeopleService;

    @Autowired
    public DiscoverPeopleController(DiscoverPeopleService discoverPeopleService) {
        this.discoverPeopleService = discoverPeopleService;
    }

    @GetMapping("/discoverPeople")
    private ResponseEntity discoverPeopleBasedOnFollowerAndFollowing(@RequestHeader String userId){
        StatusResponse discoverPeopleResponse = discoverPeopleService.discoverPeopleBasedOnUserId(userId);
        return new ResponseEntity(discoverPeopleResponse, discoverPeopleResponse.getResponse());
    }
}
