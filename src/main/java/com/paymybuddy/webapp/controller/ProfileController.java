package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.exception.ProfileException;
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
    public String getProfile(Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            return "oauth_profile";
        }
        UserDTO user = profileService.getCurrentUserAsDTO();
        model.addAttribute("userDTO", user);
        return "profile";
    }

    @GetMapping("/oauth_profile")
    public String getOauthProfile(Model model, Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String clientName = oauthToken.getAuthorizedClientRegistrationId();
        logger.info("oauth connection email: " + ((OAuth2User) authentication.getPrincipal()).getName());
        model.addAttribute("clientName", clientName.equals("google") ? "Google" : "GitHub");

        return "oauth_profile";
    }

    @PostMapping(path = "/profile")
    public String updateUser(@Valid @ModelAttribute UserDTO userDTO, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            logger.error("Result binding has errors");
            return "profile";
        } else {
            try {
                profileService.updateUser(userDTO);
                attributes.addFlashAttribute("message", "success");
                attributes.addFlashAttribute("success", "Votre profil a été modifié avec succès");
            } catch (ProfileException e) {
                logger.error("Failed to update profile: " + e.getMessage());
                attributes.addFlashAttribute("message", "error");
                attributes.addFlashAttribute("error", e.getMessage());
            }
            return "redirect:/profile";
        }
    }
}
