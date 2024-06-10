package yaremax.com.sa_task_04_06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yaremax.com.sa_task_04_06.entity.User;

import java.util.Optional;


/**
 * Jpa repository for {@link User}.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Checks if a {@link User} with the given email exists.
     *
     * @param  email  the email of the user to check
     * @return        true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds a {@link User} by email.
     *
     * @param  email  the email of the user to find
     * @return        the user with the given email, or empty if the user does not exist
     */
    Optional<User> findByEmail(String email);

}
