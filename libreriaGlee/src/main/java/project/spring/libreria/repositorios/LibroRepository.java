package project.spring.libreria.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.spring.libreria.entidades.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, String> {

    @Query("SELECT c FROM Libro c")
    public List<Libro> buscarAutores();

}
