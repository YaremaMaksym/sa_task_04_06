package yaremax.com.sa_task_04_06.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * User entity for Spring Security.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    /**
     * ID of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Email of the user.
     */
    @Column(nullable = false)
    private String email;

    /**
     * Password of the user.
     */
    @Column(nullable = false)
    private String password;

    @Override
    /**
     * Retrieves the authorities of the user.
     *
     * @return the authorities of the user
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(("Authenticated user")));
        return authorities;
    }

    @Override
    /**
     * Retrieves the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return this.password;
    }

    @Override
    /**
     * Retrieves the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    /**
     * Checks if the user's account is non-expired.
     *
     * @return true if the user's account is non-expired
     */
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    /**
     * Checks if the user's account is non-locked.
     *
     * @return true if the user's account is non-locked
     */
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    /**
     * Checks if the user's credentials are non-expired.
     *
     * @return true if the user's credentials are non-expired
     */
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    /**
     * Checks if the user is enabled.
     *
     * @return true if the user is enabled
     */
    public boolean isEnabled() {
        return true;
    }
}
