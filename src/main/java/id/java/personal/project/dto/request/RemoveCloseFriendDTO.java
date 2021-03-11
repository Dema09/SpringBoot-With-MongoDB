package id.java.personal.project.dto.request;

import java.util.List;

public class RemoveCloseFriendDTO {
    private List<String> userIds;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
