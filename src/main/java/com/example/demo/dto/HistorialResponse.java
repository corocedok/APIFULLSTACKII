package com.example.demo.dto;

import com.example.demo.model.EstadoEncomienda;

import java.time.LocalDateTime;

public record HistorialResponse(
        Long id,
        EstadoEncomienda estado,
        String ubicacion,
        LocalDateTime fecha
) {}