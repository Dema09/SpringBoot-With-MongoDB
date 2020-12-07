package id.java.personal.project.service.impl;

import id.java.personal.project.constant.AppConstant;
import id.java.personal.project.dao.UserRepository;
import id.java.personal.project.domain.DummyUser;
import id.java.personal.project.dto.request.RegisterDTO;
import id.java.personal.project.dto.response.UserResponse;
import id.java.personal.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String insertUserData(RegisterDTO registerDTO) throws ParseException {
        DummyUser dummyUser = new DummyUser(registerDTO.getUsername(),
                passwordEncoder.encode(registerDTO.getPassword()),
                registerDTO.getAddress(),
                registerDTO.getPhoneNumber(),
                sdf.parse(registerDTO.getDateOfBirth()));


        userRepository.save(dummyUser);
        return AppConstant.SUCCESS_REGISTER_USER.getMessage() + registerDTO.getUsername();
    }

    @Override
    public UserResponse getUserDataByUserId(String userId, HttpServletResponse response) throws ParseException {
        DummyUser currentDummyUser = userRepository.findOne(userId);
        if (currentDummyUser == null)
            return null;

        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(currentDummyUser.getId());
        userResponse.setUsername(currentDummyUser.getUsername());
        userResponse.setAddress(currentDummyUser.getAddress());
        userResponse.setUserAge(calculateUserAge(currentDummyUser.getDateOfBirth()));

        return userResponse;
    }

    private Integer calculateUserAge(Date dateOfBirth) throws ParseException {
        LocalDate dateOfBirthInLocalDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        
        return Period.between(dateOfBirthInLocalDate, currentDate).getYears();
    }

}
