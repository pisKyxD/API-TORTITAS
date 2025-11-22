package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Imagen;
import com.example.demo.service.ImagenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/imagenes-productos")
@Tag(name = "Imágenes de Productos", description = "Operaciones relacionadas con imágenes reales de productos")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @GetMapping
    @Operation(summary = "Listar todas las imágenes", description = "Obtiene una lista de todas las imágenes de productos registradas.")
    public ResponseEntity<List<Imagen>> findAll() {
        List<Imagen> imagenes = imagenService.findAll();
        if (imagenes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(imagenes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar imagen por ID", description = "Obtiene la información de una imagen de producto específica por su ID.")
    public ResponseEntity<Imagen> findById(@PathVariable Long id) {
        Imagen imagen = imagenService.findById(id);
        if (imagen == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imagen);
    }

    @PostMapping
    @Operation(summary = "Crear una imagen manualmente", description = "Crea un registro de imagen de producto sin archivo (solo JSON).")
    public ResponseEntity<Imagen> save(@RequestBody Imagen imagen) {
        Imagen created = imagenService.save(imagen);
        return ResponseEntity.status(201).body(created);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Subir una imagen real de producto", description = "Sube una imagen real asociada a un producto. Si el producto ya tenía una imagen, se reemplaza.")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("productoId") Long productoId) {

        try {
            Imagen imagen = imagenService.guardarImagen(file, productoId);

            if (imagen == null) {
                return ResponseEntity.badRequest().body("Error al subir la imagen o producto no existe");
            }

            return ResponseEntity.status(201).body(imagen);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }
    // por las dudas
    @PostMapping(value = "/upload/{idProducto}", consumes = { "multipart/form-data" })
    @Operation(summary = "Subir imagen WEBP a Cloudinary y asociarla al producto")
    public ResponseEntity<Imagen> uploadImagen(
            @PathVariable Long idProducto,
            @RequestPart("file") MultipartFile file) {
        try {
            Imagen imagen = imagenService.guardarImagen(file, idProducto);
            return ResponseEntity.status(201).body(imagen);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una imagen", description = "Modifica todos los campos de una imagen existente.")
    public ResponseEntity<Imagen> update(
            @PathVariable Long id,
            @RequestBody Imagen imagen) {

        Imagen updated = imagenService.update(id, imagen);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una imagen", description = "Modifica solo algunos campos de la imagen.")
    public ResponseEntity<Imagen> patch(
            @PathVariable Long id,
            @RequestBody Imagen imagen) {

        Imagen patched = imagenService.patch(id, imagen);
        if (patched == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patched);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una imagen", description = "Elimina una imagen de producto del sistema por su ID.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Imagen imagen = imagenService.findById(id);
        if (imagen == null) {
            return ResponseEntity.notFound().build();
        }

        imagenService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
