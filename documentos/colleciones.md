# Elección y Justificación de Colecciones (Parte B.1)

En el diseño del sistema de la Terminal de Ómnibus, cada colección fue seleccionada en función de la **operación más frecuente** que realiza sobre los datos, priorizando la eficiencia algorítmica y la coherencia del modelo de dominio.

---

### Tabla Comparativa de Colecciones (B.1)

| Requisito / Entidad | Operación más frecuente | Colección Elegida | Complejidad | Alternativas Descartadas | Justificación |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **1. Servicios en la Terminal** | Búsqueda por código (`"NEC-MDP-0700"`) en cada venta o consulta. | `HashMap<String, Servicio>` | $O(1)$ promedio | `ArrayList<Servicio>` ($O(n)$) | La terminal consulta servicios continuamente a partir de su código. Un `HashMap` permite acceso directo en tiempo constante ($O(1)$) mediante la clave única del código, evitando recorrer secuencialmente una lista. |
| **2. Pasajes y Asientos del Servicio** | Acceso, asignación y liberación por número de asiento (ej: asiento 3). | `ArrayList<Pasaje>` | $O(1)$ directo | `LinkedList<Pasaje>` ($O(n)$ acceso)<br>`HashSet<Pasaje>` (sin orden) | Los asientos son numerados y correlativos (del 1 al $N$). Un `ArrayList` preinicializado modela los casilleros de los asientos, permitiendo verificar si está libre (`null`), ocupar o liberar en tiempo $O(1)$ mediante el índice `numeroAsiento - 1`. |
| **3. Lista de Espera del Servicio** | Encolar pasajeros cuando el micro se llena y desencolar al primero cuando se libera un asiento. | `Queue<T>` / `LinkedList<T>` (encapsulada en `ListaDeEspera<T>`) | $O(1)$ encolar y desencolar | `ArrayList<T>` ($O(n)$ al remover del inicio) | La lista de espera debe cumplir estrictamente la política **FIFO** (*First-In, First-Out*): el primer pasajero que llegó a la espera es el primero en ser atendido. Una cola basada en `LinkedList` inserta (`offer`) y extrae (`poll`) en $O(1)$ sin necesidad de desplazar elementos en memoria. |
| **4. Recaudación por Empresa** | Acumulación por nombre de empresa y consulta final de totales. | `HashMap<String, Double>` | $O(1)$ actualización y búsqueda | `ArrayList<ParEmpresa>` ($O(n)$) | Permite acumular el total recaudado por cada empresa de forma directa: al vender o devolver un pasaje se actualiza el saldo de la empresa en $O(1)$ asociando el nombre de la empresa como clave. |
| **5. Parada de Taxis (Segundo uso genérico)** | Llegada de taxis y asignación en orden de llegada al pasajero. | `ListaDeEspera<Taxi>` | $O(1)$ | `Stack<Taxi>` (LIFO incorrecto) | Los taxis hacen fila física en la dársena de la terminal; el primer taxi en llegar es el primero en salir con un pasajero (FIFO). La misma abstracción genérica `ListaDeEspera<T>` resuelve este requerimiento a la perfección. |

---

### Criterio de Búsquedas y Manejo de Ausencia
- Todas las operaciones de búsqueda que pueden legítimamente no encontrar un resultado (como `buscarPasaje` o `buscarServicio`) **lo declaran en su tipo de retorno** mediante `Optional<T>`.
- **Nunca se devuelve `null`**, obligando al consumidor del método a contemplar explícitamente el caso en que el elemento buscado no exista (utilizando `ifPresent`, `isEmpty`, o `.orElseThrow()`).