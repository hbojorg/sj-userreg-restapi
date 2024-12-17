package com.smartjob.userreg.interfaces;

import com.smartjob.userreg.infrastructure.config.JWTProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/token")
@Tag(name = "Token", description = "API to generate JWT token")
public class TokenAPIController {

    private final JWTProvider jwtProvider;

    public TokenAPIController(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Operation(summary = "Generate a JWT token")
    @GetMapping
    public ResponseEntity<TokenAPI> generate() {
        TokenAPI tokenAPI = new TokenAPI(jwtProvider.generateToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenAPI);
    }

}
