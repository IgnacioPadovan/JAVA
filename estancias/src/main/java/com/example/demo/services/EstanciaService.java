package com.example.demo.services;

import com.example.demo.entities.Casa;
import com.example.demo.entities.Cliente;
import com.example.demo.entities.Estancia;
import com.example.demo.repositories.CasaRepository;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.repositories.EstanciaRepository;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstanciaService {

    @Autowired
    private EstanciaRepository estanciaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private CasaRepository casaRepository;

    @Transactional
    public void registrar(String huesped, Date fechaDesde, Date fechaHasta, String idCliente, String idCasa) throws Error {

        validar(huesped, fechaDesde, fechaHasta);

        Optional<Cliente> respuesta1 = clienteRepository.findById(idCliente);
        if (respuesta1.isPresent()) {
            Cliente cliente = respuesta1.get();
            Optional<Casa> respuesta2 = casaRepository.findById(idCasa);
            if (respuesta2.isPresent()) {
                Casa casa = respuesta2.get();

                Estancia estancia = new Estancia();

                estancia.setCasa(casa);
                estancia.setCliente(cliente);
                estancia.setFechaDesde(fechaDesde);
                estancia.setFechaHasta(fechaHasta);
                estancia.setHuesped(huesped);

                estanciaRepository.save(estancia);
            } else {
                throw new Error("No se encontró la casa solicitada.");
            }
        } else {
            throw new Error("No se encontró el cliente solicitado.");
        }

    }

    @Transactional
    public void modificar(String id, String huesped, Date fechaDesde, Date fechaHasta, String idCliente, String idCasa) throws Error {

        validar(huesped, fechaDesde, fechaHasta);

        Optional<Estancia> respuesta = estanciaRepository.findById(id);
        if (respuesta.isPresent()) {
            Estancia estancia = respuesta.get();
            Optional<Cliente> respuesta1 = clienteRepository.findById(idCliente);
            if (respuesta1.isPresent()) {
                Cliente cliente = respuesta1.get();
                Optional<Casa> respuesta2 = casaRepository.findById(idCasa);
                if (respuesta2.isPresent()) {
                    Casa casa = respuesta2.get();

                    estancia.setCasa(casa); // Analizar logica para cambiar datos de la casa
                    estancia.setCliente(cliente); // Analizar logica para cambiar datos del cliente
                    estancia.setFechaDesde(fechaDesde);
                    estancia.setFechaHasta(fechaHasta);
                    estancia.setHuesped(huesped)
                            ;
                    estanciaRepository.save(estancia);
                } else {
                    throw new Error("No se encontró la casa solicitada.");
                }
            } else {
                throw new Error("No se encontró el cliente solicitado.");
            }
        } else {
            throw new Error("No se encontró la estancia/reserva solicitada.");
        }
    }

    @Transactional
    public void eliminar(String id) throws Error {

        Optional<Estancia> respuesta = estanciaRepository.findById(id);
        if (respuesta.isPresent()) {
            
            Estancia estancia = respuesta.get();

            estanciaRepository.deleteById(estancia.getId());

        } else {
            throw new Error("No se encontró la estancia/reserva solicitada.");
        }

    }

    private void validar(String huesped, Date fechaDesde, Date fechaHasta) {

        if (huesped == null || huesped.trim().isEmpty()) {
            throw new Error("Debe indicar el nombre");
        }
        if (fechaDesde.after(fechaHasta) || fechaDesde.equals(fechaHasta)) {
            throw new Error("La fecha de arrivo no debe ser posterior o igual a la fecha de regreso");
        }
        if (fechaDesde == null || fechaDesde.toString().isEmpty()) {
            throw new Error("Se debe ingresar la fecha del arrivo");
        }
        if (fechaHasta == null || fechaHasta.toString().isEmpty()) {
            throw new Error("Se debe ingresar la fecha de regreso");
        }
        
        
        
    }
}
