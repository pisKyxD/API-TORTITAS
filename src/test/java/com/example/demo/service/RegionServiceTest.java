package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.model.Comuna;
import com.example.demo.model.Direccion;
import com.example.demo.model.Region;
import com.example.demo.repository.ComunaRepository;
import com.example.demo.repository.DireccionRepository;
import com.example.demo.repository.EnvioRepository;
import com.example.demo.repository.RegionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("all")
public class RegionServiceTest {

    @Autowired
    private RegionService regionService;

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private EnvioRepository envioRepository;

    @MockBean
    private DireccionRepository direccionRepository;

    @MockBean
    private ComunaRepository comunaRepository;

    private Region createEntidad() {
        Region r = new Region();
        r.setId_region(1L);
        r.setNombre_region("Metropolitana");
        return r;
    }

    @Test
    public void testFindAll() {
        when(regionRepository.findAll()).thenReturn(List.of(createEntidad()));

        List<Region> regiones = regionService.findAll();

        assertNotNull(regiones);
        assertEquals(1, regiones.size());
    }

    @Test
    public void testFindById() {
        when(regionRepository.findById(1L)).thenReturn(Optional.of(createEntidad()));

        Region region = regionService.findById(1L);

        assertNotNull(region);
        assertEquals("Metropolitana", region.getNombre_region());
    }

    @Test
    public void testSave() {
        Region r = createEntidad();
        when(regionRepository.save(r)).thenReturn(r);

        Region saved = regionService.save(r);

        assertNotNull(saved);
        assertEquals("Metropolitana", saved.getNombre_region());
    }

    @Test
    public void testUpdate() {
        Region existing = createEntidad();

        Region updated = new Region();
        updated.setNombre_region("Valparaíso");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(regionRepository.save(any(Region.class))).thenReturn(existing);

        Region result = regionService.update(1L, updated);

        assertNotNull(result);
        assertEquals("Valparaíso", result.getNombre_region());
    }

    @Test
    public void testPatchEntidad() {
        Region existing = createEntidad();

        Region patchData = new Region();
        patchData.setNombre_region("Biobío");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(regionRepository.save(any(Region.class))).thenReturn(existing);

        Region result = regionService.patch(1L, patchData);

        assertNotNull(result);
        assertEquals("Biobío", result.getNombre_region());
    }

    @Test
    public void testDeleteById() {
        Region region = createEntidad();

        Comuna comuna = new Comuna();
        comuna.setId_comuna(10L);

        Direccion dir = new Direccion();
        dir.setId_direccion(99L);

        comuna.setDirecciones(List.of(dir));
        region.setComunas(List.of(comuna));

        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));

        regionService.delete(1L);

        verify(envioRepository, times(1)).deleteByDireccion(dir);
        verify(direccionRepository, times(1)).delete(dir);
        verify(comunaRepository, times(1)).delete(comuna);
        verify(regionRepository, times(1)).delete(region);
    }
}
