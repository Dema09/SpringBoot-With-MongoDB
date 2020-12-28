package id.java.personal.project.service.user_service.impl;

import id.java.personal.project.dao.FollowerAndFollowingRepository;
import id.java.personal.project.dao.UserRepository;
import id.java.personal.project.domain.DummyUser;
import id.java.personal.project.domain.FollowerAndFollowing;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.user_service.FollowerAndFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static id.java.personal.project.constant.AppEnum.*;

@Service
public class FollowerAndFollowingServiceImpl implements FollowerAndFollowingService {

    private final UserRepository userRepository;
    private final FollowerAndFollowingRepository followerAndFollowingRepository;

    @Autowired
    public FollowerAndFollowingServiceImpl(UserRepository userRepository,
                                           FollowerAndFollowingRepository followerAndFollowingRepository) {
        this.userRepository = userRepository;
        this.followerAndFollowingRepository = followerAndFollowingRepository;
    }

    @Override
    public StatusResponse followingUserByUsername(String username, String currentUserId) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser followedUser = userRepository.findDummyUserByUsername(username);
        if(followedUser == null)
            return statusResponse.statusNotFound(THIS_USER_WITH_USERNAME.getMessage() + username + IS_NOT_EXISTS.getMessage(), null);

        DummyUser currentUserFollowing = userRepository.findOne(currentUserId);
        if(currentUserFollowing == null)
            return statusResponse.statusNotFound(USER_WITH_ID.getMessage() + currentUserId + IS_NOT_EXISTS.getMessage(), null);

        if(followedUser.getId().equals(currentUserFollowing.getId()))
            return statusResponse.statusBadRequest(YOU_CAN_NOT_FOLLOW_YOURSELF.getMessage(), null);

        FollowerAndFollowing currentUserWhoFollowedBy = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(followedUser);
        FollowerAndFollowing userWhoFollowingTheUser = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(currentUserFollowing);

        List<DummyUser> currentFollowedList = currentUserWhoFollowedBy.getFollowers();
        List<DummyUser> personFollowingToUserList = userWhoFollowingTheUser.getFollowings();

        if(currentFollowedList == null || currentFollowedList.size() == 0)
            currentFollowedList = new ArrayList<>();

        if(personFollowingToUserList == null || personFollowingToUserList.size() == 0)
            personFollowingToUserList = new ArrayList<>();

        boolean checkIfUserAlreadyInFollowerAndFollowingList = checkIfUserAlreadyInFollowerAndFollowingList(followedUser, currentUserFollowing, currentFollowedList, personFollowingToUserList, statusResponse);
        if(checkIfUserAlreadyInFollowerAndFollowingList)
            return statusResponse.statusNotModified(ALREADY_EXISTS_IN_FOLLOWER_OR_FOLLOWING_LIST.getMessage(), null);

        insertIntoCurrentFollowedListAndCurrentFollowingList(currentUserFollowing, followedUser,currentFollowedList, personFollowingToUserList, currentUserWhoFollowedBy, userWhoFollowingTheUser);
        return statusResponse.statusOk(SUCCESSFULLY_FOLLOWED_USER_WITH_USERNAME.getMessage() + username);
    }

    private void insertIntoCurrentFollowedListAndCurrentFollowingList(DummyUser currentUserFollowing, DummyUser followedUser ,List<DummyUser> currentFollowedList, List<DummyUser> personFollowingToUserList, FollowerAndFollowing currentUserWhoFollowedBy, FollowerAndFollowing userWhoFollowingTheUser) {

        currentFollowedList.add(currentUserFollowing);
        currentUserWhoFollowedBy.setFollowers(currentFollowedList);
        followerAndFollowingRepository.save(currentUserWhoFollowedBy);

        personFollowingToUserList.add(followedUser);
        userWhoFollowingTheUser.setFollowings(personFollowingToUserList);
        followerAndFollowingRepository.save(userWhoFollowingTheUser);
    }

    @Override
    public StatusResponse unfollowingUserByUsername(String username, String currentUserId) {
        StatusResponse statusResponse = new StatusResponse();

        DummyUser personThatUnfollowed = userRepository.findDummyUserByUsername(username);
        if(personThatUnfollowed == null)
            return statusResponse.statusNotFound(THIS_USER_WITH_USERNAME.getMessage() + username + IS_NOT_EXISTS, null);

        DummyUser personWhoUnfollowing = userRepository.findOne(currentUserId);
        if(personWhoUnfollowing == null)
            return statusResponse.statusNotFound(USER_WITH_ID.getMessage() + currentUserId + IS_NOT_EXISTS.getMessage(), null);

        if(personThatUnfollowed.getId().equals(personWhoUnfollowing.getId()))
            return statusResponse.statusBadRequest(CANNOT_UNFOLLOW_YOURSELF.getMessage(), null);

        FollowerAndFollowing personUnfollowedDetail = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(personThatUnfollowed);
        List<DummyUser> followerList = personUnfollowedDetail.getFollowers();

        Iterator<DummyUser> userUnfollowedIterator = followerList.iterator();
        while(userUnfollowedIterator.hasNext()){
            DummyUser userWhoUnfollowed = userUnfollowedIterator.next();
            if(userWhoUnfollowed.getId().equals(personWhoUnfollowing.getId()))
                userUnfollowedIterator.remove();
        }

        personUnfollowedDetail.setFollowers(followerList);
        followerAndFollowingRepository.save(personUnfollowedDetail);

        FollowerAndFollowing personWhoUnfollowingDetail = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(personWhoUnfollowing);
        List<DummyUser> followingList = personWhoUnfollowingDetail.getFollowings();

        Iterator<DummyUser> userWhoUnfollowingIterator = followingList.iterator();
        while(userWhoUnfollowingIterator.hasNext()){
            DummyUser userWhoUnfollowing = userWhoUnfollowingIterator.next();
            if(userWhoUnfollowing.getId().equals(personThatUnfollowed.getId()))
                userUnfollowedIterator.remove();
        }

        personWhoUnfollowingDetail.setFollowings(followingList);
        followerAndFollowingRepository.save(personWhoUnfollowingDetail);

        return statusResponse.statusOk(SUCCESSFULLY_UNFOLLOWED_USER_WITH_USERNAME.getMessage() + username);
    }

    private boolean checkIfUserAlreadyInFollowerAndFollowingList(DummyUser followedUser, DummyUser currentUserFollowing, List<DummyUser> currentFollowedList, List<DummyUser> personFollowingToUserList, StatusResponse statusResponse) {
        for(DummyUser currentFollowedUser: currentFollowedList){
            if(currentFollowedUser.getId().equals(currentUserFollowing.getId()))
                return true;
        }

        for(DummyUser currentFollowingUser : personFollowingToUserList){
            if(currentFollowingUser.getId().equals(followedUser))
                return true;
        }
        return false;
    }

}
