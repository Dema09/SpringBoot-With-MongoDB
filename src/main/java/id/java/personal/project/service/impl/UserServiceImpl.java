package id.java.personal.project.service.impl;

import id.java.personal.project.constant.AppConstant;
import id.java.personal.project.dao.UserRepository;
import id.java.personal.project.domain.DummyUser;
import id.java.personal.project.dto.request.ProfileDTO;
import id.java.personal.project.dto.request.RegisterDTO;
import id.java.personal.project.dto.response.UserResponse;
import id.java.personal.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Environment env, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.env = env;
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
    public UserResponse getUserDataByUserId(String userId) throws ParseException {
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

    @Override
    public String updateUserDataByUserId(String userId, ProfileDTO profileDTO) throws IOException {
        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return "The User Data with Id: " + userId + " is Not Exists!";

        currentUser.setUsername(profileDTO.getUsername());
        currentUser.setAddress(profileDTO.getAddress());
        currentUser.setPassword(passwordEncoder.encode(profileDTO.getPassword()));
        currentUser.setProfilePicture(profileDTO.getProfilePicture().getOriginalFilename());
        convertProfilePicture(profileDTO.getProfilePicture().getBytes(), profileDTO.getProfilePicture());

        userRepository.save(currentUser);
        return AppConstant.SUCCESS_UPDATED_USER_DATA_PROFILE.getMessage() + currentUser.getId();

    }

    private void convertProfilePicture(byte[] data, MultipartFile profilePicture) throws IOException {
        File file = new File(env.getProperty("profilePicturePath") + profilePicture.getOriginalFilename());
        InputStream inputStream = profilePicture.getInputStream();
        int read = 0;

        if(!file.exists()) file.createNewFile();
        OutputStream outputStream = new FileOutputStream(file);
        while((read = inputStream.read(data)) != -1){
            outputStream.write(data,0,read);
        }
        profilePicture.transferTo(file);
        file.getAbsolutePath();

    }

    private Integer calculateUserAge(Date dateOfBirth) throws ParseException {
        LocalDate dateOfBirthInLocalDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        
        return Period.between(dateOfBirthInLocalDate, currentDate).getYears();
    }

}
