package com.example.demo.service;

import com.example.demo.model.Categoria;
import com.example.demo.model.Producto;
import com.example.demo.model.Sabor;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.DetallePedidoRepository;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.SaborRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SaborRepository saborRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public Producto findById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto update(Long id, Producto producto) {
        Producto productoToUpdate = productoRepository.findById(id).orElse(null);
        if (productoToUpdate != null) {
            productoToUpdate.setNombre(producto.getNombre());
            productoToUpdate.setDescripcion(producto.getDescripcion());
            productoToUpdate.setPrecio(producto.getPrecio());
            productoToUpdate.setStock(producto.getStock());
            productoToUpdate.setCategoria(producto.getCategoria());
            productoToUpdate.setSabor(producto.getSabor());
            return productoRepository.save(productoToUpdate);
        }
        return null;
    }

    public Producto crearProducto(
            String nombre,
            String descripcion,
            BigDecimal precio,
            int stock,
            Long categoriaId,
            Long saborId) {
        Producto producto = new Producto();

        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        Sabor sabor = saborRepository.findById(saborId)
                .orElseThrow(() -> new RuntimeException("Sabor no encontrado"));

        producto.setCategoria(categoria);
        producto.setSabor(sabor);

        return productoRepository.save(producto);
    }

    public Producto patch(Long id, Producto producto) {
        Producto productoToPatch = productoRepository.findById(id).orElse(null);
        if (productoToPatch != null) {
            if (producto.getNombre() != null)
                productoToPatch.setNombre(producto.getNombre());
            if (producto.getDescripcion() != null)
                productoToPatch.setDescripcion(producto.getDescripcion());
            if (producto.getPrecio() != null)
                productoToPatch.setPrecio(producto.getPrecio());
            if (producto.getStock() != 0)
                productoToPatch.setStock(producto.getStock());
            if (producto.getCategoria() != null)
                productoToPatch.setCategoria(producto.getCategoria());
            if (producto.getSabor() != null)
                productoToPatch.setSabor(producto.getSabor());
            return productoRepository.save(productoToPatch);
        }
        return null;
    }

    public void delete(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        imagenRepository.deleteByProducto(producto);
        detallePedidoRepository.deleteByProducto(producto);
        productoRepository.delete(producto);
    }

    public List<Producto> findByCategoria(Long idCategoria) {
        return productoRepository.findByCategoriaIdCategoria(idCategoria);
    }

    public List<Producto> findBySabor(Long idSabor) {
        return productoRepository.findBySaborIdSabor(idSabor);
    }

}
