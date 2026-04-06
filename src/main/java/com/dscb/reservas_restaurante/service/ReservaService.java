package com.dscb.reservas_restaurante.service;


import org.springframework.stereotype.Service;

import com.dscb.reservas_restaurante.model.Cliente;
import com.dscb.reservas_restaurante.model.Mesa;
import com.dscb.reservas_restaurante.model.Reserva;
import com.dscb.reservas_restaurante.repository.ClienteRepository;
import com.dscb.reservas_restaurante.repository.MesaRepository;
import com.dscb.reservas_restaurante.repository.ReservaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ReservaService {

    private final ClienteRepository clienteRepository;
    private final MesaRepository mesaRepository;
    private final ReservaRepository reservaRepository;

    public Reserva crearReserva(Reserva reserva){

        Cliente cliente = clienteRepository.findById(reserva.getCliente().getId())
            .orElseThrow(() -> new RuntimeException ("Cliente no encontrado"));
        Mesa mesa = mesaRepository.findById(reserva.getMesa().getId())
            .orElseThrow(() -> new RuntimeException ("Mesa no encontrada"));
    }
}
