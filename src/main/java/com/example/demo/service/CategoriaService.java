package com.example.demo.service;

import com.example.demo.model.Categoria;
import com.example.demo.model.Producto;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.DetallePedidoRepository;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria update(Long id, Categoria categoria) {
        Categoria categoriaToUpdate = categoriaRepository.findById(id).orElse(null);
        if (categoriaToUpdate != null) {
            categoriaToUpdate.setNombre_categoria(categoria.getNombre_categoria());
            categoriaToUpdate.setDescripcion(categoria.getDescripcion());
            return categoriaRepository.save(categoriaToUpdate);
        }
        return null;
    }

    public Categoria patch(Long id, Categoria categoria) {
        Categoria categoriaToPatch = categoriaRepository.findById(id).orElse(null);
        if (categoriaToPatch != null) {
            if (categoria.getNombre_categoria() != null)
                categoriaToPatch.setNombre_categoria(categoria.getNombre_categoria());
            if (categoria.getDescripcion() != null)
                categoriaToPatch.setDescripcion(categoria.getDescripcion());
            return categoriaRepository.save(categoriaToPatch);
        }
        return null;
    }

    public void delete(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        List<Producto> productos = categoria.getProductos();
        for (Producto producto : productos) {
            imagenRepository.deleteByProducto(producto);
            detallePedidoRepository.deleteByProducto(producto);
            productoRepository.delete(producto);
        }
        categoriaRepository.delete(categoria);
    }

}
