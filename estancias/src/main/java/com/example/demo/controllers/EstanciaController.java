package com.example.demo.controllers;

import com.example.demo.entities.Cliente;
import com.example.demo.services.ClienteService;
import com.example.demo.services.FamiliaService;
import com.example.demo.services.UsuarioService;
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
@RequestMapping("/estancia")
public class EstanciaController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private FamiliaService familiaService;
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/")
    public String index(@RequestParam String idUsuario, ModelMap modelo) {
        
        Cliente cliente = clienteService.buscarPorUsuario(idUsuario);
        modelo.put("cliente", cliente);
        
        return "alojamientos.html";
    }
}
