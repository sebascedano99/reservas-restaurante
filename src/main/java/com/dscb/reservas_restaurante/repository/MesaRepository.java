package com.dscb.reservas_restaurante.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dscb.reservas_restaurante.model.Mesa;

public interface MesaRepository extends JpaRepository<Mesa, Long>{

        List<Mesa> findByCapacidadGreaterThanEqual(int numeroPersonas);

}
