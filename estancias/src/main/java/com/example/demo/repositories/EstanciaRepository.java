package com.example.demo.repositories;

import com.example.demo.entities.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstanciaRepository extends JpaRepository<Estancia, String> {

}
