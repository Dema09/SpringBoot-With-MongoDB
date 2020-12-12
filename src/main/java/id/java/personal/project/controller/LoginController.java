package id.java.personal.project.controller;

import id.java.personal.project.dto.request.LoginDTO;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.user_service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    private ResponseEntity userLogin(@RequestBody LoginDTO loginDTO){
        StatusResponse loginResponse = userService.loginUser(loginDTO);
        return new ResponseEntity(loginResponse, loginResponse.getResponse());
    }

}
