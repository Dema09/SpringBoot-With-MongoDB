package id.java.personal.project.controller.user_controller;

import id.java.personal.project.constant.AppConstant;
import id.java.personal.project.dto.request.ProfileDTO;
import id.java.personal.project.dto.request.RegisterDTO;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    private ResponseEntity registerUser(@RequestBody RegisterDTO registerDTO) throws ParseException {
        StatusResponse successRegisterUser = userService.insertUserData(registerDTO);
        return new ResponseEntity(successRegisterUser, successRegisterUser.getResponse());
    }


    @PostMapping(value = "/editUserProfile/{userId}")
    private ResponseEntity editUserProfile(@PathVariable String userId, @ModelAttribute ProfileDTO profileDTO) throws IOException {
        StatusResponse updateResponse = userService.updateUserDataByUserId(userId, profileDTO);
        return new ResponseEntity(updateResponse, updateResponse.getResponse());
    }

    @GetMapping(value = "/getUserProfile/{userId}")
    private ResponseEntity getUserProfile(@PathVariable String userId) throws IOException {
        StatusResponse profileResponse = userService.getUserDataProfileById(userId);
        return new ResponseEntity(profileResponse, profileResponse.getResponse());
    }
}
