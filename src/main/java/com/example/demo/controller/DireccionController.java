package com.example.demo.controller;

import com.example.demo.model.Direccion;
import com.example.demo.service.DireccionService;
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
@RequestMapping("/api/v1/direcciones")
@Tag(name = "Direcciones", description = "Operaciones relacionadas con las direcciones de los usuarios")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @GetMapping
    @Operation(summary = "Listar todas las direcciones")
    public ResponseEntity<List<Direccion>> findAll() {
        List<Direccion> direcciones = direccionService.findAll();
        if (direcciones.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(direcciones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar dirección por ID")
    public ResponseEntity<Direccion> findById(@PathVariable Long id) {
        Direccion direccion = direccionService.findById(id);
        return direccion != null ? ResponseEntity.ok(direccion) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva dirección")
    public ResponseEntity<Direccion> save(@RequestBody Direccion direccion) {
        return ResponseEntity.status(201).body(direccionService.save(direccion));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una dirección")
    public ResponseEntity<Direccion> update(@PathVariable Long id, @RequestBody Direccion direccion) {
        Direccion updated = direccionService.update(id, direccion);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una dirección")
    public ResponseEntity<Direccion> patch(@PathVariable Long id, @RequestBody Direccion direccion) {
        Direccion patched = direccionService.patch(id, direccion);
        return patched != null ? ResponseEntity.ok(patched) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una dirección")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Direccion direccion = direccionService.findById(id);
        if (direccion == null) return ResponseEntity.notFound().build();
        direccionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
