package com.smartjob.userreg.application.user.service;

import com.smartjob.userreg.application.UserNotFoundException;
import com.smartjob.userreg.domain.entity.UserSJ;
import com.smartjob.userreg.domain.valueobject.PhoneRegistration;
import com.smartjob.userreg.domain.valueobject.UserRegistration;
import com.smartjob.userreg.infrastructure.persistence.UserRepository;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManagementService {

    private final UserRepository userRepository;

    private final UserPhoneManagementService userPhoneManagementService;

    public UserManagementService(UserRepository userRepository, UserPhoneManagementService userPhoneManagementService) {
        this.userRepository = userRepository;
        this.userPhoneManagementService = userPhoneManagementService;
    }

    /**
     * Get all list of registered users.
     * @return List of registered users
     */
    public List<UserRegistration> getAllUsers() {
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

    /**
     * This method registers a new user
     * @param userRegistration Information of the new user to register
     * @param token a validated jwt token
     * @return the new created user
     */
    @Transactional
    public UserSJ createUser(UserRegistration userRegistration, String token) {
        if (userRepository.existsUserSJByEmail(userRegistration.email())) {
            throw new InvalidParameterException("The provided email is already registered or belongs to another user, please enter another email.");
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
        userPhoneManagementService.createUserPhones(user, userRegistration.phones());
        return user;
    }

    /**
     * This method updates a register user given their user identifier
     * @param uuid The identifier from registered user
     * @param newUserInfo user information to update
     * @param token a validated jwt token
     * @return the updated user
     */
    @Transactional
    public UserSJ updateUser(String uuid, UserRegistration newUserInfo, String token) {
        Optional<UserSJ> userOptional = userRepository.findByUuid(uuid);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("The user uuid you provided is invalid or does not exist, please enter a valid one");
        }
        UserSJ user = userOptional.get();
        if (!user.getEmail().equalsIgnoreCase(newUserInfo.email()) && userRepository.existsUserSJByEmail(newUserInfo.email())) {
            throw new InvalidParameterException("The provided email is already registered or belongs to another user, please enter another email.");
        }
        user.setName(newUserInfo.name());
        user.setEmail(newUserInfo.email());
        user.setPassword(newUserInfo.password());
        user.setLastLogin(new Date());
        user.setModifiedAt(new Date());
        user.setToken(token);
        userPhoneManagementService.updateUserPhones(user, newUserInfo.phones());
        userRepository.save(user);
        return user;
    }

    /**
     * This method deletes a registered user given their user identifier
     * @param uuid The identifier from registered user
     */
    @Transactional
    public void deleteUser(String uuid) {
        Optional<UserSJ> userOptional = userRepository.findByUuid(uuid);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("The uuis of the user you wish to delete is invalid or does not exist, please enter a valid one");
        }
        userRepository.delete(userOptional.get());
    }

}
