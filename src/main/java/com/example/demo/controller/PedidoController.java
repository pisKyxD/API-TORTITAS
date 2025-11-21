package com.example.demo.controller;

import com.example.demo.model.Pedido;
import com.example.demo.service.PedidoService;
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
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con los pedidos de los usuarios")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Listar todos los pedidos")
    public ResponseEntity<List<Pedido>> findAll() {
        List<Pedido> pedidos = pedidoService.findAll();
        if (pedidos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID")
    public ResponseEntity<Pedido> findById(@PathVariable Long id) {
        Pedido pedido = pedidoService.findById(id);
        return pedido != null ? ResponseEntity.ok(pedido) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo pedido")
    public ResponseEntity<Pedido> save(@RequestBody Pedido pedido) {
        return ResponseEntity.status(201).body(pedidoService.save(pedido));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pedido")
    public ResponseEntity<Pedido> update(@PathVariable Long id, @RequestBody Pedido pedido) {
        Pedido updated = pedidoService.update(id, pedido);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un pedido")
    public ResponseEntity<Pedido> patch(@PathVariable Long id, @RequestBody Pedido pedido) {
        Pedido patched = pedidoService.patch(id, pedido);
        return patched != null ? ResponseEntity.ok(patched) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pedido")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Pedido pedido = pedidoService.findById(id);
        if (pedido == null) return ResponseEntity.notFound().build();
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
