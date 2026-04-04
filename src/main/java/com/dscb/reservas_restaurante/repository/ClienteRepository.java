package com.dscb.reservas_restaurante.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dscb.reservas_restaurante.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
