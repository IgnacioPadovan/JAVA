package com.example.demo.repositories;

import com.example.demo.entities.Casa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CasaRepository extends JpaRepository<Casa, String> {

    @Query("SELECT c FROM Casa c WHERE c.calle = :calle AND c.numero = :numero AND c.ciudad = :ciudad")
    public Casa buscarCasaPorCaractersiticas(@Param("calle") String calle, @Param("numero") int numero, @Param("ciudad") String ciudad);
}
