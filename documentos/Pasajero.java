public class Pasajero {
    private String nombre;
    private String dni;

    public Pasajero(String nombre, String dni) {
        setNombre(nombre);
        setDni(dni);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (dni == null || dni.isBlank() || dni.startsWith("-"))
            throw new IllegalArgumentException("El DNI no puede ser nulo ni negativo");
        this.dni = dni;
    }

    // Metodo mismpo pasajero segun dni
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pasajero pasajero = (Pasajero) o;
        return dni.equals(pasajero.dni);
    }

    @Override
    public int hashCode() {
        return dni.hashCode();
    }
}