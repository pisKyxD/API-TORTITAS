package com.example.demo.service;

import com.example.demo.model.Imagen;
import com.example.demo.repository.ImagenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    public Imagen findById(Long id) {
        return imagenRepository.findById(id).orElse(null);
    }

    public List<Imagen> findAll() {
        return imagenRepository.findAll();
    }

    public Imagen save(Imagen imagen) {
        return imagenRepository.save(imagen);
    }

    public Imagen update(Long id, Imagen imagen) {
        Imagen imagenToUpdate = imagenRepository.findById(id).orElse(null);
        if (imagenToUpdate != null) {
            imagenToUpdate.setUrl(imagen.getUrl());
            imagenToUpdate.setProducto(imagen.getProducto());
            return imagenRepository.save(imagenToUpdate);
        }
        return null;
    }

    public Imagen patch(Long id, Imagen imagen) {
        Imagen imagenToPatch = imagenRepository.findById(id).orElse(null);
        if (imagenToPatch != null) {
            if (imagen.getUrl() != null) imagenToPatch.setUrl(imagen.getUrl());
            if (imagen.getProducto() != null) imagenToPatch.setProducto(imagen.getProducto());
            return imagenRepository.save(imagenToPatch);
        }
        return null;
    }

    public void delete(Long id) {
        imagenRepository.deleteById(id);
    }
}
