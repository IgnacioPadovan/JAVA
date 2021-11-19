
package project.spring.libreria.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.spring.libreria.entidades.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String>{

    @Query("SELECT c FROM Admin c")
    public List<Admin> buscarAdmins();

    @Query("SELECT c FROM Admin c WHERE c.documento = :documento")
    public Admin buscarPorDocumento(String documento);

}
