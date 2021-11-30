package com.example.demo.services;

import com.example.demo.entities.Casa;
import com.example.demo.repositories.CasaRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CasaService {

    @Autowired
    private CasaRepository casaRepository;

    @Transactional
    public void registrar(String calle, String codPostal, String ciudad,
            String tipoVivienda, Date fechaDesde, Date fechaHasta, int numero,
            int minDias, int maxDias, double precio) throws Error {

        validar(calle, codPostal, ciudad, tipoVivienda, fechaDesde, fechaHasta, numero, minDias, maxDias, precio);

        Casa casa = new Casa();

        casa.setCalle(calle);
        casa.setCiudad(ciudad);
        casa.setCodPostal(codPostal);
        casa.setFechaDesde(fechaDesde);
        casa.setFechaHasta(fechaHasta);
        casa.setMaxDias(maxDias);
        casa.setMinDias(minDias);
        casa.setNumero(numero);
        casa.setPais(calle);
        casa.setPrecio(precio);
        casa.setTipoVivienda(tipoVivienda);

        casaRepository.save(casa);

    }

    @Transactional
    public void modificar(String id, String calle, String codPostal, String ciudad,
            String tipoVivienda, Date fechaDesde, Date fechaHasta, int numero,
            int minDias, int maxDias, double precio) throws Error {

        validar(calle, codPostal, ciudad, tipoVivienda, fechaDesde, fechaHasta, numero, minDias, maxDias, precio);

        Optional<Casa> respuesta = casaRepository.findById(id);
        if (respuesta.isPresent()) {
            Casa casa = respuesta.get();

            casa.setCalle(calle);
            casa.setCiudad(ciudad);
            casa.setCodPostal(codPostal);
            casa.setFechaDesde(fechaDesde);
            casa.setFechaHasta(fechaHasta);
            casa.setMaxDias(maxDias);
            casa.setMinDias(minDias);
            casa.setNumero(numero);
            casa.setPais(calle);
            casa.setPrecio(precio);
            casa.setTipoVivienda(tipoVivienda);

            casaRepository.save(casa);
        } else {
            throw new Error("No se encontró la casa solicitada.");
        }
    }

    @Transactional
    public void baja(String id) throws Error {

        Optional<Casa> respuesta = casaRepository.findById(id);
        if (respuesta.isPresent()) {
            Casa casa = respuesta.get();
            casaRepository.save(casa);
        } else {
            throw new Error("No se encontró la casa solicitada.");
        }

    }

    private void validar(String calle, String codPostal, String ciudad,
            String tipoVivienda, Date fechaDesde, Date fechaHasta, int numero,
            int minDias, int maxDias, double precio) {

        if (calle == null || calle.trim().isEmpty()) {
            throw new Error("Debe indicar la calle");
        }
        if (codPostal == null || codPostal.trim().isEmpty()) {
            throw new Error("Debe indicar el codigo postal");
        }
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new Error("Debe indicar la ciudad");
        }
        if (tipoVivienda == null || tipoVivienda.trim().isEmpty()) {
            throw new Error("Debe indicar el tipo de vivienda");
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
        if (minDias < 1 || maxDias < 1 || maxDias == minDias) {
            throw new Error("Los dias maximos y minimos no pueden ser cero, tampoco iguales");
        }
        if (precio < 100) {
            throw new Error("El precio debe ser mayor a 100");
        }
    }

    @Transactional
    public List<Casa> listarCasas() {

        List<Casa> casas = casaRepository.findAll();
        if (casas != null) {
            return casas;
        } else {
            throw new Error("No se encontraron casas para mostrar");
        }

    }
}
