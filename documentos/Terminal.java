package documentos;

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
        public Servicio buscarServicio(String codigo) {
            Servicio servicio = servicios.get(codigo);

            if (servicio == null) {
                throw new NoSuchElementException(
                        "No existe el servicio: " + codigo
                );
            }

            return servicio;
        }

        // Vender un pasaje
        public Pasaje venderPasaje(String codigo, Pasajero pasajero,
                                   String asientoPedido) {

            Servicio servicio = buscarServicio(codigo);

            if (pasajero == null) {
                throw new IllegalArgumentException("El pasajero no puede ser nulo.");
            }

            if (asientoPedido == null) {
                throw new IllegalArgumentException("El asiento no puede ser nulo.");
            }

            asientoPedido = asientoPedido.trim();

            int numeroAsiento;

            // Si pide cualquier asiento (*), buscamos el primero libre
            if (asientoPedido.equals("*")) {

                numeroAsiento = 1;

                while (numeroAsiento <= servicio.getAsientos().size()
                        && servicio.getAsientos().get(numeroAsiento - 1) != null) {
                    numeroAsiento++;
                }

                // Si no hay asientos libres, Servicio lo manda a espera
                if (numeroAsiento > servicio.getAsientos().size()) {
                    numeroAsiento = 1;
                }

            } else {

                try {
                    numeroAsiento = Integer.parseInt(asientoPedido);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            "El asiento debe ser un numero o *."
                    );
                }
            }

            return servicio.venderPasaje(pasajero, numeroAsiento);
        }

        // Buscar un pasaje por DNI y codigo de servicio
        public Pasaje buscarPasaje(String dni, String codigo) {

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
        public void informePasajeros(String codigo) {

            Servicio servicio = buscarServicio(codigo);

            System.out.println("PASAJEROS DEL SERVICIO " + codigo);

            // Los asientos ya están ordenados en el ArrayList
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
    }


