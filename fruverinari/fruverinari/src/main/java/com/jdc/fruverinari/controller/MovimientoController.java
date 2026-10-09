package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.entities.MovimientoEntity;
import com.jdc.fruverinari.services.MovimientoService;
import com.jdc.fruverinari.repository.ProductoRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;
    private final ProductoRepository productoRepository;

    public MovimientoController(MovimientoService movimientoService, ProductoRepository productoRepository) {
        this.movimientoService = movimientoService;
        this.productoRepository = productoRepository;
    }

    @GetMapping("/lista")
    public String listarMovimientos(Model model) {
        model.addAttribute("movimientos", movimientoService.obtenerTodos());
        model.addAttribute("productosDisponibles", productoRepository.findAll());
        return "cajero-reporte";
    }

    @GetMapping("/{id}")
    public String verMovimiento(@PathVariable Long id) {
        if (movimientoService.obtenerPorId(id) == null) {
            return "redirect:/movimientos/lista";
        }
        return "redirect:/movimientos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoMovimiento(Model model) {
        model.addAttribute("movimiento", new MovimientoEntity());
        model.addAttribute("productos", productoRepository.findAll());
        return "lista";
    }

    // Guardar movimiento
    @PostMapping("/guardar")
    public String guardarMovimiento(
            @ModelAttribute MovimientoEntity movimiento,
            @RequestParam("productoId") Long productoId
    ) {

        // Si no manda fecha → poner fecha actual
        if (movimiento.getFechaMovimiento() == null) {
            movimiento.setFechaMovimiento(LocalDateTime.now());
        }

        // Asignar producto
        productoRepository.findById(productoId).ifPresent(movimiento::setProducto);

        movimientoService.guardar(movimiento);

        return "redirect:/movimientos/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMovimiento(@PathVariable Long id) {
        movimientoService.eliminar(id);
        return "redirect:/movimientos/lista";
    }

    @GetMapping("/historial")
    public String historialMovimientos(Model model) {
        model.addAttribute("movimientos", movimientoService.obtenerHistorial());
        model.addAttribute("productosDisponibles", productoRepository.findAll());
        return "cajero-reporte";
    }

    @GetMapping("/filtrar")
    public String filtrarPorFecha(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            Model model) {

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        model.addAttribute("movimientos",
                movimientoService.obtenerMovimientosPorRangoDeFecha(inicioDateTime, finDateTime));
        model.addAttribute("productosDisponibles", productoRepository.findAll());
        model.addAttribute("fechaInicio", inicio.toString());
        model.addAttribute("fechaFin", fin.toString());

        return "cajero-reporte";
    }

    @GetMapping("/filtrar/producto")
    public String filtrarPorFechaYProducto(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam("idProducto") Long idProducto,
            Model model) {

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        model.addAttribute("movimientos",
                movimientoService.obtenerMovimientosPorRangoDeFechaYProducto(inicioDateTime, finDateTime, idProducto));
        model.addAttribute("productosDisponibles", productoRepository.findAll());
        model.addAttribute("fechaInicio", inicio.toString());
        model.addAttribute("fechaFin", fin.toString());
        model.addAttribute("productoSeleccionadoId", idProducto);

        return "cajero-reporte";
    }
}
