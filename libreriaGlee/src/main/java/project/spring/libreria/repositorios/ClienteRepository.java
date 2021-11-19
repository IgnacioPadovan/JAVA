package project.spring.libreria.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.spring.libreria.entidades.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    @Query("SELECT c FROM Cliente c")
    public List<Cliente> buscarClientes();

}
