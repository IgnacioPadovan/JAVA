package project.spring.libreria.servicios;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.spring.libreria.entidades.Cliente;
import project.spring.libreria.repositorios.AutorRepository;
import project.spring.libreria.repositorios.ClienteRepository;
import project.spring.libreria.repositorios.EditorialRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public void altaCliente(Long documento, String nombre, String apellido) {

        validar(documento, nombre, apellido);
        Cliente cliente = new Cliente();

        cliente.setDocumento(documento);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setAlta(Boolean.TRUE);

        clienteRepository.save(cliente);
    }

    @Transactional
    public void bajaCliente(String id) throws Exception {

        Optional<Cliente> respuesta = clienteRepository.findById(id);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            cliente.setAlta(false);
            clienteRepository.save(cliente);
        } else {
            throw new Error("No se encontró el cliente solicitado.");
        }

    }

    @Transactional
    public void editarCliente(String id, Long documento, String nombre, String apellido) {

        validar(documento, nombre, apellido);

        Optional<Cliente> respuesta = clienteRepository.findById(id);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();

            cliente.setDocumento(documento);
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setAlta(Boolean.TRUE);

            clienteRepository.save(cliente);
        } else {
            throw new Error("No se encontró el cliente solicitado.");
        }

    }

    private void validar(Long documento, String nombre, String apellido) {

        if (documento == null) {
            throw new Error("Debe indicar el ISBN");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Error("Debe indicar el titulo");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new Error("Debe indicar el titulo");
        }

    }

    public List<Cliente> buscarClientes() {

        return clienteRepository.buscarClientes();

    }

    public Cliente buscarPorId(String id) {

        Optional<Cliente> respuesta = clienteRepository.findById(id);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            return cliente;
        } else {
            throw new Error("No se encontró el libro solicitado.");
        }

    }

}
