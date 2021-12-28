package com.github.throyer.common.springboot.domain.services.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
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

    @NotEmpty(message = "Por favor, forneça uma senha.")
    @Size(min = 8, max = 255, message = "A senha deve conter no minimo {min} caracteres.")
    private String password;

    public Update(Codes codes) {
        BeanUtils.copyProperties(codes, this);
    }
        
    public void validate(BindingResult result) { }

    public String code() {
        return String.format("%s%s%s%s", first, second, third, fourth);
    }
}
