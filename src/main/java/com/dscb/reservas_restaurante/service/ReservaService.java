package com.dscb.reservas_restaurante.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.dscb.reservas_restaurante.exception.BusinessException;
import com.dscb.reservas_restaurante.exception.ResourceNotFoundException;
import com.dscb.reservas_restaurante.model.Cliente;
import com.dscb.reservas_restaurante.model.EstadoReserva;
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
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        Mesa mesa = mesaRepository.findById(reserva.getMesa().getId())
            .orElseThrow(() -> new ResourceNotFoundException ("Mesa no encontrada"));

        List <Reserva> reservasExistentes = reservaRepository.findByMesaAndFechaHora(mesa, reserva.getFechaHora());
        if (!reservasExistentes.isEmpty()) {
            throw new BusinessException("Mesa no disponible");
        }

        int capacidad = mesa.getCapacidad();
        if (reserva.getNumeroPersonas()>capacidad) {
            throw new BusinessException("El número de personas no puede exceder la capacidad de la mesa");
        }

        reserva.setEstado(EstadoReserva.PENDIENTE);
        
        return reservaRepository.save(reserva);
    }

    public Reserva obtenerReserva(Long id) {
        return reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
    }

    public List<Reserva> obtenerReservasPorCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
            
        return reservaRepository.findByCliente(cliente);
            

    }

    public Reserva actualizarReserva(Long id, Reserva reservaActualizada) {
        Reserva reservaExistente = reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
        reservaExistente.setFechaHora(reservaActualizada.getFechaHora());
        reservaExistente.setNumeroPersonas(reservaActualizada.getNumeroPersonas());
        reservaExistente.setEstado(reservaActualizada.getEstado());
        return reservaRepository.save(reservaExistente);
    }

    public Reserva cancelarReserva(Long id) {
        Reserva reservaExistente = reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
        reservaExistente.setEstado(EstadoReserva.CANCELADA);
        return reservaRepository.save(reservaExistente);
        
    }
}
