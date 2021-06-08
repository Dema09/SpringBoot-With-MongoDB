package id.java.personal.project.dao;

import id.java.personal.project.domain.DummyUser;
import id.java.personal.project.domain.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
    Post findPostByDummyUser(DummyUser dummyUser);
}
