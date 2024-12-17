package com.smartjob.userreg.application;

import com.smartjob.userreg.domain.Phone;
import com.smartjob.userreg.domain.UserSJ;
import com.smartjob.userreg.domain.vo.PhoneRegistration;
import com.smartjob.userreg.infrastructure.repository.PhoneRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class UserPhoneSaver {

    private final PhoneRepository phoneRepository;

    public UserPhoneSaver(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Transactional
    public void create(UserSJ user, List<PhoneRegistration> phones) {
        if (phones.isEmpty()) {
            return;
        }
        phones.forEach(phoneRegistration -> {
            create(user, phoneRegistration);
        });
    }

    @Transactional
    public void create(UserSJ user, PhoneRegistration phoneRegistration) {
        Phone phone = toPhone(user, phoneRegistration);
        phoneRepository.save(phone);
    }

    @Transactional
    public void update(UserSJ user, List<PhoneRegistration> newPhones) {
        if (CollectionUtils.isEmpty(newPhones)) {
            phoneRepository.deleteAll(user.getPhones());
            user.setPhones(new HashSet<>());
            return;
        }
        if (CollectionUtils.isEmpty(user.getPhones())) {
            create(user, newPhones);
            return;
        }
        Set<Phone> newSetPhones =  newPhones.stream()
                .map(phoneRegistration -> toPhone(user, phoneRegistration))
                .collect(Collectors.toSet());

        Set<Phone> toRemove = user.getPhones().stream()
                .filter(phone -> !newSetPhones.contains(phone))
                .collect(Collectors.toSet());

        phoneRepository.deleteAll(toRemove);
        user.getPhones().addAll(newSetPhones);
        phoneRepository.saveAll(user.getPhones());
    }

    public Phone toPhone(UserSJ user, PhoneRegistration phoneRegistration) {
        Phone phone = new Phone();
        phone.setUser(user);
        phone.setNumber(phoneRegistration.number());
        phone.setCityCode(phoneRegistration.cityCode());
        phone.setCountryCode(phoneRegistration.countryCode());
        return phone;
    }

}
