package id.java.personal.project.dao;

import id.java.personal.project.domain.Follower;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowerRepository extends MongoRepository<Follower, String> {
}
