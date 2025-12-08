package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.model.Producto;
import com.example.demo.model.Sabor;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("all")
class SaborServiceTest {

    @Autowired
    private SaborService saborService;

    @MockBean
    private SaborRepository saborRepository;

    @MockBean
    private ImagenRepository imagenRepository;

    @MockBean
    private DetallePedidoRepository detallePedidoRepository;

    @MockBean
    private ProductoRepository productoRepository;

    private Sabor sabor;

    @BeforeEach
    void setUp() {
        sabor = createEntidad();
    }

    private Sabor createEntidad() {
        Sabor s = new Sabor();
        s.setIdSabor(1L);
        s.setNombre_sabor("Vainilla");
        s.setProductos(new ArrayList<>());
        return s;
    }

    @Test
    void testFindAll() {
        List<Sabor> lista = List.of(sabor);

        when(saborRepository.findAll()).thenReturn(lista);

        List<Sabor> resultado = saborService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Vainilla", resultado.get(0).getNombre_sabor());
    }

    @Test
    void testFindById() {
        when(saborRepository.findById(1L)).thenReturn(Optional.of(sabor));

        Sabor resultado = saborService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Vainilla", resultado.getNombre_sabor());
    }

    @Test
    void testSave() {
        when(saborRepository.save(sabor)).thenReturn(sabor);

        Sabor resultado = saborService.save(sabor);

        assertNotNull(resultado);
        assertEquals("Vainilla", resultado.getNombre_sabor());
    }

    @Test
    void testPatchEntidad() {
        Sabor patch = new Sabor();
        patch.setNombre_sabor("Chocolate");

        when(saborRepository.findById(1L)).thenReturn(Optional.of(sabor));
        when(saborRepository.save(any(Sabor.class))).thenReturn(sabor);

        Sabor resultado = saborService.patch(1L, patch);

        assertNotNull(resultado);
        assertEquals("Chocolate", resultado.getNombre_sabor());
    }

    @Test
    void testDeleteById() {
        Producto p1 = new Producto();
        p1.setId_producto(10L);

        List<Producto> productos = List.of(p1);
        sabor.setProductos(productos);

        when(saborRepository.findById(1L)).thenReturn(Optional.of(sabor));

        saborService.delete(1L);

        verify(imagenRepository, times(1)).deleteByProducto(p1);
        verify(detallePedidoRepository, times(1)).deleteByProducto(p1);
        verify(productoRepository, times(1)).delete(p1);
        verify(saborRepository, times(1)).delete(sabor);
    }
}
