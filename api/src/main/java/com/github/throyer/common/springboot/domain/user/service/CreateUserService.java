package com.github.throyer.common.springboot.domain.user.service;

import com.github.throyer.common.springboot.domain.role.repository.RoleRepository;
import com.github.throyer.common.springboot.domain.toast.Toasts;
import com.github.throyer.common.springboot.domain.user.entity.User;
import com.github.throyer.common.springboot.domain.user.form.CreateUserProps;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.github.throyer.common.springboot.constants.TOAST_MESSAGES.TOAST_SUCCESS_MESSAGE;
import static com.github.throyer.common.springboot.domain.mail.validation.EmailValidations.validateEmailUniqueness;
import static com.github.throyer.common.springboot.domain.toast.Type.SUCCESS;
import static com.github.throyer.common.springboot.utils.Messages.message;

@Service
public class CreateUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CreateUserService(
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User create(CreateUserProps props) {
        validateEmailUniqueness(props);
        return save(props);
    }

    public void create(
        CreateUserProps props,
        BindingResult validations,
        RedirectAttributes redirect,
        Model model
    ) {
        validateEmailUniqueness(props, validations);

        if (validations.hasErrors()) {
            model.addAttribute("user", props);
            Toasts.add(model, validations);
            return;
        }

        Toasts.add(redirect, message(TOAST_SUCCESS_MESSAGE), SUCCESS);
        save(props);
    }

    private User save(CreateUserProps props) {
        var roles = roleRepository.findOptionalByInitials("USER")
            .map(List::of)
                .orElseGet(List::of);
        return userRepository.save(new User(props, roles));
    }
}
