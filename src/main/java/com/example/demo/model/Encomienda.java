package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "encomienda")
@Getter
@Setter
@NoArgsConstructor
public class Encomienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_seguimiento", unique = true, nullable = false, length = 20)
    private String codigoSeguimiento;

    @Column(nullable = false)
    private String remitente;

    @Column(nullable = false)
    private String destinatario;

    @Column(name = "direccion_destino", nullable = false)
    private String direccionDestino;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoEncomienda estado;

    @Column(name = "ubicacion_actual")
    private String ubicacionActual;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "encomienda", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fecha ASC, id ASC")
    private List<HistorialEstado> historial = new ArrayList<>();
}