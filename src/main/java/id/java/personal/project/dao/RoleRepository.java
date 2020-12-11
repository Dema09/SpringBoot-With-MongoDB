package id.java.personal.project.dao;

import id.java.personal.project.constant.RoleEnum;
import id.java.personal.project.domain.DummyUserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<DummyUserRole, String> {

    Optional<DummyUserRole> findByUserRole(RoleEnum roleName);

}
