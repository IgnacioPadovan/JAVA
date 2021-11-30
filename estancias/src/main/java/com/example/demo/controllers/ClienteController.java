package com.example.demo.controllers;

import com.example.demo.entities.Cliente;
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
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private FamiliaService familiaService;
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/")
    public String registrar(HttpSession session, @RequestParam String accion, ModelMap modelo) {
        
        modelo.put("accion","accion");
        modelo.put("email","session.getEmail");
        
        return "registro.html";
    }
    
    @PostMapping("/registro")
    public String crear(@RequestParam String idUsuario, @RequestParam String nombre, @RequestParam Integer numero,
            @RequestParam String calle, ModelMap modelo, @RequestParam String codPostal, HttpSession session,
            @RequestParam String ciudad, @RequestParam String pais) {

       
        try {

            if (idUsuario == null || idUsuario.isEmpty()) {
                throw new Error("idUsuario no enviada");
            }
            
            clienteService.registrar(nombre, calle, numero, codPostal, ciudad, pais, usuarioService.buscarPorId(idUsuario).getEmail() , idUsuario);
            Cliente cliente  = clienteService.buscarPorUsuario(idUsuario);
            
            modelo.put("cliente", cliente);
            
            return "alojamientos";

        } catch (Error ex) {

            Cliente cliente = new Cliente();
            cliente.setCalle(calle);
            cliente.setCiudad(ciudad);
            cliente.setCodPostal(codPostal);
            cliente.setEmail(pais);
            cliente.setNombre(nombre);
            cliente.setNumero(numero);
            cliente.setPais(pais);
            
            modelo.put("error", ex.getMessage());
            modelo.put("accion", "Cliente");
            modelo.put("perfil", cliente);

            return "registro.html";
        }

    }
}
