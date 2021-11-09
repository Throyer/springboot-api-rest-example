package com.github.throyer.common.springboot.domain.services.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Codes {
    
    private String first;
    private String second;
    private String third;
    private String fourth;
    
    @Email
    @NotNull
    @NotEmpty
    private String email;
    
    public String code() {
        return String.format("%s%s%s%s", first, second, third, fourth);
    }
}
