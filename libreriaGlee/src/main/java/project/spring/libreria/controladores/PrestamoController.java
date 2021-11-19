package project.spring.libreria.controladores;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.spring.libreria.entidades.Admin;
import project.spring.libreria.entidades.Prestamo;
import project.spring.libreria.servicios.ClienteService;
import project.spring.libreria.servicios.LibroService;
import project.spring.libreria.servicios.PrestamoService;

@PreAuthorize("hasAnyRole('ROLE_ADMIN_REGISTRADO')")
@Controller
@RequestMapping("/prestamo")
public class PrestamoController {

    @Autowired
    private LibroService libroService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PrestamoService prestamoService;

    @GetMapping("/crear-prestamo")
    public String crear(@RequestParam String idLibro, @RequestParam String idCliente,
            @RequestParam("fechaPrestamo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaPrestamo,
            @RequestParam("fechaDevolucion") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaDevolucion,
            ModelMap modelo, @RequestParam(required = false) String id, HttpSession session) {


        try {

            if (idLibro == null || idLibro.isEmpty()) {
                throw new Error("Editorial no enviada");
            }
            if (idCliente == null || idCliente.isEmpty()) {
                throw new Error("Autor no enviado");
            }

            if (id == null || id.isEmpty()) {
                prestamoService.altaPrestamo(idLibro, idCliente, fechaPrestamo, fechaDevolucion);
            } else {
                prestamoService.editarPrestamo(id, idLibro, idCliente, fechaDevolucion);
            }
            return "redirect:/prestamo/mis-prestamos";

        } catch (Error ex) {

            Prestamo prestamo = new Prestamo();
            prestamo.setCliente(clienteService.buscarPorId(idCliente));
            prestamo.setLibro(libroService.buscarPorId(idLibro));
            prestamo.setFechaDevolucion(fechaDevolucion);
            prestamo.setFechaPrestamo(fechaPrestamo);

            modelo.put("error", ex.getMessage());
            modelo.put("libros", libroService.buscarLibros());
            modelo.put("clientes", clienteService.buscarClientes());
            modelo.put("accion", "Actualizar");
            modelo.put("perfil", prestamo);

            return "prestamo.html";
        }

    }

    @GetMapping("/eliminar-prestamo")
    public String eliminar(@RequestParam(required = false) String id, HttpSession session) {

        try {
            prestamoService.bajaPrestamo(id);
        } catch (Exception ex) {
            Logger.getLogger(PrestamoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/prestamo/mis-prestamos";
    }

    @GetMapping("/mis-prestamos")
    public String misPrestamos(@RequestParam(required = false) String id, ModelMap modelo, HttpSession session) {

        
        List<Prestamo> prestamos = prestamoService.buscarPrestamos();

        modelo.put("prestamos", prestamos);
        return "prestamos";
    }

    @GetMapping("/editar-prestamo")
    public String editarPrestamo(@RequestParam(required = false) String id, ModelMap modelo, 
            @RequestParam(required = false) String accion, HttpSession session) {

        
        if (accion == null) {
            accion = "Crear";
        }

        Prestamo prestamo = new Prestamo();
        if (id != null && !id.isEmpty()) {
            prestamo = prestamoService.buscarPorId(id);
        }

        modelo.put("libros", libroService.buscarLibros());
        modelo.put("clientes", clienteService.buscarClientes());
        modelo.put("perfil", prestamo);
        modelo.put("accion", accion);

        return "prestamo";
    }

}
