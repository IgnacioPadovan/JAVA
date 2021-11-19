package project.spring.libreria.controladores;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.spring.libreria.entidades.Admin;
import project.spring.libreria.entidades.Autor;
import project.spring.libreria.servicios.AutorService;

@PreAuthorize("hasAnyRole('ROLE_ADMIN_REGISTRADO')")
@Controller
@RequestMapping("/autor")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping("/crear-autor")
    public String crear(ModelMap modelo, @RequestParam(required = false) String id, @RequestParam(required = false) String nombre, HttpSession session) {

        try {
            if (id == null || id.isEmpty()) {
                autorService.altaAutor(nombre);
            } else {
                autorService.editarAutor(id, nombre);
            }
            return "redirect:/autor/mis-autores";

        } catch (Error ex) {

            Autor autor = new Autor();
            autor.setId(id);
            autor.setNombre(nombre);

            modelo.put("error", ex.getMessage());

            modelo.put("accion", "Actualizar");
            modelo.put("perfil", autor);

            return "autor.html";
        }

    }

    @GetMapping("/eliminar-autor")
    public String eliminar(@RequestParam(required = false) String id, HttpSession session) {


        autorService.bajaAutor(id);

        return "redirect:/autor/mis-autores";
    }

    @GetMapping("/mis-autores")
    public String misAutores(@RequestParam(required = false) String id, ModelMap modelo, HttpSession session) {


        List<Autor> autores = autorService.buscarAutores();

        modelo.put("autores", autores);
        return "autores";
    }

    @GetMapping("/editar-autor")
    public String editarPerfil(@RequestParam(required = false) String id, ModelMap modelo, @RequestParam(required = false) String accion, HttpSession session) {


        if (accion == null) {
            accion = "Crear";
        }

        Autor autor = new Autor();
        if (id != null && !id.isEmpty()) {
            autor = autorService.buscarPorId(id);
        }
        modelo.put("perfil", autor);
        modelo.put("accion", accion);

        return "autor";
    }

}
