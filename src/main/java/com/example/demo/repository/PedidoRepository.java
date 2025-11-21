package com.example.demo.repository;

import com.example.demo.model.Pedido;
import com.example.demo.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario usuario);
    void deleteByUsuario(Usuario usuario);
}
