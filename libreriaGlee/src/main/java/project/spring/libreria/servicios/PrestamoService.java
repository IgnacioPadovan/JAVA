package project.spring.libreria.servicios;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.spring.libreria.controladores.PrestamoController;
import project.spring.libreria.entidades.Cliente;
import project.spring.libreria.entidades.Libro;
import project.spring.libreria.entidades.Prestamo;
import project.spring.libreria.repositorios.ClienteRepository;
import project.spring.libreria.repositorios.LibroRepository;
import project.spring.libreria.repositorios.PrestamoRepository;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public void altaPrestamo(String idLibro, String idCliente, Date fechaPrestamo, Date fechaDevolucion) {

        Optional<Libro> respuesta1 = libroRepository.findById(idLibro);
        if (respuesta1.isPresent()) {
            Libro libro = respuesta1.get();

            validar(fechaPrestamo, fechaDevolucion, libro);

            libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() - 1);
            libro.setEjemplaresUsados(libro.getEjemplaresUsados() + 1);

            Optional<Cliente> respuesta2 = clienteRepository.findById(idCliente);
            if (respuesta2.isPresent()) {
                Cliente cliente = respuesta2.get();

                Prestamo prestamo = new Prestamo();
                prestamo.setAlta(Boolean.TRUE);
                prestamo.setFechaPrestamo(fechaPrestamo);
                prestamo.setFechaDevolucion(fechaDevolucion);
                prestamo.setLibro(libro);
                prestamo.setCliente(cliente);

                prestamoRepository.save(prestamo);

            } else {
                throw new Error("No se encontró el cliente solicitado.");
            }
        } else {
            throw new Error("No se encontró el libro solicitado.");
        }
    }

    @Transactional
    public void bajaPrestamo(String id) throws Exception {

        Optional<Prestamo> respuesta = prestamoRepository.findById(id);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();

            Libro libro = prestamo.getLibro();
            libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() + 1);
            libro.setEjemplaresUsados(libro.getEjemplaresUsados() - 1);

            prestamo.setAlta(false);

            if (prestamo.getFechaDevolucion().before(new Date())) {
                prestamo.setFechaDevolucion(new Date());
                LocalDate fechaDevolucion
                        = Instant.ofEpochMilli(prestamo.getFechaDevolucion().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                Period periodo = Period.between(fechaDevolucion, LocalDate.now());

                throw new Error("El cliente ha devuelto el libro con tardanza de " + periodo.getDays() + " dias");
            }

            prestamoRepository.save(prestamo);
        } else {
            throw new Error("No se encontró el prestamo solicitado.");
        }

    }

    @Transactional
    public void editarPrestamo(String id, String idLibro, String idCliente, Date fechaDevolucionNueva) {

        Optional<Prestamo> respuesta = prestamoRepository.findById(id);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            if (prestamo.getLibro().getId().equalsIgnoreCase(idLibro) || prestamo.getCliente().getId().equalsIgnoreCase(idCliente)) {

                Libro libro = libroRepository.getById(idLibro);

                validar(prestamo.getFechaPrestamo(), fechaDevolucionNueva, libro);

                prestamo.setFechaDevolucion(fechaDevolucionNueva);
                prestamo.setCliente(clienteRepository.getById(idCliente));
                prestamo.setLibro(libro);
                prestamo.setAlta(Boolean.TRUE);

                prestamoRepository.save(prestamo);
            } else {
                throw new Error("El cliente o la libro no coincide con el prestamo seleccionado.");
            }

        } else {
            throw new Error("No se encontró el prestamo solicitado.");
        }

    }

    public List<Prestamo> buscarPrestamos() {

        return prestamoRepository.buscarPrestamos();

    }

    public Prestamo buscarPorId(String id) {

        Optional<Prestamo> respuesta = prestamoRepository.findById(id);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            return prestamo;
        } else {
            throw new Error("No se encontró el prestamo solicitado.");
        }

    }

    private void validar(Date fechaPrestamo, Date fechaDevolucion, Libro libro) {

        if (fechaPrestamo.after(fechaDevolucion) || fechaPrestamo.equals(fechaDevolucion)) {
            throw new Error("La fecha de prestamo no debe ser posterior o igual a la fecha de devolución");
        }
        if (fechaPrestamo == null || fechaPrestamo.toString().isEmpty()) {
            throw new Error("Se debe ingresar la fecha del prestamo");
        }
        if (fechaDevolucion == null || fechaDevolucion.toString().isEmpty()) {
            throw new Error("Se debe ingresar la fecha de devolución");
        }
        if (libro.getEjemplaresRestantes() <= 0) {
            throw new Error("No hay ejemplares disponibles para prestar. Pruebe con otro libro");
        }

    }

}
