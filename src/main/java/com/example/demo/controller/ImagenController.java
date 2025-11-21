package com.example.demo.controller;

import com.example.demo.model.Imagen;
import com.example.demo.service.ImagenService;
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
@RequestMapping("/api/v1/imagenes")
@Tag(name = "Imágenes", description = "Operaciones relacionadas con las imágenes de los productos")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @GetMapping
    @Operation(summary = "Listar todas las imágenes")
    public ResponseEntity<List<Imagen>> findAll() {
        List<Imagen> imagenes = imagenService.findAll();
        if (imagenes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(imagenes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar imagen por ID")
    public ResponseEntity<Imagen> findById(@PathVariable Long id) {
        Imagen imagen = imagenService.findById(id);
        return imagen != null ? ResponseEntity.ok(imagen) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva imagen")
    public ResponseEntity<Imagen> save(@RequestBody Imagen imagen) {
        return ResponseEntity.status(201).body(imagenService.save(imagen));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una imagen")
    public ResponseEntity<Imagen> update(@PathVariable Long id, @RequestBody Imagen imagen) {
        Imagen updated = imagenService.update(id, imagen);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una imagen")
    public ResponseEntity<Imagen> patch(@PathVariable Long id, @RequestBody Imagen imagen) {
        Imagen patched = imagenService.patch(id, imagen);
        return patched != null ? ResponseEntity.ok(patched) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una imagen")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Imagen imagen = imagenService.findById(id);
        if (imagen == null) return ResponseEntity.notFound().build();
        imagenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
