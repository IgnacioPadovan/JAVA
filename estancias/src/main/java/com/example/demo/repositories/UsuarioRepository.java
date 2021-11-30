
package com.example.demo.repositories;

import com.example.demo.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    @Query("SELECT c FROM Usuario c WHERE c.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);
    
}
