package id.java.personal.project.service.user_service.impl;

import id.java.personal.project.dao.FollowerAndFollowingRepository;
import id.java.personal.project.dao.UserRepository;
import id.java.personal.project.domain.DummyUser;
import id.java.personal.project.domain.FollowerAndFollowing;
import id.java.personal.project.dto.response.DiscoverPeopleHeadResponse;
import id.java.personal.project.dto.response.DiscoverPeopleResponse;
import id.java.personal.project.dto.response.error.StatusResponse;
import id.java.personal.project.service.user_service.DiscoverPeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static id.java.personal.project.constant.AppEnum.IS_NOT_EXISTS;
import static id.java.personal.project.constant.AppEnum.USER_WITH_ID;

@Service
public class DiscoverPeopleServiceImpl implements DiscoverPeopleService {

    private final UserRepository userRepository;
    private final FollowerAndFollowingRepository followerAndFollowingRepository;

    @Autowired
    public DiscoverPeopleServiceImpl(UserRepository userRepository, FollowerAndFollowingRepository followerAndFollowingRepository) {
        this.userRepository = userRepository;
        this.followerAndFollowingRepository = followerAndFollowingRepository;
    }

    @Override
    public StatusResponse discoverPeopleBasedOnUserId(String userId) {
        StatusResponse statusResponse = new StatusResponse();
        DiscoverPeopleHeadResponse discoverPeopleHeadResponse = new DiscoverPeopleHeadResponse();
        DiscoverPeopleResponse discoverPeopleResponse = new DiscoverPeopleResponse();
        List<DiscoverPeopleResponse> discoverPeopleResponseList = new ArrayList<>();

        DummyUser currentUser = userRepository.findOne(userId);
        if (currentUser == null)
            return statusResponse.statusNotFound(USER_WITH_ID.getMessage() + userId + IS_NOT_EXISTS.getMessage(), null);

        FollowerAndFollowing currentUserFollowerAndFollowing = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(currentUser);
        if (currentUserFollowerAndFollowing.getFollowings().size() == 0)
            return statusResponse.statusOk(new DiscoverPeopleHeadResponse());

        for (DummyUser userWhoFollowedByCurrentUser : currentUserFollowerAndFollowing.getFollowings()) {
            insertCurrentUserDiscoverPeople(userWhoFollowedByCurrentUser, discoverPeopleResponse, currentUser, currentUserFollowerAndFollowing);
            discoverPeopleResponseList.add(discoverPeopleResponse);
        }

        discoverPeopleHeadResponse.setDiscoverPeopleResponses(discoverPeopleResponseList);
        return statusResponse.statusOk(discoverPeopleHeadResponse);
    }

    private DiscoverPeopleResponse insertToDiscoverPeopleResponse(DummyUser userWhoFollowedBuCurrentUser, DiscoverPeopleResponse discoverPeopleResponse) {
        discoverPeopleResponse.setUserId(userWhoFollowedBuCurrentUser.getId());
        discoverPeopleResponse.setUsername(userWhoFollowedBuCurrentUser.getUsername());
        discoverPeopleResponse.setUserRole(userWhoFollowedBuCurrentUser.getDummyUserRole().getUserRole());
        discoverPeopleResponse.setUserAge(calculateAge(userWhoFollowedBuCurrentUser.getDateOfBirth()));

        return discoverPeopleResponse;
    }

    private Integer calculateAge(Date dateOfBirth) {
        LocalDate dateOfBirthInLocalDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        return Period.between(dateOfBirthInLocalDate, currentDate).getYears();
    }

    private void insertCurrentUserDiscoverPeople(DummyUser userWhoFollowedByCurrentUser, DiscoverPeopleResponse discoverPeopleResponse, DummyUser currentUser, FollowerAndFollowing currentUserFollowerAndFollowing) {
        FollowerAndFollowing followerAndFollowingOfUserWhoFollowedByCurrentUser = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(userWhoFollowedByCurrentUser);

        if (followerAndFollowingOfUserWhoFollowedByCurrentUser.getFollowings() == null || followerAndFollowingOfUserWhoFollowedByCurrentUser.getFollowings().size() == 0)
            return;

        for(DummyUser followingUser: followerAndFollowingOfUserWhoFollowedByCurrentUser.getFollowings()){
            if(followingUser.getId().equals(currentUser.getId())){
                continue;
            }
            else{
                boolean checkFollowEachOther = checkUserFollowEachOther(followingUser, userWhoFollowedByCurrentUser);
                if(checkFollowEachOther && !currentUserFollowerAndFollowing.getFollowers().contains(followingUser))
                    insertToDiscoverPeopleResponse(followingUser, discoverPeopleResponse);
            }
        }
    }

    private boolean checkUserFollowEachOther(DummyUser firstUser, DummyUser secondUser){
        FollowerAndFollowing followerAndFollowing = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(firstUser);
        List<DummyUser> followingList = followerAndFollowing.getFollowings();

        Iterator<DummyUser> followingIterator = followingList.iterator();
        while(followingIterator.hasNext()){
            DummyUser currentUser = followingIterator.next();
            if(currentUser.getId().equals(secondUser.getId()))
                return true;
        }

        return false;
    }
}
