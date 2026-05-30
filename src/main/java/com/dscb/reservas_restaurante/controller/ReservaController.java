package com.dscb.reservas_restaurante.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dscb.reservas_restaurante.dto.ActualizarReservaRequest;
import com.dscb.reservas_restaurante.dto.CrearReservaRequest;
import com.dscb.reservas_restaurante.dto.ReservaResponse;
import com.dscb.reservas_restaurante.mapper.ReservaMapper;
import com.dscb.reservas_restaurante.model.Reserva;
import com.dscb.reservas_restaurante.service.ReservaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor

public class ReservaController {

    private final ReservaService reservaService;
    private final ReservaMapper reservaMapper;

    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(@RequestBody @Valid CrearReservaRequest request) {
        Reserva reserva = reservaMapper.toEntity(request);
        Reserva nuevaReserva = reservaService.crearReserva(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaMapper.toResponse(nuevaReserva));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerReserva(@PathVariable Long id) {
        Reserva reserva = reservaService.obtenerReserva(id);
        return ResponseEntity.ok(reservaMapper.toResponse(reserva));
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> obtenerReservasPorCliente(@RequestParam Long clienteId) {
        List<ReservaResponse> reservas = reservaService.obtenerReservasPorCliente(clienteId).stream()
            .map(reservaMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(reservas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> actualizarReserva(@PathVariable Long id, @RequestBody @Valid ActualizarReservaRequest request) {
        Reserva reserva = reservaService.actualizarReserva(id, request);
        return ResponseEntity.ok(reservaMapper.toResponse(reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservaResponse> cancelarReserva(@PathVariable Long id) {
        Reserva reserva = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(reservaMapper.toResponse(reserva));
    }

}

