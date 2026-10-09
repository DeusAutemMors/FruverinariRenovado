package com.jdc.fruverinari.config;

import com.jdc.fruverinari.services.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDataInitializer implements CommandLineRunner {

    private final UsuarioService usuarioService;

    public UsuarioDataInitializer(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void run(String... args) {
        usuarioService.crearSiNoExiste("admin", "1234", "ADMIN");
        usuarioService.crearSiNoExiste("cajero", "1234", "CAJERO");
        usuarioService.crearSiNoExiste("cliente", "1234", "CLIENTE");
    }
}
