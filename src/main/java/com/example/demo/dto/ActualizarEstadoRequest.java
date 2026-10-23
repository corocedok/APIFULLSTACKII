package com.example.demo.dto;

import com.example.demo.model.EstadoEncomienda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActualizarEstadoRequest(
        @NotNull(message = "El estado es obligatorio")
        EstadoEncomienda estado,

        @NotBlank(message = "La ubicación es obligatoria")
        String ubicacion
) {}