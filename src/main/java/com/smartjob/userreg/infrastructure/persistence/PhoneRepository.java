package com.smartjob.userreg.infrastructure.persistence;

import com.smartjob.userreg.domain.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

}
