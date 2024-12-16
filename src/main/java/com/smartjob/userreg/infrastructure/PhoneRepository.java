package com.smartjob.userreg.infrastructure;

import com.smartjob.userreg.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

}
