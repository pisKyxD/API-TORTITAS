package com.example.demo.repository;

import com.example.demo.model.Imagen;
import com.example.demo.model.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    void deleteByProducto(Producto producto);
}
