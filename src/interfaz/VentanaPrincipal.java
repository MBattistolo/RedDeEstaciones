package interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modelo.*;


//Ventana principal de la aplicacion. Contiene la unica instancia de Red y se la pasa por constructor a cada panel, de modo que los cinco trabajan
//sobre el mismo objeto y cualquier cambio hecho en uno se refleja en los otros
 
public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private Red red;
	private JTabbedPane pestanas;

	public VentanaPrincipal(Red red) {

		this.red = red;

		setTitle("Red de monitoreo ambiental");
		setSize(1250, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		pestanas = new JTabbedPane();
		pestanas.addTab("Contaminantes", new PanelContaminantes(red));
		pestanas.addTab("Personal", new PanelPersonal(red));
		pestanas.addTab("Estaciones", new PanelEstaciones(red));
		pestanas.addTab("Mediciones", new PanelMediciones(red));
		pestanas.addTab("Reportes", new PanelReportes(red));

		//los combos de mediciones y reportes dependen de lo que exista en las otras pestañas, por eso se recargan cada vez que el usuario entra
		//a una de ellas
		pestanas.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (pestanas.getSelectedComponent() instanceof Refrescable) {
					((Refrescable) pestanas.getSelectedComponent()).refrescar();
				}
			}
		});

		add(pestanas, BorderLayout.CENTER);
		add(construirBarraInferior(), BorderLayout.SOUTH);
	}

	private JPanel construirBarraInferior() {

		JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnGuardar = new JButton("Guardar estado");

		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					red.copiarFicheroRed("datos/red.obj");
					JOptionPane.showMessageDialog(VentanaPrincipal.this,
							"Estado guardado en datos/red.obj");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(VentanaPrincipal.this,
							"No se pudo guardar: " + ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		barra.add(btnGuardar);
		return barra;
	}
}