package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Direccion;
import com.example.demo.model.Usuario;
import com.example.demo.repository.DireccionRepository;
import com.example.demo.repository.EnvioRepository;
import com.example.demo.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("all")
public class DireccionServiceTest {

    @Autowired
    private DireccionService direccionService;

    @MockBean
    private DireccionRepository direccionRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private EnvioRepository envioRepository;

    private Direccion createDireccion() {
        Direccion d = new Direccion();
        d.setId_direccion(1L);
        d.setCalle("Calle 1");
        d.setNumero("123");
        d.setReferencia("Casa roja");
        return d;
    }

    private Usuario createUsuario() {
        Usuario u = new Usuario();
        u.setId_usuario(1L);
        return u;
    }

    @Test
    public void testFindAll() {
        when(direccionRepository.findAll()).thenReturn(List.of(createDireccion()));

        List<Direccion> lista = direccionService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
    }

    @Test
    public void testFindById() {
        when(direccionRepository.findById(1L)).thenReturn(Optional.of(createDireccion()));

        Direccion direccion = direccionService.findById(1L);

        assertNotNull(direccion);
        assertEquals("Calle 1", direccion.getCalle());
    }

    @Test
    public void testFindByUsuarioId() {
        Usuario usuario = createUsuario();
        Direccion direccion = createDireccion();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(direccionRepository.findByUsuario(usuario)).thenReturn(List.of(direccion));

        List<Direccion> resultado = direccionService.findByUsuarioId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testSave() {
        Direccion direccion = createDireccion();
        when(direccionRepository.save(direccion)).thenReturn(direccion);

        Direccion saved = direccionService.save(direccion);

        assertNotNull(saved);
        assertEquals("Calle 1", saved.getCalle());
    }

    @Test
    public void testUpdate() {
        Direccion existing = createDireccion();

        Direccion updated = new Direccion();
        updated.setCalle("Nueva calle");
        updated.setNumero("999");
        updated.setReferencia("Frente a plaza");

        when(direccionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(direccionRepository.save(any(Direccion.class))).thenReturn(existing);

        Direccion result = direccionService.update(1L, updated);

        assertNotNull(result);
        assertEquals("Nueva calle", result.getCalle());
        assertEquals("999", result.getNumero());
        assertEquals("Frente a plaza", result.getReferencia());
    }

    @Test
    public void testPatch() {
        Direccion existing = createDireccion();

        Direccion patchData = new Direccion();
        patchData.setCalle("Parcial calle");

        when(direccionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(direccionRepository.save(any(Direccion.class))).thenReturn(existing);

        Direccion result = direccionService.patch(1L, patchData);

        assertNotNull(result);
        assertEquals("Parcial calle", result.getCalle());
    }

    @Test
    public void testDelete() {
        Direccion direccion = createDireccion();

        when(direccionRepository.findById(1L)).thenReturn(Optional.of(direccion));

        direccionService.delete(1L);

        verify(envioRepository, times(1)).deleteByDireccion(direccion);
        verify(direccionRepository, times(1)).delete(direccion);
    }
}
