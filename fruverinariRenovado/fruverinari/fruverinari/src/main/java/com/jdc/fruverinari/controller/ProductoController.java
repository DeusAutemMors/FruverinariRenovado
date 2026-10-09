package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.entities.ProductoEntity;
import com.jdc.fruverinari.services.HistorialService;
import com.jdc.fruverinari.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Import necesario para List

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private HistorialService historialService;

    // ----------------------------------------------------------------------
    // 1. LISTAR PRODUCTOS CON BÚSQUEDA (GET /productos)
    // ----------------------------------------------------------------------
    @GetMapping // Mapea a /productos
    public String listarProductos(@RequestParam(required = false) String query, Model model) {

        List<ProductoEntity> productos;

        if (query != null && !query.trim().isEmpty()) {
            // Si hay un término de búsqueda
            productos = productoService.buscarPorNombreODescripcion(query);
            model.addAttribute("query", query); // Para mantener el texto en el campo de búsqueda
        } else {
            // Si no hay query, mostramos todos los productos
            productos = productoService.obtenerTodos();
        }

        // 🔑 NOTA: La vista Productos.html usará esta lista para mostrar la tabla.
        model.addAttribute("productos", productos);

        return "Productos"; // Asumiendo que tu plantilla se llama 'Productos.html'
    }

    // ----------------------------------------------------------------------
    // 2. REGISTRAR PRODUCTO (GET /productos/registrar)
    // ----------------------------------------------------------------------
    @GetMapping("/registrar")
    public String mostrarFormularioRegistrar(Model model) {
        // Inicializa un producto vacío, el ID será null
        model.addAttribute("producto", new ProductoEntity());
        return "registrarProducto";
    }

    // ----------------------------------------------------------------------
    // 3. EDITAR PRODUCTO (GET /productos/editar/{id})
    // ----------------------------------------------------------------------
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        ProductoEntity producto = productoService.obtenerPorId(id);

        if (producto == null) {
            return "redirect:/productos";
        }

        model.addAttribute("producto", producto);
        return "editarProducto";
    }

    // ----------------------------------------------------------------------
    // 4. GUARDAR / ACTUALIZAR PRODUCTO (POST /productos/guardar)
    // ----------------------------------------------------------------------
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute ProductoEntity producto) {

        // Determina si es una creación o una edición
        String accion;
        String descripcionAccion;

        if (producto.getId() == null) {
            // Es un nuevo producto (Creación)
            accion = "CREAR";
            descripcionAccion = "Se creó el producto: " + producto.getNombre();
        } else {
            // Es un producto existente (Edición)
            accion = "EDITAR";
            descripcionAccion = "Se editó el producto: " + producto.getNombre();
        }

        productoService.guardar(producto);

        // Registro en el historial
        historialService.registrarAccion(
                "Producto",
                producto.getId(),
                accion,
                descripcionAccion,
                "admin"
        );

        return "redirect:/productos";
    }

    // ----------------------------------------------------------------------
    // 5. SUSPENDER / ACTIVAR PRODUCTO (POST /productos/suspender/{id})
    // ----------------------------------------------------------------------
    @PostMapping("/suspender/{id}")
    public String suspenderProducto(@PathVariable Long id) {

        productoService.suspender(id);

        historialService.registrarAccion(
                "Producto",
                id,
                "SUSPENDER/ACTIVAR",
                "Se suspendió o reactivó el producto ID: " + id,
                "admin"
        );

        return "redirect:/productos";
    }

    // ----------------------------------------------------------------------
    // 6. DATOS PARA GRÁFICOS (GET /productos/dashboard) 🔑 NUEVO MÉTODO
    // ----------------------------------------------------------------------
    /**
     * Carga los datos necesarios para mostrar la gráfica de stock u otras métricas en un dashboard.
     */
    @GetMapping("/dashboard")
    public String mostrarDashboardConGrafica(Model model) {
        // 1. Obtener la lista de productos (para stock actual, etc.)
        List<ProductoEntity> productos = productoService.obtenerTodos();

        // 2. Enviar la lista de productos.
        // 🔑 Esta lista será usada por el código JavaScript (Chart.js)
        model.addAttribute("productos", productos);

        // (Opcional) Si existe, se podría añadir aquí el número de productos con bajo stock
        // model.addAttribute("totalAlertas", productoService.contarProductosConBajoStock());

        return "ProductosDashboard"; // Asume la vista HTML 'ProductosDashboard.html'
    }
}