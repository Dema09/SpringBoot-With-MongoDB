package id.java.personal.project.controller.user_controller;

import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.user_service.FollowerAndFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class FollowerAndFollowingController {

    private final FollowerAndFollowingService followerAndFollowingService;

    @Autowired
    public FollowerAndFollowingController(FollowerAndFollowingService followerAndFollowingService) {
        this.followerAndFollowingService = followerAndFollowingService;
    }

    @PostMapping("/followingUser/{username}")
    private ResponseEntity followingUserByUsername(@PathVariable String username, @RequestHeader(value = "userId") String userId){
        StatusResponse followingUserResponse = followerAndFollowingService.followingUserByUsername(username, userId);
        return new ResponseEntity(followingUserResponse, followingUserResponse.getResponse());
    }

    @PostMapping("/unfollowingUser/{username}")
    private ResponseEntity unfollowingUserByUsername(@PathVariable String username, @RequestHeader(value = "userId") String userId){
        StatusResponse unfollowingUserResponse = followerAndFollowingService.unfollowingUserByUsername(username, userId);
        return new ResponseEntity(unfollowingUserResponse, unfollowingUserResponse.getResponse());
    }

    @GetMapping("/{username}/followers")
    private ResponseEntity getAllFollowers(@PathVariable String username){
        StatusResponse getFollowerResponse = followerAndFollowingService.getAllFollowersByUsername(username);
        return new ResponseEntity(getFollowerResponse, getFollowerResponse.getResponse());
    }

}
