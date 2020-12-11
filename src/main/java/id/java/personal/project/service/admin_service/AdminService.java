package id.java.personal.project.service.admin_service;

import id.java.personal.project.dto.response.error.StatusResponse;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    StatusResponse getUserDataByUserId(String userId);

}
