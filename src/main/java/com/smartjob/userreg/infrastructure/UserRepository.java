package com.smartjob.userreg.infrastructure;

import com.smartjob.userreg.domain.UserSJ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserSJ, Long> {

}
