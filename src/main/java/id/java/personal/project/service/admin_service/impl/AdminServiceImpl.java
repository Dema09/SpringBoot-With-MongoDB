package id.java.personal.project.service.admin_service.impl;

import id.java.personal.project.dao.UserRepository;
import id.java.personal.project.domain.DummyUser;
import id.java.personal.project.dto.response.UserResponseWithAge;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.admin_service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public StatusResponse getUserDataByUserId(String userId) {
        DummyUser currentDummyUser = userRepository.findOne(userId);
        StatusResponse statusResponse = new StatusResponse();
        if (currentDummyUser == null)
            return null;

        UserResponseWithAge userResponseWithAge = new UserResponseWithAge();
        userResponseWithAge.setUserId(currentDummyUser.getId());
        userResponseWithAge.setUsername(currentDummyUser.getUsername());
        userResponseWithAge.setAddress(currentDummyUser.getAddress());
        userResponseWithAge.setUserRole(currentDummyUser.getDummyUserRole().getUserRole());
        userResponseWithAge.setUserAge(calculateUserAge(currentDummyUser.getDateOfBirth()));

        return statusResponse.statusOk(userResponseWithAge);
    }

    private Integer calculateUserAge(Date dateOfBirth) {
        LocalDate dateOfBirthInLocalDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        return Period.between(dateOfBirthInLocalDate, currentDate).getYears();
    }

}
