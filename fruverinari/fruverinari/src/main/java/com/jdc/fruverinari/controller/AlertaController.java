package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.entities.AlertaEntity;
import com.jdc.fruverinari.services.AlertaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity; // Importamos para mejor manejo de respuestas
import java.util.List;

@RestController // Para manejar peticiones API que devuelven JSON
@RequestMapping("/api/v1/alertas") // 🚀 Cambié la URL para evitar conflictos con el Controller de vistas.
public class AlertaController {

    private final AlertaService alertaService;

    // Inyección por constructor (Buena práctica)
    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @GetMapping
    public List<AlertaEntity> obtenerAlertas() {
        return alertaService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaEntity> obtenerAlertaPorId(@PathVariable Long id) {
        AlertaEntity alerta = alertaService.obtenerPorId(id);
        if (alerta != null) {
            return ResponseEntity.ok(alerta);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public void agregarAlerta(@RequestBody AlertaEntity alertaEntity) {
        alertaService.guardar(alertaEntity);
    }

    @PutMapping("/{id}")
    public void actualizarAlerta(@PathVariable Long id, @RequestBody AlertaEntity alertaEntity) {
        // 🚀 CORRECCIÓN: Usar el setter de ID correcto.
        // Si tienes setId(), úsalo. Si no, usa setIdAlerta().
        // Probamos con setIdAlerta:
        alertaEntity.setIdAlerta(id);
        alertaService.guardar(alertaEntity);
    }

    @DeleteMapping("/{id}")
    public void eliminarAlerta(@PathVariable Long id) {
        alertaService.eliminar(id);
    }
}