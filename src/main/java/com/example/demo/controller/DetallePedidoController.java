package com.example.demo.controller;

import com.example.demo.model.DetallePedido;
import com.example.demo.service.DetallePedidoService;
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
@RequestMapping("/api/v1/detalles-pedido")
@Tag(name = "Detalles de Pedido", description = "Operaciones relacionadas con los detalles de cada pedido")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @GetMapping
    @Operation(summary = "Listar todos los detalles de pedido")
    public ResponseEntity<List<DetallePedido>> findAll() {
        List<DetallePedido> detalles = detallePedidoService.findAll();
        if (detalles.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar detalle de pedido por ID")
    public ResponseEntity<DetallePedido> findById(@PathVariable Long id) {
        DetallePedido detalle = detallePedidoService.findById(id);
        return detalle != null ? ResponseEntity.ok(detalle) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo detalle de pedido")
    public ResponseEntity<DetallePedido> save(@RequestBody DetallePedido detalle) {
        return ResponseEntity.status(201).body(detallePedidoService.save(detalle));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un detalle de pedido")
    public ResponseEntity<DetallePedido> update(@PathVariable Long id, @RequestBody DetallePedido detalle) {
        DetallePedido updated = detallePedidoService.update(id, detalle);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un detalle de pedido")
    public ResponseEntity<DetallePedido> patch(@PathVariable Long id, @RequestBody DetallePedido detalle) {
        DetallePedido patched = detallePedidoService.patch(id, detalle);
        return patched != null ? ResponseEntity.ok(patched) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un detalle de pedido")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        DetallePedido detalle = detallePedidoService.findById(id);
        if (detalle == null) return ResponseEntity.notFound().build();
        detallePedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
