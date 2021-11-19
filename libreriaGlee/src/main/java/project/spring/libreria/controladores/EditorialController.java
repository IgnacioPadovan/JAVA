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
import project.spring.libreria.entidades.Editorial;
import project.spring.libreria.servicios.EditorialService;

@PreAuthorize("hasAnyRole('ROLE_ADMIN_REGISTRADO')")

@Controller
@RequestMapping("/editorial")
public class EditorialController {

    @Autowired
    private EditorialService editorialService;

    @GetMapping("/crear-editorial")
    public String crear(ModelMap modelo, @RequestParam(required = false) String id, @RequestParam(required = false) String nombre, HttpSession session) {

        try {
            if (id == null || id.isEmpty()) {
                editorialService.altaEditorial(nombre);
            } else {
                editorialService.editarEditorial(id, nombre);
            }
            return "redirect:/editorial/mis-editoriales";

        } catch (Error ex) {

            Editorial editorial = new Editorial();
            editorial.setId(id);
            editorial.setNombre(nombre);

            modelo.put("error", ex.getMessage());

            modelo.put("accion", "Actualizar");
            modelo.put("perfil", editorial);

            return "editorial.html";
        }

    }

    @GetMapping("/eliminar-editorial")
    public String eliminar(@RequestParam String id, HttpSession session) {


        editorialService.bajaEditorial(id);

        return "redirect:/editorial/mis-editoriales";

    }

    @GetMapping("/mis-editoriales")
    public String misEditoriales(@RequestParam(required = false) String id, ModelMap modelo, HttpSession session) {


        List<Editorial> editoriales = editorialService.buscarEditoriales();

        modelo.put("editoriales", editoriales);
        return "editoriales";
    }

    @GetMapping("/editar-editorial")
    public String editarPerfil(@RequestParam(required = false) String id, ModelMap modelo, @RequestParam(required = false) String accion, HttpSession session) {


        if (accion == null) {
            accion = "Crear";
        }

        Editorial editorial = new Editorial();
        if (id != null && !id.isEmpty()) {
            editorial = editorialService.buscarPorId(id);
        }
        modelo.put("perfil", editorial);
        modelo.put("accion", accion);

        return "editorial.html";
    }

}
