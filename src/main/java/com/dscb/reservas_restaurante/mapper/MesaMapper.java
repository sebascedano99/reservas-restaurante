package com.dscb.reservas_restaurante.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dscb.reservas_restaurante.dto.MesaResponse;
import com.dscb.reservas_restaurante.model.Mesa;

@Component
public class MesaMapper {

    public MesaResponse toResponse(Mesa mesa) {
        if (mesa == null) return null;
        return new MesaResponse(mesa.getId(), mesa.getNumero(), mesa.getCapacidad());
    }

    public List<MesaResponse> toResponseList(List<Mesa> mesas) {
        return mesas.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

}
