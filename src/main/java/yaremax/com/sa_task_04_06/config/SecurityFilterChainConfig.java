package yaremax.com.sa_task_04_06.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * This class contains the configuration for the Spring Security filter chain.
 * It configures the security filter chain and sets up the authentication provider.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilterChainConfig {

    private final JwtAuthenticationFilterConfig jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private final DelegatedAuthenticationEntryPoint authEntryPoint;

    /**
     * This method configures the Spring Security filter chain and sets up the authentication provider.
     * It configures the CSRF protection to be disabled, and sets up authentication for the "/api/v1/auth/**" path.
     * Any other request requires authentication.
     * It also sets up the authentication entry point for unauthenticated requests.
     * It sets up the session creation policy to be stateless.
     * It adds the JWT authentication filter before the username password authentication filter.
     *
     * @param http the HttpSecurity object
     * @return SecurityFilterChain
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests()
                    .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                            "/swagger-resources/**", "/webjars/**").permitAll() // Documentation
                    .antMatchers("/api/v1/auth/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
