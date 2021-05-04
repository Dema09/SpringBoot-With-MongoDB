package id.java.personal.project.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "block_user")
public class BlockUser {

    @Id
    private String blockId;

    private List<String> dummyUserIdList;

    @DBRef
    private DummyUser currentUser;

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public List<String> getDummyUserIdList() {
        return dummyUserIdList;
    }

    public void setDummyUserIdList(List<String> dummyUserIdList) {
        this.dummyUserIdList = dummyUserIdList;
    }

    public DummyUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(DummyUser currentUser) {
        this.currentUser = currentUser;
    }
}
