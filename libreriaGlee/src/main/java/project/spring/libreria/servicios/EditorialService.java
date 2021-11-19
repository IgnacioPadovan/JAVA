package project.spring.libreria.servicios;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.spring.libreria.entidades.Editorial;
import project.spring.libreria.repositorios.EditorialRepository;

@Service
public class EditorialService {

    @Autowired
    private EditorialRepository editorialRepository;

    @Transactional
    public void altaEditorial(String nombre) throws Error {

        validar(nombre);

        Editorial editorial = new Editorial();

        editorial.setNombre(nombre);
        editorial.setAlta(true);

        editorialRepository.save(editorial);

    }

    @Transactional
    public void bajaEditorial(String id) throws Error {

        Optional<Editorial> respuesta = editorialRepository.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(false);
            editorialRepository.save(editorial);
        } else {
            throw new Error("No se encontró la editorial solicitada.");
        }
    }

    @Transactional
    public void editarEditorial(String id, String nombre) throws Error {

        validar(nombre);

        Optional<Editorial> respuesta = editorialRepository.findById(id);
        if (respuesta.isPresent()) {

            Editorial editorial = respuesta.get();

            editorial.setNombre(nombre);
            editorial.setAlta(Boolean.TRUE);

            editorialRepository.save(editorial);
        } else {
            throw new Error("No se encontró la editorial solicitada.");
        }
    }

    private void validar(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Error("Debe indicar el nombre");
        }

    }

    public Editorial buscarPorId(String id) {

        Optional<Editorial> respuesta = editorialRepository.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            return editorial;
        } else {
            throw new Error("No se encontró la editorial solicitada.");
        }

    }

    public List<Editorial> buscarEditoriales() {

        return editorialRepository.buscarEditoriales();

    }

}
