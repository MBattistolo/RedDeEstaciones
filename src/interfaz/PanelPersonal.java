package interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo.*;

//  pestaña para personal técnico

public class PanelPersonal extends JPanel implements Refrescable {

	private static final long serialVersionUID = 1L;

	private Red red;
	private DefaultTableModel modeloTabla;
	private JTable tabla;

	private JTextField txtTipoDoc = new JTextField();
	private JTextField txtDocumento = new JTextField();
	private JTextField txtNombre = new JTextField();
	private JTextField txtApellido = new JTextField();
	private JTextField txtGenero = new JTextField();
	private JTextField txtDireccion = new JTextField();
	private JTextField txtTelefono = new JTextField();

	PanelPersonal(Red red) {

		this.red = red;
		setLayout(new BorderLayout(5, 5));

		modeloTabla = new DefaultTableModel(new String[] { "Tipo doc", "Documento",
				"Nombre", "Apellido", "Genero", "Direccion", "Telefono" }, 0) {
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

		JPanel campos = new JPanel(new GridLayout(4, 4, 5, 5));
		campos.add(new JLabel("Tipo documento:"));
		campos.add(txtTipoDoc);
		campos.add(new JLabel("Numero documento:"));
		campos.add(txtDocumento);
		campos.add(new JLabel("Nombre:"));
		campos.add(txtNombre);
		campos.add(new JLabel("Apellido:"));
		campos.add(txtApellido);
		campos.add(new JLabel("Genero (F/M):"));
		campos.add(txtGenero);
		campos.add(new JLabel("Direccion:"));
		campos.add(txtDireccion);
		campos.add(new JLabel("Telefono:"));
		campos.add(txtTelefono);

		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregar();
			}
		});

		JButton btnEliminar = new JButton("Eliminar seleccionado");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminar();
			}
		});

		JPanel botones = new JPanel(new GridLayout(1, 2, 5, 5));
		botones.add(btnAgregar);
		botones.add(btnEliminar);

		JPanel formulario = new JPanel(new BorderLayout(5, 5));
		formulario.setBorder(BorderFactory.createTitledBorder("Personal tecnico"));
		formulario.add(campos, BorderLayout.CENTER);
		formulario.add(botones, BorderLayout.SOUTH);
		return formulario;
	}

	private void agregar() {

		String genero = txtGenero.getText().trim();

		//charAt(0) sobre un campo vacio lanza StringIndexOutOfBounds, se valida antes
		if (genero.isEmpty()) {
			Dialogos.error(this, "El genero no puede estar vacio");
			return;
		}

		try {
			red.addPersonal(txtTipoDoc.getText().trim(), txtDocumento.getText().trim(),
					txtNombre.getText().trim(), txtApellido.getText().trim(),
					genero.charAt(0), txtDireccion.getText().trim(),
					txtTelefono.getText().trim());

			limpiar();
			refrescar();

		} catch (Exception ex) {
			Dialogos.error(this, ex.getMessage());
		}
	}

	private void eliminar() {

		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			Dialogos.error(this, "Seleccione un tecnico de la tabla");
			return;
		}

		try {
			red.eliminarPersonal((String) modeloTabla.getValueAt(fila, 1));
			refrescar();
		} catch (Exception ex) {
			Dialogos.error(this, ex.getMessage());
		}
	}

	public void refrescar() {

		modeloTabla.setRowCount(0);

		for (Personal p : red.getPersonal()) {
			modeloTabla.addRow(new Object[] { p.getTipoDocumento(), p.getNumeroDocumento(),
					p.getNombre(), p.getApellido(), p.getGenero(), p.getDireccion(),
					p.getTelefono() });
		}
	}

	private void limpiar() {
		txtTipoDoc.setText("");
		txtDocumento.setText("");
		txtNombre.setText("");
		txtApellido.setText("");
		txtGenero.setText("");
		txtDireccion.setText("");
		txtTelefono.setText("");
	}
}