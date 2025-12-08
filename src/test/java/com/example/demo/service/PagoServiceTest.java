package com.example.demo.service;

import com.example.demo.model.Pago;
import com.example.demo.repository.PagoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("all")
public class PagoServiceTest {

    @Autowired
    private PagoService pagoService;

    @MockBean
    private PagoRepository pagoRepository;

    private Pago createPago() {
        Pago p = new Pago();
        p.setId_pago(1L);
        p.setMonto(new BigDecimal("1000.00"));
        p.setMetodo_pago("Tarjeta");
        p.setEstado_pago("PENDIENTE");
        p.setFecha_pago(LocalDateTime.now());
        return p;
    }

    @Test
    public void testFindAll() {
        when(pagoRepository.findAll()).thenReturn(List.of(createPago()));

        List<Pago> pagos = pagoService.findAll();

        assertNotNull(pagos);
        assertEquals(1, pagos.size());
    }

    @Test
    public void testFindById() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(createPago()));

        Pago pago = pagoService.findById(1L);

        assertNotNull(pago);
        assertEquals("Tarjeta", pago.getMetodo_pago());
    }

    @Test
    public void testSave() {
        Pago pago = createPago();
        when(pagoRepository.save(pago)).thenReturn(pago);

        Pago saved = pagoService.save(pago);

        assertNotNull(saved);
        assertEquals(new BigDecimal("1000.00"), saved.getMonto());
    }

    @Test
    public void testUpdate() {
        Pago existing = createPago();

        Pago updated = new Pago();
        updated.setMonto(new BigDecimal("2000.00"));
        updated.setMetodo_pago("Efectivo");
        updated.setEstado_pago("PAGADO");
        updated.setFecha_pago(LocalDateTime.now());

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(pagoRepository.save(any(Pago.class))).thenReturn(existing);

        Pago result = pagoService.update(1L, updated);

        assertNotNull(result);
        assertEquals(new BigDecimal("2000.00"), result.getMonto());
        assertEquals("Efectivo", result.getMetodo_pago());
        assertEquals("PAGADO", result.getEstado_pago());
    }

    @Test
    public void testPatch() {
        Pago existing = createPago();

        Pago patchData = new Pago();
        patchData.setEstado_pago("RECHAZADO");

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(pagoRepository.save(any(Pago.class))).thenReturn(existing);

        Pago result = pagoService.patch(1L, patchData);

        assertNotNull(result);
        assertEquals("RECHAZADO", result.getEstado_pago());
    }

    @Test
    public void testDelete() {
        pagoService.delete(1L);

        verify(pagoRepository, times(1)).deleteById(1L);
    }
}
