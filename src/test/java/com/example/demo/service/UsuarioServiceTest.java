package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.model.Envio;
import com.example.demo.model.Pago;
import com.example.demo.model.Pedido;
import com.example.demo.model.Rol;
import com.example.demo.model.Usuario;
import com.example.demo.repository.DetallePedidoRepository;
import com.example.demo.repository.DireccionRepository;
import com.example.demo.repository.EnvioRepository;
import com.example.demo.repository.PagoRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.UsuarioRepository;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("all")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private DireccionRepository direccionRepository;

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private DetallePedidoRepository detallePedidoRepository;

    @MockBean
    private PagoRepository pagoRepository;

    @MockBean
    private EnvioRepository envioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol();
        rol.setId_rol(1L);
        rol.setNombre("CLIENTE");

        usuario = new Usuario();
        usuario.setId_usuario(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setEmail("correo@test.com");
        usuario.setPassword("1234");
        usuario.setTelefono("123456789");
        usuario.setRol(rol);
    }

    private Usuario createUsuario() {
        Usuario u = new Usuario();
        u.setId_usuario(1L);
        u.setNombre("Juan");
        u.setApellido("Pérez");
        u.setEmail("example@test.com");
        u.setPassword("1234");
        u.setTelefono("987654321");

        Rol r = new Rol();
        r.setId_rol(1L);
        u.setRol(r);

        return u;
    }

    @Test
    void testFindAll() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> result = usuarioService.findAll();

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
    }

    @Test
    void testFindById() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findById(1L);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
    }

    @Test
    void testSave() {
        Usuario nuevo = createUsuario();

        when(passwordEncoder.encode("1234")).thenReturn("hashed1234");
        when(usuarioRepository.save(nuevo)).thenReturn(nuevo);

        Usuario result = usuarioService.save(nuevo);

        assertEquals("hashed1234", result.getPassword());
        verify(usuarioRepository, times(1)).save(nuevo);
    }

    @Test
    void testUpdate() {
        Usuario updateData = new Usuario();
        updateData.setNombre("Nuevo");
        updateData.setApellido("Apellido");
        updateData.setEmail("nuevo@test.com");
        updateData.setPassword("passNueva");
        updateData.setTelefono("1111");
        updateData.setRol(rol);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("passNueva")).thenReturn("hashedPassNueva");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.update(1L, updateData);

        assertNotNull(result);
        assertEquals("Nuevo", result.getNombre());
        assertEquals("hashedPassNueva", result.getPassword());
    }

    @Test
    void testPatch() {
        Usuario patchData = new Usuario();
        patchData.setNombre("NuevoNombre");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.patch(1L, patchData);

        assertEquals("NuevoNombre", result.getNombre());
    }

    @Test
    void testLogin() {
        when(usuarioRepository.findByEmail("correo@test.com")).thenReturn(usuario);
        when(passwordEncoder.matches("1234", usuario.getPassword())).thenReturn(true);

        Map<String, String> body = new HashMap<>();
        body.put("email", "correo@test.com");
        body.put("password", "1234");

        ResponseEntity<?> response = usuarioService.login(body);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testDeleteUsuario() {
        Pedido pedido = new Pedido();
        pedido.setId_pedido(10L);

        Pago pago = new Pago();
        pago.setId_pago(5L);
        pedido.setPago(pago);

        Envio envio = new Envio();
        envio.setId_envio(7L);
        pedido.setEnvio(envio);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(pedidoRepository.findByUsuario(usuario)).thenReturn(List.of(pedido));

        boolean deleted = usuarioService.deleteUsuario(1L);

        assertTrue(deleted);
        verify(usuarioRepository, times(1)).delete(usuario);
    }
}
