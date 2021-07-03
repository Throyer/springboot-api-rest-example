package com.github.throyer.common.springboot.api.domain.services.security.dto;

import static com.github.throyer.common.springboot.api.utils.Constants.SECURITY.HOUR_IN_SECONDS;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.throyer.common.springboot.api.domain.models.entity.RefreshToken;
import com.github.throyer.common.springboot.api.domain.models.entity.User;

public class SessionResponse {    
    private final UserDetails user;
    private final String token;
    private final RefreshToken refreshToken;
    private final Long expiresIn;
    private final String type = "Bearer";
    
    public SessionResponse(
        User user,
        String token,
        RefreshToken refreshToken,
        Integer tokenExpirationInHours
    ) {
        this.user = new UserDetails(user);
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresIn = (tokenExpirationInHours * HOUR_IN_SECONDS);
    }

    public UserDetails getUser() {
        return user;
    }

    @JsonProperty("access_token")
    public String getToken() {
        return token;
    }

    @JsonProperty("refresh_token")
    public String getRefresh() {
        return refreshToken.getCode();
    }

    @JsonProperty("expires_in")
    public Long getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
        return type;
    }

    public class UserDetails {
        private final Long id;
        private final String name;
        private final String email;
        private final List<String> roles;

        public UserDetails(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
            this.roles = user.getRoles()
                .stream()
                    .map(role -> role.getAuthority())
                        .toList();
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public List<String> getRoles() {
            return roles;
        }
    }
}
