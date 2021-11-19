package project.spring.libreria.controladores;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.spring.libreria.entidades.Admin;
import project.spring.libreria.servicios.AdminService;

@PreAuthorize("hasAnyRole('ROLE_ADMIN_REGISTRADO')")
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/editar-perfil")
    public String editarPerfil(@RequestParam String id, HttpSession session, ModelMap modelo) {
        
        try {

            Admin admin = adminService.buscarPorId(id);
            modelo.addAttribute("perfil", admin);
        } catch (Error ex) {
            modelo.addAttribute("error", ex.getMessage());
        }

        return "perfil.html";

    }

    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(ModelMap modelo, HttpSession session, @RequestParam String id,
            @RequestParam String nombre, @RequestParam String apellido, @RequestParam String documento,
            @RequestParam String clave1, @RequestParam String clave2) {

        Admin admin = null;

        try {

            admin = adminService.buscarPorId(id);
            adminService.editarAdmin(id, nombre, nombre, apellido, clave2, clave2);
            session.setAttribute("adminsession", admin);
            return "redirect/inicio";
        } catch (Error ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", admin);
            return "perfil.html";
        }
    }

}
