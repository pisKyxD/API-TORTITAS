package com.example.demo.repository;

import com.example.demo.model.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria_Id_categoria(Long id_categoria);
    List<Producto> findBySabor_Id_sabor(Long id_sabor);
    List<Producto> findByCategoria_Id_categoriaAndSabor_Id_sabor(Long id_categoria, Long id_sabor);
}
