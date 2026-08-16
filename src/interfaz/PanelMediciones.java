package interfaz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo.*;

//pestaña para mediciones


//único panel que necesita combos, porque los add de la red piden los objetos Personal y TipoContaminante completos y no sus codigos. Al meter 
//los objetos directamente en el JComboBox, Swing los pinta con su toString() y getSelectedItem() devuelve el objeto listo para pasarlo al add

public class PanelMediciones extends JPanel implements Refrescable {

	private static final long serialVersionUID = 1L;

	private Red red;
	private DefaultTableModel modeloTabla;
	private JTable tabla;

	private JTextField txtCodigo = new JTextField();
	private JTextField txtFecha = new JTextField();
	private JTextField txtValor = new JTextField();

	private JComboBox<String> cmbTipo = new JComboBox<>(new String[] { "AIRE", "RUIDO" });
	private JComboBox<Estacion> cmbEstacion = new JComboBox<>();
	private JComboBox<Personal> cmbTecnico = new JComboBox<>();
	private JComboBox<TipoContaminante> cmbContaminante = new JComboBox<>();
	private JComboBox<Jornada> cmbJornada = new JComboBox<>(Jornada.values());

	PanelMediciones(Red red) {

		this.red = red;
		setLayout(new BorderLayout(5, 5));

		modeloTabla = new DefaultTableModel(new String[] { "Codigo", "Tipo", "Estacion",
				"Tecnico", "Fecha", "Valor", "Detalle", "Critica" }, 0) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};

		tabla = new JTable(modeloTabla);
		add(new JScrollPane(tabla), BorderLayout.CENTER);
		add(construirFormulario(), BorderLayout.SOUTH);

		//solo se habilita el combo que corresponde al tipo elegido
		cmbTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ajustarCombos();
			}
		});

		refrescar();
		ajustarCombos();
	}

	private JPanel construirFormulario() {

		JPanel campos = new JPanel(new GridLayout(4, 4, 5, 5));
		campos.add(new JLabel("Tipo:"));
		campos.add(cmbTipo);
		campos.add(new JLabel("Codigo medicion:"));
		campos.add(txtCodigo);
		campos.add(new JLabel("Estacion:"));
		campos.add(cmbEstacion);
		campos.add(new JLabel("Tecnico:"));
		campos.add(cmbTecnico);
		campos.add(new JLabel("Fecha (aaaa-mm-dd):"));
		campos.add(txtFecha);
		campos.add(new JLabel("Valor medido:"));
		campos.add(txtValor);
		campos.add(new JLabel("Contaminante (aire):"));
		campos.add(cmbContaminante);
		campos.add(new JLabel("Jornada (ruido):"));
		campos.add(cmbJornada);

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
		formulario.setBorder(BorderFactory.createTitledBorder("Medicion"));
		formulario.add(campos, BorderLayout.CENTER);
		formulario.add(botones, BorderLayout.SOUTH);
		return formulario;
	}

	private void ajustarCombos() {
		boolean esAire = "AIRE".equals(cmbTipo.getSelectedItem());
		cmbContaminante.setEnabled(esAire);
		cmbJornada.setEnabled(!esAire);
	}

	private void agregar() {

		Estacion estacion = (Estacion) cmbEstacion.getSelectedItem();
		Personal tecnico = (Personal) cmbTecnico.getSelectedItem();

		if (estacion == null || tecnico == null) {
			Dialogos.error(this, "Debe existir al menos una estacion y un tecnico");
			return;
		}

		try {
			LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
			double valor = Double.parseDouble(txtValor.getText().trim());
			String codigo = txtCodigo.getText().trim();

			if ("AIRE".equals(cmbTipo.getSelectedItem())) {

				TipoContaminante tc = (TipoContaminante) cmbContaminante.getSelectedItem();
				if (tc == null) {
					Dialogos.error(this, "Debe existir al menos un tipo de contaminante");
					return;
				}
				red.addMedicionAire(estacion.getCodigo(), codigo, tecnico, fecha, valor, tc);

			} else {
				red.addMedicionRuido(estacion.getCodigo(), codigo, tecnico, fecha, valor,
						(Jornada) cmbJornada.getSelectedItem());
			}

			limpiar();
			refrescar();

		} catch (DateTimeParseException ex) {
			Dialogos.error(this, "La fecha debe tener el formato aaaa-mm-dd");
		} catch (NumberFormatException ex) {
			Dialogos.error(this, "El valor medido debe ser un numero");
		} catch (Exception ex) {
			Dialogos.error(this, ex.getMessage());
		}
	}

	private void eliminar() {

		int fila = tabla.getSelectedRow();
		if (fila == -1) {
			Dialogos.error(this, "Seleccione una medicion de la tabla");
			return;
		}

		try {
			red.eliminarMedicion((String) modeloTabla.getValueAt(fila, 0));
			refrescar();
		} catch (Exception ex) {
			Dialogos.error(this, ex.getMessage());
		}
	}

	//recarga la tabla y los tres combos que dependen de las otras pestanas
	public void refrescar() {

		cmbEstacion.removeAllItems();
		for (Estacion e : red.getEstaciones()) {
			cmbEstacion.addItem(e);
		}

		cmbTecnico.removeAllItems();
		for (Personal p : red.getPersonal()) {
			cmbTecnico.addItem(p);
		}

		cmbContaminante.removeAllItems();
		for (TipoContaminante t : red.getTiposContaminante()) {
			cmbContaminante.addItem(t);
		}

		modeloTabla.setRowCount(0);

		for (Medicion m : red.getMediciones()) {

			String tipo;
			String detalle;

			//instanceof y casteo porque getTipoContaminante y getJornada solo existen en las subclases, no en Medicion
			if (m instanceof MedicionAire) {
				tipo = "AIRE";
				detalle = ((MedicionAire) m).getTipoContaminante().getCodigo();
			} else {
				tipo = "RUIDO";
				detalle = ((MedicionRuido) m).getJornada().toString();
			}

			modeloTabla.addRow(new Object[] { m.getCodigo(), tipo, estacionDe(m),
					m.getTecnicoRegistro().getNumeroDocumento(), m.getFecha(),
					m.getValorMedido(), detalle, m.critica() ? "SI" : "no" });
		}
	}

	// Una medicion no guarda su estacion, hay que preguntarle a cada una
	private String estacionDe(Medicion m) {

		for (Estacion e : red.getEstaciones()) {
			if (e.buscarMedicion(m.getCodigo()) != null) {
				return e.getCodigo();
			}
		}
		return "-";
	}

	private void limpiar() {
		txtCodigo.setText("");
		txtFecha.setText("");
		txtValor.setText("");
	}
}