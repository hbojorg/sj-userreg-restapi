package com.smartjob.userreg.interfaces;

import com.smartjob.userreg.application.UserService;
import com.smartjob.userreg.domain.UserSJ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "APIs to user registration")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Add a new user")
    @PostMapping
    public ResponseEntity<UserSJ> create(@Valid @RequestBody UserSJ user) {
        user.setUuid(UUID.randomUUID().toString());
        userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
