package com.example.demo.controller;

import com.example.demo.model.Pago;
import com.example.demo.service.PagoService;
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
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "Operaciones relacionadas con los pagos de los pedidos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    @Operation(summary = "Listar todos los pagos")
    public ResponseEntity<List<Pago>> findAll() {
        List<Pago> pagos = pagoService.findAll();
        if (pagos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pago por ID")
    public ResponseEntity<Pago> findById(@PathVariable Long id) {
        Pago pago = pagoService.findById(id);
        return pago != null ? ResponseEntity.ok(pago) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo pago")
    public ResponseEntity<Pago> save(@RequestBody Pago pago) {
        return ResponseEntity.status(201).body(pagoService.save(pago));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pago")
    public ResponseEntity<Pago> update(@PathVariable Long id, @RequestBody Pago pago) {
        Pago updated = pagoService.update(id, pago);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un pago")
    public ResponseEntity<Pago> patch(@PathVariable Long id, @RequestBody Pago pago) {
        Pago patched = pagoService.patch(id, pago);
        return patched != null ? ResponseEntity.ok(patched) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pago")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Pago pago = pagoService.findById(id);
        if (pago == null) return ResponseEntity.notFound().build();
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
