package com.dscb.reservas_restaurante.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dscb.reservas_restaurante.model.Cliente;
import com.dscb.reservas_restaurante.model.Mesa;
import com.dscb.reservas_restaurante.model.Reserva;


public interface ReservaRepository extends JpaRepository<Reserva, Long>{

    List<Reserva> findByMesaAndFechaHora(Mesa mesa, LocalDateTime fechaHora);
    List<Reserva> findByCliente(Cliente cliente);
    List<Reserva> findByFechaHora(LocalDateTime fechaHora);
}
