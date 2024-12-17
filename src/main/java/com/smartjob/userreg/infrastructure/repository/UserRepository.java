package com.smartjob.userreg.infrastructure.repository;

import com.smartjob.userreg.domain.UserSJ;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserSJ, Long> {

    boolean existsUserSJByEmail(String email);

    Optional<UserSJ> findByUuid(String uuid);

    void deleteByUuid(String uuid);
}
