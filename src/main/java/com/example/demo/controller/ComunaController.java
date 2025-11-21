package com.example.demo.controller;

import com.example.demo.model.Comuna;
import com.example.demo.service.ComunaService;
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
@RequestMapping("/api/v1/comunas")
@Tag(name = "Comunas", description = "Operaciones relacionadas con las comunas")
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    @GetMapping
    @Operation(summary = "Listar todas las comunas")
    public ResponseEntity<List<Comuna>> findAll() {
        List<Comuna> comunas = comunaService.findAll();
        if (comunas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(comunas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar comuna por ID")
    public ResponseEntity<Comuna> findById(@PathVariable Long id) {
        Comuna comuna = comunaService.findById(id);
        return comuna != null ? ResponseEntity.ok(comuna) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva comuna")
    public ResponseEntity<Comuna> save(@RequestBody Comuna comuna) {
        return ResponseEntity.status(201).body(comunaService.save(comuna));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una comuna")
    public ResponseEntity<Comuna> update(@PathVariable Long id, @RequestBody Comuna comuna) {
        Comuna updated = comunaService.update(id, comuna);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una comuna")
    public ResponseEntity<Comuna> patch(@PathVariable Long id, @RequestBody Comuna comuna) {
        Comuna patched = comunaService.patch(id, comuna);
        return patched != null ? ResponseEntity.ok(patched) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una comuna")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Comuna comuna = comunaService.findById(id);
        if (comuna == null) return ResponseEntity.notFound().build();
        comunaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
