package com.smartjob.userreg.presentation.restcontroller.user;

import com.smartjob.userreg.application.user.service.UserManagementService;
import com.smartjob.userreg.domain.entity.UserSJ;
import com.smartjob.userreg.domain.valueobject.UserRegistration;
import com.smartjob.userreg.infrastructure.security.AuthorizationHeaderTokenExtractor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "APIs to user registration")
public class UserManagementController {

    private final UserManagementService userManagementService;

    private final HttpServletRequest request;

    public UserManagementController(UserManagementService userManagementService, HttpServletRequest request) {
        this.userManagementService = userManagementService;
        this.request = request;
    }

    /**
     * Get all list of registered users.
     * @return List of registered users
     */
    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<List<UserRegistration>> getAllUsers() {
        List<UserRegistration> users = userManagementService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * This method registers a user
     * @param userRegistration user information to register
     * @return the new registered user
     */
    @Operation(summary = "Add a new user")
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegistration userRegistration) {
        UserSJ user = userManagementService.createUser(userRegistration, getToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * This method updates a register user given their user identifier
     * @param uuid The identifier from registered user
     * @param userRegistration user information to update
     * @return the updated user
     */
    @Operation(summary = "Update an existing user")
    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateUser(@PathVariable String uuid, @Valid @RequestBody UserRegistration userRegistration) {
        UserSJ user = userManagementService.updateUser(uuid, userRegistration, getToken());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    /**
     * This method deletes a registered user given their user identifier
     * @param uuid The identifier from registered user
     * @return without content
     */
    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<?> deleteUser(@PathVariable String uuid) {
        userManagementService.deleteUser(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves the JWT token from the request.
     * @return the JWT token as a String.
     */
    private String getToken() {
        return AuthorizationHeaderTokenExtractor.getBearerToken(request.getHeader(HttpHeaders.AUTHORIZATION));
    }

}
