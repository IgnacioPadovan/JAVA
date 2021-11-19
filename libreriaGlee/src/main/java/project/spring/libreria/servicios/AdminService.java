package project.spring.libreria.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import project.spring.libreria.entidades.Admin;
import project.spring.libreria.repositorios.AdminRepository;

@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Transactional
    public void altaAdmin(String documento, String nombre, String apellido, String clave, String clave2) {

        validar(documento, nombre, apellido, clave, clave2);
        Admin admin = new Admin();

        admin.setDocumento(documento);
        admin.setNombre(nombre);
        admin.setApellido(apellido);
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        admin.setClave(encriptada);
        admin.setAlta(Boolean.TRUE);

        adminRepository.save(admin);
    }

    @Transactional
    public void bajaAdmin(String id) throws Exception {

        Optional<Admin> respuesta = adminRepository.findById(id);
        if (respuesta.isPresent()) {
            Admin Admin = respuesta.get();
            Admin.setAlta(false);
            adminRepository.save(Admin);
        } else {
            throw new Error("No se encontró el Admin solicitado.");
        }

    }

    @Transactional
    public void editarAdmin(String id, String documento, String nombre, String apellido, String clave, String clave2) {

        validar(documento, nombre, apellido, clave, clave2);

        Optional<Admin> respuesta = adminRepository.findById(id);
        if (respuesta.isPresent()) {
            Admin admin = respuesta.get();

            admin.setDocumento(documento);
            admin.setNombre(nombre);
            admin.setApellido(apellido);
            admin.setAlta(Boolean.TRUE);

            adminRepository.save(admin);
        } else {
            throw new Error("No se encontró el Admin solicitado.");
        }

    }

    private void validar(String documento, String nombre, String apellido, String clave, String clave2) {

        if (documento == null || documento.isEmpty() || documento.length() < 5) {
            throw new Error("Debe indicar un documento valido");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Error("Debe indicar el nombre del admin");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new Error("Debe indicar el apellido del admin");
        }
        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new Error("La clave del usuario no puede ser nula o  menor a seis digitos");
        }
        if (!clave.equals(clave2)) {
            throw new Error("Las claves deben ser iguales");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String documento) throws UsernameNotFoundException {

        Admin admin = adminRepository.buscarPorDocumento(documento);

        if (admin != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_ADMIN_REGISTRADO");
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("adminsession", admin);

            User user = new User(admin.getDocumento(), admin.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }

    public List<Admin> buscarAdmins() {

        return adminRepository.buscarAdmins();

    }

    public Admin buscarPorId(String id) {

        Optional<Admin> respuesta = adminRepository.findById(id);
        if (respuesta.isPresent()) {
            Admin admin = respuesta.get();
            return admin;
        } else {
            throw new Error("No se encontró el Admin solicitado.");
        }

    }

}
