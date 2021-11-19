package project.spring.libreria.controladores;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.spring.libreria.entidades.Admin;
import project.spring.libreria.entidades.Cliente;
import project.spring.libreria.servicios.ClienteService;

@PreAuthorize("hasAnyRole('ROLE_ADMIN_REGISTRADO')")
@Controller
@RequestMapping("/cliente")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/crear-cliente")
    public String crear(@RequestParam String nombre, 
            @RequestParam String apellido, @RequestParam Long documento, ModelMap modelo, 
            @RequestParam(required = false) String id, HttpSession session) {
        
        try {

            if (id == null || id.isEmpty()) {
                clienteService.altaCliente(documento, nombre, apellido); //VERIFICAR SERVICIO
            } else {
                clienteService.editarCliente(id, documento, nombre, apellido); //VERIFICAR SERVICIO
            }
            return "redirect:/cliente/mis-clientes";

        } catch (Error ex) {

            Cliente cliente = new Cliente();
            
            cliente.setApellido(apellido);
            cliente.setDocumento(documento);
            cliente.setNombre(nombre);

            modelo.put("error", ex.getMessage());
            modelo.put("accion", "Actualizar");
            modelo.put("perfil", cliente);

            return "cliente.html";
        }

    }

    @GetMapping("/eliminar-cliente")
    public String eliminar(@RequestParam(required = false) String id, HttpSession session) {


        try {
            clienteService.bajaCliente(id);
        } catch (Exception ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/cliente/mis-clientes";
    }

    @GetMapping("/mis-clientes")
    public String misClientes(@RequestParam(required = false) String id, ModelMap modelo, HttpSession session) {


        List<Cliente> clientes = clienteService.buscarClientes();

        modelo.put("clientes", clientes);
        return "clientes";
    }

    @GetMapping("/editar-cliente")
    public String editarPerfil(@RequestParam(required = false) String id, ModelMap modelo, @RequestParam(required = false) String accion, HttpSession session) {


        if (accion == null) {
            accion = "Crear";
        }

        Cliente cliente = new Cliente();
        if (id != null && !id.isEmpty()) {
            cliente = clienteService.buscarPorId(id);
        }

        modelo.put("perfil", cliente);
        modelo.put("accion", accion);

        return "cliente";
    }

}
