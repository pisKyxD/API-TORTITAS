package com.example.demo.repository;

import com.example.demo.model.Direccion;
import com.example.demo.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    void deleteByDireccion(Direccion direccion);
}
