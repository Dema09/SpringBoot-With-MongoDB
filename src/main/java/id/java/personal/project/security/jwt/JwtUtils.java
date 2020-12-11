package id.java.personal.project.security.jwt;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component
public class JwtUtils {

    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());

    private String jwtSecret;

    @Value("{$jwt.token.timeout}")
    private int jwtTimeout;

    public String generateJwtToken(Authentication authentication){
        return Jwts.builder().set
    }

}
