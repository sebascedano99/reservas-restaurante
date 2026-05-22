package com.dscb.reservas_restaurante.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class ErrorResponse {
    private int status;
    private String mensaje;
    private LocalDateTime timestamp;


}
