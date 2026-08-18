package interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import modelo.*;

//pestaña para reportes

public class PanelReportes extends JPanel implements Refrescable {

	private static final long serialVersionUID = 1L;

	private Red red;
	private JTextArea areaResultado = new JTextArea();
	private JComboBox<String> cmbContaminante = new JComboBox<>();

	PanelReportes(Red red) {

		this.red = red;
		setLayout(new BorderLayout(5, 5));

		areaResultado.setEditable(false);
		add(new JScrollPane(areaResultado), BorderLayout.CENTER);
		add(construirBarra(), BorderLayout.NORTH);

		refrescar();
	}

	private JPanel construirBarra() {

		JButton btnInventario = new JButton("Inventario");
		btnInventario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				areaResultado.setText(red.toString());
			}
		});

		JButton btnCriticas = new JButton("Estaciones con medicion critica");
		btnCriticas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrar(red.estacionesConMedicionCritica());
			}
		});

		JButton btnPorContaminante = new JButton("Aire critico por contaminante");
		btnPorContaminante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				porContaminante();
			}
		});

		JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
		barra.setBorder(BorderFactory.createTitledBorder("Consultas"));
		barra.add(btnInventario);
		barra.add(btnCriticas);
		barra.add(new JLabel("  Contaminante:"));
		barra.add(cmbContaminante);
		barra.add(btnPorContaminante);
		return barra;
	}

	private void porContaminante() {

		TipoContaminante tc = (TipoContaminante) cmbContaminante.getSelectedItem();

		if (tc == null) {
			Dialogos.error(this, "No hay contaminantes registrados");
			return;
		}

		try {
			mostrar(red.estacionesConMedicionAireCritica(tc.getCodigo()));
		} catch (Exception ex) {
			areaResultado.setText(ex.getMessage());
		}
	}

	private void mostrar(Estacion[] estaciones) {

		if (estaciones.length == 0) {
			areaResultado.setText("Ninguna estacion cumple la condicion.");
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (Estacion e : estaciones) {
			sb.append(e.getCodigo()).append(" - ").append(e.getNombre()).append(" (")
					.append(e.getComuna()).append(")\n");
		}
		areaResultado.setText(sb.toString());
	}

	public void refrescar() {

		cmbContaminante.removeAllItems();
		for (TipoContaminante t : red.getTiposContaminante()) {
			cmbContaminante.addItem(t.getCodigo());
		}
	}
}