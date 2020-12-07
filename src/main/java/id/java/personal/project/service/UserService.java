package id.java.personal.project.service;

import id.java.personal.project.dto.request.ProfileDTO;
import id.java.personal.project.dto.request.RegisterDTO;
import id.java.personal.project.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

@Service
public interface UserService {
    String insertUserData(RegisterDTO registerDTO) throws ParseException;

    UserResponse getUserDataByUserId(String userId) throws ParseException;

    String updateUserDataByUserId(String userId, ProfileDTO profileDTO) throws IOException;
}
