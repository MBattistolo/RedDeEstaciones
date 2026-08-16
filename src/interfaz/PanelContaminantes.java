package interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo.*;

//pestaña para los tipos de contaminante

public class PanelContaminantes extends JPanel implements Refrescable {

	private static final long serialVersionUID = 1L;

	private Red red;
	private DefaultTableModel modeloTabla;
	private JTable tabla;

	private JTextField txtCodigo = new JTextField();
	private JTextField txtNombre = new JTextField();
	private JTextField txtMinimo = new JTextField();
	private JTextField txtMaximo = new JTextField();

	PanelContaminantes(Red red) {

		this.red = red;
		setLayout(new BorderLayout(5, 5));

		//la tabla no es editable, los cambios se hacen por el formulario
		modeloTabla = new DefaultTableModel(
				new String[] { "Codigo", "Nombre", "Minimo", "Maximo" }, 0) {
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
		campos.add(new JLabel("Valor minimo:"));
		campos.add(txtMinimo);
		campos.add(new JLabel("Valor maximo:"));
		campos.add(txtMaximo);

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
		formulario.setBorder(BorderFactory.createTitledBorder("Tipo de contaminante"));
		formulario.add(campos, BorderLayout.CENTER);
		formulario.add(botones, BorderLayout.SOUTH);
		return formulario;
	}

	private void agregar() {

		try {
			//parseDouble lanza NumberFormatException si el campo no es numérico, por eso se captura aparte de las excepciones propias del modelo
			red.addTipoContaminante(
					txtCodigo.getText().trim(),
					txtNombre.getText().trim(),
					Double.parseDouble(txtMinimo.getText().trim()),
					Double.parseDouble(txtMaximo.getText().trim()));

			limpiar();
			refrescar();

		} catch (NumberFormatException ex) {
			Dialogos.error(this, "Los valores minimo y maximo deben ser numeros");
		} catch (Exception ex) {
			Dialogos.error(this, ex.getMessage());
		}
	}

	private void eliminar() {

		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			Dialogos.error(this, "Seleccione un contaminante de la tabla");
			return;
		}

		try {
			red.eliminarTipoContaminante((String) modeloTabla.getValueAt(fila, 0));
			refrescar();
		} catch (Exception ex) {
			Dialogos.error(this, ex.getMessage());
		}
	}

	//vacia la tabla y la vuelve a llenar con lo que hay en la red
	public void refrescar() {

		modeloTabla.setRowCount(0);

		for (TipoContaminante t : red.getTiposContaminante()) {
			modeloTabla.addRow(new Object[] { t.getCodigo(), t.getNombreContaminante(),
					t.getValorMinimo(), t.getValorMaximo() });
		}
	}

	private void limpiar() {
		txtCodigo.setText("");
		txtNombre.setText("");
		txtMinimo.setText("");
		txtMaximo.setText("");
	}
}