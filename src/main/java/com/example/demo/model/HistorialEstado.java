package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estado")
@Getter
@Setter
@NoArgsConstructor
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "encomienda_id", nullable = false)
    private Encomienda encomienda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoEncomienda estado;

    private String ubicacion;

    private LocalDateTime fecha;
}