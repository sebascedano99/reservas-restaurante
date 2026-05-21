package com.dscb.reservas_restaurante.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dscb.reservas_restaurante.model.Mesa;
import com.dscb.reservas_restaurante.service.MesaService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/mesas")
@RequiredArgsConstructor

public class MesaController {

    private final MesaService mesaService;

    @GetMapping("/disponibles")
    public ResponseEntity<List<Mesa>> obtenerMesasDisponibles(@RequestParam LocalDateTime fechaHora, @RequestParam Integer numeroPersonas) {
        return ResponseEntity.ok(mesaService.obtenerMesasDisponibles(fechaHora, numeroPersonas));
    }
    

}
