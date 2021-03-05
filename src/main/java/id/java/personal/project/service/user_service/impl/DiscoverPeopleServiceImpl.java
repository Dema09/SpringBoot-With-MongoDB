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
        List<DiscoverPeopleResponse> discoverPeopleResponseList = new ArrayList<>();


        DummyUser currentUser = userRepository.findOne(userId);
        if (currentUser == null)
            return statusResponse.statusNotFound(USER_WITH_ID.getMessage() + userId + IS_NOT_EXISTS.getMessage(), null);

        FollowerAndFollowing currentUserFollowerAndFollowing = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(currentUser);
        if (currentUserFollowerAndFollowing.getFollowings().size() == 0)
            return statusResponse.statusOk(new DiscoverPeopleHeadResponse());

        for (DummyUser userWhoFollowedByCurrentUser : currentUserFollowerAndFollowing.getFollowings()) {
            discoverPeopleResponseList = insertCurrentUserDiscoverPeople(userWhoFollowedByCurrentUser, currentUser, currentUserFollowerAndFollowing);
        }

        discoverPeopleHeadResponse.setDiscoverPeopleResponses(discoverPeopleResponseList);
        return statusResponse.statusOk(discoverPeopleHeadResponse);
    }

    private DiscoverPeopleResponse insertToDiscoverPeopleResponse(DummyUser userWhoFollowedBuCurrentUser, DiscoverPeopleResponse discoverPeopleResponse) {

        discoverPeopleResponse.setUserId(userWhoFollowedBuCurrentUser.getId());
        discoverPeopleResponse.setUsername(userWhoFollowedBuCurrentUser.getUsername());
        discoverPeopleResponse.setUserRole(userWhoFollowedBuCurrentUser.getDummyUserRole().getUserRole());
        discoverPeopleResponse.setUserAge(calculateAge(userWhoFollowedBuCurrentUser.getDateOfBirth()));
        discoverPeopleResponse.setMutualUsers(insertMutualUsers(userWhoFollowedBuCurrentUser));
        discoverPeopleResponse.setNumberOfMutualUser(insertMutualUsers(userWhoFollowedBuCurrentUser).size());
        return discoverPeopleResponse;
    }

    private List<DummyUser> insertMutualUsers(DummyUser userWhoFollowedByCurrentUser) {
        FollowerAndFollowing userWhoFollowedByCurrentUserFollowerAndFollowing = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(userWhoFollowedByCurrentUser);
        Iterator<DummyUser> followerIterator = userWhoFollowedByCurrentUserFollowerAndFollowing.getFollowers().iterator();
        List<DummyUser> followerAndFollowingList = new ArrayList<>();

        while(followerIterator.hasNext()) {
            DummyUser dummyUser = followerIterator.next();
            followerAndFollowingList.add(dummyUser);
            }

        return followerAndFollowingList;
    }

    private Integer calculateAge(Date dateOfBirth) {
        LocalDate dateOfBirthInLocalDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        return Period.between(dateOfBirthInLocalDate, currentDate).getYears();
    }

    private List<DiscoverPeopleResponse> insertCurrentUserDiscoverPeople(DummyUser userWhoFollowedByCurrentUser,
                                                 DummyUser currentUser,
                                                 FollowerAndFollowing currentUserFollowerAndFollowing) {

        List<DiscoverPeopleResponse> discoverPeopleResponses = new ArrayList<>();

        FollowerAndFollowing followerAndFollowingOfUserWhoFollowedByCurrentUser = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(userWhoFollowedByCurrentUser);

        if (followerAndFollowingOfUserWhoFollowedByCurrentUser.getFollowings() == null || followerAndFollowingOfUserWhoFollowedByCurrentUser.getFollowings().size() == 0)
            return new ArrayList<>();

        for(DummyUser followingUser: followerAndFollowingOfUserWhoFollowedByCurrentUser.getFollowings()){
            if(followingUser.getId().equals(currentUser.getId())){
                continue;
            }
            else{
                DiscoverPeopleResponse discoverPeopleResponse = new DiscoverPeopleResponse();
                boolean checkFollowEachOther = checkUserFollowEachOther(followingUser, userWhoFollowedByCurrentUser);

//                Integer followingUserInCurrentUserFollowingListHashCode1 = currentUserFollowerAndFollowing.getFollowings().hashCode();
//                Integer followingUserInCurrentUserFollowingListHashCode2 = followingUser.hashCode();

                for(int counter = 0; counter < currentUserFollowerAndFollowing.getFollowings().size(); counter++){
                    if(checkFollowEachOther && currentUserFollowerAndFollowing.getFollowings().get(counter).equals(followingUser))
                        discoverPeopleResponses.add(insertToDiscoverPeopleResponse(followingUser, discoverPeopleResponse));

                }
            }
        }
        return discoverPeopleResponses;
    }

    private boolean checkUserFollowEachOther(DummyUser firstUser, DummyUser secondUser){
        FollowerAndFollowing followerAndFollowing = followerAndFollowingRepository.findFollowerAndFollowingByDummyUser(firstUser);
        List<DummyUser> followingList = followerAndFollowing.getFollowings();

        for(DummyUser dummyUser : followingList){
            if(dummyUser.getId().equals(secondUser.getId()))
                return true;
        }

        return false;
    }
}
