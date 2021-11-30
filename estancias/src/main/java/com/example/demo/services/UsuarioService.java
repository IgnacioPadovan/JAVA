package com.example.demo.services;

import com.example.demo.entities.Usuario;
import com.example.demo.repositories.UsuarioRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void registrar(String alias, String email, String clave, String clave2) throws Error {

        validar(alias, email, clave, clave2);

        Usuario usuario = new Usuario();
        usuario.setAlias(alias);
        usuario.setEmail(email);
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        usuario.setFechaAlta(new Date());
        usuario.setFechaBaja(null);

        usuarioRepository.save(usuario);

    }

    @Transactional
    public void modificar(String id, String alias, String email, String clave, String clave2) throws Error {

        validar(alias, email, clave, clave2);

        Optional<Usuario> respuesta = usuarioRepository.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setAlias(alias);
            usuario.setEmail(email);
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);
            usuario.setFechaAlta(new Date());

            usuarioRepository.save(usuario);
        } else {
            throw new Error("No se encontró el usuario solicitado.");
        }
    }

    @Transactional
    public void baja(String id) throws Error {

        Optional<Usuario> respuesta = usuarioRepository.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setFechaBaja(new Date());
            usuarioRepository.save(usuario);
        } else {
            throw new Error("No se encontró el usuario solicitado.");
        }

    }
    
    @Transactional(readOnly=true)
    public Usuario buscarPorId(String id) throws Error {

        Optional<Usuario> respuesta = usuarioRepository.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new Error("No se encontró el usuario solicitado.");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.buscarPorEmail(email);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);
            GrantedAuthority p2 = new SimpleGrantedAuthority("ROLE_CLIENTE_REGISTRADO");
            permisos.add(p2);
            GrantedAuthority p3 = new SimpleGrantedAuthority("ROLE_FAMILIA_REGISTRADO");
            permisos.add(p3);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }

    private void validar(String alias, String email, String clave, String clave2) {

        if (alias == null || alias.trim().isEmpty()) {
            throw new Error("Debe indicar el alias");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new Error("Debe indicar el correo electronico");
        }
        if (clave == null || clave.trim().isEmpty()) {
            throw new Error("Debe indicar la clave");
        }
        if (clave2 == null || clave2.trim().isEmpty()) {
            throw new Error("Debe indicar la clave");
        }
        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new Error("La clave del usuario no puede ser nula o  menor a seis digitos");
        }
        if (!clave.equals(clave2)) {
            throw new Error("Las claves deben ser iguales");
        }
    }
}
