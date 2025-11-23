package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductoCreateRequest {

    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private Long categoriaId;
    private Long saborId;

}
