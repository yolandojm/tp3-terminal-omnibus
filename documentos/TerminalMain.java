package documentos;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TerminalMain {

    public static void main(String[] args) {
        Terminal terminal = new Terminal();

        terminal.agregarServicio(new Servicio("NEC-MDP-0700", "Empresa A", "Mar del Plata", "07:00", 5000.0, 4));
        terminal.agregarServicio(new Servicio("NEC-TRA-1400", "Empresa B", "Tres Arroyos", "14:00", 4000.0, 10));
        terminal.agregarServicio(new Servicio("NEC-BA-2200", "Empresa C", "Buenos Aires", "22:00", 15000.0, 20));
        terminal.agregarServicio(new Servicio("MDP-NEC-1000", "Empresa A", "Necochea", "10:00", 5000.0, 10));

        System.out.println("=================================================");
        System.out.println("     SISTEMA DE GESTIÓN - TERMINAL DE ÓMNIBUS    ");
        System.out.println("=================================================");

        System.out.println("\n>> 1. Procesando archivo 'ventas.txt'...");
        terminal.procesarArchivo("ventas.txt");

        System.out.println("\n>> 2. Intentando procesar un archivo que no existe...");
        terminal.procesarArchivo("archivo_que_no_existe.txt");

        System.out.println("\n>> 3. Devolución de pasaje de Bruno Ortiz en NEC-MDP-0700...");
        try {
            Optional<Pasaje> optPasajeBruno = terminal.buscarPasaje("28999111", "NEC-MDP-0700");
            if (optPasajeBruno.isPresent()) {
                Pasaje pasajeBruno = optPasajeBruno.get();
                System.out.println("Pasaje original de Bruno: " + pasajeBruno);

                Pasaje pasajeReasignado = terminal.devolverPasaje("NEC-MDP-0700", pasajeBruno.getNumeroAsiento());
                if (pasajeReasignado != null) {
                    System.out.println(">> ¡Asiento reasignado automáticamente!");
                    System.out.println("Nuevo pasaje emitido para: " + pasajeReasignado.getPasajero().getNombre()
                            + " (DNI: " + pasajeReasignado.getPasajero().getDni() + ")"
                            + " en el Asiento " + pasajeReasignado.getNumeroAsiento());
                } else {
                    System.out.println("El asiento quedó libre (no había pasajeros en lista de espera).");
                }
            } else {
                System.out.println("Bruno Ortiz no tenía un pasaje comprado.");
            }
        } catch (ServicioNoEncontradoException | IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error al procesar devolución: " + e.getMessage());
        }

        System.out.println("\n>> 4. Informes de la Parte B:");
        System.out.println("\n[Informe B.1: Pasajeros del Servicio NEC-MDP-0700]");
        try {
            System.out.println(terminal.informePasajeros("NEC-MDP-0700"));
        } catch (ServicioNoEncontradoException e) {
            System.out.println("Error en informe de pasajeros: " + e.getMessage());
        }

        System.out.println("[Informe B.2: Recaudación por Empresa]");
        for (Map.Entry<String, Double> entrada : terminal.informeRecaudacion().entrySet()) {
            System.out.println("- Empresa: " + entrada.getKey() + " | Total Recaudado: $" + entrada.getValue());
        }

        System.out.println("\n>> 5. Búsqueda de pasajes:");
        try {
            System.out.println("Buscando pasaje de Ana Molina (DNI 30111222) en NEC-TRA-1400...");
            Optional<Pasaje> pasajeAna = terminal.buscarPasaje("30111222", "NEC-TRA-1400");
            if (pasajeAna.isPresent()) {
                System.out.println("Resultado: " + pasajeAna.get());
            } else {
                System.out.println("No se encontró pasaje para Ana Molina.");
            }

            System.out.println("\nBuscando pasaje de un DNI inexistente (DNI 99999999) en NEC-TRA-1400...");
            Optional<Pasaje> pasajeFalso = terminal.buscarPasaje("99999999", "NEC-TRA-1400");
            if (pasajeFalso.isPresent()) {
                System.out.println("Resultado inesperado: " + pasajeFalso.get());
            } else {
                System.out.println(
                        "Búsqueda exitosa: No se encontró ningún pasaje (retornó Optional.empty, sin devolver null ni caerse).");
            }
        } catch (ServicioNoEncontradoException e) {
            System.out.println("Error: El servicio indicado no existe: " + e.getMessage());
        }

        System.out.println("\n>> 6. Demostración de ListaDeEspera con segundo tipo (Taxi):");
        terminal.registrarLlegadaTaxi(new Taxi("AB123CD"));
        terminal.registrarLlegadaTaxi(new Taxi("AF456GH"));
        try {
            Taxi taxi = terminal.asignarTaxiAPasajero();
            System.out.println("Taxi asignado al pasajero saliente: Patente " + taxi.getPatente());
        } catch (NoSuchElementException e) {
            System.out.println("No hay taxis disponibles en la parada: " + e.getMessage());
        }

        System.out.println("\n=================================================");
        System.out.println("            FIN DE LA EJECUCIÓN                  ");
        System.out.println("=================================================");
    }
}
