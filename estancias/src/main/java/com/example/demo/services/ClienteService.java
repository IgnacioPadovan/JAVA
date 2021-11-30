package com.example.demo.services;

import com.example.demo.entities.Cliente;
import com.example.demo.entities.Usuario;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.repositories.UsuarioRepository;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void registrar(String nombre, String calle, int numero, String codPostal, String ciudad, String pais, String email, String idUsuario) throws Error {

        validar(nombre, calle, numero, codPostal, ciudad, pais, email);

        Optional<Usuario> respuesta = usuarioRepository.findById(idUsuario);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            Cliente cliente = new Cliente();
            cliente.setCalle(calle);
            cliente.setCiudad(ciudad);
            cliente.setCodPostal(codPostal);
            cliente.setEmail(email);
            cliente.setNombre(nombre);
            cliente.setNumero(numero);
            cliente.setPais(pais);
            cliente.setUsuario(usuario);

            clienteRepository.save(cliente);
        } else {
            throw new Error("No se encontró el usuario solicitado.");
        }

    }

    @Transactional
    public void modificar(String id, String nombre, String calle, int numero, String codPostal, String ciudad, String pais, String email, String idUsuario) throws Error {

        validar(nombre, calle, numero, codPostal, ciudad, pais, email);

        Optional<Cliente> respuesta = clienteRepository.findById(id);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            Optional<Usuario> respuesta2 = usuarioRepository.findById(idUsuario);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta2.get();

                cliente.setCalle(calle);
                cliente.setCiudad(ciudad);
                cliente.setCodPostal(codPostal);
                cliente.setEmail(email);
                cliente.setNombre(nombre);
                cliente.setNumero(numero);
                cliente.setPais(pais);

                cliente.setUsuario(usuario); // Analizar logica para cambiar datos del usuario

                clienteRepository.save(cliente);
            } else {
                throw new Error("No se encontró el usuario solicitado.");
            }
        } else {
            throw new Error("No se encontró el cliente solicitado.");
        }
    }

    @Transactional(readOnly = true)
    public void baja(String id) throws Error {

        Optional<Cliente> respuesta = clienteRepository.findById(id);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();

            cliente.getUsuario().setFechaBaja(new Date());

            clienteRepository.save(cliente);

        } else {
            throw new Error("No se encontró el cliente solicitado.");
        }

    }

    @Transactional(readOnly = true)
    public void buscarPorId(String id) throws Error {

        Optional<Cliente> respuesta = clienteRepository.findById(id);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();

            cliente.getUsuario().setFechaBaja(new Date());

            clienteRepository.save(cliente);

        } else {
            throw new Error("No se encontró el cliente solicitado.");
        }

    }

    @Transactional(readOnly = true)
    public Cliente buscarPorUsuario(String idUsuario) {

        return clienteRepository.buscarPorUsuario(idUsuario);

    }

    private void validar(String nombre, String calle, Integer numero, String codPostal, String ciudad, String pais, String email) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Error("Debe indicar el nombre");
        }
        if (calle == null || calle.trim().isEmpty()) {
            throw new Error("Debe indicar el calle");
        }
        if (codPostal == null || codPostal.trim().isEmpty()) {
            throw new Error("Debe indicar el codPostal");
        }
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new Error("Debe indicar el ciudad");
        }
        if (pais == null || pais.trim().isEmpty()) {
            throw new Error("Debe indicar el pais");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new Error("Debe indicar el correo electronico");
        }
        if (numero < 0) {
            throw new Error("Debe indicar la numero de casa");
        }
    }
}
