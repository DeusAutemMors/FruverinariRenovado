package com.jdc.fruverinari.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "canjes_oferta")
public class CanjeOfertaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "oferta_id")
    private OfertaEntity oferta;

    private Integer puntosUsados;

    private LocalDateTime fechaCanje;

    @PrePersist
    protected void onCreate() {
        fechaCanje = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public OfertaEntity getOferta() {
        return oferta;
    }

    public void setOferta(OfertaEntity oferta) {
        this.oferta = oferta;
    }

    public Integer getPuntosUsados() {
        return puntosUsados;
    }

    public void setPuntosUsados(Integer puntosUsados) {
        this.puntosUsados = puntosUsados;
    }

    public LocalDateTime getFechaCanje() {
        return fechaCanje;
    }

    public void setFechaCanje(LocalDateTime fechaCanje) {
        this.fechaCanje = fechaCanje;
    }
}
