package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.example.demo.model.DetallePedido;
import com.example.demo.repository.DetallePedidoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("all")
public class DetallePedidoServiceTest {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @MockBean
    private DetallePedidoRepository detallePedidoRepository;

    private DetallePedido createDetalle() {
        DetallePedido d = new DetallePedido();
        d.setId_detalle(1L);
        d.setCantidad(2);
        d.setPrecio_unitario(new BigDecimal("0.01"));
        return d;
    }

    @Test
    public void testFindAll() {
        when(detallePedidoRepository.findAll()).thenReturn(List.of(createDetalle()));

        List<DetallePedido> detalles = detallePedidoService.findAll();

        assertNotNull(detalles);
        assertEquals(1, detalles.size());
    }

    @Test
    public void testFindById() {
        when(detallePedidoRepository.findById(1L)).thenReturn(Optional.of(createDetalle()));

        DetallePedido detalle = detallePedidoService.findById(1L);

        assertNotNull(detalle);
        assertEquals(2, detalle.getCantidad());
    }

    @Test
    public void testSave() {
        DetallePedido detalle = createDetalle();
        when(detallePedidoRepository.save(detalle)).thenReturn(detalle);

        DetallePedido saved = detallePedidoService.save(detalle);

        assertNotNull(saved);
        assertEquals(new BigDecimal("0.01"), saved.getPrecio_unitario());
    }

    @Test
    public void testUpdate() {
        DetallePedido existing = createDetalle();

        DetallePedido updated = new DetallePedido();
        updated.setCantidad(10);
        updated.setPrecio_unitario(new BigDecimal("2000.0"));

        when(detallePedidoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(detallePedidoRepository.save(any(DetallePedido.class))).thenReturn(existing);

        DetallePedido result = detallePedidoService.update(1L, updated);

        assertNotNull(result);
        assertEquals(10, result.getCantidad());
        assertEquals(new BigDecimal("2000.0"), result.getPrecio_unitario());
    }

    @Test
    public void testPatch() {
        DetallePedido existing = createDetalle();

        DetallePedido patchData = new DetallePedido();
        patchData.setCantidad(5);

        when(detallePedidoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(detallePedidoRepository.save(any(DetallePedido.class))).thenReturn(existing);

        DetallePedido result = detallePedidoService.patch(1L, patchData);

        assertNotNull(result);
        assertEquals(5, result.getCantidad());
    }

    @Test
    public void testDelete() {
        detallePedidoService.delete(1L);

        verify(detallePedidoRepository, times(1)).deleteById(1L);
    }
}
