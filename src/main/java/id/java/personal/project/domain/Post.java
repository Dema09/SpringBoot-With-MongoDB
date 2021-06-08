package id.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "user_post")
public class Post {
    @Id
    private String postId;

    private List<String> postPicture;

    @DBRef
    private DummyUser dummyUser;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<String> getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(List<String> postPicture) {
        this.postPicture = postPicture;
    }

    public DummyUser getDummyUser() {
        return dummyUser;
    }

    public void setDummyUser(DummyUser dummyUser) {
        this.dummyUser = dummyUser;
    }
}
