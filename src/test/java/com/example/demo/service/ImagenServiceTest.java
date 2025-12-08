package com.example.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.demo.model.Imagen;
import com.example.demo.model.Producto;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.ProductoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("all")
class ImagenServiceTest {

    @Autowired
    private ImagenService imagenService;

    @MockBean
    private ImagenRepository imagenRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private Cloudinary cloudinary;

    private Imagen createImagen() {
        Imagen img = new Imagen();
        img.setId_imagen(1L);
        img.setUrl("http://test.com/img1.webp");
        img.setProducto(new Producto());
        return img;
    }

    private Producto createProducto() {
        Producto p = new Producto();
        p.setId_producto(10L);
        return p;
    }

    @Test
    void testFindAll() {
        when(imagenRepository.findAll()).thenReturn(List.of(createImagen()));

        List<Imagen> result = imagenService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        when(imagenRepository.findById(1L)).thenReturn(Optional.of(createImagen()));

        Imagen result = imagenService.findById(1L);

        assertNotNull(result);
        assertEquals("http://test.com/img1.webp", result.getUrl());
    }

    @Test
    void testSave() {
        Imagen img = createImagen();
        when(imagenRepository.save(img)).thenReturn(img);

        Imagen result = imagenService.save(img);

        assertNotNull(result);
        assertEquals(img.getUrl(), result.getUrl());
    }

    @Test
    void testUpdate() {
        Imagen existente = createImagen();
        Imagen nuevo = new Imagen();
        nuevo.setUrl("http://new.com/updated.webp");
        nuevo.setProducto(createProducto());

        when(imagenRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(imagenRepository.save(existente)).thenReturn(existente);

        Imagen result = imagenService.update(1L, nuevo);

        assertNotNull(result);
        assertEquals("http://new.com/updated.webp", result.getUrl());
    }

    @Test
    void testPatch() {
        Imagen existente = createImagen();
        Imagen patch = new Imagen();
        patch.setUrl("http://patch.com/img.webp");

        when(imagenRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(imagenRepository.save(existente)).thenReturn(existente);

        Imagen result = imagenService.patch(1L, patch);

        assertNotNull(result);
        assertEquals("http://patch.com/img.webp", result.getUrl());
    }

    @Test
    void testDelete() {
        imagenService.delete(1L);
        verify(imagenRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGuardarImagen() throws Exception {

        Producto producto = createProducto();
        MockMultipartFile archivo =
                new MockMultipartFile("file", "test.png", "image/png", "fakeimage".getBytes());

        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));

        when(imagenRepository.findByProducto(producto)).thenReturn(new ArrayList<>());

        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);

        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "http://cloudinary.com/newimg.webp");

        when(uploader.upload(any(), any())).thenReturn(uploadResult);

        Imagen imgGuardada = new Imagen();
        imgGuardada.setUrl("http://cloudinary.com/newimg.webp");
        imgGuardada.setProducto(producto);

        when(imagenRepository.save(any(Imagen.class))).thenReturn(imgGuardada);

        Imagen result = imagenService.guardarImagen(archivo, 10L);

        assertNotNull(result);
        assertEquals("http://cloudinary.com/newimg.webp", result.getUrl());
        verify(imagenRepository, times(1)).save(any(Imagen.class));
    }
}
