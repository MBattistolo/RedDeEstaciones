# Monitoreo ambiental para una red de estaciones

Líder: David Ossa
Integrantes: Matias Battistolo, David Botero, Susana Marín

Simulación de una red de estaciones para llevar control del monitoreo ambiental. 
Existen mediciones de ruido y de aire registradas en cada estación, las cuales se revisan si son criticas bajo ciertas condiciones.
Se tiene registro del personal técnico que trabaja en la red junto a la información de cada estación existente. 

---

## Modelo del dominio

**Estaciones:** Puntos fijos de monitoreo, identificados por código y ubicados
en una comuna. Cada una acumula el histórico de mediciones que ha registrado.

**Mediciones:** Toda medición guarda quién la registró, cuándo, y el valor
medido. Existen dos tipos, y cada uno define su propio criterio de criticidad:

| Tipo | Dato adicional | Es crítica cuando |
|---|---|---|
| Aire | tipo de contaminante | el valor queda **estrictamente fuera** del rango de la norma |
| Ruido | jornada | el valor sale de [30, 65] dB **o** la jornada no es diurna |

Ese criterio se resuelve por polimorfismo: `Medicion` declara `critica()` como
abstracto y cada subclase lo implementa a su manera, de modo que la red puede
evaluar cualquier medición sin preguntar de qué tipo es.

**Tipos de contaminante:** Catálogo de referencia con los valores mínimo y
máximo permitidos por la norma. Ejemplo: PM2.5 entre 0 y 37 µg/m³.

**Personal técnico:** Quienes toman las mediciones. Cada medición conserva la
referencia a su autor.

---

## Decisiones de diseño

**Referencia compartida:** Una medición vive simultáneamente en el arreglo de
`Red` y en el de su `Estacion`, pero es **un solo objeto** referenciado dos
veces, no una copia. `Red` es la única clase que crea o elimina mediciones, y
lo hace en ambos arreglos dentro de la misma operación.

**Estrategia de borrado según el tipo de relación** No todos los `eliminar`
se comportan igual, y la diferencia sigue el diagrama:

| Se elimina | Razón |
|---|---|
| Estación | la medición pertenece a la estación, sin ella pierde su contexto físico |
| Personal | la medición solo lo referencia, el histórico debe conservar quién la tomó |
| Tipo de contaminante | su rango se necesita para poder evaluar la criticidad |

**Códigos únicos:** Los cinco métodos de adición validan que el código no
exista previamente, eso garantiza que un código identifica un solo objeto y que
las búsquedas nunca dejen registros inalcanzables

---

## Cómo correrlo

1. Clonar el repositorio
2. En Eclipse: **File → Import → Existing Projects into Workspace** y
   seleccionar la carpeta clonada
3. Verificar que `datos/` quede en la **raíz del proyecto**, al lado de `src/`
4. Ejecutar `src/Main.java`

### Puntos de entrada

| Clase | Qué hace |
|---|---|
| `Main` | Lanza la interfaz gráfica. Es el punto de entrada normal. |
| `MainDemo` | Prueba el modelo por consola: escribe los ficheros, los carga, corre los reportes y provoca las excepciones. Sirve para verificar el modelo sin pasar por la interfaz. |

---

## Persistencia

`Main` intenta cargar `datos/red.obj` (el estado guardado). Si no existe, carga
los cuatro ficheros de texto.

```
datos/
├── contaminantes.txt   ← datos iniciales, editables a mano
├── personal.txt
├── estaciones.txt
├── mediciones.txt
└── red.obj             ← estado guardado, lo genera el programa
```

El botón **Guardar estado** escribe `red.obj`. Los `.txt` nunca se modifican
desde la aplicación: son la semilla, no el estado de trabajo.

**Para volver a los datos originales:** borrar el archivo `datos/red.obj` y volver a correr.

### Formato de los ficheros

Campos separados por `;`, sin espacios alrededor. Decimales con punto. Fechas en
formato ISO `aaaa-mm-dd`.

```
contaminantes.txt   codigo;nombre;valorMinimo;valorMaximo
personal.txt        tipoDoc;numeroDoc;nombre;apellido;genero;direccion;telefono
estaciones.txt      codigo;nombre;comuna;fechaInstalacion
mediciones.txt      AIRE;codMedicion;codEstacion;documento;fecha;valor;codContaminante
                    RUIDO;codMedicion;codEstacion;documento;fecha;valor;jornada
```

El género es **un solo carácter** (`F`/`M`) y la jornada va en mayúscula
(`DIURNA` / `NOCTURNA`). El primer campo de `mediciones.txt` indica qué subclase
crear: el texto plano no guarda tipos, así que sin él no se podría distinguir si
el último campo es un contaminante o una jornada.

**Orden de carga:** contaminantes → personal → estaciones → mediciones. Las
mediciones referencian técnicos y contaminantes que deben existir previamente.

---

## Estructura del proyecto

```
src/
├── modelo/       lógica del dominio, validaciones y persistencia
├── interfaz/     ventana y paneles (Swing)
├── Main.java     lanza la interfaz
└── MainDemo.java prueba por consola
datos/            ficheros de datos
```

El paquete `modelo` no depende de `interfaz`: ninguna clase del dominio conoce
la existencia de la ventana.
