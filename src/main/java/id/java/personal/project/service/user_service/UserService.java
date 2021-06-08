package id.java.personal.project.service.user_service;

import id.java.personal.project.dto.request.*;
import id.java.personal.project.dto.response.error.StatusResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

@Service
public interface UserService {
    StatusResponse insertUserData(RegisterDTO registerDTO) throws ParseException;

    StatusResponse updateUserDataByUserId(String userId, ProfileDTO profileDTO) throws IOException;

    StatusResponse getUserDataProfileById(String userId, String currentUserId) throws IOException;

    StatusResponse loginUser(LoginDTO loginDTO);

    StatusResponse setProtectedAccountByUserId(String userId);

    StatusResponse unsetProtectedAccount(String userId);

    StatusResponse insertCloseFriend(String userId, CloseFriendRequestDTO closeFriendRequestDTO);

    StatusResponse removeCloseFriendByUserId(String userId, CloseFriendRequestDTO closeFriendRequestDTO);

    StatusResponse changeUserPassword(String userId, ChangePasswordDTO changePasswordDTO);

    StatusResponse blockUserByUserId(String userId, String blockedUserId);

    StatusResponse unblockUserByUserId(String userId, String unblockUserId);
}
