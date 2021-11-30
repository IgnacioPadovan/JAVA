package com.example.demo.controllers;

import com.example.demo.entities.Casa;
import com.example.demo.entities.Cliente;
import com.example.demo.entities.Familia;
import com.example.demo.services.CasaService;
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

@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@Controller
@RequestMapping("/casa")
public class CasaController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private FamiliaService familiaService;
    @Autowired
    private CasaService casaService;
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/")
    public String index(@RequestParam String idUsuario, ModelMap modelo, String accion) {

        Familia familia = familiaService.buscarPorUsuario(idUsuario);

        if (familia != null) {
            modelo.put("familia", familia);
            if (familia.getCasa() != null) {
            modelo.put("calle", familia.getCasa().getCalle());
            modelo.put("codPostal", familia.getCasa().getCodPostal());
            modelo.put("ciudad", familia.getCasa().getCiudad());
            modelo.put("pais", familia.getCasa().getPais());
            modelo.put("numero", familia.getCasa().getNumero());
            modelo.put("tipoVivienda", familia.getCasa().getTipoVivienda());
            modelo.put("fechaDesde", familia.getCasa().getFechaDesde());
            modelo.put("fechaHasta", familia.getCasa().getFechaHasta());
            modelo.put("minDias", familia.getCasa().getMinDias());
            modelo.put("maxDias", familia.getCasa().getMaxDias());
            modelo.put("precio", familia.getCasa().getPrecio());
                return "casa";
            } else {
                return "casa";
            }
            
        } else {
            modelo.put("error", "Debe registrarse como familia alojadora");
            modelo.put("idUsuario", idUsuario);
            modelo.put("accion", "crear-familia");
            return "registro";
        }

    }

    @GetMapping("/registro")
    public String registro(ModelMap modelo, @RequestParam String calle, @RequestParam String codPostal,
            @RequestParam String ciudad, @RequestParam String pais, @RequestParam Integer numero,
            @RequestParam String tipoVivienda, @RequestParam Date fechaDesde, @RequestParam Date fechaHasta,
            @RequestParam Integer minDias, @RequestParam Integer maxDias, @RequestParam double precio) {

        try {
            casaService.registrar(calle, codPostal, ciudad, tipoVivienda, fechaDesde, fechaHasta, numero, minDias, maxDias, precio);
        } catch (Error e) {
            modelo.put("error", e.getMessage());
            modelo.put("calle", calle);
            modelo.put("codPostal", codPostal);
            modelo.put("ciudad", ciudad);
            modelo.put("pais", pais);
            modelo.put("numero", numero);
            modelo.put("tipoVivienda", tipoVivienda);
            modelo.put("fechaDesde", fechaDesde);
            modelo.put("fechaHasta", fechaHasta);
            modelo.put("minDias", minDias);
            modelo.put("maxDias", maxDias);
            modelo.put("precio", precio);
            
            return "casa.html";
        }
        
        return "inicio.html";
    }

    @GetMapping("/casas")
    public String inicio(@RequestParam String idUsuario, ModelMap modelo) {
        List<Casa> casas = casaService.listarCasas();
        modelo.put("casa", casas);
        Cliente cliente = clienteService.buscarPorUsuario(idUsuario);
        modelo.put("cliente", cliente);
        
        return "alojamientos.html";
    }

}
