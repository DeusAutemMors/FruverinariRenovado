package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.services.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/Login")
    public String login(
            @RequestParam(required = false) String tab,
            Authentication authentication,
            Model model) {
        if (estaAutenticado(authentication)) {
            return redireccionPorRol(authentication);
        }
        model.addAttribute("tabActiva", tab != null ? tab : "login");
        return "Login";
    }

    @PostMapping("/registro")
    public String registrar(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam(defaultValue = "CAJERO") String rol,
            RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("registroError", "Las contrasenas no coinciden.");
            return "redirect:/Login?tab=registro";
        }

        try {
            usuarioService.registrar(username, password, rol);
            redirectAttributes.addFlashAttribute("registroOk",
                    "Cuenta creada. Ya puedes iniciar sesion con tu usuario.");
            return "redirect:/Login?tab=login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("registroError", e.getMessage());
            return "redirect:/Login?tab=registro";
        }
    }

    private boolean estaAutenticado(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String);
    }

    private String redireccionPorRol(Authentication authentication) {
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (esAdmin) {
            return "redirect:/productos";
        }
        boolean esCajero = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_CAJERO".equals(a.getAuthority()));
        if (esCajero) {
            return "redirect:/cajero/consultarInventario";
        }
        return "redirect:/Login";
    }
}
