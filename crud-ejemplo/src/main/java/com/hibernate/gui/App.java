package com.hibernate.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.hibernate.dao.PacienteDAO;
import com.hibernate.model.Paciente;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

class ConnectionSingleton {
	private static Connection con;

	public static Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://127.0.0.1:3307/pacientesJDBC";
		String user = "alumno";
		String password = "alumno";
		if (con == null || con.isClosed()) {
			con = DriverManager.getConnection(url, user, password);
		}
		return con;
	}
}

public class App {

	private JFrame frame;
	private DefaultTableModel modelJDBC;
	private DefaultTableModel modelHibernate;
	private JTable tableJDBC;
	private JTable tableHibernate;
	PacienteDAO pacienteDAO = new PacienteDAO();
	private JTextField txtId;
	private JTextField txtNombre;
	private JTextField txtGlucosa;
	private JTextField txtHierro;
	private JLabel lblNumPac;
	private JLabel lblNumDia;
	private JLabel lblHierroTotal;
	private JLabel lblId_1;
	private JLabel lblNombre_1;
	private JLabel lblGlucosa_1;
	private JLabel lblHierro_1;
	private JTextField txtHierroHib;
	private JTextField txtGlucosaHib;
	private JTextField txtNombreHib;
	private JTextField txtIdHib;
	private JButton btnInsertarHib;
	private JButton btnActualizarHib;
	private JButton btnBorrarHib;
	private JLabel lblNumPacHib;
	private JLabel lblNumDiaHib;
	private JLabel lblHierroTotalHib;
	int hierroTotal=0;
	void actualizarTablas() {
		modelJDBC.setRowCount(0);
		try {
			Connection con = ConnectionSingleton.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM paciente");
			while (rs.next()) {
				Object[] row = new Object[4];
				row[0] = rs.getInt("idPaciente");
				row[1] = rs.getString("nombre");
				row[2] = rs.getInt("glucosa");
				row[3] = rs.getInt("hierro");
				modelJDBC.addRow(row);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.getMessage();
		}
		
		lblNumPac.setText("Nº pacientes: "+contarPacientes());
		lblNumDia.setText("Nº diabéticos: "+contarDiabeticos());
		lblHierroTotal.setText("Hierro total: "+sumarHierro());
		
		
		modelHibernate.setRowCount(0);
		List<Paciente> pacientes = pacienteDAO.selectAllPaciente();
		for (Paciente p : pacientes) {
			Object[] row = new Object[4];
			row[0] = p.getIdPaciente();
			row[1] = p.getNombre();
			row[2] = p.getGlucosa();
			row[3] = p.getHierro();
			modelHibernate.addRow(row);
		}
		lblNumPacHib.setText("Nº pacientes: "+ pacientes.size());
		
		List<Paciente> diabeticos = pacienteDAO.selectAllDiabetico();
		lblNumDiaHib.setText("Nº diabeticos: "+diabeticos.size());
		
		hierroTotal=0;
		for(Paciente p : pacientes) {
			hierroTotal += p.getHierro();
		}
		lblHierroTotalHib.setText("Hierro total: "+hierroTotal);
		
		
		
	}
	
	int contarPacientes() {
		int cantidadPaciente = 0;
		try {
			Connection con = ConnectionSingleton.getConnection();
			PreparedStatement ins_pstmt = con.prepareStatement("SELECT COUNT(*) FROM paciente");
			ResultSet rs = ins_pstmt.executeQuery();
			if (rs.next()) {
				cantidadPaciente = rs.getInt(1);
			}
			ins_pstmt.close();
			con.close();

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		return cantidadPaciente;
	}
	
	int contarDiabeticos() {
		int cantidadPaciente = 0;
		try {
			Connection con = ConnectionSingleton.getConnection();
			PreparedStatement ins_pstmt = con.prepareStatement("SELECT COUNT(*) FROM paciente WHERE glucosa > 125");
			ResultSet rs = ins_pstmt.executeQuery();
			if (rs.next()) {
				cantidadPaciente = rs.getInt(1);
			}
			ins_pstmt.close();
			con.close();

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		return cantidadPaciente;
	}
	
	int sumarHierro() {
		int hierroTotal=0;
		try {
			Connection con = ConnectionSingleton.getConnection();
			PreparedStatement ins_pstmt = con.prepareStatement("SELECT SUM(hierro) FROM paciente");
			ResultSet rs = ins_pstmt.executeQuery();
			if (rs.next()) {
				hierroTotal = rs.getInt(1);
			}
			ins_pstmt.close();
			con.close();

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		return hierroTotal;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 874, 607);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		modelJDBC = new DefaultTableModel();

		modelJDBC.addColumn("id");
		modelJDBC.addColumn("nombre");
		modelJDBC.addColumn("glucosa");
		modelJDBC.addColumn("hierro");
		tableJDBC = new JTable(modelJDBC);
		tableJDBC.setBounds(1, 1, 307, 0);

		try {
			Connection con = ConnectionSingleton.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM paciente");
			while (rs.next()) {
				Object[] row = new Object[4];
				row[0] = rs.getInt("idPaciente");
				row[1] = rs.getString("nombre");
				row[2] = rs.getInt("glucosa");
				row[3] = rs.getInt("hierro");
				modelJDBC.addRow(row);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.getMessage();
		}
		frame.getContentPane().setLayout(null);

		tableJDBC.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		frame.getContentPane().add(tableJDBC, BorderLayout.CENTER);

		JScrollPane scrollPaneJDBC = new JScrollPane(tableJDBC);
		scrollPaneJDBC.setBounds(40, 55, 390, 163);
		frame.getContentPane().add(scrollPaneJDBC);

		modelHibernate = new DefaultTableModel();

		modelHibernate.addColumn("id");
		modelHibernate.addColumn("nombre");
		modelHibernate.addColumn("glucosa");
		modelHibernate.addColumn("hierro");
		tableHibernate = new JTable(modelHibernate);
		tableHibernate.setBounds(1, 1, 307, 0);

		List<Paciente> pacientes = pacienteDAO.selectAllPaciente();
		for (Paciente p : pacientes) {
			Object[] row = new Object[4];
			row[0] = p.getIdPaciente();
			row[1] = p.getNombre();
			row[2] = p.getGlucosa();
			row[3] = p.getHierro();
			modelHibernate.addRow(row);
		}
		frame.getContentPane().setLayout(null);

		tableHibernate.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		frame.getContentPane().add(tableHibernate, BorderLayout.CENTER);

		JScrollPane scrollPaneHibernate = new JScrollPane(tableHibernate);
		scrollPaneHibernate.setBounds(442, 55, 390, 163);
		frame.getContentPane().add(scrollPaneHibernate);

		JLabel lblId = new JLabel("id:");
		lblId.setBounds(88, 243, 60, 17);
		frame.getContentPane().add(lblId);

		txtId = new JTextField();
		txtId.setEditable(false);
		txtId.setBounds(166, 241, 114, 21);
		frame.getContentPane().add(txtId);
		txtId.setColumns(10);

		JLabel lblNombre = new JLabel("nombre:");
		lblNombre.setBounds(88, 272, 60, 17);
		frame.getContentPane().add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(166, 270, 114, 21);
		frame.getContentPane().add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblGlucosa = new JLabel("glucosa:");
		lblGlucosa.setBounds(88, 311, 60, 17);
		frame.getContentPane().add(lblGlucosa);

		txtGlucosa = new JTextField();
		txtGlucosa.setBounds(166, 309, 114, 21);
		frame.getContentPane().add(txtGlucosa);
		txtGlucosa.setColumns(10);

		JLabel lblHierro = new JLabel("hierro:");
		lblHierro.setBounds(88, 344, 60, 17);
		frame.getContentPane().add(lblHierro);

		txtHierro = new JTextField();
		txtHierro.setBounds(166, 342, 114, 21);
		frame.getContentPane().add(txtHierro);
		txtHierro.setColumns(10);

		JButton btnInsertar = new JButton("Insertar");
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					if (!txtNombre.getText().matches("[A-Za-z]+")) {
						JOptionPane.showMessageDialog(null, "Nombre vacio o formato no valido (solo letras)", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (!txtGlucosa.getText().matches("\\d{1,3}")) {
						JOptionPane.showMessageDialog(null, "Glucosa vacia o formato no permitido (0-999)", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (!txtHierro.getText().matches("\\d{1,3}")) {
						JOptionPane.showMessageDialog(null, "Hierro vacio o formato no permitido (0-999)", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						Connection con = ConnectionSingleton.getConnection();
						PreparedStatement ins_pstmt = con
								.prepareStatement("INSERT INTO paciente (nombre, glucosa, hierro) VALUES (?, ?, ?)");
						ins_pstmt.setString(1, txtNombre.getText());
						ins_pstmt.setInt(2, Integer.parseInt(txtGlucosa.getText()));
						ins_pstmt.setInt(3, Integer.parseInt(txtHierro.getText()));
						ins_pstmt.executeUpdate();
						ins_pstmt.close();
						con.close();
						actualizarTablas();
						JOptionPane.showMessageDialog(null, "Paciente insertado correctamente");
					}
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnInsertar.setBounds(292, 243, 105, 27);
		frame.getContentPane().add(btnInsertar);

		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (!txtNombre.getText().matches("[A-Za-z]+")) {
						JOptionPane.showMessageDialog(null, "Nombre vacio o formato no valido (solo letras)", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (!txtGlucosa.getText().matches("\\d{1,3}")) {
						JOptionPane.showMessageDialog(null, "Glucosa vacia o formato no permitido (0-999)", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (!txtHierro.getText().matches("\\d{1,3}")) {
						JOptionPane.showMessageDialog(null, "Hierro vacio o formato no permitido (0-999)", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						Connection con = ConnectionSingleton.getConnection();
						PreparedStatement upd_pstmt = con.prepareStatement(
								"UPDATE paciente SET nombre = ?, glucosa = ?, hierro = ? WHERE idPaciente = ?");
						upd_pstmt.setString(1, (txtNombre.getText()));
						upd_pstmt.setInt(2, Integer.parseInt(txtGlucosa.getText()));
						upd_pstmt.setInt(3, Integer.parseInt(txtHierro.getText()));
						upd_pstmt.setInt(4, Integer.parseInt(txtId.getText()));
						upd_pstmt.executeUpdate();
						upd_pstmt.close();
						actualizarTablas();
						JOptionPane.showMessageDialog(null, "Paciente actualizado correctamente");
					}
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnActualizar.setBounds(292, 289, 105, 27);
		frame.getContentPane().add(btnActualizar);

		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (txtId.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Campo del id vacio", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						Connection con = ConnectionSingleton.getConnection();
						PreparedStatement dele_pstmt = con
								.prepareStatement("DELETE FROM paciente WHERE idPaciente = ?");
						dele_pstmt.setInt(1, Integer.parseInt(txtId.getText()));
						dele_pstmt.executeUpdate();
						dele_pstmt.close();
						actualizarTablas();
						JOptionPane.showMessageDialog(null, "paciente eliminado correctamente");
					}
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnBorrar.setBounds(293, 339, 105, 27);
		frame.getContentPane().add(btnBorrar);
		
		lblNumPac = new JLabel("");
		lblNumPac.setBounds(40, 26, 123, 17);
		frame.getContentPane().add(lblNumPac);
		lblNumPac.setText("Nº pacientes: "+contarPacientes());
		
		lblNumDia = new JLabel("");
		lblNumDia.setBounds(195, 26, 123, 17);
		frame.getContentPane().add(lblNumDia);
		lblNumDia.setText("Nº diabéticos: "+contarDiabeticos());
		
		lblHierroTotal = new JLabel("");
		lblHierroTotal.setBounds(330, 26, 105, 17);
		frame.getContentPane().add(lblHierroTotal);
		lblHierroTotal.setText("Hierro total: "+sumarHierro());
		
		lblId_1 = new JLabel("id:");
		lblId_1.setBounds(488, 241, 60, 17);
		frame.getContentPane().add(lblId_1);
		
		lblNombre_1 = new JLabel("nombre:");
		lblNombre_1.setBounds(488, 270, 60, 17);
		frame.getContentPane().add(lblNombre_1);
		
		lblGlucosa_1 = new JLabel("glucosa:");
		lblGlucosa_1.setBounds(488, 309, 60, 17);
		frame.getContentPane().add(lblGlucosa_1);
		
		lblHierro_1 = new JLabel("hierro:");
		lblHierro_1.setBounds(488, 342, 60, 17);
		frame.getContentPane().add(lblHierro_1);
		
		txtHierroHib = new JTextField();
		txtHierroHib.setColumns(10);
		txtHierroHib.setBounds(566, 340, 114, 21);
		frame.getContentPane().add(txtHierroHib);
		
		txtGlucosaHib = new JTextField();
		txtGlucosaHib.setColumns(10);
		txtGlucosaHib.setBounds(566, 307, 114, 21);
		frame.getContentPane().add(txtGlucosaHib);
		
		txtNombreHib = new JTextField();
		txtNombreHib.setColumns(10);
		txtNombreHib.setBounds(566, 268, 114, 21);
		frame.getContentPane().add(txtNombreHib);
		
		txtIdHib = new JTextField();
		txtIdHib.setEditable(false);
		txtIdHib.setColumns(10);
		txtIdHib.setBounds(566, 239, 114, 21);
		frame.getContentPane().add(txtIdHib);
		
		btnInsertarHib = new JButton("Insertar");
		btnInsertarHib.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!txtNombreHib.getText().matches("[A-Za-z]+")) {
					JOptionPane.showMessageDialog(null, "Nombre vacio o formato no valido (solo letras)", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!txtGlucosaHib.getText().matches("\\d{1,3}")) {
					JOptionPane.showMessageDialog(null, "Glucosa vacia o formato no permitido (0-999)", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!txtHierroHib.getText().matches("\\d{1,3}")) {
					JOptionPane.showMessageDialog(null, "Hierro vacio o formato no permitido (0-999)", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					Paciente paciente = new Paciente(txtNombreHib.getText(), Integer.parseInt(txtGlucosaHib.getText()), Integer.parseInt(txtHierroHib.getText()));
					pacienteDAO.insertPaciente(paciente);
					actualizarTablas();
				}
			}
		});
		btnInsertarHib.setBounds(692, 241, 105, 27);
		frame.getContentPane().add(btnInsertarHib);
		
		btnActualizarHib = new JButton("Actualizar");
		btnActualizarHib.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!txtNombreHib.getText().matches("[A-Za-z]+")) {
					JOptionPane.showMessageDialog(null, "Nombre vacio o formato no valido (solo letras)", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!txtGlucosaHib.getText().matches("\\d{1,3}")) {
					JOptionPane.showMessageDialog(null, "Glucosa vacia o formato no permitido (0-999)", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!txtHierroHib.getText().matches("\\d{1,3}")) {
					JOptionPane.showMessageDialog(null, "Hierro vacio o formato no permitido (0-999)", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					Paciente paciente = pacienteDAO.selectPaciente(Integer.parseInt(txtIdHib.getText()));
					paciente.setNombre(txtNombreHib.getText());
					paciente.setGlucosa(Integer.parseInt(txtGlucosaHib.getText()));
					paciente.setHierro(Integer.parseInt(txtHierroHib.getText()));
					pacienteDAO.updatePaciente(paciente);
					actualizarTablas();
				}
			}
		});
		btnActualizarHib.setBounds(692, 287, 105, 27);
		frame.getContentPane().add(btnActualizarHib);
		
		btnBorrarHib = new JButton("Borrar");
		btnBorrarHib.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtIdHib.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Campo del id vacio", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					pacienteDAO.deletePaciente(Integer.parseInt(txtIdHib.getText()));
					actualizarTablas();
				}
			}
		});
		btnBorrarHib.setBounds(693, 337, 105, 27);
		frame.getContentPane().add(btnBorrarHib);
		
		lblNumPacHib = new JLabel("");
		lblNumPacHib.setBounds(447, 26, 123, 17);
		frame.getContentPane().add(lblNumPacHib);
		
		lblNumPacHib.setText("Nº pacientes: "+ pacientes.size());
		
		lblNumDiaHib = new JLabel("");
		lblNumDiaHib.setBounds(566, 26, 114, 17);
		frame.getContentPane().add(lblNumDiaHib);
		
		List<Paciente> diabeticos = pacienteDAO.selectAllDiabetico();
		lblNumDiaHib.setText("Nº diabeticos: "+diabeticos.size());
		
		
		for(Paciente p : pacientes) {
			hierroTotal += p.getHierro();
		}
		lblHierroTotalHib = new JLabel("");
		lblHierroTotalHib.setBounds(708, 26, 124, 17);
		frame.getContentPane().add(lblHierroTotalHib);
		lblHierroTotalHib.setText("Hierro total: "+hierroTotal);
		
		tableJDBC.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = tableJDBC.getSelectedRow();
				TableModel model = tableJDBC.getModel();
				txtId.setText(model.getValueAt(index, 0).toString());
				txtNombre.setText(model.getValueAt(index, 1).toString());
				txtGlucosa.setText(model.getValueAt(index, 2).toString());
				txtHierro.setText(model.getValueAt(index, 3).toString());
			}
		});
		
		tableHibernate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = tableHibernate.getSelectedRow();
				TableModel model = tableHibernate.getModel();
				txtIdHib.setText(model.getValueAt(index, 0).toString());
				txtNombreHib.setText(model.getValueAt(index, 1).toString());
				txtGlucosaHib.setText(model.getValueAt(index, 2).toString());
				txtHierroHib.setText(model.getValueAt(index, 3).toString());
			}
		});

	}

}
