package yaremax.com.sa_task_04_06.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class is responsible for handling authentication exceptions when the user is not authenticated.
 * It is used to customize the response to the user when they are not authenticated.
 * This class is a Spring bean and is wired into the Spring Security configuration.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Component("delegatedAuthenticationEntryPoint")
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * The HandlerExceptionResolver object used to resolve exceptions. This is used to customize the response to the user
     * when they are not authenticated.
     */
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    /**
     * This method is called by Spring Security when a user is not authenticated. It resolves the exception and sends
     * the response to the user.
     *
     * @param request      the HTTP request
     * @param response     the HTTP response
     * @param authException the authentication exception
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        resolver.resolveException(request, response, null, authException);
    }
}
