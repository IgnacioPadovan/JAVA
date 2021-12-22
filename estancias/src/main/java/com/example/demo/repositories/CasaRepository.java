package com.example.demo.repositories;

import com.example.demo.entities.Casa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CasaRepository extends JpaRepository<Casa, String> {

    @Query("SELECT c FROM Casa c WHERE c.calle = :calle AND c.numero = :numero AND c.ciudad = :ciudad AND c.pais = :pais")
    public Casa buscarCasaPorCaractersiticas(@Param("calle") String calle,
            @Param("numero") int numero, @Param("pais") String pais, @Param("ciudad") String ciudad);
    
    @Query("SELECT c FROM Casa c WHERE c.id = :idCasa")
    public Casa buscarPorEstancia(@Param("idCasa") String idCasa);
}
