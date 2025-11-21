package com.example.demo.repository;

import com.example.demo.model.Direccion;
import com.example.demo.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    void deleteByUsuario(Usuario usuario);
}
