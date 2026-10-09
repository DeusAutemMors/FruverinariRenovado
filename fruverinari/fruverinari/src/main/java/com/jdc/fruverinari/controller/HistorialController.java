package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.services.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistorialController {

    @Autowired
    private HistorialService historialService;

    @GetMapping("/historial")
    public String historial(Model model) {
        model.addAttribute("historiales", historialService.obtenerTodos());
        return "historial"; //
    }
}
