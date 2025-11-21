package com.example.demo.controller;

import com.example.demo.model.Envio;
import com.example.demo.service.EnvioService;
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
@RequestMapping("/api/v1/envios")
@Tag(name = "Envíos", description = "Operaciones relacionadas con los envíos de pedidos")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @GetMapping
    @Operation(summary = "Listar todos los envíos")
    public ResponseEntity<List<Envio>> findAll() {
        List<Envio> envios = envioService.findAll();
        if (envios.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(envios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar envío por ID")
    public ResponseEntity<Envio> findById(@PathVariable Long id) {
        Envio envio = envioService.findById(id);
        return envio != null ? ResponseEntity.ok(envio) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo envío")
    public ResponseEntity<Envio> save(@RequestBody Envio envio) {
        return ResponseEntity.status(201).body(envioService.save(envio));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un envío")
    public ResponseEntity<Envio> update(@PathVariable Long id, @RequestBody Envio envio) {
        Envio updated = envioService.update(id, envio);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un envío")
    public ResponseEntity<Envio> patch(@PathVariable Long id, @RequestBody Envio envio) {
        Envio patched = envioService.patch(id, envio);
        return patched != null ? ResponseEntity.ok(patched) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un envío")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Envio envio = envioService.findById(id);
        if (envio == null) return ResponseEntity.notFound().build();
        envioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
