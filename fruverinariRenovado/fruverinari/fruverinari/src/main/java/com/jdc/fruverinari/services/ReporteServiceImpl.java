package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.MovimientoEntity;
import com.jdc.fruverinari.entities.ProductoEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * PDF deshabilitado en perfil local (sin dependencia OpenPDF).
 * Agrega com.github.librepdf:openpdf al pom para exportar PDFs.
 */
@Service
public class ReporteServiceImpl implements ReporteService {

    @Override
    public void exportarProductosPDF(HttpServletResponse response, List<ProductoEntity> productos,
                                     String nombreArchivo, String titulo) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED,
                "Exportacion PDF no disponible en modo local.");
    }

    @Override
    public void exportarMovimientosPDF(HttpServletResponse response, List<MovimientoEntity> movimientos,
                                       String nombreArchivo, String titulo) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED,
                "Exportacion PDF no disponible en modo local.");
    }
}
