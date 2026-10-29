public class Pasaje {
    private Pasajero pasajero;
    private Servicio servicio;
    private int numeroAsiento;

    public Pasaje(Pasajero pasajero, Servicio servicio, int numeroAsiento) {
        setPasajero(pasajero);
        setServicio(servicio);
        setNumeroAsiento(numeroAsiento);
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        if (pasajero == null) {
            throw new IllegalArgumentException("El pasajero no puede ser nulo.");
        }
        this.pasajero = pasajero;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio no puede ser nulo.");
        }
        this.servicio = servicio;
    }

    public int getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(int numeroAsiento) {
        if (numeroAsiento <= 0) {
            throw new IllegalArgumentException("El número de asiento debe ser mayor a cero.");
        }
        this.numeroAsiento = numeroAsiento;
    }

    @Override
    public String toString() {
        return "Pasaje [Servicio: " + servicio.getCodigo() +
                " | Asiento: " + numeroAsiento +
                " | Pasajero: " + pasajero.getNombre() +
                " (DNI: " + pasajero.getDni() + ")]";
    }
}