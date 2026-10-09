package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.entities.UsuarioEntity;
import com.jdc.fruverinari.services.ClientePortalService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final ClientePortalService clientePortalService;

    public ClienteController(ClientePortalService clientePortalService) {
        this.clientePortalService = clientePortalService;
    }

    @GetMapping
    public String recetas(@RequestParam(required = false) String momento, Model model, Authentication auth) {
        cargarCliente(model, auth);
        model.addAttribute("title", "Recetas - Fruver Inari");
        model.addAttribute("momentoSeleccionado", momento == null ? "TODAS" : momento);
        model.addAttribute("recetas", clientePortalService.listarRecetas(momento));
        return "cliente/recetas";
    }

    @GetMapping("/recomendados")
    public String recomendados(Model model, Authentication auth) {
        cargarCliente(model, auth);
        model.addAttribute("title", "Recomendados - Fruver Inari");
        model.addAttribute("recomendados", clientePortalService.productosPocoVendidos(12));
        return "cliente/recomendados";
    }

    @GetMapping("/tienda")
    public String tienda(Model model, Authentication auth) {
        cargarCliente(model, auth);
        model.addAttribute("title", "Tienda - Fruver Inari");
        model.addAttribute("productos", clientePortalService.productosDisponibles());
        return "cliente/tienda";
    }

    @PostMapping("/comprar")
    public String comprar(@RequestParam Long productoId,
                          @RequestParam int cantidad,
                          @RequestParam(defaultValue = "/cliente/tienda") String volver,
                          Authentication auth,
                          RedirectAttributes redirectAttributes) {
        try {
            var resultado = clientePortalService.comprar(auth.getName(), productoId, cantidad);
            String extra = resultado.descuentoAplicado() > 0
                    ? " Se aplico tu descuento del " + resultado.descuentoAplicado() + "%."
                    : "";
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Compraste " + resultado.cantidad() + " de " + resultado.producto()
                            + " por $" + Math.round(resultado.total())
                            + ". Ganaste " + resultado.puntosGanados() + " puntos." + extra);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:" + (volver.startsWith("/cliente") ? volver : "/cliente/tienda");
    }

    @GetMapping("/ofertas")
    public String ofertas(Model model, Authentication auth) {
        cargarCliente(model, auth);
        model.addAttribute("title", "Ofertas - Fruver Inari");
        model.addAttribute("ofertas", clientePortalService.listarOfertas());
        model.addAttribute("canjes", clientePortalService.historialCanjes(auth.getName()));
        return "cliente/ofertas";
    }

    @PostMapping("/canjear")
    public String canjear(@RequestParam Long ofertaId,
                          Authentication auth,
                          RedirectAttributes redirectAttributes) {
        try {
            clientePortalService.canjearOferta(auth.getName(), ofertaId);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Oferta canjeada. Si incluye descuento, se aplicara en tu proxima compra.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cliente/ofertas";
    }

    private void cargarCliente(Model model, Authentication auth) {
        UsuarioEntity cliente = clientePortalService.obtenerCliente(auth.getName());
        model.addAttribute("cliente", cliente);
        model.addAttribute("puntosCliente", cliente.getPuntos());
    }
}
