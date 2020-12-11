package id.java.personal.project.service.user_service.impl;

import id.java.personal.project.constant.AppConstant;
import id.java.personal.project.constant.RoleEnum;
import id.java.personal.project.dao.UserRepository;
import id.java.personal.project.domain.DummyUser;
import id.java.personal.project.domain.DummyUserRole;
import id.java.personal.project.dto.request.LoginDTO;
import id.java.personal.project.dto.request.ProfileDTO;
import id.java.personal.project.dto.request.RegisterDTO;
import id.java.personal.project.dto.response.ProfileResponse;
import id.java.personal.project.dto.response.UserResponseWithAge;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;

    SimpleDateFormat sdf = new SimpleDateFormat(AppConstant.DATE_FORMAT.getMessage());

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           Environment env,
                           PasswordEncoder passwordEncoder
                           ) {
        this.userRepository = userRepository;
        this.env = env;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public StatusResponse insertUserData(RegisterDTO registerDTO) throws ParseException {
        StatusResponse statusResponse = new StatusResponse();
        DummyUserRole userRole = new DummyUserRole();
        userRole.setUserRole(RoleEnum.ROLE_USER);

        DummyUser dummyUser = new DummyUser(registerDTO.getUsername(),
                passwordEncoder.encode(registerDTO.getPassword()),
                registerDTO.getAddress(),
                registerDTO.getEmail(),
                registerDTO.getPhoneNumber(),
                sdf.parse(registerDTO.getDateOfBirth()),
                userRole);

        userRepository.save(dummyUser);
        return statusResponse.statusCreated(AppConstant.SUCCESS_REGISTER_USER.getMessage() + registerDTO.getUsername(), "Id: " + dummyUser.getId());
    }


    @Override
    public StatusResponse updateUserDataByUserId(String userId, ProfileDTO profileDTO) throws IOException {
        StatusResponse statusResponse = new StatusResponse();
        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound("The User Data with Id: " + userId + " is Not Exists!", null);

        currentUser.setUsername(profileDTO.getUsername());
        currentUser.setAddress(profileDTO.getAddress());
        currentUser.setPassword(passwordEncoder.encode(profileDTO.getPassword()));
        currentUser.setProfilePicture(profileDTO.getProfilePicture().getOriginalFilename());
        convertProfilePicture(profileDTO.getProfilePicture().getBytes(), profileDTO.getProfilePicture());

        userRepository.save(currentUser);
        return statusResponse.statusOk(AppConstant.SUCCESS_UPDATED_USER_DATA_PROFILE.getMessage() + currentUser.getId());

    }

    @Override
    public StatusResponse getUserDataProfileById(String userId) throws IOException {
        ProfileResponse profileResponse = new ProfileResponse();
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound("Data with id: " + userId + " is not exists!", null);

        profileResponse.setUserId(currentUser.getId());
        profileResponse.setUsername(currentUser.getUsername());
        profileResponse.setAddress(currentUser.getAddress());
        profileResponse.setUserProfilePicture(getImage(currentUser.getProfilePicture()));

        return statusResponse.statusOk(profileResponse);
    }

    @Override
    public StatusResponse loginUser(LoginDTO loginDTO) {
        StatusResponse statusResponse = new StatusResponse();

    }

    private String getImage(String profilePicture) throws IOException {
        File file = new File(env.getProperty("profilePicturePath") + profilePicture);
        if(file == null)
            throw new IOException(AppConstant.IMAGE_NOT_FOUND_OR_CORRUPTED.getMessage());

        byte[] profileImageByte = Files.readAllBytes(file.toPath().toAbsolutePath());
        String encodedImage = Base64.getEncoder().encodeToString(profileImageByte);
        return encodedImage;
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
}
