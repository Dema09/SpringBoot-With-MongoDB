package id.java.personal.project.service.user_service.impl;

import id.java.personal.project.constant.AppEnum;
import id.java.personal.project.constant.RoleEnum;
import id.java.personal.project.dao.RoleRepository;
import id.java.personal.project.dao.UserRepository;
import id.java.personal.project.domain.DummyUser;
import id.java.personal.project.domain.DummyUserRole;
import id.java.personal.project.dto.request.LoginDTO;
import id.java.personal.project.dto.request.ProfileDTO;
import id.java.personal.project.dto.request.RegisterDTO;
import id.java.personal.project.dto.response.LoginResponse;
import id.java.personal.project.dto.response.ProfileResponse;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.security.jwt.JwtUtils;
import id.java.personal.project.security.service.UserDetailsImpl;
import id.java.personal.project.service.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    SimpleDateFormat sdf = new SimpleDateFormat(AppEnum.DATE_FORMAT.getMessage());

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           Environment env,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           RoleRepository roleRepository,
                           JwtUtils jwtUtils
                           ) {
        this.userRepository = userRepository;
        this.env = env;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public StatusResponse insertUserData(RegisterDTO registerDTO) throws ParseException {
        StatusResponse statusResponse = new StatusResponse();
        DummyUserRole userRole = roleRepository.findDummyUserRoleByUserRole(RoleEnum.ROLE_USER.getMessage());
        DummyUser checkUser = userRepository.findDummyUserByUsername(registerDTO.getUsername());

        if(checkUser != null)
            return statusResponse.statusConflict(AppEnum.THIS_USER_WITH_USERNAME.getMessage() + registerDTO.getUsername() + AppEnum.HAS_BEEN_EXISTS.getMessage(), null);

        DummyUser dummyUser = new DummyUser(registerDTO.getUsername(),
                passwordEncoder.encode(registerDTO.getPassword()),
                registerDTO.getAddress(),
                registerDTO.getEmail(),
                registerDTO.getPhoneNumber(),
                sdf.parse(registerDTO.getDateOfBirth()),
                userRole);

        userRepository.save(dummyUser);
        return statusResponse.statusCreated(AppEnum.SUCCESS_REGISTER_USER.getMessage(), "Id: " + dummyUser.getId());
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
        return statusResponse.statusOk(AppEnum.SUCCESS_UPDATED_USER_DATA_PROFILE.getMessage() + currentUser.getId());

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
        DummyUser userData = userRepository.findDummyUserByUsername(loginDTO.getUsername());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userData.getDummyUserRole().getUserRole()));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword(), authorities));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setId(userDetails.getId());
        loginResponse.setUsername(userDetails.getUsername());
        loginResponse.setEmail(userDetails.getEmail());
        loginResponse.setRoles(userData.getDummyUserRole().getUserRole());
        loginResponse.setAccessToken(jwt);

        return statusResponse.statusOk(loginResponse);

    }

    private String getImage(String profilePicture) throws IOException {
        File file = new File(env.getProperty("profilePicturePath") + profilePicture);
        if(file == null)
            throw new IOException(AppEnum.IMAGE_NOT_FOUND_OR_CORRUPTED.getMessage());

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
