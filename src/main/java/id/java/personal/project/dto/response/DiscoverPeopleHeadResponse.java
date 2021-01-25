package id.java.personal.project.dto.response;

import java.util.List;

public class DiscoverPeopleHeadResponse {
    private List<DiscoverPeopleResponse> discoverPeopleResponses;

    public List<DiscoverPeopleResponse> getDiscoverPeopleResponses() {
        return discoverPeopleResponses;
    }

    public void setDiscoverPeopleResponses(List<DiscoverPeopleResponse> discoverPeopleResponses) {
        this.discoverPeopleResponses = discoverPeopleResponses;
    }
}
