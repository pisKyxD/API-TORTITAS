package com.example.demo.service;

import com.example.demo.model.Pedido;
import com.example.demo.model.Usuario;
import com.example.demo.repository.DetallePedidoRepository;
import com.example.demo.repository.DireccionRepository;
import com.example.demo.repository.EnvioRepository;
import com.example.demo.repository.PagoRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

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
        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        return usuarioRepository.save(usuario);
    }

    public Usuario update(Long id, Usuario usuario) {
        Usuario usuarioToUpdate = usuarioRepository.findById(id).orElse(null);
        if (usuarioToUpdate != null) {
            usuarioToUpdate.setNombre(usuario.getNombre());
            usuarioToUpdate.setApellido(usuario.getApellido());
            usuarioToUpdate.setEmail(usuario.getEmail());
            if (usuario.getContrasenia() != null) {
                usuarioToUpdate.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
            }
            usuarioToUpdate.setTelefono(usuario.getTelefono());
            usuarioToUpdate.setRol(usuario.getRol());
            usuarioToUpdate.setDireccionPrincipal(usuario.getDireccionPrincipal());
            return usuarioRepository.save(usuarioToUpdate);
        }
        return null;
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
            if (usuario.getContrasenia() != null) {
                usuarioToPatch.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
            }
            if (usuario.getTelefono() != null)
                usuarioToPatch.setTelefono(usuario.getTelefono());
            if (usuario.getRol() != null)
                usuarioToPatch.setRol(usuario.getRol());
            if (usuario.getDireccionPrincipal() != null)
                usuarioToPatch.setDireccionPrincipal(usuario.getDireccionPrincipal());
            return usuarioRepository.save(usuarioToPatch);
        }
        return null;
    }

    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
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
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
