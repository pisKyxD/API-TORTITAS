package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.model.DetallePedido;
import com.example.demo.model.Envio;
import com.example.demo.model.Pago;
import com.example.demo.model.Pedido;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.DetallePedidoRepository;
import com.example.demo.repository.EnvioRepository;
import com.example.demo.repository.PagoRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("all")
public class PedidoServiceTest {

    @Autowired
    private PedidoService pedidoService;

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private DetallePedidoRepository detallePedidoRepository;

    @MockBean
    private PagoRepository pagoRepository;

    @MockBean
    private EnvioRepository envioRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private Pedido createPedido() {
        Pedido p = new Pedido();
        p.setId_pedido(1L);
        p.setFecha_pedido(LocalDateTime.now());
        p.setEstado("PENDIENTE");
        p.setTotal(BigDecimal.valueOf(5000));

        Usuario u = new Usuario();
        u.setId_usuario(1L);
        p.setUsuario(u);

        Pago pago = new Pago();
        pago.setId_pago(1L);
        pago.setMonto(BigDecimal.valueOf(5000));
        p.setPago(pago);

        Envio envio = new Envio();
        envio.setId_envio(1L);
        p.setEnvio(envio);

        return p;
    }

    @Test
    public void testFindAll() {
        when(pedidoRepository.findAll()).thenReturn(List.of(createPedido()));

        List<Pedido> result = pedidoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindById() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(createPedido()));

        Pedido p = pedidoService.findById(1L);

        assertNotNull(p);
        assertEquals("PENDIENTE", p.getEstado());
    }

    @Test
    public void testSave() {
        Pedido p = createPedido();
        when(pedidoRepository.save(p)).thenReturn(p);

        Pedido saved = pedidoService.save(p);

        assertNotNull(saved);
        assertEquals(BigDecimal.valueOf(5000), saved.getTotal());
    }

    @Test
    public void testUpdate() {
        Pedido existing = createPedido();

        Pedido updated = new Pedido();
        updated.setEstado("PAGADO");
        updated.setTotal(BigDecimal.valueOf(9000));
        updated.setFecha_pedido(LocalDateTime.now());

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(existing);

        Pedido result = pedidoService.update(1L, updated);

        assertNotNull(result);
        assertEquals("PAGADO", result.getEstado());
        assertEquals(BigDecimal.valueOf(9000), result.getTotal());
    }

    @Test
    public void testPatch() {
        Pedido existing = createPedido();

        Pedido patch = new Pedido();
        patch.setEstado("CANCELADO");

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(existing);

        Pedido result = pedidoService.patch(1L, patch);

        assertNotNull(result);
        assertEquals("CANCELADO", result.getEstado());
    }

    @Test
    public void testDelete() {
        Pedido p = createPedido();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(p));

        pedidoService.delete(1L);

        verify(detallePedidoRepository).deleteByPedido(p);
        verify(pagoRepository).delete(p.getPago());
        verify(envioRepository).delete(p.getEnvio());
        verify(pedidoRepository).delete(p);
    }

    @Test
    public void testProcesarPedidoCarrito() {

        Usuario usuario = new Usuario();
        usuario.setId_usuario(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Producto prod = new Producto();
        prod.setId_producto(10L);
        prod.setNombre("Producto Test");
        prod.setPrecio(BigDecimal.valueOf(2000));
        prod.setStock(10);

        when(productoRepository.findById(10L)).thenReturn(Optional.of(prod));
        when(productoRepository.save(any())).thenReturn(prod);

        when(pagoRepository.save(any(Pago.class))).thenAnswer(i -> i.getArguments()[0]);
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArguments()[0]);

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> {
            Pedido pp = (Pedido) i.getArguments()[0];
            pp.setId_pedido(99L);
            return pp;
        });

        Map<String, Object> item = Map.of(
                "idProducto", 10L,
                "cantidad", 2);

        Map<String, Object> data = Map.of(
                "idUsuario", 1L,
                "direccionEnvio", "Calle 123",
                "metodoPago", "TARJETA",
                "tipoEntrega", "DOMICILIO",
                "items", List.of(item));

        Pedido pedido = pedidoService.procesarPedidoCarrito(data);

        assertNotNull(pedido);
        assertEquals(BigDecimal.valueOf(4000), pedido.getTotal());
        assertEquals(99L, pedido.getId_pedido());
        assertNotNull(pedido.getPago());
        assertNotNull(pedido.getEnvio());

        verify(productoRepository, times(2)).findById(10L);
        verify(detallePedidoRepository, times(1)).save(any(DetallePedido.class));
    }

}
