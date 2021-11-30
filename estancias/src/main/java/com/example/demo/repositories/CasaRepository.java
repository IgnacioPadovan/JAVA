
package com.example.demo.repositories;

import com.example.demo.entities.Casa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasaRepository extends JpaRepository<Casa, String> {
    
}
