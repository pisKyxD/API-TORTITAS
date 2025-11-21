package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_direccion;

    @Column(nullable = false, length = 150)
    private String calle;

    @Column(nullable = false, length = 20)
    private String numero;

    @Column(length = 200)
    private String referencia;

    @ManyToOne
    @JoinColumn(name = "id_comuna")
    private Comuna comuna;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
