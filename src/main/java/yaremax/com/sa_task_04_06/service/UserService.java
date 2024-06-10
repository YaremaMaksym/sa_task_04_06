package yaremax.com.sa_task_04_06.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yaremax.com.sa_task_04_06.entity.User;
import yaremax.com.sa_task_04_06.exception.custom.DuplicateResourceException;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.UserRepository;


/**
 * Service class for {@link User}.
 * It provides methods for finding and creating users.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Finds a {@link User} by email.
     *
     * @param  email  the email of the {@link User}
     * @return        the {@link User} with the given email
     * @throws ResourceNotFoundException if the user is not found
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
    }

    /**
     * Creates a new {@link User}.
     *
     * @param  user  the {@link User} to create
     * @throws DuplicateResourceException if the {@link User} already exists
     */
    @Transactional
    public void createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User with email " + user.getEmail() + " already exists");
        }
        userRepository.save(user);
    }
}
