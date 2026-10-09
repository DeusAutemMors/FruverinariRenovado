package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.entities.ProveedorEntity;
import com.jdc.fruverinari.services.HistorialService;
import com.jdc.fruverinari.services.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private HistorialService historialService;

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("proveedores", proveedorService.obtenerTodos());
        return "Proveedores";
    }

    // FORM REGISTRAR
    @GetMapping("/registrar")
    public String mostrarFormularioRegistrar(Model model) {
        model.addAttribute("proveedor", new ProveedorEntity());
        return "registrarProveedor";
    }

    // GUARDAR NUEVO
    @PostMapping("/registrar")
    public String registrarProveedor(ProveedorEntity proveedor) {

        proveedorService.guardar(proveedor);

        historialService.registrarAccion(
                "Proveedor",
                proveedor.getId(),
                "CREAR",
                "Se registró proveedor: " + proveedor.getNombre(),
                "admin"
        );

        return "redirect:/proveedores";
    }

    // FORM EDITAR
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        ProveedorEntity proveedor = proveedorService.obtenerPorId(id);
        model.addAttribute("proveedor", proveedor);
        return "editarProveedor";
    }

    // GUARDAR EDICIÓN
    @PostMapping("/editar")
    public String editarProveedor(ProveedorEntity proveedor) {

        proveedorService.guardar(proveedor);

        historialService.registrarAccion(
                "Proveedor",
                proveedor.getId(),
                "EDITAR",
                "Se editó proveedor: " + proveedor.getNombre(),
                "admin"
        );

        return "redirect:/proveedores";
    }

    // SUSPENDER
    @PostMapping("/suspender/{id}")
    public String suspenderProveedor(@PathVariable Long id) {

        proveedorService.suspender(id);

        historialService.registrarAccion(
                "Proveedor",
                id,
                "SUSPENDER",
                "Se suspendió proveedor",
                "admin"
        );

        return "redirect:/proveedores";
    }
}
