package com.example.demo.controllers;

import com.example.demo.entities.Casa;
import com.example.demo.entities.Estancia;
import com.example.demo.entities.Usuario;
import com.example.demo.services.CasaService;
import com.example.demo.services.EstanciaService;
import com.example.demo.services.FamiliaService;
import com.example.demo.services.UsuarioService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private FamiliaService familiaService;
    @Autowired
    private EstanciaService estanciaService;
    @Autowired
    private CasaService casaService;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/registro")
    public String registro(ModelMap modelo) {
        return "registro.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo, HttpSession session) {

        try {

            Estancia estancia = estanciaService.buscarPorUsuario((Usuario) session.getAttribute("usuariosession"));

            if (estancia == null) {

                modelo.put("estancia", estancia);
                modelo.put("casa", null);
                
            } else {
                
                Casa casa = casaService.buscarPorEstancia(estancia);
                
                modelo.put("estancia", estancia);
                modelo.put("casa", casa);
            }

            return "inicio.html";

        } catch (Error e) {

            modelo.put("estancia", null);
            modelo.put("casa", null);

            modelo.put("error", e.getMessage());

            return "inicio.html";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model, @RequestParam(required = false) String logaut) {
        if (error != null) {
            model.put("error", "Nombre de usuario o clave incorrectos");
        }
        if (logaut != null) {
            model.put("logaut", "Ha salido correctamente de la plataforma");
        }
        return "login.html";

    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, @RequestParam String alias, @RequestParam String email, @RequestParam String clave1, @RequestParam String clave2) {

        try {
            usuarioService.registrar(alias, email, clave1, clave2);
        } catch (Error e) {
            modelo.put("error", e.getMessage());
            modelo.put("alias", alias);
            modelo.put("email", email);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            return "registro.html";
        }

        modelo.put("titulo", "Bienvenidos a la App de Estancias");
        modelo.put("descripcion", "Tu usuario fue registrado con exito");
        return "exito.html";
    }
}
