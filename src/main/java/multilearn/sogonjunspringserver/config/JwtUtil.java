package multilearn.sogonjunspringserver.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    private static final String secretKey;
    private static final SecretKey key;

    static {
        secretKey = "akajsdhakdhajkdhakjdkhsakdhsakjdhkshssdhakdhsk";
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