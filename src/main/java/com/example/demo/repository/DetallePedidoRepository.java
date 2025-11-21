package com.example.demo.repository;

import com.example.demo.model.DetallePedido;
import com.example.demo.model.Pedido;
import com.example.demo.model.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    void deleteByPedido(Pedido pedido);
    void deleteByProducto(Producto producto);
}
