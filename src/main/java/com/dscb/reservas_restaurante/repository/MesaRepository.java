package com.dscb.reservas_restaurante.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dscb.reservas_restaurante.model.Mesa;

public interface MesaRepository extends JpaRepository<Mesa, Long> {

    List<Mesa> findByCapacidadGreaterThanEqual(int numeroPersonas);

    @Query("""
        SELECT m FROM Mesa m
        WHERE m.capacidad >= :numeroPersonas
          AND m.id NOT IN (
            SELECT r.mesa.id FROM Reserva r
            WHERE r.fechaHora > :inicio
              AND r.fechaHora < :fin
              AND r.estado <> 'CANCELADA'
          )
        """)
    List<Mesa> findDisponibles(@Param("inicio") LocalDateTime inicio,
                               @Param("fin") LocalDateTime fin,
                               @Param("numeroPersonas") int numeroPersonas);
}
