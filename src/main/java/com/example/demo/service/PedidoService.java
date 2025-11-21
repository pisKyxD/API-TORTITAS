package com.example.demo.service;

import com.example.demo.model.Pedido;
import com.example.demo.repository.DetallePedidoRepository;
import com.example.demo.repository.EnvioRepository;
import com.example.demo.repository.PagoRepository;
import com.example.demo.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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

}
