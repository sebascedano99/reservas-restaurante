package com.dscb.reservas_restaurante.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dscb.reservas_restaurante.dto.ActualizarReservaRequest;
import com.dscb.reservas_restaurante.dto.CrearReservaRequest;
import com.dscb.reservas_restaurante.exception.BusinessException;
import com.dscb.reservas_restaurante.exception.ResourceNotFoundException;
import com.dscb.reservas_restaurante.mapper.ReservaMapper;
import com.dscb.reservas_restaurante.model.Cliente;
import com.dscb.reservas_restaurante.model.EstadoReserva;
import com.dscb.reservas_restaurante.model.Mesa;
import com.dscb.reservas_restaurante.model.Reserva;
import com.dscb.reservas_restaurante.service.ReservaService;

@WebMvcTest(ReservaController.class)
@Import(ReservaMapper.class)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservaService reservaService;

    private ObjectMapper objectMapper;

    private final Cliente cliente = new Cliente(1L, "Juan Pérez", "juan@example.com", "603000000");
    private final Mesa mesa = new Mesa(1L, 1, 4);
    private final LocalDateTime fechaHora = LocalDateTime.of(2026, 6, 1, 20, 30);

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void postCrearReserva_CuandoValida_Retorna201() throws Exception {
        CrearReservaRequest request = new CrearReservaRequest(1L, 1L, fechaHora, 2);
        Reserva reservaCreada = new Reserva(1L, cliente, mesa, fechaHora, 2, EstadoReserva.PENDIENTE);

        when(reservaService.crearReserva(any())).thenReturn(reservaCreada);

        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.estado").value("PENDIENTE"))
            .andExpect(jsonPath("$.cliente.id").value(1L))
            .andExpect(jsonPath("$.mesa.numero").value(1));
    }

    @Test
    public void postCrearReserva_CuandoMesaNoDisponible_Retorna400() throws Exception {
        CrearReservaRequest request = new CrearReservaRequest(1L, 1L, fechaHora, 2);

        when(reservaService.crearReserva(any())).thenThrow(new BusinessException("Mesa no disponible"));

        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensaje").value("Mesa no disponible"));
    }

    @Test
    public void getObtenerReserva_CuandoExiste_Retorna200() throws Exception {
        Reserva reserva = new Reserva(1L, cliente, mesa, fechaHora, 2, EstadoReserva.PENDIENTE);

        when(reservaService.obtenerReserva(1L)).thenReturn(reserva);

        mockMvc.perform(get("/reservas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.cliente.nombre").value("Juan Pérez"));
    }

    @Test
    public void getObtenerReserva_CuandoNoExiste_Retorna404() throws Exception {
        when(reservaService.obtenerReserva(99L)).thenThrow(new ResourceNotFoundException("Reserva no encontrada"));

        mockMvc.perform(get("/reservas/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.mensaje").value("Reserva no encontrada"));
    }

    @Test
    public void getObtenerReservasPorCliente_Retorna200() throws Exception {
        Reserva reserva = new Reserva(1L, cliente, mesa, fechaHora, 2, EstadoReserva.PENDIENTE);

        when(reservaService.obtenerReservasPorCliente(1L)).thenReturn(List.of(reserva));

        mockMvc.perform(get("/reservas?clienteId=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void putActualizarReserva_CuandoExiste_Retorna200() throws Exception {
        ActualizarReservaRequest request = new ActualizarReservaRequest(
            LocalDateTime.of(2026, 6, 2, 21, 0), 3, EstadoReserva.CONFIRMADA);
        Reserva reservaResultado = new Reserva(1L, cliente, mesa,
            LocalDateTime.of(2026, 6, 2, 21, 0), 3, EstadoReserva.CONFIRMADA);

        when(reservaService.actualizarReserva(any(), any())).thenReturn(reservaResultado);

        mockMvc.perform(put("/reservas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estado").value("CONFIRMADA"));
    }

    @Test
    public void deleteCancelarReserva_CuandoExiste_Retorna200() throws Exception {
        Reserva reservaCancelada = new Reserva(1L, cliente, mesa, fechaHora, 2, EstadoReserva.CANCELADA);

        when(reservaService.cancelarReserva(1L)).thenReturn(reservaCancelada);

        mockMvc.perform(delete("/reservas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estado").value("CANCELADA"));
    }

    @Test
    public void deleteCancelarReserva_CuandoNoExiste_Retorna404() throws Exception {
        when(reservaService.cancelarReserva(99L)).thenThrow(new ResourceNotFoundException("Reserva no encontrada"));

        mockMvc.perform(delete("/reservas/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.mensaje").value("Reserva no encontrada"));
    }
}
