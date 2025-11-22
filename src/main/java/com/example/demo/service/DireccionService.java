package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Direccion;
import com.example.demo.repository.DireccionRepository;
import com.example.demo.repository.EnvioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DireccionService {

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private EnvioRepository envioRepository;

    public Direccion findById(Long id) {
        return direccionRepository.findById(id).orElse(null);
    }

    public List<Direccion> findAll() {
        return direccionRepository.findAll();
    }

    public List<Direccion> findByUsuarioId(Long idUsuario) {
        return direccionRepository.findByUsuarioId(idUsuario);
    }

    public Direccion save(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    public Direccion update(Long id, Direccion direccion) {
        Direccion direccionToUpdate = direccionRepository.findById(id).orElse(null);
        if (direccionToUpdate != null) {
            direccionToUpdate.setCalle(direccion.getCalle());
            direccionToUpdate.setNumero(direccion.getNumero());
            direccionToUpdate.setReferencia(direccion.getReferencia());
            direccionToUpdate.setComuna(direccion.getComuna());
            direccionToUpdate.setUsuario(direccion.getUsuario());
            return direccionRepository.save(direccionToUpdate);
        }
        return null;
    }

    public Direccion patch(Long id, Direccion direccion) {
        Direccion direccionToPatch = direccionRepository.findById(id).orElse(null);
        if (direccionToPatch != null) {
            if (direccion.getCalle() != null)
                direccionToPatch.setCalle(direccion.getCalle());
            if (direccion.getNumero() != null)
                direccionToPatch.setNumero(direccion.getNumero());
            if (direccion.getReferencia() != null)
                direccionToPatch.setReferencia(direccion.getReferencia());
            if (direccion.getComuna() != null)
                direccionToPatch.setComuna(direccion.getComuna());
            if (direccion.getUsuario() != null)
                direccionToPatch.setUsuario(direccion.getUsuario());
            return direccionRepository.save(direccionToPatch);
        }
        return null;
    }

    public void delete(Long id) {
        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Direccion no encontrada"));
        envioRepository.deleteByDireccion(direccion);
        direccionRepository.delete(direccion);
    }

}