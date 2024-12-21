package com.smartjob.userreg.application.user.service;

import com.smartjob.userreg.domain.entity.Phone;
import com.smartjob.userreg.domain.entity.UserSJ;
import com.smartjob.userreg.domain.valueobject.PhoneRegistration;
import com.smartjob.userreg.infrastructure.persistence.PhoneRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class UserPhoneManagementService {

    private final PhoneRepository phoneRepository;

    public UserPhoneManagementService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    /**
     * This method creates the user's phones
     * @param user User entity to whom the new phones belongs
     * @param phones user's phones information to create
     */
    @Transactional
    public void createUserPhones(UserSJ user, List<PhoneRegistration> phones) {
        if (phones.isEmpty()) {
            return;
        }
        phones.forEach(phoneRegistration -> {
            createUserPhone(user, phoneRegistration);
        });
    }

    /**
     * This method creates the user's phone
     * @param user User entity to whom the new phone belongs
     * @param phoneRegistration user's phone information to create
     */
    @Transactional
    public void createUserPhone(UserSJ user, PhoneRegistration phoneRegistration) {
        Phone phone = convertToPhoneEntity(user, phoneRegistration);
        phoneRepository.save(phone);
    }

    /**
     * This method sets or updates the new phones that belong to the user
     * @param user User entity to whom the new phones belongs
     * @param newPhones new user's phones information
     */
    @Transactional
    public void updateUserPhones(UserSJ user, List<PhoneRegistration> newPhones) {
        if (CollectionUtils.isEmpty(newPhones)) {
            phoneRepository.deleteAll(user.getPhones());
            user.setPhones(new HashSet<>());
            return;
        }
        if (CollectionUtils.isEmpty(user.getPhones())) {
            createUserPhones(user, newPhones);
            return;
        }
        Set<Phone> newSetPhones =  newPhones.stream()
                .map(phoneRegistration -> convertToPhoneEntity(user, phoneRegistration))
                .collect(Collectors.toSet());

        Set<Phone> toRemove = user.getPhones().stream()
                .filter(phone -> !newSetPhones.contains(phone))
                .collect(Collectors.toSet());

        phoneRepository.deleteAll(toRemove);
        user.getPhones().addAll(newSetPhones);
        phoneRepository.saveAll(user.getPhones());
    }

    /**
     * Given a value object or user's phone information, it converts it into a phone entity.
     * @param user User entity to whom the phone belongs
     * @param phoneRegistration user's phone information
     * @return phone entity
     */
    public Phone convertToPhoneEntity(UserSJ user, PhoneRegistration phoneRegistration) {
        Phone phone = new Phone();
        phone.setUser(user);
        phone.setNumber(phoneRegistration.number());
        phone.setCityCode(phoneRegistration.cityCode());
        phone.setCountryCode(phoneRegistration.countryCode());
        return phone;
    }

}
