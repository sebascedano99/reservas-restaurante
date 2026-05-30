package com.dscb.reservas_restaurante.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dscb.reservas_restaurante.dto.ActualizarReservaRequest;
import com.dscb.reservas_restaurante.exception.BusinessException;
import com.dscb.reservas_restaurante.exception.ResourceNotFoundException;
import com.dscb.reservas_restaurante.model.Cliente;
import com.dscb.reservas_restaurante.model.EstadoReserva;
import com.dscb.reservas_restaurante.model.Mesa;
import com.dscb.reservas_restaurante.model.Reserva;
import com.dscb.reservas_restaurante.repository.ClienteRepository;
import com.dscb.reservas_restaurante.repository.MesaRepository;
import com.dscb.reservas_restaurante.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private MesaRepository mesaRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @InjectMocks
    private ReservaService reservaService;

    private final Cliente cliente = new Cliente(1L, "Juan Pérez", "juan.perez@example.com", "123456789");
    private final Mesa mesa = new Mesa(1L, 1, 4);
    private final LocalDateTime fechaHora = LocalDateTime.of(2026, 4, 26, 20, 30);

    @Test
    public void crearReserva_CuandoTodoValido_RetornaReservaConEstadoPendiente() {
        Reserva reserva = new Reserva(null, cliente, mesa, fechaHora, 2, null);
        Reserva reservaGuardada = new Reserva(1L, cliente, mesa, fechaHora, 2, EstadoReserva.PENDIENTE);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));
        when(reservaRepository.findByMesaAndFechaHora(mesa, fechaHora)).thenReturn(Collections.emptyList());
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaGuardada);

        Reserva resultado = reservaService.crearReserva(reserva);

        assertNotNull(resultado);
        assertEquals(EstadoReserva.PENDIENTE, resultado.getEstado());
        assertEquals(1L, resultado.getId());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    public void crearReserva_CuandoClienteNoExiste_LanzaResourceNotFoundException() {
        Reserva reserva = new Reserva(null, cliente, mesa, fechaHora, 2, null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservaService.crearReserva(reserva));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    public void crearReserva_CuandoMesaNoExiste_LanzaResourceNotFoundException() {
        Reserva reserva = new Reserva(null, cliente, mesa, fechaHora, 2, null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mesaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservaService.crearReserva(reserva));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    public void crearReserva_CuandoMesaNoDisponible_LanzaBusinessException() {
        Reserva reserva = new Reserva(null, cliente, mesa, fechaHora, 2, null);
        Reserva reservaExistente = new Reserva(2L, cliente, mesa, fechaHora, 3, EstadoReserva.PENDIENTE);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));
        when(reservaRepository.findByMesaAndFechaHora(mesa, fechaHora)).thenReturn(List.of(reservaExistente));

        assertThrows(BusinessException.class, () -> reservaService.crearReserva(reserva));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    public void crearReserva_CuandoExcedeCapacidad_LanzaBusinessException() {
        Reserva reserva = new Reserva(null, cliente, mesa, fechaHora, 6, null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));
        when(reservaRepository.findByMesaAndFechaHora(mesa, fechaHora)).thenReturn(Collections.emptyList());

        assertThrows(BusinessException.class, () -> reservaService.crearReserva(reserva));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    public void obtenerReserva_CuandoExiste_RetornaReserva() {
        Reserva reserva = new Reserva(1L, cliente, mesa, fechaHora, 2, EstadoReserva.PENDIENTE);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Reserva resultado = reservaService.obtenerReserva(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(reservaRepository).findById(1L);
    }

    @Test
    public void obtenerReserva_CuandoNoExiste_LanzaResourceNotFoundException() {
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservaService.obtenerReserva(99L));
    }

    @Test
    public void obtenerReservasPorCliente_CuandoClienteExiste_RetornaLista() {
        Reserva reserva = new Reserva(1L, cliente, mesa, fechaHora, 2, EstadoReserva.PENDIENTE);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(reservaRepository.findByCliente(cliente)).thenReturn(List.of(reserva));

        List<Reserva> resultado = reservaService.obtenerReservasPorCliente(1L);

        assertEquals(1, resultado.size());
        verify(reservaRepository).findByCliente(cliente);
    }

    @Test
    public void obtenerReservasPorCliente_CuandoClienteNoExiste_LanzaResourceNotFoundException() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservaService.obtenerReservasPorCliente(99L));
    }

    @Test
    public void actualizarReserva_CuandoExiste_RetornaReservaActualizada() {
        Reserva reservaExistente = new Reserva(1L, cliente, mesa, fechaHora, 2, EstadoReserva.PENDIENTE);
        ActualizarReservaRequest request = new ActualizarReservaRequest(
            LocalDateTime.of(2026, 4, 27, 21, 0), 3, EstadoReserva.CONFIRMADA);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaExistente));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reserva resultado = reservaService.actualizarReserva(1L, request);

        assertEquals(LocalDateTime.of(2026, 4, 27, 21, 0), resultado.getFechaHora());
        assertEquals(3, resultado.getNumeroPersonas());
        assertEquals(EstadoReserva.CONFIRMADA, resultado.getEstado());
        verify(reservaRepository).save(reservaExistente);
    }

    @Test
    public void actualizarReserva_CuandoNoExiste_LanzaResourceNotFoundException() {
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> reservaService.actualizarReserva(99L, new ActualizarReservaRequest()));
    }

    @Test
    public void cancelarReserva_CuandoExiste_CambiaEstadoACancelada() {
        Reserva reservaExistente = new Reserva(1L, cliente, mesa, fechaHora, 2, EstadoReserva.PENDIENTE);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaExistente));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reserva resultado = reservaService.cancelarReserva(1L);

        assertEquals(EstadoReserva.CANCELADA, resultado.getEstado());
        verify(reservaRepository).save(reservaExistente);
    }

    @Test
    public void cancelarReserva_CuandoNoExiste_LanzaResourceNotFoundException() {
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservaService.cancelarReserva(99L));
    }
}
