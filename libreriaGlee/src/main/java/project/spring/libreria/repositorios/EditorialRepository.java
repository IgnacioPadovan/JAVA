package project.spring.libreria.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.spring.libreria.entidades.Editorial;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, String> {

    @Query("SELECT c FROM Editorial c")
    public List<Editorial> buscarEditoriales();
    
}
