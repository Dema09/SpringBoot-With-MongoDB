package id.java.personal.project.controller.user_controller;

import id.java.personal.project.dto.request.*;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/setProtectedAccount")
    private ResponseEntity setProtectedAccount(@RequestHeader (value = "userId") String userId){
        StatusResponse setProtectedAccountResponse = userService.setProtectedAccountByUserId(userId);
        return new ResponseEntity(setProtectedAccountResponse, setProtectedAccountResponse.getResponse());
    }

    @PostMapping("/unsetProtectedAccount")
    private ResponseEntity unsetProtectedAccount(@RequestHeader (value = "userId") String userId){
        StatusResponse unsetProtectedAccountResponse = userService.unsetProtectedAccount(userId);
        return new ResponseEntity(unsetProtectedAccountResponse, unsetProtectedAccountResponse.getResponse());
    }

    @PostMapping("/insertCloseFriend")
    private ResponseEntity insertCloseFriend(@RequestHeader (value = "userId") String userId, @RequestBody CloseFriendRequestDTO closeFriendRequestDTO){
        StatusResponse insertCloseFriendResponse = userService.insertCloseFriend(userId, closeFriendRequestDTO);
        return new ResponseEntity(insertCloseFriendResponse, insertCloseFriendResponse.getResponse());
    }

    @PostMapping("/removeCloseFriend")
    private ResponseEntity removeCloseFriend(@RequestHeader (value = "userId") String userId, @RequestBody CloseFriendRequestDTO closeFriendRequestDTO){
        StatusResponse removeCloseFriendResponse = userService.removeCloseFriendByUserId(userId, closeFriendRequestDTO);
        return new ResponseEntity(removeCloseFriendResponse, removeCloseFriendResponse.getResponse());
    }

    @PutMapping("/changePassword")
    private ResponseEntity changeUserPassword(@RequestHeader (value = "userId") String userId, @RequestBody ChangePasswordDTO changePasswordDTO){
        StatusResponse changePasswordResponse = userService.changeUserPassword(userId, changePasswordDTO);
        return new ResponseEntity(changePasswordResponse, changePasswordResponse.getResponse());
    }

    @PostMapping("/blockUser/{blockedUserId}")
    private ResponseEntity blockUserByUserId(@RequestHeader (value = "userId") String userId, @PathVariable String blockedUserId){
        StatusResponse blockUserResponse = userService.blockUserByUserId(userId, blockedUserId);
        return new ResponseEntity(blockUserResponse, blockUserResponse.getResponse());
    }

    @PostMapping("/unblockUser/{unblockUserId}")
    private ResponseEntity unblockUserByUserId(@RequestHeader (value = "userId") String userId, @PathVariable String unblockUserId){
        StatusResponse unblockUserResponse = userService.unblockUserByUserId(userId, unblockUserId);
        return new ResponseEntity(unblockUserResponse, unblockUserResponse.getResponse());
    }
}
