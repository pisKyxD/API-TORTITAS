package com.example.demo.service;

import com.example.demo.model.Comuna;
import com.example.demo.model.Direccion;
import com.example.demo.model.Region;
import com.example.demo.repository.ComunaRepository;
import com.example.demo.repository.DireccionRepository;
import com.example.demo.repository.EnvioRepository;
import com.example.demo.repository.RegionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    public Region findById(Long id) {
        return regionRepository.findById(id).orElse(null);
    }

    public List<Region> findAll() {
        return regionRepository.findAll();
    }

    public Region save(Region region) {
        return regionRepository.save(region);
    }

    public Region update(Long id, Region region) {
        Region regionToUpdate = regionRepository.findById(id).orElse(null);
        if (regionToUpdate != null) {
            regionToUpdate.setNombre_region(region.getNombre_region());
            return regionRepository.save(regionToUpdate);
        }
        return null;
    }

    public Region patch(Long id, Region region) {
        Region regionToPatch = regionRepository.findById(id).orElse(null);
        if (regionToPatch != null && region.getNombre_region() != null) {
            regionToPatch.setNombre_region(region.getNombre_region());
            return regionRepository.save(regionToPatch);
        }
        return null;
    }

    public void delete(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region no encontrada"));
        List<Comuna> comunas = region.getComunas();
        for (Comuna comuna : comunas) {
            List<Direccion> direcciones = comuna.getDirecciones();
            for (Direccion dir : direcciones) {
                envioRepository.deleteByDireccion(dir);
                direccionRepository.delete(dir);
            }
            comunaRepository.delete(comuna);
        }
        regionRepository.delete(region);
    }

}
