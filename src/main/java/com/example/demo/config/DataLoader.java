package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Categoria;
import com.example.demo.model.Comuna;
import com.example.demo.model.DetallePedido;
import com.example.demo.model.Direccion;
import com.example.demo.model.Envio;
import com.example.demo.model.Imagen;
import com.example.demo.model.Pago;
import com.example.demo.model.Pedido;
import com.example.demo.model.Producto;
import com.example.demo.model.Region;
import com.example.demo.model.Rol;
import com.example.demo.model.Sabor;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ComunaRepository;
import com.example.demo.repository.DetallePedidoRepository;
import com.example.demo.repository.DireccionRepository;
import com.example.demo.repository.EnvioRepository;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.PagoRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.RolRepository;
import com.example.demo.repository.SaborRepository;
import com.example.demo.repository.UsuarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private RegionRepository regionRepository;
    @Autowired private ComunaRepository comunaRepository;
    @Autowired private DireccionRepository direccionRepository;

    @Autowired private RolRepository rolRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private SaborRepository saborRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ImagenRepository imagenRepository;

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private DetallePedidoRepository detallePedidoRepository;
    @Autowired private PagoRepository pagoRepository;
    @Autowired private EnvioRepository envioRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    private Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        String[] regionesChile = {
                "Región Metropolitana",
                "Valparaíso",
                "Biobío",
                "O'Higgins"
        };

        for (String r : regionesChile) {
            Region region = new Region();
            region.setNombre_region(r);
            regionRepository.save(region);
        }

        List<Region> regiones = regionRepository.findAll();

        Map<String, List<String>> comunasPorRegion = Map.of(
                "Región Metropolitana", List.of("Santiago", "Maipú", "Puente Alto"),
                "Valparaíso", List.of("Viña del Mar", "Valparaíso", "Quilpué"),
                "Biobío", List.of("Concepción", "Talcahuano", "Chiguayante"),
                "O'Higgins", List.of("Rancagua", "Machalí", "Graneros")
        );

        for (Region r : regiones) {
            for (String comunaNombre : comunasPorRegion.get(r.getNombre_region())) {
                Comuna c = new Comuna();
                c.setNombre_comuna(comunaNombre);
                c.setRegion(r);
                comunaRepository.save(c);
            }
        }

        List<Comuna> comunas = comunaRepository.findAll();

        if (rolRepository.count() == 0) {
            rolRepository.save(new Rol(null, "ADMIN", null));
            rolRepository.save(new Rol(null, "USUARIO", null));
        }

        List<Rol> roles = rolRepository.findAll();

        for (int i = 1; i <= 6; i++) {

            Direccion dir = new Direccion();
            dir.setCalle("Calle " + (100 + i));
            dir.setNumero(String.valueOf(50 + i));
            dir.setReferencia("Casa " + i);
            dir.setComuna(comunas.get(random.nextInt(comunas.size())));
            direccionRepository.save(dir);

            Usuario u = new Usuario();
            u.setNombre("Usuario" + i);
            u.setApellido("Apellido" + i);
            u.setEmail("user" + i + "@gmail.com");
            u.setPassword(passwordEncoder.encode("123456"));
            u.setTelefono("9" + (10000000 + i));
            u.setRol(roles.get(1));
            u.setDireccionPrincipal(dir);

            usuarioRepository.save(u);
        }

        List<Usuario> usuarios = usuarioRepository.findAll();

        String[] categoriasInit = {
                "Tortas",
                "Queques",
                "Cupcakes",
                "Postres",
                "Cheesecakes"
        };

        for (String c : categoriasInit) {
            Categoria cat = new Categoria();
            cat.setNombre_categoria(c);
            cat.setDescripcion("Productos de " + c);
            categoriaRepository.save(cat);
        }

        List<Categoria> categorias = categoriaRepository.findAll();

        String[] saboresInit = {
                "Chocolate",
                "Vainilla",
                "Frutilla",
                "Manjar",
                "Tres Leches",
                "Limón",
                "Red Velvet"
        };

        for (String s : saboresInit) {
            Sabor sabor = new Sabor();
            sabor.setNombre_sabor(s);
            saborRepository.save(sabor);
        }

        List<Sabor> sabores = saborRepository.findAll();

        String[] nombresProductos = {
                "Torta Selva Negra",
                "Torta Tres Leches",
                "Torta Frutilla-Manjar",
                "Queque Marmoleado",
                "Cupcakes de Vainilla",
                "Cheesecake de Frambuesa",
                "Brownie Premium",
                "Torta de Chocolate Intenso"
        };

        for (String nombre : nombresProductos) {
            Producto p = new Producto();
            p.setNombre(nombre);
            p.setDescripcion("Delicioso " + nombre + " artesanal.");
            p.setPrecio(BigDecimal.valueOf(8000 + random.nextInt(12000)));
            p.setStock(10 + random.nextInt(40));
            p.setCategoria(categorias.get(random.nextInt(categorias.size())));
            p.setSabor(sabores.get(random.nextInt(sabores.size())));
            productoRepository.save(p);
        }

        List<Producto> productos = productoRepository.findAll();

        for (Producto p : productos) {
            for (int i = 1; i <= 2; i++) {
                Imagen img = new Imagen();
                img.setUrl("https://picsum.photos/seed/" + p.getId_producto() + "-" + i + "/500/500");
                img.setProducto(p);
                imagenRepository.save(img);
            }
        }

        for (int i = 1; i <= 4; i++) {

            Usuario user = usuarios.get(random.nextInt(usuarios.size()));

            Pago pago = new Pago(null,
                    BigDecimal.valueOf(10000 + random.nextInt(20000)),
                    "TARJETA",
                    "COMPLETADO",
                    LocalDateTime.now(),
                    null
            );
            pagoRepository.save(pago);

            Envio envio = new Envio(null,
                    user.getDireccionPrincipal().getCalle() + " " + user.getDireccionPrincipal().getNumero(),
                    LocalDateTime.now(),
                    "EN CAMINO",
                    user.getDireccionPrincipal(),
                    null
            );
            envioRepository.save(envio);

            Pedido pedido = new Pedido();
            pedido.setEstado("PAGADO");
            pedido.setFecha_pedido(LocalDateTime.now());
            pedido.setTotal(pago.getMonto());
            pedido.setUsuario(user);
            pedido.setPago(pago);
            pedido.setEnvio(envio);
            pedidoRepository.save(pedido);

            for (int j = 1; j <= 2; j++) {
                Producto prod = productos.get(random.nextInt(productos.size()));
                DetallePedido det = new DetallePedido();
                det.setPedido(pedido);
                det.setProducto(prod);
                det.setCantidad(1 + random.nextInt(4));
                det.setPrecio_unitario(prod.getPrecio());
                detallePedidoRepository.save(det);
            }
        }

        System.out.println("DataLoader completado correctamente");
    }
}