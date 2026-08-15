import java.io.*;
import java.time.LocalDate;

public class MainDemo {

	private static void escribir(String fichero, String... lineas) {

		FileWriter fw = null;
		BufferedWriter b = null;

		try {
			fw = new FileWriter(fichero);
			b = new BufferedWriter(fw);

			for (int i = 0; i < lineas.length; i++) {
				b.write(lineas[i]);
				b.newLine();
			}

		} catch (IOException e) {
			System.out.println("No se pudo escribir en el fichero " + fichero);

		} finally {
			if (b != null) {
				try {
					b.close();
					fw.close();
				} catch (IOException e) {
					System.out.println("No se pudo cerrar el fichero");
				}
			}
		}
	}

	private static void crearFicheros() {

		escribir("datos/contaminantes.txt",
				"PM25;Material particulado 2.5;0.0;37.0",
				"PM10;Material particulado 10;0.0;75.0");

		escribir("datos/personal.txt",
				"CC;1017253841;Mariana;Restrepo;F;Cra 43A #18-25;3104558812",
				"CC;1128476390;Julian;Ospina;M;Calle 37 Sur #45-12;3125589074");

		escribir("datos/estaciones.txt",
				"EST-ENV;Envigado Centro;Envigado;2025-03-14",
				"EST-SAB;Sabaneta La Doctora;Sabaneta;2025-06-02");

		//22.4 dentro de 0-37 normal, 48.9 supera el maximo critica
		//58.0 dentro de 30-65 y diurna normal, 71.5 supera 65 y es nocturna critica
		escribir("datos/mediciones.txt",
				"AIRE;MED-001;EST-ENV;1017253841;2026-08-10;22.4;PM25",
				"AIRE;MED-002;EST-ENV;1017253841;2026-08-11;48.9;PM25",
				"RUIDO;MED-003;EST-SAB;1128476390;2026-08-10;58.0;DIURNA",
				"RUIDO;MED-004;EST-SAB;1128476390;2026-08-11;71.5;NOCTURNA");

		System.out.println("Ficheros escritos correctamente");
	}

	public static void main(String[] args) {

		try {
			crearFicheros();

			Red red = new Red();
			red.cargarDesdeTexto("datos");
			System.out.println(red);

			for (Medicion m : red.getMediciones()) {
				System.out.println(m);
				System.out.println("Critica: " + m.critica() + "\n");
			}

			System.out.println("Estaciones con alguna medicion critica:");
			for (Estacion e : red.estacionesConMedicionCritica()) {
				System.out.println("  " + e.getCodigo() + " - " + e.getNombre());
			}

			System.out.println("\nEstaciones con PM25 critico:");
			for (Estacion e : red.estacionesConMedicionAireCritica("PM25")) {
				System.out.println("  " + e.getCodigo() + " - " + e.getNombre());
			}

			//cada excepcion en su propio try para que el programa siga despues del error
			System.out.println("\nExcepciones:");

			try {
				red.addEstacion("EST-ENV", "Repetida", "Envigado", LocalDate.now());
			} catch (ECodigoDuplicado e) {
				System.out.println("  " + e.getMessage());
			}

			try {
				red.estacionesConMedicionAireCritica("SO2");
			} catch (ECodigoNoExiste e) {
				System.out.println("  " + e.getMessage());
			}

			try {
				red.eliminarPersonal("1017253841");
			} catch (EPersonalConMediciones e) {
				System.out.println("  " + e.getMessage());
			}

			try {
				red.eliminarTipoContaminante("PM25");
			} catch (ETipoContaminanteEnUso e) {
				System.out.println("  " + e.getMessage());
			}

			//se guarda antes de eliminar para que el fichero tenga la red completa
			red.copiarFicheroRed("datos/red.obj");
			Red recuperada = new Red("datos/red.obj");
			System.out.println("\nEstado recuperado:");
			System.out.println(recuperada);

			//el == compara referencias, si dan true la serializacion conservo el alias
			Medicion enRed = recuperada.getMediciones()[0];
			Medicion enEstacion = recuperada.getEstaciones()[0].getMediciones()[0];
			System.out.println("Alias conservado: " + (enRed == enEstacion));

			//al eliminar la estacion sus mediciones tambien salen del arreglo de Red
			System.out.println("\nAntes de eliminar EST-SAB -> estaciones: " + red.getEstaciones().length
					+ ", mediciones: " + red.getMediciones().length);
			red.eliminarEstacion("EST-SAB");
			System.out.println("Despues -> estaciones: " + red.getEstaciones().length
					+ ", mediciones: " + red.getMediciones().length);

		} catch (IOException e) {
			System.out.println("Error de lectura/escritura: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("La clase serializada no coincide: " + e.getMessage());
		} catch (ECodigoDuplicado e) {
			System.out.println("Codigo repetido: " + e.getMessage());
		} catch (ECodigoNoExiste e) {
			System.out.println("Referencia invalida: " + e.getMessage());
		}
	}
}