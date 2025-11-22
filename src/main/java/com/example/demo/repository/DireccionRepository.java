package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Direccion;
import com.example.demo.model.Usuario;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    void deleteByUsuario(Usuario usuario);
    List<Direccion> findByUsuario(Usuario usuario);
}