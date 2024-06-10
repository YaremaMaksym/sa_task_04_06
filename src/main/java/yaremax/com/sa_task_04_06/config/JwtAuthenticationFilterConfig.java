package yaremax.com.sa_task_04_06.config;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import yaremax.com.sa_task_04_06.entity.User;
import yaremax.com.sa_task_04_06.service.JwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class is responsible for authenticating users using JWT tokens.
 * It intercepts incoming HTTP requests and checks if the request contains a valid JWT token.
 * If the token is valid, it authenticates the user and adds the authentication information to the security context.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilterConfig extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * This method is the main logic of the filter. It intercepts incoming HTTP requests and checks if the request
     * contains a valid JWT token. If the token is valid, it authenticates the user and adds the authentication
     * information to the security context.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String subject = jwtService.extractSubject(jwt);

        if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = (User) userDetailsService.loadUserByUsername(subject);

            if (jwtService.isTokenValid(jwt, user)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
