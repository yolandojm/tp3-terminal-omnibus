package documentos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Terminal {
    private HashMap<String, Servicio> servicios;
    private HashMap<String, Double> recaudacionPorEmpresa;
    private ListaDeEspera<Taxi> paradaTaxis;

    public Terminal() {
        this.servicios = new HashMap<>();
        this.recaudacionPorEmpresa = new HashMap<>();
        this.paradaTaxis = new ListaDeEspera<>(10);
    }

    public void agregarServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio no puede ser nulo.");
        }
        if (servicios.containsKey(servicio.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un servicio con el código " + servicio.getCodigo());
        }
        servicios.put(servicio.getCodigo(), servicio);
        if (!recaudacionPorEmpresa.containsKey(servicio.getEmpresa())) {
            recaudacionPorEmpresa.put(servicio.getEmpresa(), 0.0);
        }
    }

    public Optional<Servicio> buscarServicio(String codigo) {
        if (codigo == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(servicios.get(codigo.trim()));
    }

    public Servicio obtenerServicio(String codigo) throws ServicioNoEncontradoException {
        return buscarServicio(codigo)
                .orElseThrow(() -> new ServicioNoEncontradoException("No existe el servicio: " + codigo));
    }

    public Pasaje venderPasaje(String codigoServicio, Pasajero pasajero, String asientoPedido)
            throws ServicioNoEncontradoException {
        Servicio s = obtenerServicio(codigoServicio);
        Pasaje pasajeVendido = s.venderPasaje(pasajero, asientoPedido);

        if (pasajeVendido != null) {
            double actual = recaudacionPorEmpresa.getOrDefault(s.getEmpresa(), 0.0);
            recaudacionPorEmpresa.put(s.getEmpresa(), actual + s.getPrecio());
        }
        return pasajeVendido;
    }

    public Pasaje venderPasaje(String codigoServicio, Pasajero pasajero, int numeroAsiento)
            throws ServicioNoEncontradoException {
        return venderPasaje(codigoServicio, pasajero, String.valueOf(numeroAsiento));
    }

    public Pasaje devolverPasaje(String codigoServicio, int numeroAsiento)
            throws ServicioNoEncontradoException {
        Servicio s = obtenerServicio(codigoServicio);
        Pasaje pasajeReasignado = s.devolverPasaje(numeroAsiento);

        if (pasajeReasignado == null) {
            double actual = recaudacionPorEmpresa.getOrDefault(s.getEmpresa(), 0.0);
            recaudacionPorEmpresa.put(s.getEmpresa(), Math.max(0.0, actual - s.getPrecio()));
        }
        return pasajeReasignado;
    }

    public Optional<Pasaje> buscarPasaje(String dni, String codigoServicio)
            throws ServicioNoEncontradoException {
        Servicio s = obtenerServicio(codigoServicio);

        if (dni == null || dni.isBlank()) {
            return Optional.empty();
        }

        for (Pasaje p : s.getAsientos()) {
            if (p != null && p.getPasajero().getDni().equals(dni)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public String informePasajeros(String codigoServicio)
            throws ServicioNoEncontradoException {
        Servicio s = obtenerServicio(codigoServicio);

        StringBuilder sb = new StringBuilder();
        sb.append("--- PASAJEROS DEL SERVICIO ").append(codigoServicio).append(" ---\n");
        sb.append(">> Asientos Ocupados:\n");
        for (Pasaje p : s.getAsientos()) {
            if (p != null) {
                sb.append("Asiento ").append(p.getNumeroAsiento()).append(": ")
                        .append(p.getPasajero().getNombre()).append(" (DNI: ").append(p.getPasajero().getDni())
                        .append(")\n");
            }
        }

        sb.append("\n>> Lista de Espera (").append(s.getListaEspera().cantidad()).append(" pasajeros):\n");
        for (Pasajero p : s.getListaEspera()) {
            sb.append("- ").append(p.getNombre()).append(" (DNI: ").append(p.getDni()).append(")\n");
        }

        return sb.toString();
    }

    public Map<String, Double> informeRecaudacion() {
        return recaudacionPorEmpresa;
    }

    public void registrarLlegadaTaxi(Taxi taxi) {
        if (taxi == null) {
            throw new IllegalArgumentException("El taxi no puede ser nulo.");
        }
        boolean pudoIngresar = paradaTaxis.agregar(taxi);
        if (!pudoIngresar) {
            System.out.println("La parada de taxis está llena. El taxi " + taxi.getPatente() + " debe retirarse.");
        }
    }

    public Taxi asignarTaxiAPasajero() {
        return paradaTaxis.entregarSiguiente();
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
                                "El renglón debe tener exactamente cuatro campos.");
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

                } catch (RenglonInvalidoException | VentaInvalidaException | IllegalStateException
                        | ServicioNoEncontradoException | IllegalArgumentException e) {

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