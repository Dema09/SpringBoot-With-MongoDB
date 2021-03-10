package id.java.personal.project.dao;

import id.java.personal.project.domain.UserCloseFriend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCloseFriendRepository extends MongoRepository<UserCloseFriend, String> {
}
