package interfaz;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

//evita repetir el mismo JOptionPane en los cinco paneles
public class Dialogos {

	static void error(JPanel origen, String mensaje) {
		JOptionPane.showMessageDialog(origen, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}
}