package com.dscb.reservas_restaurante.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.dscb.reservas_restaurante.model.EstadoReserva;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarReservaRequest {

    @NotNull
    @Future
    private LocalDateTime fechaHora;

    @NotNull
    @Positive
    private Integer numeroPersonas;

    @NotNull
    private EstadoReserva estado;

}
