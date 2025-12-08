package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Comuna;
import com.example.demo.model.Direccion;
import com.example.demo.repository.ComunaRepository;
import com.example.demo.repository.EnvioRepository;
import com.example.demo.repository.DireccionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("all")
public class ComunaServiceTest {

    @Autowired
    private ComunaService comunaService;

    @MockBean
    private ComunaRepository comunaRepository;

    @MockBean
    private EnvioRepository envioRepository;

    @MockBean
    private DireccionRepository direccionRepository;

    private Comuna createComuna() {
        Comuna c = new Comuna();
        c.setId_comuna(1L);
        c.setNombre_comuna("Santiago");
        return c;
    }

    @Test
    public void testFindAll() {
        when(comunaRepository.findAll()).thenReturn(List.of(createComuna()));

        List<Comuna> comunas = comunaService.findAll();

        assertNotNull(comunas);
        assertEquals(1, comunas.size());
    }

    @Test
    public void testFindById() {
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(createComuna()));

        Comuna comuna = comunaService.findById(1L);

        assertNotNull(comuna);
        assertEquals("Santiago", comuna.getNombre_comuna());
    }

    @Test
    public void testSave() {
        Comuna comuna = createComuna();
        when(comunaRepository.save(comuna)).thenReturn(comuna);

        Comuna saved = comunaService.save(comuna);

        assertNotNull(saved);
        assertEquals("Santiago", saved.getNombre_comuna());
    }

    @Test
    public void testUpdate() {
        Comuna existing = createComuna();

        Comuna updated = new Comuna();
        updated.setNombre_comuna("Providencia");

        when(comunaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(comunaRepository.save(any(Comuna.class))).thenReturn(existing);

        Comuna result = comunaService.update(1L, updated);

        assertNotNull(result);
        assertEquals("Providencia", result.getNombre_comuna());
    }

    @Test
    public void testPatch() {
        Comuna existing = createComuna();

        Comuna patchData = new Comuna();
        patchData.setNombre_comuna("Ñuñoa");

        when(comunaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(comunaRepository.save(any(Comuna.class))).thenReturn(existing);

        Comuna result = comunaService.patch(1L, patchData);

        assertNotNull(result);
        assertEquals("Ñuñoa", result.getNombre_comuna());
    }

    @Test
    public void testDelete() {
        Comuna comuna = createComuna();

        Direccion d1 = new Direccion();
        Direccion d2 = new Direccion();

        comuna.setDirecciones(List.of(d1, d2));

        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comuna));

        doNothing().when(envioRepository).deleteByDireccion(d1);
        doNothing().when(envioRepository).deleteByDireccion(d2);
        doNothing().when(direccionRepository).delete(d1);
        doNothing().when(direccionRepository).delete(d2);
        doNothing().when(comunaRepository).delete(comuna);

        comunaService.delete(1L);

        verify(envioRepository, times(2)).deleteByDireccion(d1);
        verify(envioRepository, times(2)).deleteByDireccion(d2);

        verify(direccionRepository, times(2)).delete(d1);
        verify(direccionRepository, times(2)).delete(d2);

        verify(comunaRepository, times(1)).delete(comuna);
    }
}
