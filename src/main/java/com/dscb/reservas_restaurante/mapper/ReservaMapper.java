package com.dscb.reservas_restaurante.mapper;

import org.springframework.stereotype.Component;

import com.dscb.reservas_restaurante.dto.ActualizarReservaRequest;
import com.dscb.reservas_restaurante.dto.CrearReservaRequest;
import com.dscb.reservas_restaurante.dto.ReservaResponse;
import com.dscb.reservas_restaurante.model.Cliente;
import com.dscb.reservas_restaurante.model.Mesa;
import com.dscb.reservas_restaurante.model.Reserva;

@Component
public class ReservaMapper {

    public ReservaResponse toResponse(Reserva reserva) {
        if (reserva == null) return null;

        ReservaResponse.ClienteInfo clienteInfo = new ReservaResponse.ClienteInfo(
            reserva.getCliente().getId(),
            reserva.getCliente().getNombre()
        );
        ReservaResponse.MesaInfo mesaInfo = new ReservaResponse.MesaInfo(
            reserva.getMesa().getId(),
            reserva.getMesa().getNumero(),
            reserva.getMesa().getCapacidad()
        );

        return new ReservaResponse(
            reserva.getId(),
            clienteInfo,
            mesaInfo,
            reserva.getFechaHora(),
            reserva.getNumeroPersonas(),
            reserva.getEstado()
        );
    }

    public Reserva toEntity(CrearReservaRequest request) {
        Cliente cliente = new Cliente();
        cliente.setId(request.getClienteId());

        Mesa mesa = new Mesa();
        mesa.setId(request.getMesaId());

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setMesa(mesa);
        reserva.setFechaHora(request.getFechaHora());
        reserva.setNumeroPersonas(request.getNumeroPersonas());

        return reserva;
    }

    public void updateEntity(Reserva reserva, ActualizarReservaRequest request) {
        reserva.setFechaHora(request.getFechaHora());
        reserva.setNumeroPersonas(request.getNumeroPersonas());
        reserva.setEstado(request.getEstado());
    }

}
