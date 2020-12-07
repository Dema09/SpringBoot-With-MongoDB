package id.java.personal.project.controller;

import id.java.personal.project.constant.AppConstant;
import id.java.personal.project.dto.request.RegisterDTO;
import id.java.personal.project.dto.response.UserResponse;
import id.java.personal.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
        String successRegisterUser = userService.insertUserData(registerDTO);
        return new ResponseEntity(successRegisterUser, HttpStatus.CREATED);
    }

    @GetMapping(value = "/getUserById/{userId}")
    private ResponseEntity getUserById(@PathVariable String userId, HttpServletResponse response) throws ParseException{
        UserResponse userResponse = userService.getUserDataByUserId(userId, response);

        if (userResponse == null) return new ResponseEntity(AppConstant.USER_DATA_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity(userResponse, HttpStatus.OK);
    }

}
