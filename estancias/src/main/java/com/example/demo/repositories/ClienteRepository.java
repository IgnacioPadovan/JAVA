
package com.example.demo.repositories;

import com.example.demo.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    @Query("SELECT c FROM Cliente c WHERE c.usuario.id = :idUsuario")
    public Cliente buscarPorUsuario(@Param("idUsuario")String idUsuario);
    
}
