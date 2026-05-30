package com.dscb.reservas_restaurante.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearReservaRequest {

    @NotNull
    private Long clienteId;

    @NotNull
    private Long mesaId;

    @NotNull
    @Future
    private LocalDateTime fechaHora;

    @NotNull
    @Positive
    private Integer numeroPersonas;

}
