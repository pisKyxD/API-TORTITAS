package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Categoria;
import com.example.demo.model.Producto;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.DetallePedidoRepository;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.ProductoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("all")
public class CategoriaServiceTest {

    @Autowired
    private CategoriaService categoriaService;

    @MockBean
    private CategoriaRepository categoriaRepository;

    @MockBean
    private ImagenRepository imagenRepository;

    @MockBean
    private DetallePedidoRepository detallePedidoRepository;

    @MockBean
    private ProductoRepository productoRepository;

    private Categoria createCategoria() {
        Categoria c = new Categoria();
        c.setIdCategoria(1L);
        c.setNombre_categoria("Bebidas");
        c.setDescripcion("Líquidos y jugos");
        return c;
    }

    @Test
    public void testFindAll() {
        when(categoriaRepository.findAll()).thenReturn(List.of(createCategoria()));

        List<Categoria> categorias = categoriaService.findAll();

        assertNotNull(categorias);
        assertEquals(1, categorias.size());
    }

    @Test
    public void testFindById() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(createCategoria()));

        Categoria categoria = categoriaService.findById(1L);

        assertNotNull(categoria);
        assertEquals("Bebidas", categoria.getNombre_categoria());
    }

    @Test
    public void testSave() {
        Categoria categoria = createCategoria();
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria saved = categoriaService.save(categoria);

        assertNotNull(saved);
        assertEquals("Bebidas", saved.getNombre_categoria());
    }

    @Test
    public void testUpdate() {
        Categoria existing = createCategoria();

        Categoria updated = new Categoria();
        updated.setNombre_categoria("Snacks");
        updated.setDescripcion("Aperitivos");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(existing);

        Categoria result = categoriaService.update(1L, updated);

        assertNotNull(result);
        assertEquals("Snacks", result.getNombre_categoria());
    }

    @Test
    public void testPatch() {
        Categoria existing = createCategoria();

        Categoria patchData = new Categoria();
        patchData.setDescripcion("Nueva descripción");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(existing);

        Categoria result = categoriaService.patch(1L, patchData);

        assertNotNull(result);
        assertEquals("Nueva descripción", result.getDescripcion());
    }

    @Test
    public void testDelete() {
        Categoria categoria = createCategoria();

        Producto p1 = new Producto();
        p1.setId_producto(10L);

        categoria.setProductos(List.of(p1));

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        doNothing().when(imagenRepository).deleteByProducto(p1);
        doNothing().when(detallePedidoRepository).deleteByProducto(p1);
        doNothing().when(productoRepository).delete(p1);
        doNothing().when(categoriaRepository).delete(categoria);

        categoriaService.delete(1L);

        verify(imagenRepository, times(1)).deleteByProducto(p1);
        verify(detallePedidoRepository, times(1)).deleteByProducto(p1);
        verify(productoRepository, times(1)).delete(p1);
        verify(categoriaRepository, times(1)).delete(categoria);
    }

}
