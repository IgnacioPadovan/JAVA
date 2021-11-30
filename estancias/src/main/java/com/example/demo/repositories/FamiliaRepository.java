package com.example.demo.repositories;

import com.example.demo.entities.Familia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FamiliaRepository extends JpaRepository<Familia, String> {

    @Query("SELECT c FROM Familia c WHERE c.usuario.id = :idUsuario")
    public Familia buscarPorUsuario(@Param("idUsuario") String idUsuario);

}
