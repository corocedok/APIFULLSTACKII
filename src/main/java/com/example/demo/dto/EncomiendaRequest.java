package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EncomiendaRequest(
        @NotBlank(message = "El remitente es obligatorio")
        @Size(max = 100, message = "Máximo 100 caracteres")
        String remitente,

        @NotBlank(message = "El destinatario es obligatorio")
        @Size(max = 100, message = "Máximo 100 caracteres")
        String destinatario,

        @NotBlank(message = "La dirección de destino es obligatoria")
        @Size(max = 200, message = "Máximo 200 caracteres")
        String direccionDestino,

        @Size(max = 255, message = "Máximo 255 caracteres")
        String descripcion,

        @NotBlank(message = "La ubicación de recepción es obligatoria")
        @Size(max = 100, message = "Máximo 100 caracteres")
        String ubicacionRecepcion
) {}