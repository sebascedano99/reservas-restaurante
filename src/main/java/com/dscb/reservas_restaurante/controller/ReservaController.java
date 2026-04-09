package com.dscb.reservas_restaurante.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dscb.reservas_restaurante.model.Reserva;
import com.dscb.reservas_restaurante.service.ReservaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor

public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<Reserva> crearReserva(@RequestBody Reserva reserva) {
        Reserva nuevaReserva = reservaService.crearReserva(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
    }

}

