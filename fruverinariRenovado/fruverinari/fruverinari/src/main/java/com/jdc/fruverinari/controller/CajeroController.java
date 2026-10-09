package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.entities.MovimientoEntity;
import com.jdc.fruverinari.entities.ProductoEntity;
import com.jdc.fruverinari.services.ProductoService;
import com.jdc.fruverinari.services.MovimientoService;
import com.jdc.fruverinari.services.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CajeroController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private HistorialService historialService;

    // --------------------------------------------------------------------------
    // 1. REGISTRAR MOVIMIENTO
    // --------------------------------------------------------------------------
    @GetMapping("/cajero/registrarMovimiento")
    public String mostrarFormularioMovimiento(Model model) {
        model.addAttribute("movimiento", new MovimientoEntity());
        // 🚀 Mejorado: Usamos "productosDisponibles" para consistencia
        model.addAttribute("productosDisponibles", productoService.obtenerTodos());
        return "cajero-registrarMovimiento";
    }

    @PostMapping("/cajero/registrarMovimiento")
    public String registrarMovimiento(@ModelAttribute("movimiento") MovimientoEntity movimiento) {

        // 1. Guardar el movimiento (Actualiza stock y guarda el registro)
        movimientoService.guardar(movimiento);

        // 2. Registrar en el historial
        historialService.registrarAccion(
                "Movimiento",
                movimiento.getId(),
                movimiento.getTipoMovimiento().toUpperCase(),
                "Movimiento realizado: " + movimiento.getTipoMovimiento() + " de " + movimiento.getCantidad() + " unidades. Producto ID: " + movimiento.getProducto().getId(),
                "cajero"
        );

        return "redirect:/cajero/consultarInventario";
    }

    // --------------------------------------------------------------------------
    // 2. CONSULTAR INVENTARIO / BÚSQUEDA
    // --------------------------------------------------------------------------
    @GetMapping("/cajero/consultarInventario")
    public String mostrarInventario(
            @RequestParam(name = "q", required = false) String q,
            Model model) {

        List<ProductoEntity> productos;
        if (q != null && !q.trim().isEmpty()) {
            productos = productoService.buscarPorNombreODescripcion(q);
            model.addAttribute("busqueda", q);
        } else {
            productos = productoService.obtenerTodos();
        }

        model.addAttribute("productos", productos);
        return "cajero-consultarInventario";
    }

    // El antiguo @GetMapping("/buscarProducto") se fusionó en /cajero/consultarInventario para simplificar.

    // --------------------------------------------------------------------------
    // 3. REPORTE DE MOVIMIENTOS (Filtros y Visualización)
    // --------------------------------------------------------------------------
    @GetMapping("/cajero/reporte") // 🚀 RUTA ESTANDARIZADA
    public String reporte(
            @RequestParam(name = "inicio", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicioLD,

            @RequestParam(name = "fin", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFinLD,

            @RequestParam(name = "idProducto", required = false) Long idProducto,

            Model model) {

        // 🚀 Mejorado: Usamos "productosDisponibles" para consistencia
        model.addAttribute("productosDisponibles", productoService.obtenerTodos());

        List<MovimientoEntity> movimientos;

        if (fechaInicioLD != null && fechaFinLD != null) {

            // CORRECCIÓN CLAVE: Convertimos LocalDate a LocalDateTime para incluir todo el día
            LocalDateTime inicioDateTime = fechaInicioLD.atStartOfDay(); // 00:00:00
            LocalDateTime finDateTime = fechaFinLD.atTime(23, 59, 59); // 23:59:59

            if (idProducto != null && idProducto != 0) {
                // Filtra por RANGO DE FECHA y PRODUCTO
                movimientos = movimientoService.obtenerMovimientosPorRangoDeFechaYProducto(inicioDateTime, finDateTime, idProducto);
            } else {
                // Filtra solo por RANGO DE FECHA
                movimientos = movimientoService.obtenerMovimientosPorRangoDeFecha(inicioDateTime, finDateTime);
            }

            // Pasamos los parámetros de regreso a la vista para mantener el estado del filtro
            model.addAttribute("fechaInicio", fechaInicioLD.toString());
            model.addAttribute("fechaFin", fechaFinLD.toString());
            model.addAttribute("productoSeleccionadoId", idProducto);

        } else {
            // Si no hay filtros de fecha, se muestra el historial completo por defecto
            movimientos = movimientoService.obtenerHistorial();
        }

        model.addAttribute("movimientos", movimientos);
        return "cajero-reporte";
    }
}