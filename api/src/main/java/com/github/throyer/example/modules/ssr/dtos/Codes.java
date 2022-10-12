package com.github.throyer.example.modules.ssr.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Codes {
    
    private String first = "";
    private String second = "";
    private String third = "";
    private String fourth = "";
    
    @Email
    @NotNull
    @NotEmpty
    private String email;

    public Codes(String email) {
        this.email = email;
    }
        
    public String code() {
        return String.format("%s%s%s%s", first, second, third, fourth);
    }
}
