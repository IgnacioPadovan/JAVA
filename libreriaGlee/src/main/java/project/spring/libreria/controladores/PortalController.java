package project.spring.libreria.controladores;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.spring.libreria.servicios.AdminService;

@Controller
@RequestMapping("/")
public class PortalController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model, @RequestParam(required = false) String logaut) {
        
        if (error != null) {
            model.put("error", "Documento de usuario o clave incorrecto");
        }
        if (logaut != null) {
            model.put("logaut", "Ha salido correctamente de la plataforma");
        }
        return "login.html";
    }

    @GetMapping("/registro")
    public String registro(ModelMap modelo) {
        return "registro.html";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String clave, @RequestParam String clave2, @RequestParam String documento) {
        try {
            adminService.altaAdmin(documento, nombre, apellido, clave, clave2);
        } catch (Error e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("documento", documento);
            modelo.put("clave", clave);
            modelo.put("clave2", clave2);
            Logger.getLogger(PortalController.class.getName()).log(Level.SEVERE, null, e);
            return "registro";
        }
        modelo.put("titulo", "Bienvenidos a la Biblioteca Glee");
        modelo.put("descripcion", "El administrador ha sido registrado con exito!");

        return "exito.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_REGISTRADO')")
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio.html";
    }
}
