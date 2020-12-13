package id.java.personal.project.security.jwt;

import id.java.personal.project.constant.AppConstant;
import id.java.personal.project.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtils {

    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());

    @Value("${spring.jwt.client.secret}")
    private String jwtSecret;

    @Value("${jwt.token.timeout}")
    private int jwtTimeout;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public String generateJwtToken(Authentication authentication){

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        setJwtSecret(jwtSecret);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtTimeout))
                .signWith(SignatureAlgorithm.HS512, getJwtSecret())
                .compact();
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(getJwtSecret()).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parser().setSigningKey(getJwtSecret()).parseClaimsJws(authToken);
            return true;
        }catch (SignatureException e){
            logger.error("Invalid JWT Signature: {}", e);
        }catch (MalformedJwtException e){
            logger.error("Invalid JWT Token: {}", e);
        }catch (ExpiredJwtException e){
            logger.error("JWT Token is Expired: {}", e);
        }catch (UnsupportedJwtException e){
            logger.error("JWT Token is Unsupported: {}", e);
        }catch(IllegalArgumentException e){
            logger.error("JWT Claims String is Empty: {}", e);
        }
        return false;
    }

//    private String generateSecretKey(){
//        StringBuilder sb = new StringBuilder(32);
//        for(int i=0; i<32; i++){
//            int index = (int)(AppConstant.ALPHA_NUMERIC_STRING.getMessage().length() * Math.random());
//            sb.append(AppConstant.ALPHA_NUMERIC_STRING.getMessage().charAt(index));
//        }
//        return sb.toString();
//    }

}
