package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.model.Categoria;
import com.example.demo.model.Producto;
import com.example.demo.model.Sabor;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.DetallePedidoRepository;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.SaborRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("all")
public class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private CategoriaRepository categoriaRepository;

    @MockBean
    private SaborRepository saborRepository;

    @MockBean
    private ImagenRepository imagenRepository;

    @MockBean
    private DetallePedidoRepository detallePedidoRepository;

    private Producto producto;
    private Categoria categoria;
    private Sabor sabor;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId_producto(1L);
        producto.setNombre("Torta Chocolate");
        producto.setDescripcion("Deliciosa torta");
        producto.setPrecio(BigDecimal.valueOf(15000));
        producto.setStock(10);

        categoria = new Categoria();
        categoria.setIdCategoria(1L);
        categoria.setNombre_categoria("Tortas");

        sabor = new Sabor();
        sabor.setIdSabor(1L);
        sabor.setNombre_sabor("Chocolate");

        producto.setCategoria(categoria);
        producto.setSabor(sabor);
    }

    // -------------------------------------------------------
    // Crear entidad auxiliar
    // -------------------------------------------------------
    private Producto createEntidad() {
        Producto p = new Producto();
        p.setId_producto(2L);
        p.setNombre("Cheesecake");
        p.setDescripcion("Postre frío");
        p.setPrecio(BigDecimal.valueOf(10000));
        p.setStock(5);
        p.setCategoria(categoria);
        p.setSabor(sabor);
        return p;
    }

    // -------------------------------------------------------
    // testFindAll
    // -------------------------------------------------------
    @Test
    void testFindAll() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));
        List<Producto> lista = productoService.findAll();
        assertEquals(1, lista.size());
    }

    // -------------------------------------------------------
    // testFindById
    // -------------------------------------------------------
    @Test
    void testFindById() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        Producto result = productoService.findById(1L);
        assertNotNull(result);
        assertEquals("Torta Chocolate", result.getNombre());
    }

    // -------------------------------------------------------
    // testSave
    // -------------------------------------------------------
    @Test
    void testSave() {
        when(productoRepository.save(producto)).thenReturn(producto);
        Producto result = productoService.save(producto);
        assertNotNull(result);
        assertEquals(producto.getNombre(), result.getNombre());
    }

    // -------------------------------------------------------
    // testUpdate
    // -------------------------------------------------------
    @Test
    void testUpdate() {
        Producto updated = createEntidad();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(updated);

        Producto result = productoService.update(1L, updated);

        assertNotNull(result);
        assertEquals("Cheesecake", result.getNombre());
    }

    // -------------------------------------------------------
    // testPatchEntidad
    // -------------------------------------------------------
    @Test
    void testPatchEntidad() {
        Producto patch = new Producto();
        patch.setNombre("Torta Vainilla");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.patch(1L, patch);

        assertNotNull(result);
        assertEquals("Torta Vainilla", result.getNombre());
    }

    // -------------------------------------------------------
    // testDeleteById
    // -------------------------------------------------------
    @Test
    void testDeleteById() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        productoService.delete(1L);

        verify(imagenRepository, times(1)).deleteByProducto(producto);
        verify(detallePedidoRepository, times(1)).deleteByProducto(producto);
        verify(productoRepository, times(1)).delete(producto);
    }

    // -------------------------------------------------------
    // testFindByCategoria
    // -------------------------------------------------------
    @Test
    void testFindByCategoria() {
        when(productoRepository.findByCategoriaIdCategoria(1L))
                .thenReturn(Arrays.asList(producto));

        List<Producto> lista = productoService.findByCategoria(1L);

        assertEquals(1, lista.size());
        assertEquals("Torta Chocolate", lista.get(0).getNombre());
    }

    // -------------------------------------------------------
    // testFindBySabor
    // -------------------------------------------------------
    @Test
    void testFindBySabor() {
        when(productoRepository.findBySaborIdSabor(1L))
                .thenReturn(Arrays.asList(producto));

        List<Producto> lista = productoService.findBySabor(1L);

        assertEquals(1, lista.size());
        assertEquals("Chocolate", lista.get(0).getSabor().getNombre_sabor());
    }
}
