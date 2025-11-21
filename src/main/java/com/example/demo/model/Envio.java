package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_envio;

    @Column(nullable = false, length = 255)
    private String direccion_envio;

    private LocalDateTime fecha_envio;

    @Column(nullable = false, length = 30)
    private String estado_envio;

    @ManyToOne
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @OneToOne(mappedBy = "envio")
    private Pedido pedido;
}
