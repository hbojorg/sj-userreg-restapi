package com.smartjob.userreg.infrastructure.persistence;

import com.smartjob.userreg.domain.entity.UserSJ;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserSJ, Long> {

    /**
     * Checks if there is any user in the database with the provided email
     * @param email
     * @return 'true' if a user exists with that email, otherwise 'false'
     */
    boolean existsUserSJByEmail(String email);

    /**
     * Finds a user by their UUID identifier
     * @param uuid user identifier
     * @return an optional of the user entity.
     */
    Optional<UserSJ> findByUuid(String uuid);

}
