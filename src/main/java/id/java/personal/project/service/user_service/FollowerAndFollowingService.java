package id.java.personal.project.service.user_service;

import id.java.personal.project.dto.response.error.StatusResponse;
import org.springframework.stereotype.Service;

@Service
public interface FollowerAndFollowingService {

    StatusResponse followingUserByUsername(String username, String currentUserId);

    StatusResponse unfollowingUserByUsername(String username, String currentUserId);

    StatusResponse getAllFollowersByUsername(String username);

    StatusResponse getAllFollowingsByUsername(String username);
}
