package documentos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Servicio {
        private String codigo;
        private String empresa;
        private String destino;
        private String horarioSalida;
        private double precio;
        private int cantidadAsientos;
    
        private ArrayList<Pasaje> asientos;
        private Queue<Pasajero> listaEspera;
    
        public Servicio(String codigo, String empresa, String destino, String horarioSalida, double precio, int cantidadAsientos) {
            if (cantidadAsientos <= 0) {
                throw new IllegalArgumentException("El servicio debe tener al menos 1 asiento.");
            }
            this.codigo = codigo;
            this.empresa = empresa;
            this.destino = destino;
            this.horarioSalida = horarioSalida;
            this.precio = precio;
            this.cantidadAsientos = cantidadAsientos;
            this.asientos = new ArrayList<>(cantidadAsientos);
            for (int i = 0; i < cantidadAsientos; i++) {
                this.asientos.add(null); 
            }
            
            this.listaEspera = new LinkedList<>();
        }
    

        public boolean estaLleno() {
            for (Pasaje asiento : asientos) {
                if (asiento == null) {
                    return false; 
                }
            }
            return true; 
        }
    
        public Pasaje venderPasaje(Pasajero pasajero, int numeroAsiento) {
            if (estaLleno()) {
                if (listaEspera.size() < 5) {
                    listaEspera.offer(pasajero);
                    System.out.println("Servicio lleno. " + pasajero.getNombre() + " ingresó a la lista de espera.");
                    return null; 
                } else {
                    throw new IllegalStateException("El servicio y la lista de espera (máx 5) están llenos.");
                }
            }
    

            int indice = numeroAsiento - 1;
            if (indice < 0 || indice >= cantidadAsientos) {
                throw new IllegalArgumentException("Número de asiento inválido. Debe estar entre 1 y " + cantidadAsientos);
            }
    
            if (asientos.get(indice) != null) {
                throw new IllegalStateException("El asiento " + numeroAsiento + " ya se encuentra ocupado.");
            }
    
           
            Pasaje nuevoPasaje = new Pasaje(pasajero, this, numeroAsiento);
            asientos.set(indice, nuevoPasaje);
            return nuevoPasaje;
        }
    
        public Pasaje devolverPasaje(int numeroAsiento) {
            int indice = numeroAsiento - 1;
            
          
            if (indice < 0 || indice >= cantidadAsientos) {
                throw new IllegalArgumentException("Número de asiento inválido.");
            }
    
          
            if (asientos.get(indice) == null) {
                throw new IllegalStateException("No hay un pasaje vendido en el asiento " + numeroAsiento);
            }
    
            
            asientos.set(indice, null);
            System.out.println("Se devolvió el pasaje. El asiento " + numeroAsiento + " quedó liberado.");
    
            
            if (!listaEspera.isEmpty()) {
                
                Pasajero pasajeroAfortunado = listaEspera.poll(); 
                
                
                Pasaje nuevoPasajeReasignado = new Pasaje(pasajeroAfortunado, this, numeroAsiento);
                asientos.set(indice, nuevoPasajeReasignado);
                
                System.out.println("El asiento " + numeroAsiento + " fue asignado automáticamente a " + pasajeroAfortunado.getNombre() + " (proveniente de la lista de espera).");
                
                
                return nuevoPasajeReasignado;
            }
    
            return null; 
        }
    
       
        public String getCodigo() { return codigo; }
        public String getEmpresa() { return empresa; }
        public String getDestino() { return destino; }
        public String getHorarioSalida() { return horarioSalida; }
        public double getPrecio() { return precio; }
        public ArrayList<Pasaje> getAsientos() { return asientos; }
        public Queue<Pasajero> getListaEspera() { return listaEspera; }
}
