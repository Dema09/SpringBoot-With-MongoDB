package id.java.personal.project.dao;

import id.java.personal.project.domain.DummyUser;
import id.java.personal.project.domain.DummyUserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<DummyUser, String> {

    DummyUser findDummyUserByUsername(String username);

    List<DummyUser> findAllByDummyUserRole(DummyUserRole dummyUserRole);

    Optional<DummyUser> findByUsername(String username);

}
