package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 12)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "id_direccion", nullable = true)
    private Direccion direccionPrincipal;

    @OneToMany(mappedBy = "usuario", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Direccion> direcciones;

    @OneToMany(mappedBy = "usuario", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Pedido> pedidos;
}
