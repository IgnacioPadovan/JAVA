package com.example.demo.repositories;

import com.example.demo.entities.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstanciaRepository extends JpaRepository<Estancia, String> {

    @Query("SELECT c FROM Estancia c WHERE c.cliente.id = :idCliente")
    public Estancia buscarPorCliente(@Param("idCliente")String idCliente);
    

}
