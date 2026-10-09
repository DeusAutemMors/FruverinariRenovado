package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.services.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private AlertaService alertaService;

    @ModelAttribute("alertasCount")
    public Integer getAlertasCount() {
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

    private boolean tieneRol(String rol) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(rol));
    }
}