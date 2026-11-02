package documentos;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import documentos.Servicio;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

    public class Terminal {

        private HashMap<String, Servicio> servicios;

        // Constructor
        public Terminal() {
            servicios = new HashMap<>();
        }

        // Agregar un servicio a la terminal
        public void agregarServicio(Servicio servicio) {
            if (servicio == null) {
                throw new IllegalArgumentException("El servicio no puede ser nulo.");
            }

            if (servicios.containsKey(servicio.getCodigo())) {
                throw new IllegalArgumentException("Ya existe ese servicio.");
            }

            servicios.put(servicio.getCodigo(), servicio);
        }

        // Buscar un servicio por su codigo
        public Servicio buscarServicio(String codigo)
                throws ServicioNoEncontradoException {

            Servicio servicio = servicios.get(codigo);

            if (servicio == null) {
                throw new ServicioNoEncontradoException(
                        "No existe el servicio: " + codigo
                );
            }

            return servicio;
        }

        // Vender un pasaje
        public Pasaje venderPasaje(String codigo, Pasajero pasajero,
                                   String asientoPedido)
                throws ServicioNoEncontradoException {

            Servicio servicio = buscarServicio(codigo);

            if (pasajero == null) {
                throw new IllegalArgumentException("El pasajero no puede ser nulo.");
            }

            if (asientoPedido == null) {
                throw new IllegalArgumentException("El asiento no puede ser nulo.");
            }

            asientoPedido = asientoPedido.trim();

            int numeroAsiento;

            if (asientoPedido.equals("*")) {

                numeroAsiento = 1;

                while (numeroAsiento <= servicio.getAsientos().size()
                        && servicio.getAsientos().get(numeroAsiento - 1) != null) {
                    numeroAsiento++;
                }

                if (numeroAsiento > servicio.getAsientos().size()) {
                    numeroAsiento = 1;
                }

            } else {

                try {
                    numeroAsiento = Integer.parseInt(asientoPedido);
                } catch (NumberFormatException e) {
                    throw new VentaInvalidaException(
                            "El asiento debe ser un número o *."
                    );
                }
            }

            return servicio.venderPasaje(pasajero, numeroAsiento);
        }

        // Buscar un pasaje por DNI y codigo de servicio
        public Pasaje buscarPasaje(String dni, String codigo)
                throws ServicioNoEncontradoException {

            Servicio servicio = buscarServicio(codigo);

            for (Pasaje pasaje : servicio.getAsientos()) {

                if (pasaje != null &&
                        pasaje.getPasajero().getDni().equals(dni)) {

                    return pasaje;
                }
            }

            throw new NoSuchElementException(
                    "No se encontro un pasaje para el DNI " + dni
            );
        }

        // Informe de pasajeros de un servicio
        public void informePasajeros(String codigo)
                throws ServicioNoEncontradoException {

            Servicio servicio = buscarServicio(codigo);

            System.out.println("PASAJEROS DEL SERVICIO " + codigo);

            for (Pasaje pasaje : servicio.getAsientos()) {

                if (pasaje != null) {
                    System.out.println(
                            "Asiento " + pasaje.getNumeroAsiento()
                                    + ": " + pasaje.getPasajero().getNombre()
                    );
                }
            }

            System.out.println("LISTA DE ESPERA:");

            for (Pasajero pasajero : servicio.getListaEspera()) {
                System.out.println(pasajero.getNombre());
            }
        }

        // Informe de recaudacion por empresa
        public void informeRecaudacion() {

            HashMap<String, Double> recaudacion = new HashMap<>();

            for (Servicio servicio : servicios.values()) {

                int cantidadVendidos = 0;

                for (Pasaje pasaje : servicio.getAsientos()) {
                    if (pasaje != null) {
                        cantidadVendidos++;
                    }
                }

                double total = cantidadVendidos * servicio.getPrecio();

                String empresa = servicio.getEmpresa();

                recaudacion.put(
                        empresa,
                        recaudacion.getOrDefault(empresa, 0.0) + total
                );
            }

            System.out.println("RECAUDACION POR EMPRESA:");

            for (Map.Entry<String, Double> entrada : recaudacion.entrySet()) {
                System.out.println(
                        entrada.getKey() + ": $" + entrada.getValue()
                );
            }
        }

        public void procesarArchivo(String nombreArchivo) {

            int renglonesLeidos = 0;
            int ventasConcretadas = 0;
            int pasajerosEnEspera = 0;
            int renglonesRechazados = 0;

            try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {

                String renglon;

                while ((renglon = lector.readLine()) != null) {
                    renglonesLeidos++;

                    try {
                        String[] campos = renglon.split(";", -1);

                        if (campos.length != 4) {
                            throw new RenglonInvalidoException(
                                    "El renglón debe tener exactamente cuatro campos."
                            );
                        }

                        String codigo = campos[0].trim();
                        String dni = campos[1].trim();
                        String nombre = campos[2].trim();
                        String asiento = campos[3].trim();

                        Pasajero pasajero = new Pasajero(nombre, dni);

                        Pasaje pasaje = venderPasaje(codigo, pasajero, asiento);

                        if (pasaje == null) {
                            pasajerosEnEspera++;
                        } else {
                            ventasConcretadas++;
                        }

                    } catch (RenglonInvalidoException | VentaInvalidaException |
                             IllegalStateException | ServicioNoEncontradoException |
                             IllegalArgumentException e) {

                        renglonesRechazados++;

                        System.out.println("Renglón " + renglonesLeidos + ": " + e.getMessage());
                    }
                }

                System.out.println("--- INFORME FINAL ---");
                System.out.println("Renglones leídos: " + renglonesLeidos);
                System.out.println("Ventas concretadas: " + ventasConcretadas);
                System.out.println("Pasajeros en espera: " + pasajerosEnEspera);
                System.out.println("Renglones rechazados: " + renglonesRechazados);

            } catch (IOException e) {
                System.out.println("No se pudo leer el archivo: " + nombreArchivo);
            }
        }
    }


