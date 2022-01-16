package com.github.throyer.common.springboot.controllers.api;

import javax.validation.Valid;

import com.github.throyer.common.springboot.domain.session.model.RefreshTokenRequest;
import com.github.throyer.common.springboot.domain.session.model.RefreshTokenResponse;
import com.github.throyer.common.springboot.domain.session.model.TokenRequest;
import com.github.throyer.common.springboot.domain.session.model.TokenResponse;
import com.github.throyer.common.springboot.domain.session.service.CreateTokenService;
import com.github.throyer.common.springboot.domain.session.service.RefreshTokenService;
import static com.github.throyer.common.springboot.utils.Responses.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessions")
public class SessionsController {

    @Autowired
    private CreateTokenService createService;
    
    @Autowired
    private RefreshTokenService refreshService;

    @PostMapping
    public ResponseEntity<TokenResponse> create(@RequestBody @Valid TokenRequest request) {
        var token = createService.create(request);
        return ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        var token = refreshService.refresh(request);
        return ok(token);
    }
}
