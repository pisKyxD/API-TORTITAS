package com.example.demo.service;

import com.example.demo.model.Envio;
import com.example.demo.repository.EnvioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    public Envio findById(Long id) {
        return envioRepository.findById(id).orElse(null);
    }

    public List<Envio> findAll() {
        return envioRepository.findAll();
    }

    public Envio save(Envio envio) {
        return envioRepository.save(envio);
    }

    public Envio update(Long id, Envio envio) {
        Envio envioToUpdate = envioRepository.findById(id).orElse(null);
        if (envioToUpdate != null) {
            envioToUpdate.setDireccion_envio(envio.getDireccion_envio());
            envioToUpdate.setFecha_envio(envio.getFecha_envio());
            envioToUpdate.setEstado_envio(envio.getEstado_envio());
            envioToUpdate.setDireccion(envio.getDireccion());
            return envioRepository.save(envioToUpdate);
        }
        return null;
    }

    public Envio patch(Long id, Envio envio) {
        Envio envioToPatch = envioRepository.findById(id).orElse(null);
        if (envioToPatch != null) {
            if (envio.getDireccion_envio() != null) envioToPatch.setDireccion_envio(envio.getDireccion_envio());
            if (envio.getFecha_envio() != null) envioToPatch.setFecha_envio(envio.getFecha_envio());
            if (envio.getEstado_envio() != null) envioToPatch.setEstado_envio(envio.getEstado_envio());
            if (envio.getDireccion() != null) envioToPatch.setDireccion(envio.getDireccion());
            return envioRepository.save(envioToPatch);
        }
        return null;
    }

    public void delete(Long id) {
        envioRepository.deleteById(id);
    }
}
