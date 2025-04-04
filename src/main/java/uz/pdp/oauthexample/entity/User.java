package uz.pdp.oauthexample.entity;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.oauthexample.entity.template.AbsLongEntity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 Created by: Mehrojbek
 DateTime: 12/02/25 19:47
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "users")
public class User extends AbsLongEntity implements UserDetails {

    private String username;

    private String password;

    private boolean enabled;

    private String name;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
