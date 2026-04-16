package com.example.store.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profilePage(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
            String roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            model.addAttribute("roles", roles);
        } else {
            model.addAttribute("username", "anonymous");
            model.addAttribute("roles", "");
        }
        return "profile";
    }
}
