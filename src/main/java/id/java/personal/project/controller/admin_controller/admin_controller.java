package id.java.personal.project.controller.admin_controller;

import id.java.personal.project.constant.AppConstant;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.admin_service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity(null, HttpStatus.OK);
    }

    @GetMapping(value = "/getUserById/{userId}")
    private ResponseEntity getUserById(@PathVariable String userId) throws ParseException {
        StatusResponse userResponse = adminService.getUserDataByUserId(userId);

        if (userResponse == null) return new ResponseEntity(AppConstant.USER_DATA_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity(userResponse, userResponse.getResponse());
    }

}
