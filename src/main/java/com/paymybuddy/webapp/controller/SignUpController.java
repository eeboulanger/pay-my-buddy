package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.exception.RegistrationException;
import com.paymybuddy.webapp.service.ISignUpService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignUpController {
    private final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    @Autowired
    private ISignUpService signUpService;

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String createNewUser(@Valid @ModelAttribute UserDTO form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.error("Result binding has errors" + bindingResult.getAllErrors());
            return "signup";
        } else {
            try {
                logger.info("Creating a new user: " + form.getUsername());
                signUpService.signUp(form);
                redirectAttributes.addFlashAttribute("success",
                        "Votre compte a été créé avec succès ! Veuillez vous connecter");
                return "redirect:/login";
            } catch (RegistrationException e) {
                logger.error(e.getMessage());
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/signup";
            }
        }
    }
}
