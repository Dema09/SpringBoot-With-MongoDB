package id.java.personal.project.service.user_service.impl;

import id.java.personal.project.constant.AppEnum;
import id.java.personal.project.constant.RoleEnum;
import id.java.personal.project.dao.*;
import id.java.personal.project.domain.*;
import id.java.personal.project.dto.request.*;
import id.java.personal.project.dto.response.LoginResponse;
import id.java.personal.project.dto.response.ProfileResponse;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.security.jwt.JwtUtils;
import id.java.personal.project.security.service.UserDetailsImpl;
import id.java.personal.project.service.user_service.RestTemplateService;
import id.java.personal.project.service.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import static id.java.personal.project.constant.AppEnum.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final FollowerAndFollowingRepository followerAndFollowingRepository;
    private final UserCloseFriendRepository userCloseFriendRepository;
    private final BlockUserRepository blockUserRepository;
    private final PostRepository postRepository;
    private final JwtUtils jwtUtils;
    private final RestTemplateService restTemplateService;

    SimpleDateFormat sdf = new SimpleDateFormat(AppEnum.DATE_FORMAT.getMessage());

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           Environment env,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           RoleRepository roleRepository,
                           FollowerAndFollowingRepository followerAndFollowingRepository,
                           BlockUserRepository blockUserRepository,
                           UserCloseFriendRepository userCloseFriendRepository,
                           PostRepository postRepository,
                           JwtUtils jwtUtils,
                           RestTemplateService restTemplateService
                           ) {
        this.userRepository = userRepository;
        this.env = env;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.followerAndFollowingRepository = followerAndFollowingRepository;
        this.userCloseFriendRepository = userCloseFriendRepository;
        this.blockUserRepository = blockUserRepository;
        this.postRepository = postRepository;
        this.jwtUtils = jwtUtils;
        this.restTemplateService = restTemplateService;
    }

    @Override
    public StatusResponse insertUserData(RegisterDTO registerDTO) throws ParseException {
        StatusResponse statusResponse = new StatusResponse();
        DummyUserRole userRole = roleRepository.findDummyUserRoleByUserRole(RoleEnum.ROLE_USER.getMessage());
        DummyUser checkUser = userRepository.findDummyUserByUsername(registerDTO.getUsername());

        if(checkUser != null)
            return statusResponse.statusConflict(AppEnum.THIS_USER_WITH_USERNAME.getMessage() + registerDTO.getUsername() + AppEnum.HAS_BEEN_EXISTS.getMessage(), null);

        DummyUser dummyUser = new DummyUser(registerDTO.getUsername(),
                registerDTO.getNickname(),
                passwordEncoder.encode(registerDTO.getPassword()),
                registerDTO.getAddress(),
                registerDTO.getEmail(),
                registerDTO.getPhoneNumber(),
                sdf.parse(registerDTO.getDateOfBirth()),
                userRole);

        userRepository.save(dummyUser);

        FollowerAndFollowing followerAndFollowing = new FollowerAndFollowing();
        followerAndFollowing.setDummyUser(dummyUser);
        followerAndFollowingRepository.save(followerAndFollowing);

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
        currentUser.setProfilePicture(profileDTO.getProfilePicture().getOriginalFilename());
        currentUser.setNickname(profileDTO.getNickname());
        convertProfilePicture(profileDTO.getProfilePicture().getBytes(), profileDTO.getProfilePicture());

        userRepository.save(currentUser);
        return statusResponse.statusOk(AppEnum.SUCCESS_UPDATED_USER_DATA_PROFILE.getMessage() + currentUser.getId());

    }

    @Override
    public StatusResponse getUserDataProfileById(String userId, String currentUserId) throws IOException {
        ProfileResponse profileResponse = new ProfileResponse();
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(currentUserId);
        if(currentUser == null)
            return statusResponse.statusBadRequest("Data with id: " + currentUserId + " is not exists!", null);

        DummyUser userWhoCheckedByCurrentUser = userRepository.findOne(userId);
        if(userWhoCheckedByCurrentUser == null)
            return statusResponse.statusNotFound("Data with id: " + userId + " is not exists!", null);

        FollowerAndFollowing followerAndFollowing = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(userWhoCheckedByCurrentUser);

        boolean isPrivateAccount = checkPrivateAccount(userWhoCheckedByCurrentUser);
        boolean isCurrentUserAlreadyFollowingCheckedUser = checkCurrentUserAlreadyFollowingCheckedUser(userWhoCheckedByCurrentUser, currentUser);

        if((isPrivateAccount && !isCurrentUserAlreadyFollowingCheckedUser) && !currentUser.getId().equals(userWhoCheckedByCurrentUser.getId()))
            setProfileResponseWhenSomeoneSeePrivateAccount(profileResponse, userWhoCheckedByCurrentUser, followerAndFollowing);
        else
            setProfileResponseWhenSomeOneSeeNonPrivateAccountAndItsProfile(profileResponse, userWhoCheckedByCurrentUser, followerAndFollowing);

        return statusResponse.statusOk(profileResponse);
    }

    private void setProfileResponseWhenSomeOneSeeNonPrivateAccountAndItsProfile(ProfileResponse profileResponse, DummyUser userWhoCheckedByCurrentUser, FollowerAndFollowing followerAndFollowing) throws IOException {
        Post currentUserPost = postRepository.findPostByDummyUser(userWhoCheckedByCurrentUser);

        profileResponse.setUserId(userWhoCheckedByCurrentUser.getId());
        profileResponse.setUsername(userWhoCheckedByCurrentUser.getUsername());
        profileResponse.setNickName(userWhoCheckedByCurrentUser.getNickname());
        profileResponse.setUserProfilePicture(userWhoCheckedByCurrentUser.getProfilePicture() == null ? "" : getImage(userWhoCheckedByCurrentUser.getProfilePicture()));
        profileResponse.setUserRole(userWhoCheckedByCurrentUser.getDummyUserRole().getUserRole());
        profileResponse.setNumberOfPosts(currentUserPost.getPostPicture().size() == 0 ? 0 : currentUserPost.getPostPicture().size());
        profileResponse.setNumberOfFollowers(followerAndFollowing.getFollowers() == null ? 0 : followerAndFollowing.getFollowers().size());
        profileResponse.setNumberOfFollowings(followerAndFollowing.getFollowings() == null ? 0 : followerAndFollowing.getFollowings().size());
    }

    private boolean checkCurrentUserAlreadyFollowingCheckedUser(DummyUser userWhoCheckedByCurrentUser, DummyUser currentUser) {
        boolean isFound = false;

        FollowerAndFollowing currentUserFollowerAndFollowing = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(currentUser);
        if(currentUserFollowerAndFollowing.getFollowings() == null || currentUserFollowerAndFollowing.getFollowings().size() == 0)
            return isFound;

        for(DummyUser dummyUser : currentUserFollowerAndFollowing.getFollowings()){
            if(dummyUser.getId().equals(userWhoCheckedByCurrentUser.getId())){
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    private void setProfileResponseWhenSomeoneSeePrivateAccount(ProfileResponse profileResponse, DummyUser currentUser, FollowerAndFollowing followerAndFollowing) {
        Post currentPost = postRepository.findPostByDummyUser(currentUser);

        profileResponse.setUsername(currentUser.getUsername());
        profileResponse.setNumberOfFollowers(followerAndFollowing.getFollowers().size() == 0 ? 0 : followerAndFollowing.getFollowers().size());
        profileResponse.setNumberOfFollowings(followerAndFollowing.getFollowings().size()== 0 ? 0 : followerAndFollowing.getFollowings().size());
        profileResponse.setNumberOfPosts(currentPost.getPostPicture().size() == 0 ? 0 : currentPost.getPostPicture().size());
    }

    private boolean checkPrivateAccount(DummyUser currentUser) {
        boolean isPrivateAccount = false;
        if(currentUser.isProtectedAccount() == true)
            isPrivateAccount = true;
        return isPrivateAccount;
    }

    @Override
    public StatusResponse loginUser(LoginDTO loginDTO) {
        StatusResponse statusResponse = new StatusResponse();
        DummyUser userData = userRepository.findDummyUserByUsername(loginDTO.getUsername());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userData.getDummyUserRole().getUserRole()));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword(), authorities));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        LoginResponse loginResponse = new LoginResponse();

        String json = restTemplateService.retrieveOauthToken(userData).toString();

        loginResponse.setId(userDetails.getId());
        loginResponse.setUsername(userDetails.getUsername());
        loginResponse.setEmail(userDetails.getEmail());
        loginResponse.setRoles(userData.getDummyUserRole().getUserRole());


        return statusResponse.statusOk(loginResponse);

    }

    @Override
    public StatusResponse setProtectedAccountByUserId(String userId) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_WITH_ID.getMessage() + userId + IS_NOT_EXISTS.getMessage(), null);

        if(currentUser.isProtectedAccount() == true)
            return statusResponse.statusNotModified(YOUR_ACCOUNT_ALREADY_PROTECTED.getMessage(), null);

        currentUser.setProtectedAccount(true);
        userRepository.save(currentUser);

        return statusResponse.statusOk(SUCCESSFULLY_SET_YOUR_ACCOUNT_TO_PROTECTED.getMessage());
    }

    @Override
    public StatusResponse unsetProtectedAccount(String userId) {
        StatusResponse statusResponse =  new StatusResponse();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_WITH_ID.getMessage() + userId + IS_NOT_EXISTS.getMessage(), null);

        if(currentUser.isProtectedAccount() == false)
            return statusResponse.statusNotModified(YOUR_ACCOUNT_ALREADY_UNPROTECTED.getMessage(), null);

        currentUser.setProtectedAccount(false);
        userRepository.save(currentUser);

        return statusResponse.statusOk(SUCCESSFULLY_SET_YOUR_ACCOUNT_TO_UNPROTECTED.getMessage());
    }

    @Override
    public StatusResponse insertCloseFriend(String userId, CloseFriendRequestDTO closeFriendRequestDTO) {
        StatusResponse statusResponse = new StatusResponse();
        UserCloseFriend userCloseFriend = new UserCloseFriend();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_DATA_NOT_FOUND.getMessage(), null);

        insertToCloseFriendData(currentUser, userCloseFriend, closeFriendRequestDTO);
        return statusResponse.statusCreated(SUCCESSFULLY_ADDED_CLOSE_FRIEND.getMessage(), userCloseFriend.getCloseFriendId());
    }

    @Override
    public StatusResponse removeCloseFriendByUserId(String userId, CloseFriendRequestDTO closeFriendRequestDTO) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_DATA_NOT_FOUND.getMessage(), null);

        UserCloseFriend currentUserCloseFriend = userCloseFriendRepository.findUserCloseFriendByDummyUser(currentUser);
        List<DummyUser> dummyUsers = currentUserCloseFriend.getCloseFriendUsers();

        Iterator<DummyUser> dummyUserIterator = dummyUsers.iterator();

        for(String currentUserId : closeFriendRequestDTO.getUserIds()){
            while(dummyUserIterator.hasNext()){
                DummyUser dummyUserWhichWantToRemove = dummyUserIterator.next();
                if(dummyUserWhichWantToRemove.getId().equals(currentUserId))
                    dummyUserIterator.remove();
            }
        }
        currentUserCloseFriend.setCloseFriendUsers(dummyUsers);
        userCloseFriendRepository.save(currentUserCloseFriend);
        return statusResponse.statusOk(SUCCESSFULLY_REMOVE_CLOSE_FRIEND.getMessage());

    }

    @Override
    public StatusResponse changeUserPassword(String userId, ChangePasswordDTO changePasswordDTO) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_DATA_NOT_FOUND.getMessage(), null);


        String checkPassword = validate(currentUser.getPassword(), changePasswordDTO);
        if(!checkPassword.equals(""))
            return statusResponse.statusInternalServerError(checkPassword, null);

        currentUser.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(currentUser);

        return statusResponse.statusOk(SUCCESSFULLY_UPDATE_YOUR_PASSWORD.getMessage());
    }

    @Override
    public StatusResponse blockUserByUserId(String userId, String blockedUserId) {
        StatusResponse statusResponse = new StatusResponse();
        List<String> blockedUsers = new ArrayList<>();

        DummyUser currentUser = userRepository.findOne(userId);
        DummyUser blockedUser = userRepository.findOne(blockedUserId);

        if(blockedUser == null || currentUser == null)
            return statusResponse.statusNotFound(USER_DATA_NOT_FOUND.getMessage(), null);

        if(currentUser.getId().equals(blockedUser.getId()))
            return statusResponse.statusBadRequest(YOU_CANNOT_BLOCK_YOURSELF.getMessage(), null);

        blockedUsers.add(blockedUserId);

        BlockUser currentBlockUser = blockUserRepository.findBlockUserByCurrentUser(currentUser);
        if(currentBlockUser == null)
            insertBlockedUser(new BlockUser(), blockedUsers, currentUser);
        else{
            boolean validateUserIfExistInBlockList = validateIfUserIsInBlockListOrNot(blockedUserId, currentBlockUser.getDummyUserIdList());
            if(!validateUserIfExistInBlockList)
                insertBlockedUser(currentBlockUser, blockedUsers, currentUser);
            else
                return statusResponse.statusNotModified(YOU_ALREADY_BLOCK_THIS_USER_WITH_ID.getMessage() + blockedUserId, null);
        }
        return statusResponse.statusOk(SUCCESSFULLY_ADD_USER_TO_BLOCK_LIST.getMessage());
    }

    private boolean validateIfUserIsInBlockListOrNot(String blockedUserId, List<String> dummyUserIdList) {
        boolean userIsExist = false;
        if(dummyUserIdList == null)
            return false;

        for(String currentDummyUserId : dummyUserIdList){
            if(blockedUserId.equals(currentDummyUserId)){
                userIsExist = true;
                break;
            }
        }
        return userIsExist;
    }

    @Override
    public StatusResponse unblockUserByUserId(String userId, String unblockUserId) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser currentUser = userRepository.findOne(userId);
        if(currentUser == null)
            return statusResponse.statusNotFound(USER_DATA_NOT_FOUND.getMessage(), null);

        DummyUser userWhoWantToUnblock = userRepository.findOne(unblockUserId);
        if(userWhoWantToUnblock == null)
            return statusResponse.statusNotFound(NO_USER_FOUND_TO_UNBLOCK.getMessage(), null);

        BlockUser blockUser = blockUserRepository.findBlockUserByCurrentUser(currentUser);

        Iterator<String> blockUserIterator = blockUser.getDummyUserIdList().iterator();
            while(blockUserIterator.hasNext()){
                String userIdWhoWantToRemoveFromBlockList = blockUserIterator.next();
                if(unblockUserId.equals(userIdWhoWantToRemoveFromBlockList)){
                    blockUserIterator.remove();
                    break;

            }
        }
        blockUser.setDummyUserIdList(blockUser.getDummyUserIdList());
        blockUserRepository.save(blockUser);
        return statusResponse.statusOk(SUCCESSFULLY_REMOVE_USER_FROM_BLOCK_USER_WITH_ID.getMessage() + unblockUserId);
    }

    private void insertBlockedUser(BlockUser blockUser, List<String> blockUsers , DummyUser currentUser) {
        blockUser.setCurrentUser(currentUser);
        blockUser.setDummyUserIdList(blockUsers);

        blockUserRepository.save(blockUser);
    }

    private String validate(String password, ChangePasswordDTO changePasswordDTO) {
        if(passwordEncoder.matches(changePasswordDTO.getNewPassword() , password))
            return OLD_PASSWORD_SAME_AS_NEW_PASSWORD.getMessage();

        if(!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword()))
            return YOUR_PASSWORD_DOES_NOT_MATCH_WITH_A_NEW_PASSWORD.getMessage();

        if(!passwordEncoder.matches(changePasswordDTO.getOldPassword() , password))
            return INCORRECT_OLD_PASSWORD.getMessage();

        return "";
    }

    private void insertToCloseFriendData(DummyUser currentUser, UserCloseFriend userCloseFriend, CloseFriendRequestDTO closeFriendRequestDTO) {
        List<DummyUser> dummyUsers = new ArrayList<>();

        for(String userId : closeFriendRequestDTO.getUserIds()){
            DummyUser currentUserCloseFriend = userRepository.findOne(userId);
            if(currentUserCloseFriend.getId().equals(currentUser.getId()))
                continue;

            dummyUsers.add(currentUserCloseFriend);
        }
        userCloseFriend.setDummyUser(currentUser);
        userCloseFriend.setCloseFriendUsers(dummyUsers);

        userCloseFriendRepository.save(userCloseFriend);
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
