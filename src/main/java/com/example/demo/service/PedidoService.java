package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Pedido update(Long id, Pedido pedido) {
        Pedido pedidoToUpdate = pedidoRepository.findById(id).orElse(null);
        if (pedidoToUpdate != null) {
            pedidoToUpdate.setFecha_pedido(pedido.getFecha_pedido());
            pedidoToUpdate.setEstado(pedido.getEstado());
            pedidoToUpdate.setTotal(pedido.getTotal());
            pedidoToUpdate.setUsuario(pedido.getUsuario());
            pedidoToUpdate.setPago(pedido.getPago());
            pedidoToUpdate.setEnvio(pedido.getEnvio());
            return pedidoRepository.save(pedidoToUpdate);
        }
        return null;
    }

    public Pedido patch(Long id, Pedido pedido) {
        Pedido pedidoToPatch = pedidoRepository.findById(id).orElse(null);
        if (pedidoToPatch != null) {
            if (pedido.getFecha_pedido() != null)
                pedidoToPatch.setFecha_pedido(pedido.getFecha_pedido());
            if (pedido.getEstado() != null)
                pedidoToPatch.setEstado(pedido.getEstado());
            if (pedido.getTotal() != null)
                pedidoToPatch.setTotal(pedido.getTotal());
            if (pedido.getUsuario() != null)
                pedidoToPatch.setUsuario(pedido.getUsuario());
            if (pedido.getPago() != null)
                pedidoToPatch.setPago(pedido.getPago());
            if (pedido.getEnvio() != null)
                pedidoToPatch.setEnvio(pedido.getEnvio());
            return pedidoRepository.save(pedidoToPatch);
        }
        return null;
    }

    public void delete(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        detallePedidoRepository.deleteByPedido(pedido);
        if (pedido.getPago() != null)
            pagoRepository.delete(pedido.getPago());
        if (pedido.getEnvio() != null)
            envioRepository.delete(pedido.getEnvio());
        pedidoRepository.delete(pedido);
    }

    public Pedido procesarPedidoCarrito(Map<String, Object> data) {

    Long idUsuario = Long.valueOf(data.get("idUsuario").toString());
    Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    String direccionEnvio = data.get("direccionEnvio").toString();
    String metodoPago = data.get("metodoPago").toString();
    String tipoEntrega = data.get("tipoEntrega").toString();

    List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
    if (items == null || items.isEmpty()) {
        throw new RuntimeException("El pedido no contiene productos.");
    }

    Pago pago = new Pago();
    pago.setMetodo_pago(metodoPago);
    pago.setEstado_pago("PENDIENTE");
    pago.setMonto(BigDecimal.ZERO);
    pago.setFecha_pago(LocalDateTime.now());
    pagoRepository.save(pago);

    Envio envio = null;

    if (tipoEntrega.equalsIgnoreCase("DOMICILIO")) {
        envio = new Envio();
        envio.setDireccion_envio(direccionEnvio);
        envio.setEstado_envio("PENDIENTE");
        envio.setFecha_envio(LocalDateTime.now());
        envioRepository.save(envio);
    }

    BigDecimal total = BigDecimal.ZERO;

    Pedido pedido = new Pedido();
    pedido.setFecha_pedido(LocalDateTime.now());
    pedido.setEstado("PENDIENTE");
    pedido.setUsuario(usuario);
    pedido.setPago(pago);
    pedido.setEnvio(envio);

    for (Map<String, Object> item : items) {

        Long idProducto = Long.valueOf(item.get("idProducto").toString());
        int cantidad = Integer.parseInt(item.get("cantidad").toString());

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("No hay stock suficiente para: " + producto.getNombre());
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

        BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
        total = total.add(subtotal);
    }

    pago.setMonto(total);
    pagoRepository.save(pago);

    pedido.setTotal(total);
    pedido = pedidoRepository.save(pedido);

    for (Map<String, Object> item : items) {

        Long idProducto = Long.valueOf(item.get("idProducto").toString());
        int cantidad = Integer.parseInt(item.get("cantidad").toString());

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecio_unitario(producto.getPrecio());

        detallePedidoRepository.save(detalle);
    }

    return pedido;
}
}