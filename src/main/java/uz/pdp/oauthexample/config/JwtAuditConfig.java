package uz.pdp.oauthexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.oauthexample.entity.User;

import java.util.Optional;

/**
 Created by: Mehrojbek
 DateTime: 17/02/25 21:01
 **/
@EnableJpaAuditing
@Configuration
public class JwtAuditConfig {

    @Bean
    public AuditorAware<Long> auditor() {
        return () -> {

            try {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                Authentication authentication = securityContext.getAuthentication();
                Object principal = authentication.getPrincipal();

                if (principal.toString().equals("anonymousUser")) {
                    return Optional.empty();
                }

                User user = (User) principal;

                return Optional.of(user.getId());
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }

}
