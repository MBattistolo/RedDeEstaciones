import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import interfaz.VentanaPrincipal;
import modelo.Red;


 //punto de entrada de la aplicación, prepara la Red y lanza la ventana

 //si existe un estado guardado se recupera, y si no se arranca desde los ficheros de texto de la carpeta datos
 
public class Main {

	public static void main(String[] args) {

		final Red red = prepararRed();

		//las ventanas de Swing deben construirse en su propio hilo
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new VentanaPrincipal(red).setVisible(true);
			}
		});
	}

	private static Red prepararRed() {

		try {
			if (new File("datos/red.obj").exists()) {
				return new Red("datos/red.obj");
			}

			Red red = new Red();
			red.cargarDesdeTexto("datos");
			return red;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"No se pudieron cargar los datos: " + e.getMessage()
							+ "\nLa aplicacion inicia vacia.",
					"Aviso", JOptionPane.WARNING_MESSAGE);
			return new Red();
		}
	}
}