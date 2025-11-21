package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Pedido;
import com.example.demo.model.Rol;
import com.example.demo.model.Usuario;
import com.example.demo.repository.DetallePedidoRepository;
import com.example.demo.repository.DireccionRepository;
import com.example.demo.repository.EnvioRepository;
import com.example.demo.repository.PagoRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario save(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Usuario update(Long id, Usuario usuario) {
        Usuario usuarioToUpdate = usuarioRepository.findById(id).orElse(null);
        if (usuarioToUpdate != null) {
            usuarioToUpdate.setNombre(usuario.getNombre());
            usuarioToUpdate.setApellido(usuario.getApellido());
            usuarioToUpdate.setEmail(usuario.getEmail());
            if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
                usuarioToUpdate.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            usuarioToUpdate.setTelefono(usuario.getTelefono());
            usuarioToUpdate.setRol(usuario.getRol());
            usuarioToUpdate.setDireccionPrincipal(usuario.getDireccionPrincipal());
            return usuarioRepository.save(usuarioToUpdate);
        }
        return null;
    }

    public Usuario actualizarDireccionPrincipal(Long idUsuario, Long idDireccion) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null)
            return null;
        var direccion = new com.example.demo.model.Direccion();
        direccion.setId_direccion(idDireccion);
        usuario.setDireccionPrincipal(direccion);
        return usuarioRepository.save(usuario);
    }

    public Usuario patch(Long id, Usuario usuario) {
        Usuario usuarioToPatch = usuarioRepository.findById(id).orElse(null);
        if (usuarioToPatch != null) {
            if (usuario.getNombre() != null)
                usuarioToPatch.setNombre(usuario.getNombre());
            if (usuario.getApellido() != null)
                usuarioToPatch.setApellido(usuario.getApellido());
            if (usuario.getEmail() != null)
                usuarioToPatch.setEmail(usuario.getEmail());
            if (usuario.getTelefono() != null)
                usuarioToPatch.setTelefono(usuario.getTelefono());
            if (usuario.getRol() != null)
                usuarioToPatch.setRol(usuario.getRol());
            if (usuario.getDireccionPrincipal() != null)
                usuarioToPatch.setDireccionPrincipal(usuario.getDireccionPrincipal());
            if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
                usuarioToPatch.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            return usuarioRepository.save(usuarioToPatch);
        }
        return null;
    }

    public Usuario saveFromBody(Map<String, Object> body) {
        Usuario usuario = new Usuario();
        usuario.setNombre((String) body.get("nombre"));
        usuario.setApellido((String) body.get("apellido"));
        usuario.setEmail((String) body.get("email"));
        usuario.setTelefono((String) body.get("telefono"));
        String password = (String) body.get("password");
        usuario.setPassword(password);
        Object rolObj = body.get("rol");
        if (rolObj instanceof Map<?, ?> rolMapRaw) {
            Object idRolObj = rolMapRaw.get("id_rol");
            Long idRol = null;
            if (idRolObj instanceof Number n) {
                idRol = n.longValue();
            } else if (idRolObj != null) {
                idRol = Long.valueOf(idRolObj.toString());
            }
            if (idRol != null) {
                Rol rol = new Rol();
                rol.setId_rol(idRol);
                usuario.setRol(rol);
            }
        } else {
            throw new IllegalArgumentException("El campo 'rol' debe ser un objeto con id_rol");
        }
        usuario.setDireccionPrincipal(null);
        return save(usuario);
    }

    public ResponseEntity<?> login(Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("Faltan campos");
        }
        Usuario usuario = findByEmail(email);
        if (usuario == null) {
            return ResponseEntity.status(404).body("Correo no registrado");
        }
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            return ResponseEntity.status(400).body("Contraseña incorrecta");
        }
        return ResponseEntity.ok(usuario);
    }

    public boolean deleteUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return false;
        }
        direccionRepository.deleteByUsuario(usuario);
        List<Pedido> pedidos = pedidoRepository.findByUsuario(usuario);
        for (Pedido pedido : pedidos) {
            detallePedidoRepository.deleteByPedido(pedido);
            if (pedido.getPago() != null) {
                pagoRepository.deleteById(pedido.getPago().getId_pago());
            }
            if (pedido.getEnvio() != null) {
                envioRepository.deleteById(pedido.getEnvio().getId_envio());
            }
        }
        pedidoRepository.deleteByUsuario(usuario);
        usuarioRepository.delete(usuario);
        return true;
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
