package com.jdc.fruverinari.controller;

import com.jdc.fruverinari.entities.CategoriaEntity;
import com.jdc.fruverinari.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaEntity> obtenerCategorias() {
        return categoriaService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public CategoriaEntity obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaService.obtenerPorId(id);
    }

    @PostMapping
    public void agregarCategoria(@RequestBody CategoriaEntity categoriaEntity) {
        categoriaService.guardar(categoriaEntity);
    }

    @PutMapping("/{id}")
    public void actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaEntity categoriaEntity) {
        categoriaEntity.setIdCategoria(id);
        categoriaService.guardar(categoriaEntity);
    }

    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
    }
}
