package com.dscb.reservas_restaurante.controller;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dscb.reservas_restaurante.mapper.MesaMapper;
import com.dscb.reservas_restaurante.model.Mesa;
import com.dscb.reservas_restaurante.service.MesaService;

@WebMvcTest(MesaController.class)
@Import(MesaMapper.class)
public class MesaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MesaService mesaService;

    @Test
    public void getMesasDisponibles_Retorna200() throws Exception {
        Mesa mesa1 = new Mesa(1L, 1, 4);
        Mesa mesa2 = new Mesa(2L, 2, 6);

        when(mesaService.obtenerMesasDisponibles(
            LocalDateTime.of(2026, 6, 1, 20, 0), 3))
            .thenReturn(List.of(mesa1, mesa2));

        mockMvc.perform(get("/mesas/disponibles")
                .param("fechaHora", "2026-06-01T20:00:00")
                .param("numeroPersonas", "3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getMesasDisponibles_CuandoSinResultados_Retorna200Vacio() throws Exception {
        when(mesaService.obtenerMesasDisponibles(
            LocalDateTime.of(2026, 6, 1, 20, 0), 10))
            .thenReturn(List.of());

        mockMvc.perform(get("/mesas/disponibles")
                .param("fechaHora", "2026-06-01T20:00:00")
                .param("numeroPersonas", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }
}
