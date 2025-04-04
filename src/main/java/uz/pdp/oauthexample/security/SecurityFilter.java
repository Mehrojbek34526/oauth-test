package uz.pdp.oauthexample.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.oauthexample.entity.User;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

/**
 Created by: Mehrojbek
 DateTime: 12/02/25 21:31
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        writeLog(request);

        checkToken(request);

        filterChain.doFilter(request, response);

        log.info("Response : URL: {}, statusCode: {}", request.getRequestURL(), response.getStatus());
    }

    private void checkToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        //bearer auth
        if (Objects.nonNull(authorization) && authorization.startsWith("Bearer ")) {

            String token = authorization.substring(7);

            String username = jwtProvider.validateToken(token);

            User user = (User) authService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //basic auth
        else if (Objects.nonNull(authorization) && authorization.startsWith("Basic ")) {

            //Basic base64(username:password)
            String basicToken = authorization.substring(6);

            String usernamePassword = new String(Base64.getDecoder().decode(basicToken));

            String[] strings = usernamePassword.split(":");

            String username = strings[0];
            String password = strings[1];

            User user = (User) authService.loadUserByUsername(username);

            boolean matches = passwordEncoder.matches(password, user.getPassword());
            if (matches) {

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    }

    private void writeLog(HttpServletRequest request) {
        try {

            String url = request.getRequestURL().toString();
            String method = request.getMethod();

            log.info("Request -> URI: {}, Method: {}", url, method);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
