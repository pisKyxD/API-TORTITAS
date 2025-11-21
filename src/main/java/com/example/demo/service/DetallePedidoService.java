package com.example.demo.service;

import com.example.demo.model.DetallePedido;
import com.example.demo.repository.DetallePedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public DetallePedido findById(Long id) {
        return detallePedidoRepository.findById(id).orElse(null);
    }

    public List<DetallePedido> findAll() {
        return detallePedidoRepository.findAll();
    }

    public DetallePedido save(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }

    public DetallePedido update(Long id, DetallePedido detalle) {
        DetallePedido detalleToUpdate = detallePedidoRepository.findById(id).orElse(null);
        if (detalleToUpdate != null) {
            detalleToUpdate.setPedido(detalle.getPedido());
            detalleToUpdate.setProducto(detalle.getProducto());
            detalleToUpdate.setCantidad(detalle.getCantidad());
            detalleToUpdate.setPrecio_unitario(detalle.getPrecio_unitario());
            return detallePedidoRepository.save(detalleToUpdate);
        }
        return null;
    }

    public DetallePedido patch(Long id, DetallePedido detalle) {
        DetallePedido detalleToPatch = detallePedidoRepository.findById(id).orElse(null);
        if (detalleToPatch != null) {
            if (detalle.getPedido() != null) detalleToPatch.setPedido(detalle.getPedido());
            if (detalle.getProducto() != null) detalleToPatch.setProducto(detalle.getProducto());
            if (detalle.getCantidad() != 0) detalleToPatch.setCantidad(detalle.getCantidad());
            if (detalle.getPrecio_unitario() != null) detalleToPatch.setPrecio_unitario(detalle.getPrecio_unitario());
            return detallePedidoRepository.save(detalleToPatch);
        }
        return null;
    }

    public void delete(Long id) {
        detallePedidoRepository.deleteById(id);
    }
}
