package com.github.throyer.common.springboot.domain.services.user.dto;

import static com.github.throyer.common.springboot.domain.services.user.dto.CreateUserApi.STRONG_PASSWORD;
import static com.github.throyer.common.springboot.domain.services.user.dto.CreateUserApi.STRONG_PASSWORD_MESSAGE;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Data
@NoArgsConstructor
public class Update {

    private static final String CONFIRM_ERROR_MESSAGE = "Valor informado na confirmação de senha invalido.";

    @Email
    @NotNull
    @NotEmpty
    private String email;

    private String first = "";
    private String second = "";
    private String third = "";
    private String fourth = "";

    @NotEmpty(message = "Por favor, forneça uma senha.")
    @Pattern(regexp = STRONG_PASSWORD, message = STRONG_PASSWORD_MESSAGE)
    private String password;

    @NotEmpty(message = "Por favor, confirme a senha.")
    private String confirmPassword;

    public Update(Codes codes) {
        BeanUtils.copyProperties(codes, this);
    }
        
    public void validate(BindingResult result) {
        if (!getConfirmPassword().equals(getPassword())) {
            result.addError(new ObjectError("confirmPassowrd", CONFIRM_ERROR_MESSAGE));
        }
    }

    public String code() {
        return String.format("%s%s%s%s", first, second, third, fourth);
    }
}
