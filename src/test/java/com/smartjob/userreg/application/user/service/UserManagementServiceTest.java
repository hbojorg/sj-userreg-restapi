package com.smartjob.userreg.application.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.smartjob.userreg.application.UserNotFoundException;
import com.smartjob.userreg.domain.entity.Phone;
import com.smartjob.userreg.domain.entity.UserSJ;
import com.smartjob.userreg.domain.valueobject.PhoneRegistration;
import com.smartjob.userreg.domain.valueobject.UserRegistration;
import com.smartjob.userreg.infrastructure.persistence.UserRepository;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class UserManagementServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserPhoneManagementService userPhoneManagementService;

    @Autowired
    private UserManagementService userManagementService;

    private UserRegistration validUserInfo;

    private UserRegistration validUserInfoToUpdate;

    private PhoneRegistration validPhone;

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MzQ0MzE3MTN9.MOPyI4YCoKHRZha7SoG-EaXD80RCloLUN3FZn7bhUjk";

    @BeforeEach
    void setUp() {
        validPhone = new PhoneRegistration("9-6666-7777", "45", "56");
        validUserInfo = new UserRegistration("user1", "user1@domain.cl", "Password1!!", Arrays.asList(validPhone));
        validUserInfoToUpdate = new UserRegistration("Jose Perez", "jose.perez@ucb.cl", "@dM1n.!!enD", Arrays.asList(validPhone));
    }

    /**
     * Test for retrieving all users.
     */
    @Test
    void testGetAllUsers() {
        String userUuid = UUID.randomUUID().toString();
        UserSJ registeredUser = new UserSJ();
        registeredUser.setUuid(userUuid);
        registeredUser.setName(validUserInfo.name());
        registeredUser.setEmail(validUserInfo.email());
        registeredUser.setPassword(validUserInfo.password());
        Phone phone = new Phone();
        phone.setNumber(validPhone.number());
        phone.setCountryCode(validPhone.countryCode());
        phone.setCityCode(validPhone.cityCode());
        registeredUser.setPhones(new HashSet<>(Arrays.asList(phone)));
        List<UserSJ> registeredUsers = Arrays.asList(registeredUser);

        when(userRepository.findAll()).thenReturn(registeredUsers);

        List<UserRegistration> users = userManagementService.getAllUsers();

        verify(this.userRepository, times(1)).findAll();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(registeredUser.getEmail(), users.get(0).email());
        assertEquals(registeredUser.getPassword(), users.get(0).password());
    }

    /**
     * Test to verify that an exception is thrown when trying to create a user with an already registered email.
     */
    @Test
    void testCreateUserShouldReturnExceptionDueToEmailIsAlreadyRegistered() {
        when(userRepository.existsUserSJByEmail(validUserInfo.email())).thenReturn(Boolean.TRUE);

        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            userManagementService.createUser(validUserInfo, TOKEN);
        });

        assertTrue(exception.getMessage().contains(UserManagementService.EMAIL_ALREADY_REGISTERED_MESSAGE));
        verify(userRepository, never()).save(any(UserSJ.class));
    }

    /**
     * Test to register a valid user.
     */
    @Test
    void testCreateUser() {
        UserSJ createdUser = new UserSJ();
        createdUser.setName(validUserInfo.name());
        createdUser.setEmail(validUserInfo.email());
        createdUser.setPassword(validUserInfo.password());

        when(userRepository.existsUserSJByEmail(validUserInfo.email())).thenReturn(Boolean.FALSE);
        when(userRepository.save(createdUser)).thenReturn(createdUser);
        doNothing().when(userPhoneManagementService).createUserPhones(any(UserSJ.class), any());

        userManagementService.createUser(validUserInfo, TOKEN);
        verify(this.userRepository, times(1)).save(any());
        verify(this.userPhoneManagementService, times(1)).createUserPhones(any(UserSJ.class), any());
    }

    /**
     * Test for updating a user.
     * This test ensures that an exception is thrown when the user is not found in the repository.
     */
    @Test
    void testUpdateUserShouldReturnExceptionDueToUserNotFound() {
        String userUuid = UUID.randomUUID().toString();
        when(userRepository.findByUuid(userUuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userManagementService.updateUser(userUuid, validUserInfo, TOKEN);
        });

        assertTrue(exception.getMessage().contains(UserManagementService.USER_NOT_FOUND_MESSAGE));
        verify(userPhoneManagementService, never()).updateUserPhones(any(UserSJ.class), any());
        verify(userRepository, never()).save(any(UserSJ.class));
    }

    /*
     * Test for updating a valid user.
     */
    @Test
    void testUpdateUser() {
        String userUuid = UUID.randomUUID().toString();
        UserSJ registeredUser = new UserSJ();
        registeredUser.setUuid(userUuid);
        registeredUser.setName(validUserInfo.name());
        registeredUser.setEmail(validUserInfo.email());
        registeredUser.setPassword(validUserInfo.password());

        when(userRepository.findByUuid(userUuid)).thenReturn(Optional.of(registeredUser));
        when(userRepository.existsUserSJByEmail(validUserInfoToUpdate.email())).thenReturn(Boolean.FALSE);
        when(userRepository.save(registeredUser)).thenReturn(registeredUser);
        doNothing().when(userPhoneManagementService).createUserPhones(any(UserSJ.class), any());

        UserSJ updatedUser = userManagementService.updateUser(userUuid, validUserInfoToUpdate, TOKEN);

        assertNotNull(updatedUser);
        assertEquals(updatedUser.getEmail(), updatedUser.getEmail());
        verify(this.userRepository, times(1)).save(any());
        verify(this.userPhoneManagementService, times(1)).updateUserPhones(any(UserSJ.class), any());
    }

    /**
     * Test to ensure that an exception is thrown when attempting to delete a user that doesn't exist.
     */
    @Test
    void testDeleteUserShouldReturnExceptionDueToUserNotFound() {
        String userUuid = UUID.randomUUID().toString();
        when(userRepository.findByUuid(userUuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userManagementService.deleteUser(userUuid);
        });

        assertTrue(exception.getMessage().contains(UserManagementService.DELETE_USER_NOT_FOUND_MESSAGE));
        verify(userRepository, never()).delete(any(UserSJ.class));
    }

    /*
     * Test for deleting a user.
     */
    @Test
    void testDeleteUser() {
        String userUuid = UUID.randomUUID().toString();
        UserSJ registeredUser = new UserSJ();
        registeredUser.setUuid(userUuid);
        registeredUser.setName(validUserInfo.name());
        registeredUser.setEmail(validUserInfo.email());
        registeredUser.setPassword(validUserInfo.password());

        when(userRepository.findByUuid(userUuid)).thenReturn(Optional.of(registeredUser));
        doNothing().when(userRepository).delete(any(UserSJ.class));

        userManagementService.deleteUser(userUuid);

        verify(userRepository, times(1)).delete(any(UserSJ.class));
    }
}