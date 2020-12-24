package id.java.personal.project.controller.admin_controller;

import id.java.personal.project.dto.request.RegisterDTO;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.admin_service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/admin")
public class admin_controller {
    private final AdminService adminService;

    @Autowired
    public admin_controller(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/getAllUser")
    private ResponseEntity getAllUser(){
        StatusResponse statusResponse = adminService.getAllUser();
        return new ResponseEntity(statusResponse, statusResponse.getResponse());
    }

    @GetMapping(value = "/getUserById/{userId}")
    private ResponseEntity getUserById(@PathVariable String userId) throws ParseException {
        StatusResponse userResponse = adminService.getUserDataByUserId(userId);
        return new ResponseEntity(userResponse, userResponse.getResponse());
    }

    @PostMapping("/register")
    private ResponseEntity registerForAdmin(@RequestBody RegisterDTO registerDTO) throws ParseException {
        StatusResponse statusResponse = adminService.registerForAdmin(registerDTO);
        return new ResponseEntity(statusResponse, statusResponse.getResponse());
    }

}
