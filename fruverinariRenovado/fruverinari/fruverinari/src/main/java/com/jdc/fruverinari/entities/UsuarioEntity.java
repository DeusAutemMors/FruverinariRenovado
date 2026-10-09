package com.jdc.fruverinari.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String rol;

    /** Puntos de fidelizacion para clientes. */
    @Column(columnDefinition = "integer default 0")
    private Integer puntos = 0;

    /** Descuento porcentual acumulado para la proxima compra del cliente. */
    @Column(columnDefinition = "integer default 0")
    private Integer descuentoPendiente = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Integer getPuntos() {
        return puntos == null ? 0 : puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos == null ? 0 : puntos;
    }

    public Integer getDescuentoPendiente() {
        return descuentoPendiente == null ? 0 : descuentoPendiente;
    }

    public void setDescuentoPendiente(Integer descuentoPendiente) {
        this.descuentoPendiente = descuentoPendiente == null ? 0 : descuentoPendiente;
    }
}
