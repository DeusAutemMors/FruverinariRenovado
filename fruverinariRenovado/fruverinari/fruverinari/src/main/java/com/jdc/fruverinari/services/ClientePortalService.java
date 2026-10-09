package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.CanjeOfertaEntity;
import com.jdc.fruverinari.entities.MovimientoEntity;
import com.jdc.fruverinari.entities.OfertaEntity;
import com.jdc.fruverinari.entities.ProductoEntity;
import com.jdc.fruverinari.entities.UsuarioEntity;
import com.jdc.fruverinari.model.CatalogoRecetas;
import com.jdc.fruverinari.model.ProductoRecomendado;
import com.jdc.fruverinari.model.ProductosReceta;
import com.jdc.fruverinari.model.RecetaPlantilla;
import com.jdc.fruverinari.model.RecetaVista;
import com.jdc.fruverinari.repository.CanjeOfertaRepository;
import com.jdc.fruverinari.repository.OfertaRepository;
import com.jdc.fruverinari.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientePortalService {

    private static final ProductosReceta INGREDIENTES_CATALOGO = new ProductosReceta(
            "fruta de temporada", "otra fruta", "fruta",
            "verdura", "hoja verde", "hortaliza",
            "hierbas", "citrico");

    private final ProductoService productoService;
    private final MovimientoService movimientoService;
    private final UsuarioRepository usuarioRepository;
    private final OfertaRepository ofertaRepository;
    private final CanjeOfertaRepository canjeOfertaRepository;

    public ClientePortalService(ProductoService productoService,
                                MovimientoService movimientoService,
                                UsuarioRepository usuarioRepository,
                                OfertaRepository ofertaRepository,
                                CanjeOfertaRepository canjeOfertaRepository) {
        this.productoService = productoService;
        this.movimientoService = movimientoService;
        this.usuarioRepository = usuarioRepository;
        this.ofertaRepository = ofertaRepository;
        this.canjeOfertaRepository = canjeOfertaRepository;
    }

    public UsuarioEntity obtenerCliente(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro el cliente."));
    }

    public List<RecetaVista> listarRecetas(String momento) {
        return CatalogoRecetas.todas().stream()
                .filter(r -> momento == null || momento.isBlank() || "TODAS".equalsIgnoreCase(momento)
                        || r.getMomento().equalsIgnoreCase(momento))
                .map(this::aVista)
                .toList();
    }

    public List<ProductoRecomendado> productosPocoVendidos(int limite) {
        Map<Long, Integer> ventas = movimientoService.obtenerTodos().stream()
                .filter(m -> m.getProducto() != null && "SALIDA".equalsIgnoreCase(m.getTipoMovimiento()))
                .collect(Collectors.groupingBy(
                        m -> m.getProducto().getId(),
                        Collectors.summingInt(MovimientoEntity::getCantidad)));

        return productoService.obtenerTodos().stream()
                .filter(p -> p.isActivo() && p.getCantidad() != null && p.getCantidad() > 0)
                .map(p -> {
                    int vendidas = ventas.getOrDefault(p.getId(), 0);
                    return new ProductoRecomendado(p, vendidas, motivoBajaVenta(vendidas));
                })
                .sorted(Comparator.comparingInt(ProductoRecomendado::getUnidadesVendidas)
                        .thenComparing(r -> r.getProducto().getNombre(), String.CASE_INSENSITIVE_ORDER))
                .limit(Math.max(1, limite))
                .toList();
    }

    public List<ProductoEntity> productosDisponibles() {
        return productoService.obtenerTodos().stream()
                .filter(p -> p.isActivo() && p.getCantidad() != null && p.getCantidad() > 0)
                .sorted(Comparator.comparing(ProductoEntity::getNombre, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public List<OfertaEntity> listarOfertas() {
        return ofertaRepository.findByActivaTrueOrderByPuntosRequeridosAsc();
    }

    public List<CanjeOfertaEntity> historialCanjes(String username) {
        return canjeOfertaRepository.findByUsuarioOrderByFechaCanjeDesc(obtenerCliente(username));
    }

    @Transactional
    public ResultadoCompra comprar(String username, Long productoId, int cantidad) {
        if (cantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser al menos 1.");
        }

        UsuarioEntity cliente = obtenerCliente(username);
        ProductoEntity producto = productoService.obtenerPorId(productoId);
        if (producto == null || !producto.isActivo()) {
            throw new IllegalArgumentException("El producto no esta disponible.");
        }

        double precio = producto.getPrecio() == null ? 0 : producto.getPrecio();
        double subtotal = precio * cantidad;
        int descuento = cliente.getDescuentoPendiente();
        double total = subtotal;
        if (descuento > 0) {
            total = subtotal * (100 - descuento) / 100.0;
        }

        MovimientoEntity movimiento = new MovimientoEntity();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento("SALIDA");
        movimiento.setCantidad(cantidad);
        movimiento.setMonto(total);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setUsuario(username);
        movimientoService.guardar(movimiento);

        int puntosGanados = Math.max(1, (int) Math.floor(total / 1000.0));
        cliente.setPuntos(cliente.getPuntos() + puntosGanados);
        if (descuento > 0) {
            cliente.setDescuentoPendiente(0);
        }
        usuarioRepository.save(cliente);

        return new ResultadoCompra(producto.getNombre(), cantidad, total, puntosGanados, descuento);
    }

    @Transactional
    public void canjearOferta(String username, Long ofertaId) {
        UsuarioEntity cliente = obtenerCliente(username);
        OfertaEntity oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() -> new IllegalArgumentException("La oferta no existe."));
        if (!oferta.isActiva()) {
            throw new IllegalArgumentException("La oferta ya no esta disponible.");
        }
        if (cliente.getPuntos() < oferta.getPuntosRequeridos()) {
            throw new IllegalArgumentException("No tienes puntos suficientes para esta oferta.");
        }

        cliente.setPuntos(cliente.getPuntos() - oferta.getPuntosRequeridos());
        if (oferta.getDescuentoPorcentaje() > 0) {
            int acumulado = Math.min(50, cliente.getDescuentoPendiente() + oferta.getDescuentoPorcentaje());
            cliente.setDescuentoPendiente(acumulado);
        }
        usuarioRepository.save(cliente);

        CanjeOfertaEntity canje = new CanjeOfertaEntity();
        canje.setUsuario(cliente);
        canje.setOferta(oferta);
        canje.setPuntosUsados(oferta.getPuntosRequeridos());
        canjeOfertaRepository.save(canje);
    }

    private RecetaVista aVista(RecetaPlantilla receta) {
        String estilos = receta.getEstilos().stream()
                .map(e -> e.substring(0, 1) + e.substring(1).toLowerCase(Locale.ROOT))
                .collect(Collectors.joining(", "));
        return new RecetaVista(
                receta.getTitulo(),
                receta.getMomento(),
                receta.aplicar(INGREDIENTES_CATALOGO),
                estilos,
                receta.isRequiereCoccion());
    }

    private String motivoBajaVenta(int unidadesVendidas) {
        if (unidadesVendidas == 0) {
            return "Aun no se ha vendido. Ideal para ganar puntos extra apoyando el inventario.";
        }
        if (unidadesVendidas <= 5) {
            return "Se vende poco (" + unidadesVendidas + " uds). Recomendado para rotar stock fresco.";
        }
        return "Rotacion baja frente al resto del catalogo.";
    }

    public record ResultadoCompra(String producto, int cantidad, double total, int puntosGanados, int descuentoAplicado) {
    }
}
