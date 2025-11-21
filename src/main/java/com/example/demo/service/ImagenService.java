package com.example.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.model.Imagen;
import com.example.demo.repository.ImagenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private Cloudinary cloudinary;

    public Imagen findById(Long id) {
        return imagenRepository.findById(id).orElse(null);
    }

    public List<Imagen> findAll() {
        return imagenRepository.findAll();
    }

    public Imagen save(Imagen imagen) {
        return imagenRepository.save(imagen);
    }

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto")
        );
        return uploadResult.get("secure_url").toString();
    }

    public Imagen guardarImagen(MultipartFile file, Long productoId) {

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto")
            );

            String imageUrl = uploadResult.get("secure_url").toString();

            Imagen imagen = new Imagen();
            imagen.setUrl(imageUrl);

            com.example.demo.model.Producto p = new com.example.demo.model.Producto();
            p.setId_producto(productoId);
            imagen.setProducto(p);

            return imagenRepository.save(imagen);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
