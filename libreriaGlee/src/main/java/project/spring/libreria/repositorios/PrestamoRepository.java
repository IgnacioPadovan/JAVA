package project.spring.libreria.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.spring.libreria.entidades.Prestamo;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, String> {

    @Query("SELECT c FROM Prestamo c")
    public List<Prestamo> buscarPrestamos();

}
