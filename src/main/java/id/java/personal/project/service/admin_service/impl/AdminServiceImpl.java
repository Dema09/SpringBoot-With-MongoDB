package id.java.personal.project.service.admin_service.impl;

import id.java.personal.project.constant.AppEnum;
import id.java.personal.project.constant.RoleEnum;
import id.java.personal.project.dao.RoleRepository;
import id.java.personal.project.dao.UserRepository;
import id.java.personal.project.domain.DummyUser;
import id.java.personal.project.domain.DummyUserRole;
import id.java.personal.project.dto.request.RegisterDTO;
import id.java.personal.project.dto.response.AllUserResponse;
import id.java.personal.project.dto.response.UserResponseWithAge;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.admin_service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static id.java.personal.project.constant.AppEnum.USER_DATA_NOT_FOUND;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public StatusResponse getUserDataByUserId(String userId) {
        DummyUser currentDummyUser = userRepository.findOne(userId);
        StatusResponse statusResponse = new StatusResponse();
        if (currentDummyUser == null)
            return statusResponse.statusNotFound(USER_DATA_NOT_FOUND.getMessage(), null);

        UserResponseWithAge userResponseWithAge = new UserResponseWithAge();
        userResponseWithAge.setUserId(currentDummyUser.getId());
        userResponseWithAge.setUsername(currentDummyUser.getUsername());
        userResponseWithAge.setUserRole(currentDummyUser.getDummyUserRole().getUserRole());
        userResponseWithAge.setUserAge(calculateUserAge(currentDummyUser.getDateOfBirth()));

        return statusResponse.statusOk(userResponseWithAge);
    }

    @Override
    public StatusResponse getAllUser() {
        DummyUserRole dummyUserRole = roleRepository.findDummyUserRoleByUserRole(RoleEnum.ROLE_USER.getMessage());
        List<DummyUser> dummyUsers = userRepository.findAllByDummyUserRole(dummyUserRole);

        StatusResponse statusResponse = new StatusResponse();
        AllUserResponse allUserResponse = new AllUserResponse();
        List<UserResponseWithAge> userResponseWithAges = new ArrayList<>();

        for(DummyUser dummyUser : dummyUsers){
            UserResponseWithAge userResponseWithAge = new UserResponseWithAge();
            userResponseWithAge.setUserId(dummyUser.getId());
            userResponseWithAge.setUsername(dummyUser.getUsername());
            userResponseWithAge.setUserRole(dummyUser.getDummyUserRole().getUserRole());
            userResponseWithAge.setUserAge(calculateUserAge(dummyUser.getDateOfBirth()));

            userResponseWithAges.add(userResponseWithAge);
        }

        allUserResponse.setUsers(userResponseWithAges);
        return statusResponse.statusOk(allUserResponse);
    }

    @Override
    public StatusResponse registerForAdmin(RegisterDTO registerDTO) throws ParseException {
        StatusResponse statusResponse = new StatusResponse();
        DummyUser currentDummyUser = userRepository.findDummyUserByUsername(registerDTO.getUsername());
        DummyUserRole adminRole = roleRepository.findDummyUserRoleByUserRole(RoleEnum.ROLE_ADMIN.getMessage());

        if(currentDummyUser != null)
            return statusResponse.statusConflict(AppEnum.THIS_USER_WITH_USERNAME.getMessage() + registerDTO.getUsername() + AppEnum.HAS_BEEN_EXISTS.getMessage(), null);

        SimpleDateFormat sdf = new SimpleDateFormat(AppEnum.DATE_FORMAT.getMessage());

        DummyUser newAdminUser = new DummyUser(
                registerDTO.getUsername(),
                registerDTO.getNickname(),
                registerDTO.getPassword(),
                registerDTO.getAddress(),
                registerDTO.getEmail(),
                registerDTO.getPhoneNumber(),
                sdf.parse(registerDTO.getDateOfBirth()),
                adminRole
        );

        userRepository.save(newAdminUser);
        return statusResponse.statusCreated(AppEnum.SUCCESS_REGISTER_USER.getMessage(), "Id: " + newAdminUser.getId());
    }


    private Integer calculateUserAge(Date dateOfBirth) {
        LocalDate dateOfBirthInLocalDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        return Period.between(dateOfBirthInLocalDate, currentDate).getYears();
    }

}
