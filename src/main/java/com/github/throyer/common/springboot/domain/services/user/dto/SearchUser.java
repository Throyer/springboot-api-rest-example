package com.github.throyer.common.springboot.domain.services.user.dto;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SearchUser {
    private Optional<String> name = Optional.empty();
    private Optional<String> email = Optional.empty();
    private List<String> roles = List.of();



    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        if (Objects.isNull(roles)) {
            throw new RuntimeException("Não é possível setar roles como null.");
        }
        this.roles = roles;
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }
}
