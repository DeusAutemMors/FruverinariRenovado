package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.model.PlanNutricionalGenerado;
import com.jdc.fruverinari.services.PlanNutricionalService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/nutricion")
public class PlanNutricionalController {

    private final PlanNutricionalService planNutricionalService;

    public PlanNutricionalController(PlanNutricionalService planNutricionalService) {
        this.planNutricionalService = planNutricionalService;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false, defaultValue = "dia") String tab,
            Model model) {
        model.addAttribute("mostrarResultado", false);
        model.addAttribute("tabActiva", tab);
        model.addAttribute("planesRecientes", planNutricionalService.obtenerRecientes());
        return "nutricion/plan-nutricional";
    }

    @PostMapping("/generar")
    public String generar(
            @RequestParam String nombreCliente,
            @RequestParam String objetivo,
            @RequestParam(defaultValue = "MODERADO") String nivelActividad,
            @RequestParam(defaultValue = "CUALQUIERA") String estiloAlimentacion,
            @RequestParam(defaultValue = "VARIADO") String preferenciaRecetas,
            @RequestParam(defaultValue = "MODERADO") String intensidad,
            @RequestParam(required = false) String[] restricciones,
            Model model) {

        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            model.addAttribute("error", "Ingresa el nombre del cliente.");
            model.addAttribute("mostrarResultado", false);
            model.addAttribute("tabActiva", "dia");
            model.addAttribute("planesRecientes", planNutricionalService.obtenerRecientes());
            return "nutricion/plan-nutricional";
        }

        List<String> listaRestricciones = restricciones != null
                ? Arrays.asList(restricciones)
                : Collections.emptyList();

        PlanNutricionalGenerado plan = planNutricionalService.generarPlan(
                nombreCliente, objetivo, nivelActividad,
                estiloAlimentacion, preferenciaRecetas, intensidad, listaRestricciones);

        model.addAttribute("plan", plan);
        model.addAttribute("mostrarResultado", true);
        model.addAttribute("tabActiva", "dia");
        model.addAttribute("planesRecientes", planNutricionalService.obtenerRecientes());
        return "nutricion/plan-nutricional";
    }

    @PostMapping("/guardar")
    public String guardar(
            @RequestParam String nombreCliente,
            @RequestParam String objetivo,
            @RequestParam String nivelActividad,
            @RequestParam(defaultValue = "CUALQUIERA") String estiloAlimentacion,
            @RequestParam(defaultValue = "VARIADO") String preferenciaRecetas,
            @RequestParam(defaultValue = "MODERADO") String intensidad,
            @RequestParam(required = false) String[] restricciones,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        List<String> listaRestricciones = restricciones != null
                ? Arrays.asList(restricciones)
                : Collections.emptyList();

        PlanNutricionalGenerado plan = planNutricionalService.generarPlan(
                nombreCliente, objetivo, nivelActividad,
                estiloAlimentacion, preferenciaRecetas, intensidad, listaRestricciones);

        String usuario = authentication != null ? authentication.getName() : "sistema";
        planNutricionalService.guardarPlan(plan, usuario);

        redirectAttributes.addFlashAttribute("mensajeExito",
                "Plan nutricional guardado para " + plan.getNombreCliente() + ".");
        return "redirect:/nutricion";
    }
}
