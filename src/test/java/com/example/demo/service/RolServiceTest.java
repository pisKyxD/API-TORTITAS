package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.model.Rol;
import com.example.demo.repository.RolRepository;

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
public class RolServiceTest {

    @Autowired
    private RolService rolService;

    @MockBean
    private RolRepository rolRepository;

    private Rol createRol() {
        Rol rol = new Rol();
        rol.setId_rol(1L);
        rol.setNombre("ADMIN");
        return rol;
    }

    @Test
    public void testFindAll() {
        when(rolRepository.findAll()).thenReturn(List.of(createRol()));

        List<Rol> roles = rolService.findAll();

        assertNotNull(roles);
        assertEquals(1, roles.size());
    }

    @Test
    public void testFindById() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(createRol()));

        Rol rol = rolService.findById(1L);

        assertNotNull(rol);
        assertEquals("ADMIN", rol.getNombre());
    }

    @Test
    public void testSave() {
        Rol rol = createRol();
        when(rolRepository.save(rol)).thenReturn(rol);

        Rol saved = rolService.save(rol);

        assertNotNull(saved);
        assertEquals("ADMIN", saved.getNombre());
    }

    @Test
    public void testUpdate() {
        Rol existing = createRol();

        Rol updated = new Rol();
        updated.setNombre("USER");

        when(rolRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(rolRepository.save(any(Rol.class))).thenReturn(existing);

        Rol result = rolService.update(1L, updated);

        assertNotNull(result);
        assertEquals("USER", result.getNombre());
    }

    @Test
    public void testPatch() {
        Rol existing = createRol();

        Rol patchData = new Rol();
        patchData.setNombre("INVITADO");

        when(rolRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(rolRepository.save(any(Rol.class))).thenReturn(existing);

        Rol result = rolService.patch(1L, patchData);

        assertNotNull(result);
        assertEquals("INVITADO", result.getNombre());
    }

    @Test
    public void testDelete() {
        doNothing().when(rolRepository).deleteById(1L);

        rolService.delete(1L);

        verify(rolRepository, times(1)).deleteById(1L);
    }
}
