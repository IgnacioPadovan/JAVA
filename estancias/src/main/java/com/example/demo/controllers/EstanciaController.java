package com.example.demo.controllers;

import com.example.demo.entities.Casa;
import com.example.demo.entities.Cliente;
import com.example.demo.entities.Familia;
import com.example.demo.services.CasaService;
import com.example.demo.services.ClienteService;
import com.example.demo.services.EstanciaService;
import com.example.demo.services.FamiliaService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    private EstanciaService estanciaService;
    @Autowired
    private FamiliaService familiaService;
    @Autowired
    private CasaService casaService;
    @Autowired
    private ClienteService clienteService;

    @PostMapping("/registro")
    public String registro(@RequestParam String idCasa, ModelMap modelo,
            @RequestParam String huesped, @RequestParam String idCliente,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaDesde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaHasta) {

        try {
            estanciaService.registrar(huesped, fechaDesde, fechaHasta, idCliente, idCasa);

            modelo.put("titulo", "¡La reserva se ha efectuado con exito!");
            modelo.put("descripcion", "Ya esta todo listo");

            return "exito";

        } catch (Error e) {

            Cliente cliente = clienteService.buscarPorId(idCliente);
            Casa casa = casaService.buscarPorId(idCasa);

            modelo.put("casa", casa);
            modelo.put("cliente", cliente);
            modelo.put("error", e.getMessage());
            modelo.put("huesped", huesped);
            modelo.put("fechaDesde", fechaDesde);
            modelo.put("fechaHasta", fechaHasta);

            return "estancia";
        }

    }

    @GetMapping("/")
    public String reservar(@RequestParam(required = false) String accion, @RequestParam String idCasa, ModelMap modelo, @RequestParam String idCliente) {
        Cliente cliente = clienteService.buscarPorId(idCliente);
        Casa casa = casaService.buscarPorId(idCasa);
        
        Familia familia = familiaService.buscarPorCasa(idCasa);
        
        modelo.put("familia", familia);
        
        modelo.put("casa", casa);
        modelo.put("cliente", cliente);

        return "estancia";
    }
    
    @GetMapping("/eliminar")
    public String eliminar(@RequestParam String idEstancia) {

        estanciaService.eliminar(idEstancia);
        
        return "redirect:/inicio";
        
    }
}
