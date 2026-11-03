TP3 programacion 2.
Prof: Augusto Fernandez Carbonell.
Alumnos: Juan Mariano Yolando, Alan Seoane.


COMPILACION:
Compilar javac -d bin documentos/*.java
Ejecutar java -cp bin documentos.TerminalMain


PREGUNTAS:
1. Excepción elegida: ServicioNoEncontradoException (la hicimos checked, heredando de Exception).
Porque nuestra terminal los codigos de micros vienen de un archivo txt, ventas.txt.
Al hacerla checked, el compilador de Java nos obliga sí o sí a escribir el bloque catch ServicioNoEncontradoException e en el método procesarArchivo. Esto nos asegura que cuando un micro no exista, el programa no se caiga de golpe, sino que capture el error, sume 1 a los renglones rechazados
2. Habríamos tenido que programar dos clases casi iguales, una llamada ListaDeEsperaPasajero y otra ListaDeEsperaTaxi. Las dos tendrían exactamente los mismos métodos (agregar, entregarSiguiente, estaVacia, cantidad), repitiendo código sin sentido.
Hacer una lista de Object.
Hacer una sola ListaDeEspera que guarde Object pero ahí perdíamos la seguridad de tipos.
Al sacar un elemento con entregarSiguiente(), nos devolvía un Object y teníamos que castearlo a mano: (Pasajero) lista.entregarSiguiente().
Si por error metíamos un Taxi en la lista del micro, el compilador no se daba cuenta y el programa explotaba recién al ejecutarse con un error de casteo (ClassCastException).
3. Por el contrato de Java entre equals y hashCode.
La regla de Java dice que si dos objetos son iguales según equals, obligatoriamente tienen que devolver el mismo número en hashCode.
Para saber si un pasajero ya está adentro primero mira su hashCode para buscarlo en la tabla hash. Si dos pasajeros tienen el mismo DNI pero dejamos el hashCode por defecto.