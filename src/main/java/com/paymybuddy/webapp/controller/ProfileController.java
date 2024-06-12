package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.service.IUserProfileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserProfileService profileService;

    @GetMapping("/profile")
    public String getProfile(Model model) {
        UserDTO user = profileService.getCurrentUser();
        model.addAttribute("userDTO", user);
        return "profile";
    }

    @GetMapping("/oauth_profile")
    public String getOauthProfile(Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {

            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String clientName = oauthToken.getAuthorizedClientRegistrationId();
            model.addAttribute("clientName", clientName.equals("google") ? "Google" : "GitHub");
        }
        return "oauth_profile";
    }

    @PostMapping(path = "/profile")
    public String updateUser(@Valid @ModelAttribute UserDTO userDTO, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            logger.error("Result binding has errors");
            return "profile";
        } else {
            attributes.addFlashAttribute("message", "Success");
            attributes.addFlashAttribute("alertClass", "alert-success");
            profileService.updateUser(userDTO);
            return "redirect:/profile";
        }
    }
}
