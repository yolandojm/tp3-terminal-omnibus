package documentos;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class ListaDeEspera<T> implements Iterable<T> {
    private Queue<T> elementos;
    private int capacidadMaxima;

    public ListaDeEspera(int capacidadMaxima) {
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a 0.");
        }
        this.elementos = new LinkedList<>();
        this.capacidadMaxima = capacidadMaxima;
    }

    public boolean agregar(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("No se puede agregar un elemento nulo.");
        }
        if (elementos.size() >= capacidadMaxima) {
            return false;
        }
        elementos.offer(elemento);
        return true;
    }

    public T entregarSiguiente() {
        if (estaVacia()) {
            throw new NoSuchElementException("La lista de espera está vacía.");
        }
        return elementos.poll();
    }

    public int cantidad() {
        return elementos.size();
    }

    public boolean estaVacia() {
        return elementos.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return elementos.iterator();
    }
}