package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.demo.model.Envio;
import com.example.demo.repository.EnvioRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("all")
public class EnvioServiceTest {

    @Autowired
    private EnvioService envioService;

    @MockBean
    private EnvioRepository envioRepository;

    private Envio createEnvio() {
        Envio e = new Envio();
        e.setId_envio(1L);
        e.setDireccion_envio("Calle 123");
        e.setFecha_envio(LocalDateTime.of(2024, 1, 1, 10, 0));
        e.setEstado_envio("EN CAMINO");
        return e;
    }

    @Test
    public void testFindAll() {
        when(envioRepository.findAll()).thenReturn(List.of(createEnvio()));

        List<Envio> envios = envioService.findAll();

        assertNotNull(envios);
        assertEquals(1, envios.size());
    }

    @Test
    public void testFindById() {
        when(envioRepository.findById(1L)).thenReturn(Optional.of(createEnvio()));

        Envio envio = envioService.findById(1L);

        assertNotNull(envio);
        assertEquals("EN CAMINO", envio.getEstado_envio());
    }

    @Test
    public void testSave() {
        Envio envio = createEnvio();

        when(envioRepository.save(envio)).thenReturn(envio);

        Envio saved = envioService.save(envio);

        assertNotNull(saved);
        assertEquals("Calle 123", saved.getDireccion_envio());
    }

    @Test
    public void testUpdate() {
        Envio existing = createEnvio();

        Envio updated = new Envio();
        updated.setDireccion_envio("Nueva dirección");
        updated.setFecha_envio(LocalDateTime.of(2025, 3, 15, 18, 30));
        updated.setEstado_envio("ENTREGADO");

        when(envioRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(envioRepository.save(any(Envio.class))).thenReturn(existing);

        Envio result = envioService.update(1L, updated);

        assertNotNull(result);
        assertEquals("Nueva dirección", result.getDireccion_envio());
        assertEquals("ENTREGADO", result.getEstado_envio());
        assertEquals(LocalDateTime.of(2025, 3, 15, 18, 30), result.getFecha_envio());
    }

    @Test
    public void testPatch() {
        Envio existing = createEnvio();

        Envio patchData = new Envio();
        patchData.setEstado_envio("ESPERANDO");

        when(envioRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(envioRepository.save(any(Envio.class))).thenReturn(existing);

        Envio result = envioService.patch(1L, patchData);

        assertNotNull(result);
        assertEquals("ESPERANDO", result.getEstado_envio());
    }

    @Test
    public void testDelete() {
        envioService.delete(1L);
        verify(envioRepository, times(1)).deleteById(1L);
    }
}
