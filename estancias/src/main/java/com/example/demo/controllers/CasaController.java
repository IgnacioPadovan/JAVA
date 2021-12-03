package com.example.demo.controllers;

import com.example.demo.entities.Casa;
import com.example.demo.entities.Cliente;
import com.example.demo.entities.Familia;
import com.example.demo.entities.Usuario;
import com.example.demo.services.CasaService;
import com.example.demo.services.ClienteService;
import com.example.demo.services.FamiliaService;
import com.example.demo.services.UsuarioService;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

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
                modelo.put("mensaje", "Edite los datos de su casa");
                modelo.put("casa", familia.getCasa());
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

    @PostMapping("/registro")
    public String registro(ModelMap modelo, @RequestParam String calle, @RequestParam String codPostal,
            @RequestParam String ciudad, @RequestParam String pais, @RequestParam Integer numero,
            @RequestParam String tipoVivienda, @RequestParam String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaDesde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaHasta, @RequestParam Integer minDias,
            @RequestParam Integer maxDias, @RequestParam double precio, HttpSession session) {

        try {
            if (id.isEmpty() || id == null) {
                Usuario usuario = (Usuario) session.getAttribute("usuariosession");
                Familia familia = familiaService.buscarPorUsuario(usuario.getId());
                casaService.registrar(familia, calle, codPostal, ciudad, tipoVivienda, fechaDesde,
                        fechaHasta, numero,
                        minDias, maxDias, precio, pais);
            } else {
                casaService.modificar(id, calle, codPostal, ciudad, tipoVivienda, fechaDesde,
                        fechaHasta, numero, minDias, maxDias, precio, pais);
            }

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
    public String mostrarCasas(@RequestParam String idUsuario, ModelMap modelo) {
        Cliente cliente = clienteService.buscarPorUsuario(idUsuario);
        List<Casa> casas = casaService.listarCasas();
        modelo.put("cliente", cliente);
        modelo.put("casas", casas);
        return "alojamientos.html";
    }

}
