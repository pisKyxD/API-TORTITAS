package com.example.demo.repository;

import com.example.demo.model.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaIdCategoria(Long idCategoria);
    List<Producto> findBySaborIdSabor(Long idSabor);
    List<Producto> findByCategoriaIdCategoriaAndSaborIdSabor(Long idCategoria, Long idSabor);
}
