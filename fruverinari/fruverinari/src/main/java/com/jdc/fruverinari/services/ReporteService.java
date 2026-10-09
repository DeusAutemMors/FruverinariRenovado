package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.MovimientoEntity;
import com.jdc.fruverinari.entities.ProductoEntity;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ReporteService {

    // Método ya existente
    void exportarProductosPDF(HttpServletResponse response, List<ProductoEntity> productos, String nombreArchivo, String titulo) throws IOException;

    // Nuevo método para movimientos
    void exportarMovimientosPDF(HttpServletResponse response,
                                List<MovimientoEntity> movimientos,
                                String nombreArchivo,
                                String titulo) throws IOException;
}
