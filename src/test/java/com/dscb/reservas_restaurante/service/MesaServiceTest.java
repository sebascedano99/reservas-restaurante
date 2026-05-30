package com.dscb.reservas_restaurante.service;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dscb.reservas_restaurante.model.EstadoReserva;
import com.dscb.reservas_restaurante.model.Mesa;
import com.dscb.reservas_restaurante.model.Reserva;
import com.dscb.reservas_restaurante.repository.MesaRepository;
import com.dscb.reservas_restaurante.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
public class MesaServiceTest {

    @Mock
    private MesaRepository mesaRepository;
    @Mock
    private ReservaRepository reservaRepository;
    @InjectMocks
    private MesaService mesaService;

    private final LocalDateTime fechaHora = LocalDateTime.of(2026, 5, 15, 20, 0);

    @Test
    public void obtenerMesasDisponibles_CuandoHayMesas_RetornaSoloDisponibles() {
        Mesa mesa1 = new Mesa(1L, 1, 2);
        Mesa mesa2 = new Mesa(2L, 2, 4);
        Mesa mesa3 = new Mesa(3L, 3, 6);

        when(mesaRepository.findByCapacidadGreaterThanEqual(3)).thenReturn(List.of(mesa2, mesa3));

        Reserva reservaExistente = new Reserva();
        reservaExistente.setMesa(mesa2);
        when(reservaRepository.findByFechaHora(fechaHora)).thenReturn(List.of(reservaExistente));

        List<Mesa> resultado = mesaService.obtenerMesasDisponibles(fechaHora, 3);

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(mesa3));
    }

    @Test
    public void obtenerMesasDisponibles_CuandoSinCapacidad_RetornaListaVacia() {
        when(mesaRepository.findByCapacidadGreaterThanEqual(10)).thenReturn(List.of());

        List<Mesa> resultado = mesaService.obtenerMesasDisponibles(fechaHora, 10);

        assertTrue(resultado.isEmpty());
    }

    @Test
    public void obtenerMesasDisponibles_CuandoTodasOcupadas_RetornaListaVacia() {
        Mesa mesa1 = new Mesa(1L, 1, 4);
        Mesa mesa2 = new Mesa(2L, 2, 4);

        when(mesaRepository.findByCapacidadGreaterThanEqual(2)).thenReturn(List.of(mesa1, mesa2));

        Reserva reserva1 = new Reserva();
        reserva1.setMesa(mesa1);
        Reserva reserva2 = new Reserva();
        reserva2.setMesa(mesa2);
        when(reservaRepository.findByFechaHora(fechaHora)).thenReturn(List.of(reserva1, reserva2));

        List<Mesa> resultado = mesaService.obtenerMesasDisponibles(fechaHora, 2);

        assertTrue(resultado.isEmpty());
    }
}
