package documentos;

public class Taxi {
    private String patente;

    public Taxi(String patente) {
        if (patente == null || patente.isBlank()) {
            throw new IllegalArgumentException("La patente no puede ser nula ni vacía.");
        }
        this.patente = patente;
    }

    public String getPatente() {
        return patente;
    }
}