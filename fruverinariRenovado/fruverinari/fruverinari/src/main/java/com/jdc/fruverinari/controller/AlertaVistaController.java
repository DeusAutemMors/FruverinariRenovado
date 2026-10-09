package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.entities.ProductoEntity;
import com.jdc.fruverinari.services.AlertaService;
import org.springframework.stereotype.Controller; // 🔑 Importante: Usar @Controller
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

// 🚀 Usamos @Controller para devolver vistas (HTML)
@Controller
@RequestMapping
public class AlertaVistaController { // Nuevo nombre para evitar conflicto con el API Controller

    private final AlertaService alertaService;

    // Inyección por constructor
    public AlertaVistaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    /**
     * RF05: Obtiene y muestra la lista de productos cuyo stock es menor o igual al stock mínimo.
     * Esta funcionalidad es visible en el menú principal.
     */
    @GetMapping("/alertas")
    public String verAlertas(Model model) {
        // 1. Obtener la lista de productos con bajo stock del servicio.
        // Se asume que el método ya maneja el filtro de cantidad <= stockMinimo.
        List<ProductoEntity> alertas = alertaService.obtenerProductosConBajoStock(); // O el nombre que uses, por ejemplo: obtenerAlertasBajoStock()

        // 2. Agregar la lista al modelo bajo el nombre "alertas" para que Thymeleaf la use.
        model.addAttribute("alertas", alertas);

        // 3. Devolver la plantilla HTML.
        // Ya corregimos el error anterior: ahora debes asegurarte de que la plantilla sea
        // "alertas/lista" (si moviste y renombraste) o "listaAlertas" (si usas el nombre de archivo existente).

        // Si seguiste la recomendación de usar la estructura de carpetas:
        return "listaAlertas";

        // Si usas el archivo existente en la raíz (listaAlertas.html):
        // return "listaAlertas";
    }

}