package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Encomienda;
import com.example.demo.model.EstadoEncomienda;
import com.example.demo.model.HistorialEstado;
import com.example.demo.repository.EncomiendaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Lógica de negocio de las encomiendas: generación del código de seguimiento,
 * registro del historial de estados y reglas de actualización.
 */
@Service
public class EncomiendaService {

    // Sin caracteres confusos (0, O, 1, I) para que el código sea fácil de leer
    private static final String ALFABETO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final EncomiendaRepository repo;

    public EncomiendaService(EncomiendaRepository repo) {
        this.repo = repo;
    }

    /** Genera un código aleatorio único con formato DNB-XXXXXXXX. */
    private String generarCodigo() {
        String codigo;
        do {
            StringBuilder sb = new StringBuilder("DNB-");
            for (int i = 0; i < 8; i++) {
                sb.append(ALFABETO.charAt(RANDOM.nextInt(ALFABETO.length())));
            }
            codigo = sb.toString();
        } while (repo.existsByCodigoSeguimiento(codigo));
        return codigo;
    }

    /** Registra un nuevo estado en el historial y actualiza el estado actual. */
    private void agregarHistorial(Encomienda e, EstadoEncomienda estado, String ubicacion) {
        HistorialEstado h = new HistorialEstado();
        h.setEncomienda(e);
        h.setEstado(estado);
        h.setUbicacion(ubicacion);
        h.setFecha(LocalDateTime.now());
        e.getHistorial().add(h);
        e.setEstado(estado);
        e.setUbicacionActual(ubicacion);
    }

    private Encomienda obtenerPorCodigo(String codigo) {
        return repo.findByCodigoSeguimiento(codigo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe una encomienda con código " + codigo));
    }

    private EncomiendaResponse aDto(Encomienda e) {
        List<HistorialResponse> historial = e.getHistorial().stream()
                .map(h -> new HistorialResponse(h.getId(), h.getEstado(), h.getUbicacion(), h.getFecha()))
                .toList();
        return new EncomiendaResponse(
                e.getId(), e.getCodigoSeguimiento(), e.getRemitente(), e.getDestinatario(),
                e.getDireccionDestino(), e.getDescripcion(), e.getEstado(),
                e.getUbicacionActual(), e.getFechaCreacion(), historial);
    }

    @Transactional
    public EncomiendaResponse crear(EncomiendaRequest req) {
        Encomienda e = new Encomienda();
        e.setCodigoSeguimiento(generarCodigo());
        e.setRemitente(req.remitente().trim());
        e.setDestinatario(req.destinatario().trim());
        e.setDireccionDestino(req.direccionDestino().trim());
        e.setDescripcion(req.descripcion());
        e.setFechaCreacion(LocalDateTime.now());
        agregarHistorial(e, EstadoEncomienda.EN_RECEPCION, req.ubicacionRecepcion().trim());
        return aDto(repo.save(e));
    }

    @Transactional(readOnly = true)
    public List<EncomiendaResponse> listar() {
        return repo.findAll().stream().map(this::aDto).toList();
    }

    @Transactional(readOnly = true)
    public EncomiendaResponse buscarPorCodigo(String codigo) {
        return aDto(obtenerPorCodigo(codigo));
    }

    /**
     * Actualiza el estado de una encomienda.
     * Reglas: no se modifica una encomienda ya entregada y el estado solo puede avanzar
     * (EN_RECEPCION -> EN_DESPACHO -> EN_TRANSITO -> ENTREGADO).
     */
    @Transactional
    public EncomiendaResponse actualizarEstado(String codigo, ActualizarEstadoRequest req) {
        Encomienda e = obtenerPorCodigo(codigo);

        if (e.getEstado() == EstadoEncomienda.ENTREGADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "La encomienda ya fue entregada y no se puede modificar");
        }

        // El orden del enum define el flujo: el nuevo estado debe ser posterior al actual
        if (req.estado().ordinal() <= e.getEstado().ordinal()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El nuevo estado debe ser posterior al estado actual");
        }

        agregarHistorial(e, req.estado(), req.ubicacion().trim());
        return aDto(repo.save(e));
    }

    @Transactional
    public void eliminar(String codigo) {
        repo.delete(obtenerPorCodigo(codigo));
    }
}