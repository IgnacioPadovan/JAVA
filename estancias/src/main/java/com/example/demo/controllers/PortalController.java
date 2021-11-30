package com.example.demo.controllers;

import com.example.demo.services.ClienteService;
import com.example.demo.services.FamiliaService;
import com.example.demo.services.UsuarioService;
import java.util.Date;
import java.util.List;
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
    private ClienteService clienteService;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/registro")
    public String registro(ModelMap modelo) {
        return "registro.html";

    }@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio.html";
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
