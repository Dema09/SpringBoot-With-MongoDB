package id.java.personal.project.service;

import id.java.personal.project.dto.request.RegisterDTO;
import id.java.personal.project.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

@Service
public interface UserService {
    String insertUserData(RegisterDTO registerDTO) throws ParseException;

    UserResponse getUserDataByUserId(String userId, HttpServletResponse response) throws ParseException;
}
