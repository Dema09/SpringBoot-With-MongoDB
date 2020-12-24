package id.java.personal.project.dao;

import id.java.personal.project.domain.Following;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowingRepository extends MongoRepository<Following, String> {
}
