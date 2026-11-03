package documentos;

import java.util.ArrayList;

public class Servicio {
    private String codigo;
    private String empresa;
    private String destino;
    private String horarioSalida;
    private double precio;
    private int cantidadAsientos;

    private ArrayList<Pasaje> asientos;

    private ListaDeEspera<Pasajero> listaEspera;

    public Servicio(String codigo, String empresa, String destino, String horarioSalida, double precio,
            int cantidadAsientos) {
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

        this.listaEspera = new ListaDeEspera<>(5);
    }

    public boolean estaLleno() {
        for (Pasaje p : asientos) {
            if (p == null)
                return false;
        }
        return true;
    }

    public Pasaje venderPasaje(Pasajero pasajero, String asientoPedido) {
        if (asientoPedido.equals("*")) {
            if (estaLleno()) {

                boolean enEspera = listaEspera.agregar(pasajero);
                if (enEspera) {
                    return null;
                } else {
                    throw new IllegalStateException("El servicio y la lista de espera (máx 5) están llenos.");
                }
            } else {
                for (int i = 0; i < cantidadAsientos; i++) {
                    if (asientos.get(i) == null) {
                        Pasaje p = new Pasaje(pasajero, this, i + 1);
                        asientos.set(i, p);
                        return p;
                    }
                }
            }
        } else {
            int numeroAsiento = Integer.parseInt(asientoPedido);
            int indice = numeroAsiento - 1;

            if (indice < 0 || indice >= cantidadAsientos)
                throw new IllegalArgumentException("Asiento inválido.");
            if (asientos.get(indice) != null)
                throw new IllegalStateException("El asiento " + numeroAsiento + " ya está ocupado.");

            Pasaje p = new Pasaje(pasajero, this, numeroAsiento);
            asientos.set(indice, p);
            return p;
        }
        return null;
    }

    public Pasaje devolverPasaje(int numeroAsiento) {
        int indice = numeroAsiento - 1;
        if (indice < 0 || indice >= cantidadAsientos)
            throw new IllegalArgumentException("Asiento inválido.");
        if (asientos.get(indice) == null)
            throw new IllegalStateException("Asiento vacío.");

        asientos.set(indice, null);

        if (!listaEspera.estaVacia()) {
            Pasajero p = listaEspera.entregarSiguiente();
            Pasaje nuevoPasaje = new Pasaje(p, this, numeroAsiento);
            asientos.set(indice, nuevoPasaje);
            return nuevoPasaje;
        }
        return null;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public double getPrecio() {
        return precio;
    }

    public ArrayList<Pasaje> getAsientos() {
        return asientos;
    }

    public ListaDeEspera<Pasajero> getListaEspera() {
        return listaEspera;
    }
}