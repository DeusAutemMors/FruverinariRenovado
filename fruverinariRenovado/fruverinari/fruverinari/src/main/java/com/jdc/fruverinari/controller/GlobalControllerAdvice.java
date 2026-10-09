package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.entities.UsuarioEntity;
import com.jdc.fruverinari.repository.UsuarioRepository;
import com.jdc.fruverinari.services.AlertaService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final AlertaService alertaService;
    private final UsuarioRepository usuarioRepository;

    public GlobalControllerAdvice(AlertaService alertaService, UsuarioRepository usuarioRepository) {
        this.alertaService = alertaService;
        this.usuarioRepository = usuarioRepository;
    }

    @ModelAttribute("alertasCount")
    public Integer getAlertasCount() {
        if (!tieneRol("ROLE_ADMIN") && !tieneRol("ROLE_CAJERO")) {
            return 0;
        }
        return alertaService.obtenerProductosConBajoStock().size();
    }

    @ModelAttribute("esAdmin")
    public boolean esAdmin() {
        return tieneRol("ROLE_ADMIN");
    }

    @ModelAttribute("esCajero")
    public boolean esCajero() {
        return tieneRol("ROLE_CAJERO");
    }

    @ModelAttribute("esCliente")
    public boolean esCliente() {
        return tieneRol("ROLE_CLIENTE");
    }

    @ModelAttribute("puntosCliente")
    public Integer puntosCliente() {
        if (!tieneRol("ROLE_CLIENTE")) {
            return 0;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return 0;
        }
        return usuarioRepository.findByUsername(auth.getName())
                .map(UsuarioEntity::getPuntos)
                .orElse(0);
    }

    private boolean tieneRol(String rol) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(rol));
    }
}
