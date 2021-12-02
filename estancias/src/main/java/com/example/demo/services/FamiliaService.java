package com.example.demo.services;

import com.example.demo.entities.Casa;
import com.example.demo.entities.Familia;
import com.example.demo.entities.Usuario;
import com.example.demo.repositories.CasaRepository;
import com.example.demo.repositories.FamiliaRepository;
import com.example.demo.repositories.UsuarioRepository;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FamiliaService {

    @Autowired
    private FamiliaRepository familiaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CasaRepository casaRepository;

    @Transactional
    public void registrar(String nombre, String email, int edadMin, int edadMax, int numHijos, String idCasa, String idUsuario) throws Error {

        validar(nombre, email, edadMin, edadMax, numHijos);

        Optional<Usuario> respuesta1 = usuarioRepository.findById(idUsuario);
        if (respuesta1.isPresent()) {
            Usuario usuario = respuesta1.get();

                Familia familia = new Familia();

                familia.setCasa(null);
                familia.setEdadMax(edadMax);
                familia.setEdadMin(edadMin);
                familia.setEmail(email);
                familia.setNombre(nombre);
                familia.setNumHijos(numHijos);
                familia.setUsuario(usuario);

                familiaRepository.save(familia);
        } else {
            throw new Error("No se encontró el usuario solicitado.");
        }

    }

    @Transactional
    public void modificar(String id, String nombre, String email, int edadMin, int edadMax, int numHijos, String idCasa, String idUsuario) throws Error {

        validar(nombre, email, edadMin, edadMax, numHijos);

        Optional<Familia> respuesta = familiaRepository.findById(id);
        if (respuesta.isPresent()) {
            Familia familia = respuesta.get();
            Optional<Usuario> respuesta1 = usuarioRepository.findById(idUsuario);
            if (respuesta1.isPresent()) {
                Usuario usuario = respuesta1.get();
                Optional<Casa> respuesta2 = casaRepository.findById(idCasa);
                if (respuesta2.isPresent()) {
                    Casa casa = respuesta2.get();

                    familia.setCasa(casa); // Analizar logica para cambiar datos de la casa
                    familia.setEdadMax(edadMax);
                    familia.setEdadMin(edadMin);
                    familia.setEmail(email);
                    familia.setNombre(nombre);
                    familia.setNumHijos(numHijos);
                    familia.setUsuario(usuario); // Analizar logica para cambiar datos del usuario

                    familiaRepository.save(familia);
                } else {
                    throw new Error("No se encontró la casa solicitada.");
                }
            } else {
                throw new Error("No se encontró el usuario solicitado.");
            }
        } else {
            throw new Error("No se encontró la familia solicitada.");
        }
    }

    @Transactional
    public void baja(String id) throws Error {

        Optional<Familia> respuesta = familiaRepository.findById(id);
        if (respuesta.isPresent()) {
            Familia familia = respuesta.get();

            familia.getUsuario().setFechaBaja(new Date());

            familiaRepository.save(familia);

        } else {
            throw new Error("No se encontró la familia solicitado.");
        }

    }

    private void validar(String nombre, String email, int edadMin, int edadMax, int numHijos) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Error("Debe indicar el nombre");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new Error("Debe indicar el correo electronico");
        }
        if (edadMin < 0) {
            throw new Error("Debe indicar la edad minima de sus hijos");
        }
        if (edadMax < 0) {
            throw new Error("Debe indicar la edad maxima de sus hijos");
        }
        if (numHijos < 0) {
            throw new Error("Debe indicar la numero de hijos en la casa");
        }
    }

    @Transactional
    public Familia buscarPorUsuario(String idUsuario) {

        return familiaRepository.buscarPorUsuario(idUsuario);

    }
}
