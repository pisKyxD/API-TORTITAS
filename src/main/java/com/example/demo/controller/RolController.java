package com.example.demo.controller;

import com.example.demo.model.Rol;
import com.example.demo.service.RolService;
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
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Operaciones relacionadas con los roles de usuario")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping
    @Operation(summary = "Listar todos los roles")
    public ResponseEntity<List<Rol>> findAll() {
        List<Rol> roles = rolService.findAll();
        if (roles.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar rol por ID")
    public ResponseEntity<Rol> findById(@PathVariable Long id) {
        Rol rol = rolService.findById(id);
        return rol != null ? ResponseEntity.ok(rol) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo rol")
    public ResponseEntity<Rol> save(@RequestBody Rol rol) {
        return ResponseEntity.status(201).body(rolService.save(rol));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un rol")
    public ResponseEntity<Rol> update(@PathVariable Long id, @RequestBody Rol rol) {
        Rol updated = rolService.update(id, rol);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un rol")
    public ResponseEntity<Rol> patch(@PathVariable Long id, @RequestBody Rol rol) {
        Rol patched = rolService.patch(id, rol);
        return patched != null ? ResponseEntity.ok(patched) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un rol")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Rol rol = rolService.findById(id);
        if (rol == null) return ResponseEntity.notFound().build();
        rolService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
