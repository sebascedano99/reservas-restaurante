package com.dscb.reservas_restaurante.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dscb.reservas_restaurante.model.Mesa;
import com.dscb.reservas_restaurante.model.Reserva;
import com.dscb.reservas_restaurante.repository.MesaRepository;
import com.dscb.reservas_restaurante.repository.ReservaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MesaService {

    private final MesaRepository mesaRepository;
    private final ReservaRepository reservaRepository;
    
    //Metodo para obtener mesas disponibles en una fecha y hora específica, filtrando por capacidad
    public List<Mesa> obtenerMesasDisponibles(LocalDateTime fechaHora, Integer numeroPersonas){

        List<Mesa> mesasConCapacidad = mesaRepository.findByCapacidadGreaterThanEqual(numeroPersonas);
        List<Reserva> reservasEnFechaHora = reservaRepository.findByFechaHora(fechaHora);
        
        //Obtener las mesas que están reservadas en la fecha y hora especificada
        List<Mesa> mesasReservadas = reservasEnFechaHora.stream()
        .map(reserva -> reserva.getMesa())
        .collect(Collectors.toList());
        
        //Devuleve las mesas que no están reservadas en la fecha y hora especificada
        return mesasConCapacidad.stream()
        .filter(mesa -> !mesasReservadas.contains(mesa))
        .collect(Collectors.toList());
    }

}
