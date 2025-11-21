package com.example.demo.controller;

import com.example.demo.model.Region;
import com.example.demo.service.RegionService;
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
@RequestMapping("/api/v1/regiones")
@Tag(name = "Regiones", description = "Operaciones relacionadas con las regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    @Operation(summary = "Listar todas las regiones")
    public ResponseEntity<List<Region>> findAll() {
        List<Region> regiones = regionService.findAll();
        if (regiones.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(regiones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar región por ID")
    public ResponseEntity<Region> findById(@PathVariable Long id) {
        Region region = regionService.findById(id);
        return region != null ? ResponseEntity.ok(region) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva región")
    public ResponseEntity<Region> save(@RequestBody Region region) {
        return ResponseEntity.status(201).body(regionService.save(region));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una región")
    public ResponseEntity<Region> update(@PathVariable Long id, @RequestBody Region region) {
        Region updated = regionService.update(id, region);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una región")
    public ResponseEntity<Region> patch(@PathVariable Long id, @RequestBody Region region) {
        Region patched = regionService.patch(id, region);
        return patched != null ? ResponseEntity.ok(patched) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una región")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Region region = regionService.findById(id);
        if (region == null) return ResponseEntity.notFound().build();
        regionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
