package com.example.demo.controllers;

import com.example.demo.entities.Cliente;
import com.example.demo.entities.Familia;
import com.example.demo.entities.Usuario;
import com.example.demo.services.ClienteService;
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

@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@Controller
@RequestMapping("/familia")
public class FamiliaController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private FamiliaService familiaService;
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/")
    public String registrar() {
        return "registro.html";
    }
    
    @PostMapping("/registro")
    public String crear(@RequestParam String nombre, @RequestParam Integer edadMin,
            @RequestParam Integer edadMax, ModelMap modelo, @RequestParam Integer numHijos, 
            HttpSession session, @RequestParam String idUsuario) {

       
        try {
            String email = usuarioService.buscarPorId(idUsuario).getEmail();
            
            familiaService.registrar(nombre, email, edadMin, edadMax, numHijos, null, idUsuario);
            Familia familia  = familiaService.buscarPorUsuario(idUsuario);
            
            modelo.put("familia", familia);
            
            return "casa";

        } catch (Error ex) {
            
            Familia familia  = new Familia();
            familia.setEdadMax(edadMax);
            familia.setEdadMin(edadMin);
            familia.setEmail(usuarioService.buscarPorId(idUsuario).getEmail());
            familia.setNombre(nombre);
            familia.setNumHijos(numHijos);
            
            modelo.put("error", ex.getMessage());
            modelo.put("accion", "crear-familia");
            modelo.put("perfil", familia);

            return "registro.html";
        }

    }
}
