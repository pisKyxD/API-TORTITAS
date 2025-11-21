package com.example.demo.controller;

import com.example.demo.model.Sabor;
import com.example.demo.service.SaborService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sabores")
@Tag(name = "Sabores", description = "Operaciones relacionadas con los sabores de productos")
public class SaborController {

    @Autowired
    private SaborService saborService;

    @GetMapping
    @Operation(summary = "Listar todos los sabores")
    public ResponseEntity<List<Sabor>> findAll() {
        List<Sabor> sabores = saborService.findAll();
        if (sabores.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(sabores);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sabor por ID")
    public ResponseEntity<Sabor> findById(@PathVariable Long id) {
        Sabor sabor = saborService.findById(id);
        return sabor != null ? ResponseEntity.ok(sabor) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo sabor")
    public ResponseEntity<Sabor> save(@RequestBody Sabor sabor) {
        return ResponseEntity.status(201).body(saborService.save(sabor));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un sabor")
    public ResponseEntity<Sabor> update(@PathVariable Long id, @RequestBody Sabor sabor) {
        Sabor updated = saborService.update(id, sabor);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un sabor")
    public ResponseEntity<Sabor> patch(@PathVariable Long id, @RequestBody Sabor sabor) {
        Sabor patched = saborService.patch(id, sabor);
        return patched != null ? ResponseEntity.ok(patched) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un sabor")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Sabor sabor = saborService.findById(id);
        if (sabor == null) return ResponseEntity.notFound().build();
        saborService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
