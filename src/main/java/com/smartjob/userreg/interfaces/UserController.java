package com.smartjob.userreg.interfaces;

import com.smartjob.userreg.application.ResourceNotFoundException;
import com.smartjob.userreg.application.UserService;
import com.smartjob.userreg.domain.UserSJ;
import com.smartjob.userreg.domain.vo.UserRegistration;
import com.smartjob.userreg.infrastructure.config.JWTProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.security.InvalidParameterException;
import java.util.List;
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
public class UserController {

    private final UserService userService;

    private final HttpServletRequest request;

    public UserController(UserService userService, HttpServletRequest request) {
        this.userService = userService;
        this.request = request;
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<List<UserRegistration>> all() {
        List<UserRegistration> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Add a new user")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserRegistration userRegistration) {
        try {
            UserSJ user = userService.create(userRegistration, getToken());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (InvalidParameterException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMsg.instance(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @Operation(summary = "Update an existing user")
    @PutMapping("/{uuid}")
    public ResponseEntity<?> update(@PathVariable String uuid, @Valid @RequestBody UserRegistration userRegistration) {
        try {
            UserSJ user = userService.update(uuid, userRegistration, getToken());
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (InvalidParameterException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMsg.instance(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMsg.instance(e.getMessage(), HttpStatus.NOT_FOUND.value()));
        }
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<?> delete(@PathVariable String uuid) {
        try {
            userService.delete(uuid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMsg.instance(e.getMessage(), HttpStatus.NOT_FOUND.value()));
        }
    }

    private String getToken() {
        return (String) request.getAttribute(JWTProvider.TOKEN_PARAM);
    }

}
