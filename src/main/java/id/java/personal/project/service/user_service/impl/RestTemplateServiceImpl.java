package id.java.personal.project.service.user_service.impl;

import id.java.personal.project.dao.UserRepository;
import id.java.personal.project.domain.DummyUser;
import id.java.personal.project.service.user_service.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RestTemplateServiceImpl implements RestTemplateService {

    private final UserRepository userRepository;
    RestTemplate restTemplate;

    @Autowired
    public RestTemplateServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity retrieveOauthToken(DummyUser dummyUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity entity = new HttpEntity(headers);

        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/oauth/token");

        return restTemplate.exchange(
                uri.toUriString(),
                HttpMethod.POST,
                entity,
                String.class);

    }
}
