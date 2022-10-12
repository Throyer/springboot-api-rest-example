package com.github.throyer.example.modules.ssr.dtos;

import static java.lang.String.format;
import static org.springframework.beans.BeanUtils.copyProperties;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.validation.BindingResult;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePasswordWithRecoveryCodeByApp {

    @Email(message = "{recovery.email.is-valid}")
    @NotEmpty(message = "{recovery.email.not-empty}")
    private String email;

    private String first = "";
    private String second = "";
    private String third = "";
    private String fourth = "";

    @NotEmpty(message = "{user.password.not-empty}")
    @Size(min = 8, max = 155, message = "{user.password.size}")
    private String password;

    public UpdatePasswordWithRecoveryCodeByApp(Codes codes) {
        copyProperties(codes, this);
    }

    public void validate(BindingResult result) {
    }

    public String code() {
        return format("%s%s%s%s", first, second, third, fourth);
    }
}
