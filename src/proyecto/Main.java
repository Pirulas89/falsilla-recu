package proyecto;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

import proyecto.dao.CancionDAO;
import proyecto.dao.EdificioDAO;
import proyecto.dao.OrdenadorDAO;
import proyecto.modelo.Cancion;
import proyecto.modelo.Edificio;
import proyecto.modelo.Ordenador;

public class Main {

    private JFrame frame;

    // ── DAOs ──────────────────────────────────────────────────────────────────
    private final CancionDAO   cancionDAO   = new CancionDAO();
    private final EdificioDAO  edificioDAO  = new EdificioDAO();
    private final OrdenadorDAO ordenadorDAO = new OrdenadorDAO();

    // ── Pestaña Cancion ───────────────────────────────────────────────────────
    private JTable            tableCancion;
    private JTextField        txtCancionId, txtCancionTitulo;
    private JSpinner          spnMinutos, spnSegundos;
    private JComboBox<String> cmbFiltro;

    // ── Pestaña Edificio ──────────────────────────────────────────────────────
    private JTable     tableEdificio;
    private JTextField txtEdificioId, txtViviendas, txtAnio;
    private JCheckBox  chkRehabilitado;

    // ── Pestaña Ordenador ─────────────────────────────────────────────────────
    private JTable     tableOrdenador;
    private JTextField txtOrdenadorId, txtRam, txtDisco, txtUsb, txtPrecio, txtUnidades;
    private JLabel     lblTotalOrdenadores, lblGananciaTotal;

    // =========================================================================

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try { new Main().frame.setVisible(true); }
            catch (Exception e) { e.printStackTrace(); }
        });
    }

    public Main() { initialize(); }

    private void initialize() {
        frame = new JFrame("Repaso Programacion - Hibernate");
        frame.setBounds(100, 100, 620, 530);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Cancion",   panelCancion());
        tabs.addTab("Edificio",  panelEdificio());
        tabs.addTab("Ordenador", panelOrdenador());
        frame.getContentPane().add(tabs);

        mostrarCancion();
        mostrarEdificio();
        mostrarOrdenador();
    }

    // =========================================================================
    // PESTAÑA 1 — CANCION
    // =========================================================================

    private JPanel panelCancion() {
        JPanel p = new JPanel();
        p.setLayout(null);

        JLabel lblFiltro = new JLabel("Mostrar:");
        lblFiltro.setBounds(12, 12, 60, 17);
        p.add(lblFiltro);

        cmbFiltro = new JComboBox<>(new String[]{
            "Todas las canciones", "Solo cortas (<3 min)", "Solo largas (>3 min)"
        });
        cmbFiltro.setBounds(75, 10, 200, 22);
        cmbFiltro.addActionListener(e -> mostrarCancion());
        p.add(cmbFiltro);

        JScrollPane scroll = new JScrollPane();
        scroll.setBounds(12, 40, 570, 130);
        p.add(scroll);

        tableCancion = new JTable();
        tableCancion.setFillsViewportHeight(true);
        tableCancion.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableCancion.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tableCancion.getSelectedRow();
                if (fila != -1) {
                    txtCancionId.setText(tableCancion.getValueAt(fila, 0).toString());
                    txtCancionTitulo.setText(tableCancion.getValueAt(fila, 1).toString());
                    int dur = Integer.parseInt(tableCancion.getValueAt(fila, 2).toString());
                    spnMinutos.setValue(dur / 60);
                    spnSegundos.setValue(dur % 60);
                }
            }
        });
        scroll.setViewportView(tableCancion);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(12, 185, 60, 17);
        p.add(lblId);
        txtCancionId = new JTextField();
        txtCancionId.setEditable(false);
        txtCancionId.setBounds(130, 183, 80, 21);
        p.add(txtCancionId);

        JLabel lblTitulo = new JLabel("Titulo:");
        lblTitulo.setBounds(12, 215, 60, 17);
        p.add(lblTitulo);
        txtCancionTitulo = new JTextField();
        txtCancionTitulo.setBounds(130, 213, 200, 21);
        p.add(txtCancionTitulo);

        JLabel lblDur = new JLabel("Duracion:");
        lblDur.setBounds(12, 245, 80, 17);
        p.add(lblDur);
        spnMinutos = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        spnMinutos.setBounds(130, 243, 60, 21);
        p.add(spnMinutos);
        JLabel lblMin = new JLabel("min");
        lblMin.setBounds(195, 245, 30, 17);
        p.add(lblMin);
        spnSegundos = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        spnSegundos.setBounds(230, 243, 60, 21);
        p.add(spnSegundos);
        JLabel lblSeg = new JLabel("seg");
        lblSeg.setBounds(295, 245, 30, 17);
        p.add(lblSeg);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(12, 290, 100, 27);
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtCancionTitulo.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "El campo titulo no debe estar vacio", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int duracion = (int) spnMinutos.getValue() * 60 + (int) spnSegundos.getValue();
                    if (duracion <= 0) {
                        JOptionPane.showMessageDialog(null, "La duracion debe ser mayor que 0", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    cancionDAO.insertar(new Cancion(txtCancionTitulo.getText(), duracion));
                    JOptionPane.showMessageDialog(null, "Creado correctamente");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                mostrarCancion();
            }
        });
        p.add(btnGuardar);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(120, 290, 100, 27);
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtCancionId.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Selecciona una cancion de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (txtCancionTitulo.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "El campo titulo no debe estar vacio", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int duracion = (int) spnMinutos.getValue() * 60 + (int) spnSegundos.getValue();
                    if (duracion <= 0) {
                        JOptionPane.showMessageDialog(null, "La duracion debe ser mayor que 0", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Cancion c = cancionDAO.obtenerPorId(Integer.parseInt(txtCancionId.getText()));
                    c.setTitulo(txtCancionTitulo.getText());
                    c.setDuracion(duracion);
                    cancionDAO.actualizar(c);
                    JOptionPane.showMessageDialog(null, "Actualizado correctamente");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error al actualizar", JOptionPane.ERROR_MESSAGE);
                }
                mostrarCancion();
            }
        });
        p.add(btnActualizar);

        JButton btnBorrar = new JButton("Borrar");
        btnBorrar.setBounds(228, 290, 100, 27);
        btnBorrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtCancionId.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Selecciona una cancion de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    cancionDAO.eliminar(Integer.parseInt(txtCancionId.getText()));
                    JOptionPane.showMessageDialog(null, "Borrado correctamente");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                mostrarCancion();
            }
        });
        p.add(btnBorrar);

        return p;
    }

    void mostrarCancion() {
        List<Cancion> lista;
        int sel = cmbFiltro.getSelectedIndex();
        if (sel == 1)      lista = cancionDAO.obtenerCortas();
        else if (sel == 2) lista = cancionDAO.obtenerLargas();
        else               lista = cancionDAO.obtenerTodas();

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID"); model.addColumn("Titulo");
        model.addColumn("Duracion (seg)"); model.addColumn("Duracion");
        for (Cancion c : lista) {
            model.addRow(new Object[]{
                c.getId(), c.getTitulo(), c.getDuracion(),
                (c.getDuracion() / 60) + "m " + (c.getDuracion() % 60) + "s"
            });
        }
        tableCancion.setModel(model);
    }

    // =========================================================================
    // PESTAÑA 2 — EDIFICIO
    // =========================================================================

    private JPanel panelEdificio() {
        JPanel p = new JPanel();
        p.setLayout(null);

        JScrollPane scroll = new JScrollPane();
        scroll.setBounds(12, 12, 570, 150);
        p.add(scroll);

        tableEdificio = new JTable();
        tableEdificio.setFillsViewportHeight(true);
        tableEdificio.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableEdificio.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tableEdificio.getSelectedRow();
                if (fila != -1) {
                    txtEdificioId.setText(tableEdificio.getValueAt(fila, 0).toString());
                    txtViviendas.setText(tableEdificio.getValueAt(fila, 1).toString());
                    txtAnio.setText(tableEdificio.getValueAt(fila, 2).toString());
                    chkRehabilitado.setSelected(tableEdificio.getValueAt(fila, 3).toString().equals("Si"));
                }
            }
        });
        scroll.setViewportView(tableEdificio);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(12, 175, 130, 17);
        p.add(lblId);
        txtEdificioId = new JTextField();
        txtEdificioId.setEditable(false);
        txtEdificioId.setBounds(200, 173, 100, 21);
        p.add(txtEdificioId);

        JLabel lblViviendas = new JLabel("Num Viviendas:");
        lblViviendas.setBounds(12, 205, 130, 17);
        p.add(lblViviendas);
        txtViviendas = new JTextField();
        txtViviendas.setBounds(200, 203, 100, 21);
        p.add(txtViviendas);

        JLabel lblAnio = new JLabel("Anio Edificacion:");
        lblAnio.setBounds(12, 235, 130, 17);
        p.add(lblAnio);
        txtAnio = new JTextField();
        txtAnio.setBounds(200, 233, 100, 21);
        p.add(txtAnio);

        JLabel lblRehab = new JLabel("Rehabilitado:");
        lblRehab.setBounds(12, 265, 130, 17);
        p.add(lblRehab);
        chkRehabilitado = new JCheckBox();
        chkRehabilitado.setBounds(200, 263, 21, 21);
        p.add(chkRehabilitado);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(12, 310, 100, 27);
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtViviendas.getText().isEmpty() || txtAnio.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int viviendas = Integer.parseInt(txtViviendas.getText());
                    int anio      = Integer.parseInt(txtAnio.getText());
                    if (viviendas <= 0) {
                        JOptionPane.showMessageDialog(null, "El numero de viviendas debe ser mayor que 0", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (anio <= 1900) {
                        JOptionPane.showMessageDialog(null, "El anio debe ser posterior a 1900", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    edificioDAO.insertar(new Edificio(viviendas, anio, chkRehabilitado.isSelected()));
                    JOptionPane.showMessageDialog(null, "Creado correctamente");
                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(null, "Num viviendas y anio deben ser numeros enteros", "Error de formato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                mostrarEdificio();
            }
        });
        p.add(btnGuardar);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(120, 310, 100, 27);
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtEdificioId.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Selecciona un edificio de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int viviendas = Integer.parseInt(txtViviendas.getText());
                    int anio      = Integer.parseInt(txtAnio.getText());
                    if (viviendas <= 0) {
                        JOptionPane.showMessageDialog(null, "El numero de viviendas debe ser mayor que 0", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (anio <= 1900) {
                        JOptionPane.showMessageDialog(null, "El anio debe ser posterior a 1900", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Edificio ed = edificioDAO.obtenerPorId(Integer.parseInt(txtEdificioId.getText()));
                    ed.setNumViviendas(viviendas);
                    ed.setAnioEdificacion(anio);
                    ed.setRehabilitado(chkRehabilitado.isSelected());
                    edificioDAO.actualizar(ed);
                    JOptionPane.showMessageDialog(null, "Actualizado correctamente");
                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(null, "Num viviendas y anio deben ser numeros enteros", "Error de formato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error al actualizar", JOptionPane.ERROR_MESSAGE);
                }
                mostrarEdificio();
            }
        });
        p.add(btnActualizar);

        JButton btnBorrar = new JButton("Borrar");
        btnBorrar.setBounds(228, 310, 100, 27);
        btnBorrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtEdificioId.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Selecciona un edificio de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    edificioDAO.eliminar(Integer.parseInt(txtEdificioId.getText()));
                    JOptionPane.showMessageDialog(null, "Borrado correctamente");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                mostrarEdificio();
            }
        });
        p.add(btnBorrar);

        return p;
    }

    void mostrarEdificio() {
        List<Edificio> lista = edificioDAO.obtenerTodos();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID"); model.addColumn("Num Viviendas");
        model.addColumn("Anio Edificacion"); model.addColumn("Rehabilitado");
        for (Edificio ed : lista) {
            model.addRow(new Object[]{
                ed.getId(), ed.getNumViviendas(), ed.getAnioEdificacion(),
                ed.isRehabilitado() ? "Si" : "No"
            });
        }
        tableEdificio.setModel(model);
    }

    // =========================================================================
    // PESTAÑA 3 — ORDENADOR
    // =========================================================================

    private JPanel panelOrdenador() {
        JPanel p = new JPanel();
        p.setLayout(null);

        JScrollPane scroll = new JScrollPane();
        scroll.setBounds(12, 12, 570, 150);
        p.add(scroll);

        tableOrdenador = new JTable();
        tableOrdenador.setFillsViewportHeight(true);
        tableOrdenador.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableOrdenador.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tableOrdenador.getSelectedRow();
                if (fila != -1) {
                    txtOrdenadorId.setText(tableOrdenador.getValueAt(fila, 0).toString());
                    txtRam.setText(tableOrdenador.getValueAt(fila, 1).toString());
                    txtDisco.setText(tableOrdenador.getValueAt(fila, 2).toString());
                    txtUsb.setText(tableOrdenador.getValueAt(fila, 3).toString());
                    txtPrecio.setText(tableOrdenador.getValueAt(fila, 4).toString());
                    txtUnidades.setText(tableOrdenador.getValueAt(fila, 5).toString());
                }
            }
        });
        scroll.setViewportView(tableOrdenador);

        lblTotalOrdenadores = new JLabel("Total ordenadores: 0");
        lblTotalOrdenadores.setBounds(12, 170, 250, 17);
        p.add(lblTotalOrdenadores);

        lblGananciaTotal = new JLabel("Ganancia total: 0.00€");
        lblGananciaTotal.setBounds(280, 170, 250, 17);
        p.add(lblGananciaTotal);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(12, 200, 170, 17);
        p.add(lblId);
        txtOrdenadorId = new JTextField();
        txtOrdenadorId.setEditable(false);
        txtOrdenadorId.setBounds(210, 198, 100, 21);
        p.add(txtOrdenadorId);

        JLabel lblRam = new JLabel("RAM (GB, potencia de 2):");
        lblRam.setBounds(12, 228, 190, 17);
        p.add(lblRam);
        txtRam = new JTextField();
        txtRam.setBounds(210, 226, 100, 21);
        p.add(txtRam);

        JLabel lblDisco = new JLabel("Disco (GB):");
        lblDisco.setBounds(12, 256, 170, 17);
        p.add(lblDisco);
        txtDisco = new JTextField();
        txtDisco.setBounds(210, 254, 100, 21);
        p.add(txtDisco);

        JLabel lblUsb = new JLabel("Num Puertos USB:");
        lblUsb.setBounds(12, 284, 170, 17);
        p.add(lblUsb);
        txtUsb = new JTextField();
        txtUsb.setBounds(210, 282, 100, 21);
        p.add(txtUsb);

        JLabel lblPrecio = new JLabel("Precio (€):");
        lblPrecio.setBounds(12, 312, 170, 17);
        p.add(lblPrecio);
        txtPrecio = new JTextField();
        txtPrecio.setBounds(210, 310, 100, 21);
        p.add(txtPrecio);

        JLabel lblUnidades = new JLabel("Unidades:");
        lblUnidades.setBounds(12, 340, 170, 17);
        p.add(lblUnidades);
        txtUnidades = new JTextField();
        txtUnidades.setBounds(210, 338, 100, 21);
        p.add(txtUnidades);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(12, 385, 100, 27);
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int ram       = Integer.parseInt(txtRam.getText());
                    int disco     = Integer.parseInt(txtDisco.getText());
                    int usb       = Integer.parseInt(txtUsb.getText());
                    double precio = Double.parseDouble(txtPrecio.getText());
                    int unidades  = Integer.parseInt(txtUnidades.getText());
                    if ((ram & (ram - 1)) != 0 || ram <= 0) {
                        JOptionPane.showMessageDialog(null, "La RAM debe ser una potencia de 2 (1,2,4,8,16,32...)", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (disco <= 0 || usb <= 0 || precio <= 0 || unidades <= 0) {
                        JOptionPane.showMessageDialog(null, "Todos los valores numericos deben ser mayores que 0", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    ordenadorDAO.insertar(new Ordenador(ram, disco, usb, precio, unidades));
                    JOptionPane.showMessageDialog(null, "Creado correctamente");
                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(null, "Todos los campos deben ser numericos", "Error de formato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                mostrarOrdenador();
            }
        });
        p.add(btnGuardar);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(120, 385, 100, 27);
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtOrdenadorId.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Selecciona un ordenador de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int ram       = Integer.parseInt(txtRam.getText());
                    int disco     = Integer.parseInt(txtDisco.getText());
                    int usb       = Integer.parseInt(txtUsb.getText());
                    double precio = Double.parseDouble(txtPrecio.getText());
                    int unidades  = Integer.parseInt(txtUnidades.getText());
                    if ((ram & (ram - 1)) != 0 || ram <= 0) {
                        JOptionPane.showMessageDialog(null, "La RAM debe ser una potencia de 2 (1,2,4,8,16,32...)", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (disco <= 0 || usb <= 0 || precio <= 0 || unidades <= 0) {
                        JOptionPane.showMessageDialog(null, "Todos los valores numericos deben ser mayores que 0", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Ordenador o = ordenadorDAO.obtenerPorId(Integer.parseInt(txtOrdenadorId.getText()));
                    o.setTamRam(ram); o.setTamDisco(disco); o.setNumUsb(usb);
                    o.setPrecio(precio); o.setUnidades(unidades);
                    ordenadorDAO.actualizar(o);
                    JOptionPane.showMessageDialog(null, "Actualizado correctamente");
                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(null, "Todos los campos deben ser numericos", "Error de formato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error al actualizar", JOptionPane.ERROR_MESSAGE);
                }
                mostrarOrdenador();
            }
        });
        p.add(btnActualizar);

        JButton btnBorrar = new JButton("Borrar");
        btnBorrar.setBounds(228, 385, 100, 27);
        btnBorrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtOrdenadorId.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Selecciona un ordenador de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    ordenadorDAO.eliminar(Integer.parseInt(txtOrdenadorId.getText()));
                    JOptionPane.showMessageDialog(null, "Borrado correctamente");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                mostrarOrdenador();
            }
        });
        p.add(btnBorrar);

        return p;
    }

    void mostrarOrdenador() {
        List<Ordenador> lista = ordenadorDAO.obtenerTodos();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID"); model.addColumn("RAM (GB)"); model.addColumn("Disco (GB)");
        model.addColumn("Puertos USB"); model.addColumn("Precio"); model.addColumn("Unidades");

        int totalOrdenadores = 0;
        double gananciaTotal = 0;

        for (Ordenador o : lista) {
            totalOrdenadores += o.getUnidades();
            gananciaTotal    += o.getUnidades() * o.getPrecio();
            model.addRow(new Object[]{
                o.getId(), o.getTamRam(), o.getTamDisco(), o.getNumUsb(), o.getPrecio(), o.getUnidades()
            });
        }
        tableOrdenador.setModel(model);
        lblTotalOrdenadores.setText("Total ordenadores: " + totalOrdenadores);
        lblGananciaTotal.setText(String.format("Ganancia total: %.2f€", gananciaTotal));
    }
}
