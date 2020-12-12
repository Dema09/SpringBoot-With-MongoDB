package id.java.personal.project.dao;

import id.java.personal.project.domain.DummyUserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<DummyUserRole, String> {

    DummyUserRole findDummyUserRoleByUserRole(String userRole);

}
