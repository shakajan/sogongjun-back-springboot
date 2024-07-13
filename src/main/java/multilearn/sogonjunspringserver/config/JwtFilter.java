package multilearn.sogonjunspringserver.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import multilearn.sogonjunspringserver.domain.User;
import multilearn.sogonjunspringserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

public class JwtFilter extends OncePerRequestFilter {

    final private UserRepository userRepository;
    final private Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private static final Set<String> UNSECURED_PATHS = Set.of(
            "/api/users/login", "/api/users/register", "https://api.openai.com/v1/chat/completions", "https://api.openai.com/v1/images/generations"
    );

    public JwtFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        logger.info("[JwtFilter] Jwt filter has been executed.");
        String path = request.getRequestURI();
        if(UNSECURED_PATHS.contains(path)) {
            logger.info("[JwtFilter] ignored api endpoint: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            logger.info("[JwtFilter] token is missing or does not start with 'Bearer'.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token is missing or does not start with 'Bearer'.");
            return;
        }
        token = token.substring(7);
        logger.info("[JwtFilter] There is a jwt token. token: {}", token);

        Claims claim;
        try {
            claim = JwtUtil.extractToken(token);
        } catch (ExpiredJwtException e) {
            logger.info("[JwtFilter] But, it is expired token.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired JWT token");
            return;
        } catch (MalformedJwtException e) {
            logger.info("[JwtFilter] Malformed JWT token.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Malformed JWT token");
            return;
        } catch (UnsupportedJwtException e) {
            logger.info("[JwtFilter] Unsupported JWT token.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unsupported JWT token");
            return;
        } catch (IllegalArgumentException e) {
            logger.info("[JwtFilter] JWT token is illegal or null.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is illegal or null");
            return;
        }

        logger.info("[JwtFilter] And, it's a valid token.");
        logger.info("[JwtFilter] username: {}", claim.get("username"));

//        var arr = claim.get("authorities").toString().split(",");
//        var authorities = Arrays.stream(arr)
//                .map(SimpleGrantedAuthority::new).toList();

        User user = userRepository.getByNickname(claim.get("username").toString());
        var authToken = new UsernamePasswordAuthenticationToken(
                user, ""
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.info("[JwtFilter] End of graceful authentication.");
        filterChain.doFilter(request, response);
    }
}
