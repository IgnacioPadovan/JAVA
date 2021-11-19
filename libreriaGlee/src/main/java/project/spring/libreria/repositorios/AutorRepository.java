
package project.spring.libreria.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.spring.libreria.entidades.Autor;

@Repository
public interface AutorRepository extends JpaRepository<Autor, String>{

    @Query("SELECT c FROM Autor c")
    public List<Autor> buscarAutores();
    
}
