package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import com.example.demo.model.*;
import com.example.demo.repository.*;

import net.datafaker.Faker;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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

    private Faker faker = new Faker();
    private Random random = new Random();

    @Override
    public void run(String... args) throws Exception {

        // =============================
        // REGIONES
        // =============================
        for (int i = 1; i <= 3; i++) {
            Region r = new Region();
            r.setNombre_region(faker.address().state());
            regionRepository.save(r);
        }
        List<Region> regiones = regionRepository.findAll();

        // =============================
        // COMUNAS
        // =============================
        for (int i = 1; i <= 5; i++) {
            Comuna c = new Comuna();
            c.setNombre_comuna(faker.address().city());
            c.setRegion(regiones.get(random.nextInt(regiones.size())));
            comunaRepository.save(c);
        }
        List<Comuna> comunas = comunaRepository.findAll();

        // =============================
        // ROLES
        // =============================
        String[] rolesInit = { "ADMIN", "USUARIO", "TRABAJADOR" };
        for (String rol : rolesInit) {
            Rol r = new Rol();
            r.setNombre(rol);
            rolRepository.save(r);
        }
        List<Rol> roles = rolRepository.findAll();

        // =============================
        // USUARIOS + DIRECCIONES
        // =============================
        for (int i = 1; i <= 5; i++) {

            // Dirección principal
            Direccion dir = new Direccion();
            dir.setCalle(faker.address().streetName());
            dir.setNumero(String.valueOf(faker.number().numberBetween(100, 999)));
            dir.setReferencia(faker.address().secondaryAddress());
            dir.setComuna(comunas.get(random.nextInt(comunas.size())));
            direccionRepository.save(dir);

            // Usuario
            Usuario u = new Usuario();
            u.setNombre(faker.name().firstName());
            u.setApellido(faker.name().lastName());
            u.setEmail(faker.internet().emailAddress());
            u.setContrasenia("123456");
            u.setTelefono("9" + faker.number().numberBetween(10000000, 99999999));
            u.setRol(roles.get(random.nextInt(roles.size())));
            u.setDireccionPrincipal(dir);
            usuarioRepository.save(u);
        }
        List<Usuario> usuarios = usuarioRepository.findAll();

        // =============================
        // CATEGORÍAS
        // =============================
        for (int i = 1; i <= 3; i++) {
            Categoria c = new Categoria();
            c.setNombre_categoria(faker.commerce().department());
            c.setDescripcion(faker.commerce().material());
            categoriaRepository.save(c);
        }
        List<Categoria> categorias = categoriaRepository.findAll();

        // =============================
        // SABORES
        // =============================
        for (int i = 1; i <= 3; i++) {
            Sabor s = new Sabor();
            s.setNombre_sabor(faker.food().ingredient());
            saborRepository.save(s);
        }
        List<Sabor> sabores = saborRepository.findAll();

        // =============================
        // PRODUCTOS
        // =============================
        for (int i = 1; i <= 6; i++) {
            Producto p = new Producto();
            p.setNombre(faker.commerce().productName());
            p.setDescripcion(faker.commerce().material());
            p.setPrecio(BigDecimal.valueOf(faker.number().numberBetween(1000, 20000)));
            p.setStock(faker.number().numberBetween(5, 50));
            p.setCategoria(categorias.get(random.nextInt(categorias.size())));
            p.setSabor(sabores.get(random.nextInt(sabores.size())));
            productoRepository.save(p);
        }
        List<Producto> productos = productoRepository.findAll();

        // =============================
        // IMÁGENES
        // =============================
        for (Producto p : productos) {
            for (int i = 1; i <= 2; i++) {
                Imagen img = new Imagen();
                img.setUrl("https://picsum.photos/seed/" + faker.number().randomNumber() + "/400/400");
                img.setProducto(p);
                imagenRepository.save(img);
            }
        }

        // =============================
        // PEDIDOS + PAGO + ENVÍO + DETALLE
        // =============================
        for (int i = 1; i <= 5; i++) {

            Usuario user = usuarios.get(random.nextInt(usuarios.size()));

            // PAGO
            Pago pago = new Pago();
            pago.setMetodo_pago("TARJETA");
            pago.setEstado_pago("COMPLETADO");
            pago.setMonto(BigDecimal.valueOf(faker.number().numberBetween(5000, 30000)));
            pago.setFecha_pago(LocalDateTime.now());
            pagoRepository.save(pago);

            // ENVIO
            Envio envio = new Envio();
            envio.setDireccion_envio(user.getDireccionPrincipal().getCalle() + " " + user.getDireccionPrincipal().getNumero());
            envio.setEstado_envio("EN CAMINO");
            envio.setFecha_envio(LocalDateTime.now());
            envio.setDireccion(user.getDireccionPrincipal());
            envioRepository.save(envio);

            // PEDIDO
            Pedido pedido = new Pedido();
            pedido.setEstado("PAGADO");
            pedido.setFecha_pedido(LocalDateTime.now());
            pedido.setTotal(pago.getMonto());
            pedido.setUsuario(user);
            pedido.setPago(pago);
            pedido.setEnvio(envio);
            pedidoRepository.save(pedido);

            // DETALLE
            for (int j = 1; j <= 2; j++) {
                DetallePedido det = new DetallePedido();
                det.setPedido(pedido);

                Producto prod = productos.get(random.nextInt(productos.size()));

                det.setProducto(prod);
                det.setCantidad(faker.number().numberBetween(1, 5));
                det.setPrecio_unitario(prod.getPrecio());
                detallePedidoRepository.save(det);
            }
        }

        System.out.println(">>> DataLoader creado exitosamente (estilo ReVive).");
    }
}
