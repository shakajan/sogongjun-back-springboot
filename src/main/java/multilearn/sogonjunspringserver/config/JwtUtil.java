package multilearn.sogonjunspringserver.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private final Environment env;

    private static SecretKey key;

    public JwtUtil(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void init() {
        String secretKey = env.getProperty("jwt.key");
        key = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "HmacSHA256");
    }

    public String createToken(Authentication auth) {
        User user = (User) auth.getPrincipal();
//        String authorities = auth.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        logger.info("[createToken] username: {}", user.getUsername());
        String jwt = Jwts.builder()
                .claim("username", user.getUsername())
//                .claim("authority", authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60))
                .signWith(key)
                .compact();
        logger.info("[createToken] created token: {}", jwt);
        return jwt;
    }

    public static Claims extractToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }

}