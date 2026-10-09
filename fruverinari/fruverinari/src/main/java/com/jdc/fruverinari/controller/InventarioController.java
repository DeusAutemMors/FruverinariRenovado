package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventarioController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/consultarInventario")
    public String consultarInventario(Model model) {

        model.addAttribute("productos", productoService.obtenerTodos());

        return "consultarInventario"; // <-- nombre EXACTO de la vista
    }
}
