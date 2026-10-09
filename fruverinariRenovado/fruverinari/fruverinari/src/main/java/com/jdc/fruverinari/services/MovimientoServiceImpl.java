package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.MovimientoEntity;
import com.jdc.fruverinari.entities.ProductoEntity;
import com.jdc.fruverinari.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepo;

    // 🚀 INYECCIÓN FALTANTE: Necesitas el ProductoService para modificar el stock
    @Autowired
    private ProductoService productoService;

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoEntity> obtenerTodos() {
        return movimientoRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoEntity obtenerPorId(Long id) {
        return movimientoRepo.findById(id).orElse(null);
    }

    /**
     * 🚀 IMPLEMENTACIÓN CRUCIAL: Guarda el Movimiento Y actualiza el stock del Producto.
     */
    @Override
    @Transactional
    public void guardar(MovimientoEntity movimiento) {

        // 1. Obtener el producto afectado (asumiendo que MovimientoEntity tiene getProducto().getId())
        ProductoEntity producto = productoService.obtenerPorId(movimiento.getProducto().getId());

        if (producto == null) {
            throw new RuntimeException("Error de Negocio: El producto no existe para realizar el movimiento.");
        }

        // 2. Determinar el tipo de movimiento y actualizar el stock
        int cantidadActual = producto.getCantidad();
        int cantidadMovimiento = movimiento.getCantidad();
        String tipo = movimiento.getTipoMovimiento().toUpperCase();

        if ("ENTRADA".equals(tipo)) {
            // Sumar la cantidad para Entradas (ej. compra a proveedor)
            producto.setCantidad(cantidadActual + cantidadMovimiento);
        } else if ("SALIDA".equals(tipo)) {
            // Restar la cantidad para Salidas (ej. venta a cliente)
            if (cantidadActual < cantidadMovimiento) {
                // 🚨 IMPORTANTE: Lanza una excepción para deshacer toda la transacción si el stock es insuficiente
                throw new RuntimeException("Error de Stock: La cantidad solicitada (" + cantidadMovimiento + ") excede el stock actual (" + cantidadActual + ").");
            }
            producto.setCantidad(cantidadActual - cantidadMovimiento);
        } else {
            // Manejo de otros tipos de movimiento (opcional, como 'AJUSTE')
            // throw new RuntimeException("Tipo de movimiento no válido: " + tipo);
        }

        // 3. Guardar (Actualizar) el producto en la base de datos
        // Se llama al servicio de producto, que guardará la nueva cantidad
        productoService.guardar(producto);

        // 4. Guardar el registro del movimiento
        movimientoRepo.save(movimiento);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        // En un sistema real, la eliminación de movimientos es compleja.
        // Si se elimina, también debería revertir el stock.
        // Por simplicidad, aquí solo eliminamos el registro.
        movimientoRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoEntity> obtenerHistorial() {
        // Asumo que el campo de fecha en la entidad es 'fechaMovimiento'
        return movimientoRepo.findAllByOrderByFechaMovimientoDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoEntity> obtenerMovimientosPorRangoDeFecha(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoRepo.findByFechaMovimientoBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoEntity> obtenerMovimientosPorRangoDeFechaYProducto(LocalDateTime inicio, LocalDateTime fin, Long idProducto) {
        // Asumo que tu repositorio usa 'fechaMovimiento' y el ID del producto
        return movimientoRepo.findByFechaMovimientoBetweenAndProducto_Id(inicio, fin, idProducto);
    }
}