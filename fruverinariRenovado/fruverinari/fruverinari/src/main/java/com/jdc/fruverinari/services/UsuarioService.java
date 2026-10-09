package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.UsuarioEntity;
import com.jdc.fruverinari.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registrar(String username, String password, String rol) {
        String usuario = username.trim();
        if (usuario.isEmpty()) {
            throw new IllegalArgumentException("El usuario no puede estar vacio.");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("La contrasena debe tener al menos 4 caracteres.");
        }
        if (usuarioRepository.existsByUsername(usuario)) {
            throw new IllegalArgumentException("Ese nombre de usuario ya existe.");
        }

        String rolNormalizado = normalizarRol(rol);

        UsuarioEntity nuevo = new UsuarioEntity();
        nuevo.setUsername(usuario);
        nuevo.setPassword(passwordEncoder.encode(password));
        nuevo.setRol(rolNormalizado);
        nuevo.setPuntos(0);
        nuevo.setDescuentoPendiente(0);
        usuarioRepository.save(nuevo);
    }

    @Transactional
    public void crearSiNoExiste(String username, String passwordPlano, String rol) {
        if (!usuarioRepository.existsByUsername(username)) {
            UsuarioEntity u = new UsuarioEntity();
            u.setUsername(username);
            u.setPassword(passwordEncoder.encode(passwordPlano));
            u.setRol(normalizarRol(rol));
            u.setPuntos(0);
            u.setDescuentoPendiente(0);
            usuarioRepository.save(u);
        }
    }

    private String normalizarRol(String rol) {
        if (rol == null || rol.isBlank()) {
            return "CAJERO";
        }
        String r = rol.trim().toUpperCase();
        if (r.equals("ADMIN") || r.equals("CAJERO") || r.equals("CLIENTE")) {
            return r;
        }
        return "CLIENTE";
    }
}
