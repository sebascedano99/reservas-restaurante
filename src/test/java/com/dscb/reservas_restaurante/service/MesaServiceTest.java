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

import com.dscb.reservas_restaurante.model.Mesa;
import com.dscb.reservas_restaurante.repository.MesaRepository;

@ExtendWith(MockitoExtension.class)
public class MesaServiceTest {

    @Mock
    private MesaRepository mesaRepository;
    @InjectMocks
    private MesaService mesaService;

    private final LocalDateTime fechaHora = LocalDateTime.of(2026, 5, 15, 20, 0);
    private final LocalDateTime inicio = fechaHora.minusHours(2);
    private final LocalDateTime fin = fechaHora.plusHours(2);

    @Test
    public void obtenerMesasDisponibles_CuandoHayMesas_RetornaSoloDisponibles() {
        Mesa mesa3 = new Mesa(3L, 3, 6);

        when(mesaRepository.findDisponibles(inicio, fin, 3)).thenReturn(List.of(mesa3));

        List<Mesa> resultado = mesaService.obtenerMesasDisponibles(fechaHora, 3);

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(mesa3));
    }

    @Test
    public void obtenerMesasDisponibles_CuandoSinCapacidad_RetornaListaVacia() {
        when(mesaRepository.findDisponibles(inicio, fin, 10)).thenReturn(List.of());

        List<Mesa> resultado = mesaService.obtenerMesasDisponibles(fechaHora, 10);

        assertTrue(resultado.isEmpty());
    }

    @Test
    public void obtenerMesasDisponibles_CuandoTodasOcupadas_RetornaListaVacia() {
        when(mesaRepository.findDisponibles(inicio, fin, 2)).thenReturn(List.of());

        List<Mesa> resultado = mesaService.obtenerMesasDisponibles(fechaHora, 2);

        assertTrue(resultado.isEmpty());
    }
}
