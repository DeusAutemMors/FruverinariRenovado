package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.CategoriaEntity;
import com.jdc.fruverinari.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaEntity> obtenerTodos() {
        return categoriaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaEntity obtenerPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void guardar(CategoriaEntity categoria) {
        categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }
}
