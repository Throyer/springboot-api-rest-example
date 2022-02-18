package com.github.throyer.common.springboot.controllers.app;

import static com.github.throyer.common.springboot.domain.toast.Type.SUCCESS;
import static com.github.throyer.common.springboot.utils.Responses.validateAndUpdateModel;

import com.github.throyer.common.springboot.domain.user.form.CreateUserProps;
import com.github.throyer.common.springboot.domain.user.service.CreateUserService;
import com.github.throyer.common.springboot.domain.toast.Toasts;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/app/register")
public class RegisterController {

    @Autowired
    private CreateUserService service;

    @GetMapping(produces = "text/html")
    public String index(Model model) {
        model.addAttribute("user", new CreateUserProps());
        return "app/register/index";
    }

    @PostMapping(produces = "text/html")
    public String create(
        @Valid CreateUserProps props,
        BindingResult result,
        RedirectAttributes redirect,
        Model model
    ) {

        props.validate(result);

        if (validateAndUpdateModel(model, props, "user", result)) {
            return "app/register/index";
        }
        
        service.create(props);
        
        Toasts.add(redirect, "Cadastro realizado com sucesso.", SUCCESS);
        
        return "redirect:/app/login";
    }
}
