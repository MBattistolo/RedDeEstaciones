package interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo.*;

//pestaña para estaciones

public class PanelEstaciones extends JPanel implements Refrescable {

	private static final long serialVersionUID = 1L;

	private Red red;
	private DefaultTableModel modeloTabla;
	private JTable tabla;

	private JTextField txtCodigo = new JTextField();
	private JTextField txtNombre = new JTextField();
	private JTextField txtComuna = new JTextField();
	private JTextField txtFecha = new JTextField();

	PanelEstaciones(Red red) {

		this.red = red;
		setLayout(new BorderLayout(5, 5));

		modeloTabla = new DefaultTableModel(new String[] { "Codigo", "Nombre", "Comuna",
				"Instalacion", "Mediciones" }, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};

		tabla = new JTable(modeloTabla);
		add(new JScrollPane(tabla), BorderLayout.CENTER);
		add(construirFormulario(), BorderLayout.SOUTH);

		refrescar();
	}

	private JPanel construirFormulario() {

		JPanel campos = new JPanel(new GridLayout(4, 2, 5, 5));
		campos.add(new JLabel("Codigo:"));
		campos.add(txtCodigo);
		campos.add(new JLabel("Nombre:"));
		campos.add(txtNombre);
		campos.add(new JLabel("Comuna:"));
		campos.add(txtComuna);
		campos.add(new JLabel("Fecha instalacion (aaaa-mm-dd):"));
		campos.add(txtFecha);

		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregar();
			}
		});

		JButton btnEliminar = new JButton("Eliminar seleccionada");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminar();
			}
		});

		JPanel botones = new JPanel(new GridLayout(1, 2, 5, 5));
		botones.add(btnAgregar);
		botones.add(btnEliminar);

		JPanel formulario = new JPanel(new BorderLayout(5, 5));
		formulario.setBorder(BorderFactory.createTitledBorder("Estacion"));
		formulario.add(campos, BorderLayout.CENTER);
		formulario.add(botones, BorderLayout.SOUTH);
		return formulario;
	}

	private void agregar() {

		try {
			//parse exige el formato ISO, cualquier otro lanza DateTimeParseException
			LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());

			red.addEstacion(txtCodigo.getText().trim(), txtNombre.getText().trim(),
					txtComuna.getText().trim(), fecha);

			limpiar();
			refrescar();

		} catch (DateTimeParseException ex) {
			Dialogos.error(this, "La fecha debe tener el formato aaaa-mm-dd");
		} catch (Exception ex) {
			Dialogos.error(this, ex.getMessage());
		}
	}

	private void eliminar() {

		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			Dialogos.error(this, "Seleccione una estacion de la tabla");
			return;
		}

		//eliminar una estacion arrastra sus mediciones, por eso se confirma
		int respuesta = JOptionPane.showConfirmDialog(this,
				"Al eliminar la estacion se borran tambien sus mediciones. Continuar?",
				"Confirmar", JOptionPane.YES_NO_OPTION);

		if (respuesta != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			red.eliminarEstacion((String) modeloTabla.getValueAt(fila, 0));
			refrescar();
		} catch (Exception ex) {
			Dialogos.error(this, ex.getMessage());
		}
	}

	public void refrescar() {

		modeloTabla.setRowCount(0);

		for (Estacion e : red.getEstaciones()) {
			modeloTabla.addRow(new Object[] { e.getCodigo(), e.getNombre(), e.getComuna(),
					e.getFechaInstalacion(), e.getMediciones().length });
		}
	}

	private void limpiar() {
		txtCodigo.setText("");
		txtNombre.setText("");
		txtComuna.setText("");
		txtFecha.setText("");
	}
}