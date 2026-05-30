package com.dscb.reservas_restaurante.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.dscb.reservas_restaurante.model.EstadoReserva;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponse {

    private Long id;
    private ClienteInfo cliente;
    private MesaInfo mesa;
    private LocalDateTime fechaHora;
    private Integer numeroPersonas;
    private EstadoReserva estado;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClienteInfo {
        private Long id;
        private String nombre;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MesaInfo {
        private Long id;
        private Integer numero;
        private Integer capacidad;
    }

}
