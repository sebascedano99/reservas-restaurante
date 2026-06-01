package com.dscb.reservas_restaurante.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dscb.reservas_restaurante.model.Mesa;
import com.dscb.reservas_restaurante.repository.MesaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MesaService {

    private final MesaRepository mesaRepository;

    public List<Mesa> obtenerMesasDisponibles(LocalDateTime fechaHora, Integer numeroPersonas) {
        LocalDateTime inicio = fechaHora.minusHours(2);
        LocalDateTime fin = fechaHora.plusHours(2);
        return mesaRepository.findDisponibles(inicio, fin, numeroPersonas);
    }
}
