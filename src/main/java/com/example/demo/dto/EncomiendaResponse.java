package com.example.demo.dto;

import com.example.demo.model.EstadoEncomienda;

import java.time.LocalDateTime;
import java.util.List;

public record EncomiendaResponse(
        Long id,
        String codigoSeguimiento,
        String remitente,
        String destinatario,
        String direccionDestino,
        String descripcion,
        EstadoEncomienda estado,
        String ubicacionActual,
        LocalDateTime fechaCreacion,
        List<HistorialResponse> historial
) {}