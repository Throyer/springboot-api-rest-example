package com.github.throyer.common.springboot.controllers.api;

import javax.validation.Valid;

import com.github.throyer.common.springboot.domain.session.model.RefreshTokenRequest;
import com.github.throyer.common.springboot.domain.session.model.RefreshTokenResponse;
import com.github.throyer.common.springboot.domain.session.model.TokenRequest;
import com.github.throyer.common.springboot.domain.session.model.TokenResponse;
import com.github.throyer.common.springboot.domain.session.service.CreateTokenService;
import com.github.throyer.common.springboot.domain.session.service.RefreshTokenService;
import static com.github.throyer.common.springboot.utils.Responses.ok;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication")
@RequestMapping("/api/sessions")
public class SessionsController {

    private final CreateTokenService createService;
    private final RefreshTokenService refreshService;

    @Autowired
    public SessionsController(
        CreateTokenService createService,
        RefreshTokenService refreshService
    ) {
        this.createService = createService;
        this.refreshService = refreshService;
    }

    @PostMapping
    @Operation(summary = "Create a jwt token")
    public ResponseEntity<TokenResponse> create(@RequestBody @Valid TokenRequest request) {
        var token = createService.create(request);
        return ok(token);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Create a new jwt token from refresh code")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        var token = refreshService.refresh(request);
        return ok(token);
    }
}
