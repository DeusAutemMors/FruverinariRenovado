package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.ProveedorEntity;
import com.jdc.fruverinari.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<ProveedorEntity> obtenerTodos() {
        return proveedorRepository.findAll(); // Devuelve todos los proveedores
    }

    @Override
    public ProveedorEntity obtenerPorId(Long id) {
        return proveedorRepository.findById(id).orElse(null); // Devuelve el proveedor por ID
    }

    @Override
    public void guardar(ProveedorEntity proveedor) {
        proveedorRepository.save(proveedor); // Guarda o actualiza el proveedor
    }

    @Override
    public void suspender(Long id) {
        ProveedorEntity proveedor = proveedorRepository.findById(id).orElse(null);
        if (proveedor != null) {
            proveedor.setActivo(false); // Cambia el estado de activo a inactivo
            proveedorRepository.save(proveedor); // Guarda el proveedor con el nuevo estado
        }
    }
}
