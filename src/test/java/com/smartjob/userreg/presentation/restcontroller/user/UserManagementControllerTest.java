package com.smartjob.userreg.presentation.restcontroller.user;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartjob.userreg.application.UserNotFoundException;
import com.smartjob.userreg.application.user.service.UserManagementService;
import com.smartjob.userreg.domain.entity.UserSJ;
import com.smartjob.userreg.domain.valueobject.PhoneRegistration;
import com.smartjob.userreg.domain.valueobject.UserRegistration;
import com.smartjob.userreg.shared.config.ValidationRegexPropertiesReader;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserManagementController.class)
@Import(ValidationRegexPropertiesReader.class)
class UserManagementControllerTest {

    @Mock
    private HttpServletRequest request;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserManagementService userManagementService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MzQ0MzE3MTN9.MOPyI4YCoKHRZha7SoG-EaXD80RCloLUN3FZn7bhUjk";

    /**
     * Test for retrieving all users. It mocks the service call and verifies the response structure and status
     */
    @Test
    void testGetAllUsers() throws Exception {
        PhoneRegistration phone1 = new PhoneRegistration("9-6666-7777", "45", "56");
        UserRegistration user1 = new UserRegistration("user1", "user1@domain.cl", "Password1!", Arrays.asList(phone1));
        List<UserRegistration> users = Arrays.asList(user1);

        when(userManagementService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(user1.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(user1.email()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].password").value(user1.password()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phones[0].number").value(phone1.number()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phones[0].cityCode").value(phone1.cityCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phones[0].countryCode").value(phone1.countryCode()));
    }

    /**
     * Test to register a new user. It mocks the service call and verifies the response structure and status.
     */
    @Test
    void testCreateValidUser() throws Exception {
        PhoneRegistration phone1 = new PhoneRegistration("9-6666-7777", "45", "56");
        UserRegistration userInfo = new UserRegistration("user1", "user1@domain.cl", "Password1!!",
                Arrays.asList(phone1));

        String uuid = UUID.randomUUID().toString();
        Date createdAt = new Date();
        UserSJ user = new UserSJ();
        user.setUuid(uuid);
        user.setToken(TOKEN);
        user.setCreatedAt(createdAt);
        user.setLastLogin(createdAt);

        when(userManagementService.createUser(userInfo, TOKEN)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsString(userInfo))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value(uuid))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(TOKEN))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastLogin").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(Boolean.TRUE));
    }

    /**
     * Test to verify that a bad request is returned when an invalid email is provided during user registration.
     */
    @Test
    void testShouldReturnBadRequestDueToInvalidEmail() throws Exception {
        String invalidEmail = "user1@outlook.com";
        PhoneRegistration phone1 = new PhoneRegistration("9-6666-7777", "45", "56");
        UserRegistration userInfo = new UserRegistration("user1", invalidEmail, "Password1!!",
                Arrays.asList(phone1));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsString(userInfo))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(UserRegistration.INVALID_EMAIL_MESSAGE));
    }

    /**
     * Test to verify that a bad request is returned when an invalid password is provided during user registration.
     */
    @Test
    void testShouldReturnBadRequestDueToInvalidPassword() throws Exception {
        String invalidPassword = "myPassword123";
        PhoneRegistration phone1 = new PhoneRegistration("9-6666-7777", "45", "56");
        UserRegistration userInfo = new UserRegistration("user1", "user1@domain.cl", invalidPassword,
                Arrays.asList(phone1));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsString(userInfo))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(UserRegistration.INVALID_PASSWORD_MESSAGE));
    }

    /**
     * Test for updating an existing user. It mocks the service call and verifies the response structure and status.
     */
    @Test
    void testUpdateUser() throws Exception {
        PhoneRegistration phone = new PhoneRegistration("9-6666-7777", "45", "56");
        UserRegistration userInfo = new UserRegistration("user1", "user1@domain.cl", "Password1!!",
                Arrays.asList(phone));

        String uuid = UUID.randomUUID().toString();
        Date createdAt = new Date();
        UserSJ user = new UserSJ();
        user.setUuid(uuid);
        user.setToken(TOKEN);
        user.setCreatedAt(createdAt);
        user.setLastLogin(createdAt);

        when(userManagementService.createUser(userInfo, TOKEN)).thenReturn(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsString(userInfo))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        UserSJ createdUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserSJ.class);

        PhoneRegistration newPhone = new PhoneRegistration("9-6666-8888", "45", "56");
        String newPassword = "New[)aU0_p101";
        UserRegistration newUserInfo = new UserRegistration("user1", "user1@domain.cl", newPassword,
                Arrays.asList(newPhone));

        Date modifiedAt = new Date();
        UserSJ updatedUser = new UserSJ();
        updatedUser.setUuid(createdUser.getUuid());
        updatedUser.setToken(TOKEN);
        updatedUser.setModifiedAt(modifiedAt);
        updatedUser.setLastLogin(modifiedAt);

        when(userManagementService.updateUser(createdUser.getUuid(), newUserInfo, TOKEN)).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/users/{uuid}", createdUser.getUuid())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsString(newUserInfo))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value(uuid))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(TOKEN))
                .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt").exists());
    }

    /*
     * Test to verify that a user is not found when requesting a user by its uuid that does not exist in the database.
     */
    @Test
    void testShouldReturnUserNotFoundByUuid() throws Exception {
        UserRegistration userInfo = new UserRegistration("user1", "user1@domain.cl", "Password1!!", new ArrayList<>());
        String uuid = UUID.randomUUID().toString();

        when(userManagementService.updateUser(uuid, userInfo, TOKEN)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/users/{uuid}", uuid)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsString(userInfo))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Test for deleting an existing user.
     * It mocks the service call and verifies the response status.
     */
    @Test
    void testDeleteUser() throws Exception {
        String uuid = UUID.randomUUID().toString();
        doNothing().when(userManagementService).deleteUser(uuid);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/users/{uuid}", uuid))
                .andExpect(status().isNoContent());
    }
}