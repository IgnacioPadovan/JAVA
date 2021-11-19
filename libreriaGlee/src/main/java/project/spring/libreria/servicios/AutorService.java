package project.spring.libreria.servicios;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.spring.libreria.entidades.Autor;
import project.spring.libreria.repositorios.AutorRepository;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Transactional
    public void altaAutor(String nombre) throws Error {

        validar(nombre);

        Autor autor = new Autor();

        autor.setNombre(nombre);
        autor.setAlta(Boolean.TRUE);

        autorRepository.save(autor);

    }

    @Transactional
    public void bajaAutor(String id) throws Error {

        Optional<Autor> respuesta = autorRepository.findById(id);

        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(false);
            autorRepository.save(autor);

        } else {
            throw new Error("No se encontró el autor solicitado.");
        }

    }

    @Transactional
    public void editarAutor(String id, String nombre) throws Error {

        validar(nombre);

        Optional<Autor> respuesta = autorRepository.findById(id);
        if (respuesta.isPresent()) {

            Autor autor = respuesta.get();

            autor.setNombre(nombre);
            autor.setAlta(Boolean.TRUE);

            autorRepository.save(autor);
        } else {
            throw new Error("No se encontró el autor solicitado.");
        }
    }

    private void validar(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Error("Debe indicar el nombre");
        }

    }

    public Autor buscarPorId(String id) {

        Optional<Autor> respuesta = autorRepository.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            return autor;
        } else {
            throw new Error("No se encontró el autor solicitado.");
        }

    }

    public List<Autor> buscarAutores() {

        return autorRepository.buscarAutores();

    }

}
