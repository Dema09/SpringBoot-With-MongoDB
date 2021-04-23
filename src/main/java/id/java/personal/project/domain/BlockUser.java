package id.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "block_user")
public class BlockUser {

    @Id
    private String blockId;

    private List<DummyUser> dummyUsers;

    private DummyUser currentUser;

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public List<DummyUser> getDummyUsers() {
        return dummyUsers;
    }

    public void setDummyUsers(List<DummyUser> dummyUsers) {
        this.dummyUsers = dummyUsers;
    }

    public DummyUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(DummyUser currentUser) {
        this.currentUser = currentUser;
    }
}
