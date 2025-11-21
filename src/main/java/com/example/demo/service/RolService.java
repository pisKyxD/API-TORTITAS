package com.example.demo.service;

import com.example.demo.model.Rol;
import com.example.demo.repository.RolRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public Rol findById(Long id) {
        return rolRepository.findById(id).orElse(null);
    }

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    public Rol update(Long id, Rol rol) {
        Rol rolToUpdate = rolRepository.findById(id).orElse(null);
        if (rolToUpdate != null) {
            rolToUpdate.setNombre(rol.getNombre());
            return rolRepository.save(rolToUpdate);
        }
        return null;
    }

    public Rol patch(Long id, Rol rol) {
        Rol rolToPatch = rolRepository.findById(id).orElse(null);
        if (rolToPatch != null) {
            if (rol.getNombre() != null) {
                rolToPatch.setNombre(rol.getNombre());
            }
            return rolRepository.save(rolToPatch);
        }
        return null;
    }

    public void delete(Long id) {
        rolRepository.deleteById(id);
    }
}
