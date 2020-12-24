package id.java.personal.project.service.user_service.impl;

import id.java.personal.project.dao.FollowerAndFollowingRepository;
import id.java.personal.project.dao.FollowerRepository;
import id.java.personal.project.dao.FollowingRepository;
import id.java.personal.project.dao.UserRepository;
import id.java.personal.project.domain.DummyUser;
import id.java.personal.project.domain.Follower;
import id.java.personal.project.domain.FollowerAndFollowing;
import id.java.personal.project.domain.Following;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.user_service.FollowerAndFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static id.java.personal.project.constant.AppEnum.*;

@Service
public class FollowerAndFollowingServiceImpl implements FollowerAndFollowingService {

    private final UserRepository userRepository;
    private final FollowerAndFollowingRepository followerAndFollowingRepository;
    private final FollowerRepository followerRepository;
    private final FollowingRepository followingRepository;

    @Autowired
    public FollowerAndFollowingServiceImpl(UserRepository userRepository,
                                           FollowerAndFollowingRepository followerAndFollowingRepository,
                                           FollowerRepository followerRepository,
                                           FollowingRepository followingRepository) {
        this.userRepository = userRepository;
        this.followerAndFollowingRepository = followerAndFollowingRepository;
        this.followerRepository = followerRepository;
        this.followingRepository = followingRepository;
    }

    @Override
    public StatusResponse followingUserByUsername(String username, String currentUserId) {

        StatusResponse statusResponse = new StatusResponse();
        List<Follower> followers =  new ArrayList<>();
        List<Following> followings = new ArrayList<>();

        DummyUser followedUser = userRepository.findDummyUserByUsername(username);
        if(followedUser == null)
            return statusResponse.statusNotFound(THIS_USER_WITH_USERNAME.getMessage() + username + IS_NOT_EXISTS.getMessage(), null);

        DummyUser currentUserFollowing = userRepository.findOne(currentUserId);
        if(currentUserFollowing == null)
            return statusResponse.statusNotFound(USER_WITH_ID.getMessage() + currentUserId + IS_NOT_EXISTS.getMessage(), null);

        FollowerAndFollowing currentUserWhoFollowedBy = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(followedUser);
        List<Follower> personThatFollowedTheCurrentUserList = insertPersonThatFollowedTheCurrentUser(currentUserFollowing, followers);
        currentUserWhoFollowedBy.setFollowers(personThatFollowedTheCurrentUserList);
        followerAndFollowingRepository.save(currentUserWhoFollowedBy);


        FollowerAndFollowing userWhoFollowingTheUser = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(currentUserFollowing);
        List<Following> personFollowingToUserList = insertPersonFollowingToUser(followedUser, followings);
        userWhoFollowingTheUser.setFollowings(personFollowingToUserList);
        followerAndFollowingRepository.save(userWhoFollowingTheUser);

        return statusResponse.statusOk(SUCCESSFULLY_FOLLOWED_USER_WITH_USERNAME.getMessage() + username);
    }

    private List<Following> insertPersonFollowingToUser(DummyUser followedUser, List<Following> followings) {

        Following following = new Following();
        following.setUserId(followedUser);
        followingRepository.save(following);

        followings.add(following);
        return followings;
    }

    private List<Follower> insertPersonThatFollowedTheCurrentUser(DummyUser currentUserFollowing, List<Follower> followers) {
        Follower personFollowedCurrentUser = new Follower();
        personFollowedCurrentUser.setUserId(currentUserFollowing);
        followerRepository.save(personFollowedCurrentUser);

        followers.add(personFollowedCurrentUser);
        return followers;

    }


}
