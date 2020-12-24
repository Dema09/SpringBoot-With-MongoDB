package id.java.personal.project.service.admin_service;

import id.java.personal.project.dto.request.RegisterDTO;
import id.java.personal.project.dto.response.error.StatusResponse;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface AdminService {

    StatusResponse getUserDataByUserId(String userId);

    StatusResponse getAllUser();

    StatusResponse registerForAdmin(RegisterDTO registerDTO) throws ParseException;
}
