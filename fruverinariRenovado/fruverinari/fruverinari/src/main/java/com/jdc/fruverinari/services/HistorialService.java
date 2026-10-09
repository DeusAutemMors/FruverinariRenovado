package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.HistorialEntity;
import com.jdc.fruverinari.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialService {

    @Autowired
    private HistorialRepository historialRepo;

    /**
     * Registra cualquier acción en el historial.
     *
     * @param entidad      Nombre de la entidad (Producto, Proveedor, Usuario, etc.)
     * @param entidadId    ID de la entidad afectada.
     * @param accion       Acción realizada (CREAR, EDITAR, SUSPENDER, ACTIVAR, ELIMINAR).
     * @param descripcion  Descripción detallada de la acción.
     * @param usuario      Usuario que realizó la acción.
     */
    public void registrarAccion(String entidad, Long entidadId, String accion, String descripcion, String usuario) {
        HistorialEntity h = new HistorialEntity();
        h.setEntidad(entidad);
        h.setEntidadId(entidadId);
        h.setAccion(accion);
        h.setDescripcion(descripcion);
        h.setUsuario(usuario);

        historialRepo.save(h);
    }

    /**
     * Alias para registrar acciones sin necesidad de cambiar código previo.
     */
    public void registrar(String entidad, Long entidadId, String accion, String descripcion, String usuario) {
        registrarAccion(entidad, entidadId, accion, descripcion, usuario);
    }

    /**
     * Obtiene todos los registros del historial.
     */
    public List<HistorialEntity> obtenerTodos() {
        return historialRepo.findAll();
    }
}
