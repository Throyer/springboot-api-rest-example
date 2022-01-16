package com.github.throyer.common.springboot.domain.recovery.model;

import static java.lang.String.format;
import static org.springframework.beans.BeanUtils.copyProperties;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

@Data
@NoArgsConstructor
public class Update {

    @Email
    @NotNull
    @NotEmpty
    private String email;

    private String first = "";
    private String second = "";
    private String third = "";
    private String fourth = "";

    @NotEmpty(message = "Please provide a password.")
    @Size(min = 8, max = 255, message = "The password must contain at least {min} characters.")
    private String password;

    public Update(Codes codes) {
        copyProperties(codes, this);
    }
        
    public void validate(BindingResult result) { }

    public String code() {
        return format("%s%s%s%s", first, second, third, fourth);
    }
}
