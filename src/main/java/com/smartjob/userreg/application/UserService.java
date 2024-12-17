package com.smartjob.userreg.application;

import com.smartjob.userreg.domain.UserSJ;
import com.smartjob.userreg.domain.vo.PhoneRegistration;
import com.smartjob.userreg.domain.vo.UserRegistration;
import com.smartjob.userreg.infrastructure.repository.UserRepository;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserPhoneSaver userPhoneSaver;

    public UserService(UserRepository userRepository, UserPhoneSaver userPhoneSaver) {
        this.userRepository = userRepository;
        this.userPhoneSaver = userPhoneSaver;
    }

    public List<UserRegistration> findAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserRegistration(
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getPhones().stream()
                                .map(phone -> new PhoneRegistration(phone.getNumber(), phone.getCityCode(),
                                        phone.getCountryCode()))
                                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public Optional<UserSJ> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public UserSJ create(UserRegistration userRegistration, String token) {
        if (userRepository.existsUserSJByEmail(userRegistration.email())) {
            throw new InvalidParameterException("The user's email is already registered.");
        }
        UserSJ user = new UserSJ();
        user.setName(userRegistration.name());
        user.setEmail(userRegistration.email());
        user.setPassword(userRegistration.password());
        user.setUuid(UUID.randomUUID().toString());
        user.setLastLogin(new Date());
        user.setActive(Boolean.TRUE);
        user.setToken(token);
        userRepository.save(user);
        userPhoneSaver.create(user, userRegistration.phones());
        return user;
    }

    @Transactional
    public UserSJ update(String uuid, UserRegistration newUserInfo, String token) {
        Optional<UserSJ> userOptional = userRepository.findByUuid(uuid);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("The user with the provided uuid does not exist");
        }
        UserSJ user = userOptional.get();
        if (!user.getEmail().equalsIgnoreCase(newUserInfo.email()) && userRepository.existsUserSJByEmail(newUserInfo.email())) {
            throw new InvalidParameterException("The user's email is already registered.");
        }
        user.setName(newUserInfo.name());
        user.setEmail(newUserInfo.email());
        user.setPassword(newUserInfo.password());
        user.setLastLogin(new Date());
        user.setModifiedAt(new Date());
        user.setToken(token);
        userPhoneSaver.update(user, newUserInfo.phones());
        userRepository.save(user);
        return user;
    }

    @Transactional
    public void delete(String uuid) {
        Optional<UserSJ> userOptional = userRepository.findByUuid(uuid);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("The user with the provided uuid does not exist");
        }
        userRepository.delete(userOptional.get());
    }

}
