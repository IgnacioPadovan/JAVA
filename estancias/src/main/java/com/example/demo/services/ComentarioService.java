package com.example.demo.services;

import com.example.demo.entities.Casa;
import com.example.demo.entities.Comentario;
import com.example.demo.repositories.CasaRepository;
import com.example.demo.repositories.ComentarioRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {

    @Autowired
    private CasaRepository casaRepository;
    @Autowired
    private ComentarioRepository comentarioRepository;

    @Transactional
    public void registrar(String descripcion, String idCasa) throws Error {

        validar(descripcion);

        Optional<Casa> respuesta2 = casaRepository.findById(idCasa);
        if (respuesta2.isPresent()) {
            Casa casa = respuesta2.get();

            Comentario comentario = new Comentario();

            comentario.setCasa(casa);
            comentario.setDescripcion(descripcion);

            comentarioRepository.save(comentario);
        } else {
            throw new Error("No se encontró la casa solicitada.");
        }

    }

    @Transactional
    public void modificar(String id, String descripcion, String idCasa) throws Error {

        validar(descripcion);

        Optional<Comentario> respuesta = comentarioRepository.findById(id);
        if (respuesta.isPresent()) {
            Comentario comentario = respuesta.get();
            Optional<Casa> respuesta2 = casaRepository.findById(idCasa);
            if (respuesta2.isPresent()) {
                Casa casa = respuesta2.get();

                comentario.setCasa(casa); // Analizar logica para cambiar datos de la casa
                comentario.setDescripcion(descripcion);
                comentarioRepository.save(comentario);
            } else {
                throw new Error("No se encontró la casa solicitada.");
            }
        } else {
            throw new Error("No se encontró el comantario solicitado.");
        }
    }

    @Transactional
    public void eliminar(String id) throws Error {

        Optional<Comentario> respuesta = comentarioRepository.findById(id);
        if (respuesta.isPresent()) {

            Comentario comentario = respuesta.get();

            comentarioRepository.deleteById(comentario.getId());

        } else {
            throw new Error("No se encontró el comentario solicitado.");
        }

    }

    private void validar(String descripcion) {

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new Error("Debe indicar una descripcion");
        }
        if (descripcion.length() > 30) {
            throw new Error("Debe indicar una descripcion mayor a 30 caracteres");
        }
    }
}
