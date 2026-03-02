package com.tienda.controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.tienda.dto.RegisterDTO;
import com.tienda.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /* ============================================================
       LOGIN
       ============================================================ */

    @GetMapping("/login")
    public String login() {
        return "authentication/login";   
    }

    /* ============================================================
       REGISTRO
       ============================================================ */

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "authentication/register";   
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("registerDTO") RegisterDTO dto,
                                  Model model) {

        // Contraseñas iguales
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "authentication/register";
        }

        // Usuario duplicado
        if (userService.findByName(dto.getUsername()) != null) {
            model.addAttribute("error", "El nombre de usuario ya existe");
            return "authentication/register";
        }

        // Email duplicado
        if (userService.findByEmail(dto.getEmail()) != null) {
            model.addAttribute("error", "El email ya está registrado");
            return "authentication/register";
        }

        // Registrar usuario
        userService.register(dto);

        return "redirect:/login?registered";
    }
}