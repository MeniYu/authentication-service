package io.incondensable.security.userdetails;

import io.incondensable.application.business.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;

/**
 * @author abbas
 */
public class MeniYuUserDetails implements UserDetails {

    private final User user;

    public MeniYuUserDetails(User user) {
        this.user = user;
        this.user.setPassword(new BCryptPasswordEncoder().encode(this.user.getPassword()));
    }

    public User getUser() {
        return user;
    }

    private List<GrantedAuthority> grantedAuthorityList() {
        return List.of(new SimpleGrantedAuthority(user.getRole().getRoleStr()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorityList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
