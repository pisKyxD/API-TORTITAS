package com.example.demo.service;

import com.example.demo.model.Comuna;
import com.example.demo.model.Direccion;
import com.example.demo.repository.ComunaRepository;
import com.example.demo.repository.DireccionRepository;
import com.example.demo.repository.EnvioRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    public Comuna findById(Long id) {
        return comunaRepository.findById(id).orElse(null);
    }

    public List<Comuna> findAll() {
        return comunaRepository.findAll();
    }

    public Comuna save(Comuna comuna) {
        return comunaRepository.save(comuna);
    }

    public Comuna update(Long id, Comuna comuna) {
        Comuna comunaToUpdate = comunaRepository.findById(id).orElse(null);
        if (comunaToUpdate != null) {
            comunaToUpdate.setNombre_comuna(comuna.getNombre_comuna());
            comunaToUpdate.setRegion(comuna.getRegion());
            return comunaRepository.save(comunaToUpdate);
        }
        return null;
    }

    public Comuna patch(Long id, Comuna comuna) {
        Comuna comunaToPatch = comunaRepository.findById(id).orElse(null);
        if (comunaToPatch != null) {
            if (comuna.getNombre_comuna() != null)
                comunaToPatch.setNombre_comuna(comuna.getNombre_comuna());
            if (comuna.getRegion() != null)
                comunaToPatch.setRegion(comuna.getRegion());
            return comunaRepository.save(comunaToPatch);
        }
        return null;
    }

    public void delete(Long id) {
        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada"));
        List<Direccion> direcciones = comuna.getDirecciones();
        for (Direccion dir : direcciones) {
            envioRepository.deleteByDireccion(dir);
            direccionRepository.delete(dir);
        }
        comunaRepository.delete(comuna);
    }

}
