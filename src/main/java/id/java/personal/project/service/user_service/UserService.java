package id.java.personal.project.service.user_service;

import id.java.personal.project.dto.request.CloseFriendRequestDTO;
import id.java.personal.project.dto.request.LoginDTO;
import id.java.personal.project.dto.request.ProfileDTO;
import id.java.personal.project.dto.request.RegisterDTO;
import id.java.personal.project.dto.response.error.StatusResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

@Service
public interface UserService {
    StatusResponse insertUserData(RegisterDTO registerDTO) throws ParseException;

    StatusResponse updateUserDataByUserId(String userId, ProfileDTO profileDTO) throws IOException;

    StatusResponse getUserDataProfileById(String userId) throws IOException;

    StatusResponse loginUser(LoginDTO loginDTO);

    StatusResponse setProtectedAccountByUserId(String userId);

    StatusResponse unsetProtectedAccount(String userId);

    StatusResponse insertCloseFriend(String userId, CloseFriendRequestDTO closeFriendRequestDTO);
}
