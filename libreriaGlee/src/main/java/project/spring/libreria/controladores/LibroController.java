package project.spring.libreria.controladores;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.spring.libreria.entidades.Admin;
import project.spring.libreria.entidades.Libro;
import project.spring.libreria.servicios.AutorService;
import project.spring.libreria.servicios.EditorialService;
import project.spring.libreria.servicios.LibroService;


@Controller
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private LibroService libroService;
    @Autowired
    private AutorService autorService;
    @Autowired
    private EditorialService editorialService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN_REGISTRADO')")
    @GetMapping("/crear-libro")
    public String crear(@RequestParam String idEditorial, @RequestParam String idAutor, @RequestParam Integer ejemplaresUsados, 
            @RequestParam Integer ejemplaresRestantes, @RequestParam Integer ejemplares, @RequestParam Integer anio, 
            @RequestParam Long isbn, @RequestParam String titulo, ModelMap modelo, @RequestParam(required = false) String id, 
            @RequestParam(required = false) String nombre, HttpSession session) {

       
        try {

            if (idEditorial == null || idEditorial.isEmpty()) {
                throw new Error("Editorial no enviada");
            }
            if (idAutor == null || idAutor.isEmpty()) {
                throw new Error("Autor no enviado");
            }

            if (id == null || id.isEmpty()) {
                libroService.altaLibro(isbn, titulo, anio, ejemplares, ejemplaresUsados, ejemplaresRestantes, Boolean.TRUE, idAutor, idEditorial); //VERIFICAR SERVICIO
            } else {
                libroService.editarLibro(id, idAutor, idEditorial, isbn, titulo, anio, ejemplares, ejemplaresUsados, ejemplaresRestantes); //VERIFICAR SERVICIO
            }
            return "redirect:/libro/mis-libros";

        } catch (Error ex) {

            Libro libro = new Libro();
            libro.setId(id);
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setAutor(autorService.buscarPorId(idAutor));
            libro.setEditorial(editorialService.buscarPorId(idEditorial));
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresRestantes(ejemplaresRestantes);
            libro.setEjemplaresUsados(ejemplaresUsados);

            modelo.put("error", ex.getMessage());
            modelo.put("autores", autorService.buscarAutores());
            modelo.put("editoriales", editorialService.buscarEditoriales());
            modelo.put("accion", "Actualizar");
            modelo.put("perfil", libro);

            return "libro.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN_REGISTRADO')")
    @GetMapping("/eliminar-libro")
    public String eliminar(@RequestParam(required = false) String id, HttpSession session) {


        try {
            libroService.bajaLibro(id);
        } catch (Exception ex) {
            Logger.getLogger(LibroController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/libro/mis-libros";
    }

    @GetMapping("/mis-libros")
    public String misAutores(@RequestParam(required = false) String id, ModelMap modelo, HttpSession session) {

        

        List<Libro> libros = libroService.buscarLibros();

        modelo.put("libros", libros);
        return "libros";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN_REGISTRADO')")
    @GetMapping("/editar-libro")
    public String editarPerfil(@RequestParam(required = false) String id, ModelMap modelo, @RequestParam(required = false) String accion, HttpSession session) {

        if (accion == null) {
            accion = "Crear";
        }

        Libro libro = new Libro();
        if (id != null && !id.isEmpty()) {
            libro = libroService.buscarPorId(id);
        }

        modelo.put("autores", autorService.buscarAutores());
        modelo.put("editoriales", editorialService.buscarEditoriales());
        modelo.put("perfil", libro);
        modelo.put("accion", accion);

        return "libro";
    }

}
