package yaremax.com.sa_task_04_06.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yaremax.com.sa_task_04_06.entity.User;
import yaremax.com.sa_task_04_06.repository.UserRepository;


/**
 * This class is service for user details.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Retrieves {@link UserDetails} by {@link User} email.
     *
     * @param  username  the email of the {@link User} to retrieve
     * @return           the {@link UserDetails}
     * @throws UsernameNotFoundException if the {@link User} is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with username " + username + " not found"));
    }
}
