package com.example.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.model.Imagen;
import com.example.demo.model.Producto;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.ProductoRepository;
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
    private ProductoRepository productoRepository;

    @Autowired
    private Cloudinary cloudinary;

    public List<Imagen> findAll() {
        return imagenRepository.findAll();
    }

    public Imagen findById(Long id) {
        return imagenRepository.findById(id).orElse(null);
    }

    public Imagen save(Imagen imagen) {
        return imagenRepository.save(imagen);
    }

    public Imagen update(Long id, Imagen imagen) {
        Imagen actual = imagenRepository.findById(id).orElse(null);

        if (actual == null)
            return null;

        actual.setUrl(imagen.getUrl());
        actual.setProducto(imagen.getProducto());

        return imagenRepository.save(actual);
    }

    public Imagen patch(Long id, Imagen imagen) {
        Imagen actual = imagenRepository.findById(id).orElse(null);

        if (actual == null)
            return null;

        if (imagen.getUrl() != null)
            actual.setUrl(imagen.getUrl());
        if (imagen.getProducto() != null)
            actual.setProducto(imagen.getProducto());

        return imagenRepository.save(actual);
    }

    public void delete(Long id) {
        imagenRepository.deleteById(id);
    }

    public Imagen guardarImagen(MultipartFile file, Long productoId) {
        try {
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            List<Imagen> actuales = imagenRepository.findByProducto(producto);

            if (!actuales.isEmpty()) {
                imagenRepository.deleteAll(actuales);
            }

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "image",
                            "format", "webp"));

            String imageUrl = uploadResult.get("secure_url").toString();

            Imagen imagen = new Imagen();
            imagen.setUrl(imageUrl);
            imagen.setProducto(producto);

            return imagenRepository.save(imagen);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
