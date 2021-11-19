package project.spring.libreria.servicios;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.spring.libreria.entidades.Autor;
import project.spring.libreria.entidades.Editorial;
import project.spring.libreria.entidades.Libro;
import project.spring.libreria.repositorios.AutorRepository;
import project.spring.libreria.repositorios.EditorialRepository;
import project.spring.libreria.repositorios.LibroRepository;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private EditorialRepository editorialRepository;
    @Autowired
    private AutorRepository autorRepository;

    @Transactional
    public void altaLibro(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresUsados, Integer ejemplaresRestantes, Boolean alta, String idAutor, String idEditorial) {

        validar(isbn, titulo, anio, ejemplares, ejemplaresUsados, ejemplaresRestantes);

        Optional<Autor> respuesta1 = autorRepository.findById(idAutor);
        if (respuesta1.isPresent()) {
            Autor autor = respuesta1.get();
            Optional<Editorial> respuesta2 = editorialRepository.findById(idEditorial);
            if (respuesta2.isPresent()) {
                Editorial editorial = respuesta2.get();

                Libro libro = new Libro();

                libro.setTitulo(titulo);
                libro.setAnio(anio);
                libro.setAlta(true);
                libro.setEjemplares(ejemplares);
                libro.setEjemplaresRestantes(ejemplaresRestantes);
                libro.setEjemplaresUsados(ejemplaresUsados);
                libro.setIsbn(isbn);
                libro.setAutor(autor);
                libro.setEditorial(editorial);

                libroRepository.save(libro);

            } else {
                throw new Error("No se encontró la editorial solicitada.");
            }
        } else {
            throw new Error("No se encontró el autor solicitado.");
        }

    }

    @Transactional
    public void bajaLibro(String id) throws Exception {

        Optional<Libro> respuesta = libroRepository.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(false);
            libroRepository.save(libro);
        } else {
            throw new Error("No se encontró el libro solicitado.");
        }

    }

    @Transactional
    public void editarLibro(String id, String idAutor, String idEditorial, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresUsados, Integer ejemplaresRestantes) {

        validar(isbn, titulo, anio, ejemplares, ejemplaresUsados, ejemplaresRestantes);

        Optional<Libro> respuesta = libroRepository.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            if (libro.getAutor().getId().equalsIgnoreCase(idAutor) || libro.getEditorial().getId().equalsIgnoreCase(idEditorial)) {

                libro.setTitulo(titulo);
                libro.setAnio(anio);
                libro.setAlta(Boolean.TRUE);
                libro.setEjemplares(ejemplares);
                libro.setEjemplaresRestantes(ejemplaresRestantes);
                libro.setEjemplaresUsados(ejemplaresUsados);
                libro.setIsbn(isbn);
                libro.setAutor(autorRepository.getById(idAutor));
                libro.setEditorial(editorialRepository.getById(idEditorial));

                libroRepository.save(libro);
            } else {
                throw new Error("El autor o la editorial no coincide con el libro seleccionado.");
            }

        } else {
            throw new Error("No se encontró el libro solicitado.");
        }

    }

    private void validar(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresUsados, Integer ejemplaresRestantes) {

        if (isbn == null) {
            throw new Error("Debe indicar el ISBN");
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new Error("Debe indicar el titulo");
        }
        if (anio == null) {
            throw new Error("Debe indicar el año");
        }
        if (ejemplares == null) {
            throw new Error("Debe indicar cantidad de ejemplares");
        }
        if (ejemplares < 0) {
            throw new Error("Debe una cantidad de ejemplares positiva");
        }
        if (ejemplaresRestantes == null || ejemplaresRestantes < 0) {
            throw new Error("La cantidad de ejemplares restantes es nula o menor que cero");
        }
        if (ejemplaresUsados == null || ejemplaresUsados < 0) {
            throw new Error("Debe indicar cantidad de ejemplares usados o menor que cero");
        }
        if (ejemplares != (ejemplaresRestantes + ejemplaresUsados) ) {
            throw new Error("La suma de ejemplares restantes y prestados no es igual a los ejemplares totales");
        }

    }

    public List<Libro> buscarLibros() {

        return libroRepository.buscarAutores();

    }

    public Libro buscarPorId(String id) {

        Optional<Libro> respuesta = libroRepository.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            return libro;
        } else {
            throw new Error("No se encontró el libro solicitado.");
        }

    }

}
