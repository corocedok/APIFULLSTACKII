package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.EncomiendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/encomienda")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@Tag(name = "Encomiendas", description = "Gestión de encomiendas Dinobox")
public class EncomiendaController {

    private final EncomiendaService service;

    public EncomiendaController(EncomiendaService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar nueva encomienda y obtener el código de seguimiento")
    @PostMapping
    public ResponseEntity<CodigoResponse> crear(@Valid @RequestBody EncomiendaRequest req) {
        EncomiendaResponse creada = service.crear(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CodigoResponse(creada.codigoSeguimiento()));
    }

    @Operation(summary = "Listar todas las encomiendas")
    @GetMapping
    public List<EncomiendaResponse> listar() {
        return service.listar();
    }

    @Operation(summary = "Consultar una encomienda por su código de seguimiento")
    @GetMapping("/{codigo}")
    public EncomiendaResponse consultar(@PathVariable String codigo) {
        return service.buscarPorCodigo(codigo);
    }

    @Operation(summary = "Actualizar el estado de una encomienda")
    @PutMapping("/{codigo}")
    public EncomiendaResponse actualizar(@PathVariable String codigo,
                                         @Valid @RequestBody ActualizarEstadoRequest req) {
        return service.actualizarEstado(codigo, req);
    }

    @Operation(summary = "Eliminar una encomienda")
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable String codigo) {
        service.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }
}
