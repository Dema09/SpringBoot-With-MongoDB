package id.java.personal.project.service.user_service;

import id.java.personal.project.domain.DummyUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface RestTemplateService {

    ResponseEntity retrieveOauthToken(DummyUser dummyUser);

}
