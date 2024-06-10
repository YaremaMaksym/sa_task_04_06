package yaremax.com.sa_task_04_06.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * This class contains the configuration for Spring Security.
 * It configures the password encoder and authentication manager.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * This method returns the password encoder bean.
     * It uses the BCryptPasswordEncoder to hash passwords.
     *
     * @return the password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This method returns the authentication manager bean.
     * It uses the AuthenticationConfiguration to get the authentication manager.
     *
     * @param config the authentication configuration
     * @return the authentication manager bean
     * @throws Exception if an error occurs
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * This method returns the authentication provider bean.
     * It uses the DaoAuthenticationProvider to authenticate users.
     * The user details service and password encoder are set for the authentication provider.
     *
     * @param userDetailsService the user details service
     * @param passwordEncoder the password encoder
     * @return the authentication provider bean
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                         PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }
}
