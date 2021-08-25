/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.LibroController;
import dao.ILibroDAO;
import dao.LibroDAOJDBCImpl;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import model.Libro;

/**
 *
 * @author Arles
 */
public class PanelLibros extends JPanel {

    private JLabel jLabelTitulo;
    JTable table = new JTable();
    JScrollPane jsp = new JScrollPane(table);
    private boolean editable;
    LibroController controller;

    public PanelLibros() {
        ILibroDAO control = new LibroDAOJDBCImpl();
        controller = new LibroController(control);
        initComponents();
        cargarLibros();
    }

    public void cargarLibros() {
        table.setModel(controller.consultarLibros());
        table.getTableHeader().setFont(new Font("SansSerif", Font.ITALIC, 14));
        table.setFont(new java.awt.Font("Tahoma", 0, 12));
        adjustTextToTable();
    }

    public void removeSelectedRows(JTable table) {
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();
        //int[] rows = table.getSelectedRows();
        //for (int i = 0; i < rows.length; i++) {
        int column = 0;
        int row = table.getSelectedRow();
        String value = model.getValueAt(row, column).toString();
        controller.eliminarLibro(Integer.parseInt(value));
        model.removeRow(row);
        //}
    }

    private void adjustTextToTable() {
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if (width > 300) {
                width = 300;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        // Establece el gestor de organización en forma de retícula de tamaño 10x1      
        jLabelTitulo = new JLabel("Gestión de Libros", SwingConstants.CENTER);
        add(jLabelTitulo, BorderLayout.NORTH);
        Font aux = jLabelTitulo.getFont();
        jLabelTitulo.setFont(new Font(aux.getFontName(), aux.getStyle(), 20));
        editable = false;
        table = new JTable() {
            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 0) {
                    return editable; //To change body of generated methods, choose Tools | Templates.
                }
                return true;
            }
        };
        jsp = new JScrollPane(table);
        table.setSelectionModel(new ForcedListSelectionModel());
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete selected row...");
        deleteItem.addActionListener((ActionEvent e) -> {
            removeSelectedRows(table);
            //JOptionPane.showMessageDialog(null, "Right-click performed on table and choose DELETE");
        });
        JMenuItem addItem = new JMenuItem("Add...");
        addItem.addActionListener((ActionEvent e) -> {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            editable = true;
            model.addRow(new Object[]{"id", "titulo", Calendar.getInstance().get(Calendar.YEAR) + "", "precio"});

        });
        popupMenu.add(addItem);
        popupMenu.add(deleteItem);
        table.setComponentPopupMenu(popupMenu);
        TableCellListener tcl = new TableCellListener(table, action);
        add(jsp, BorderLayout.AFTER_LAST_LINE);
    }

    Action action = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            TableCellListener tcl = (TableCellListener) e.getSource();
            System.out.println("Row   : " + tcl.getRow());
            System.out.println("Column: " + tcl.getColumn());
            System.out.println("Old   : " + tcl.getOldValue());
            System.out.println("New   : " + tcl.getNewValue());
            String oldValue = tcl.getOldValue().toString();
            if (!oldValue.equals("id") && !oldValue.equals("titulo") && !oldValue.equals("2021")) {
                if (!oldValue.equals("precio")) {
                    int libId = Integer.parseInt(table.getModel().getValueAt(tcl.getRow(), 0).toString());
                    String libNombre = table.getModel().getValueAt(tcl.getRow(), 1).toString();
                    int libPub = Integer.parseInt(table.getModel().getValueAt(tcl.getRow(), 2).toString());
                    int ediId = 1; //by now!
                    int autId = 1; //by now!
                    double libPrecio = Double.parseDouble(table.getModel().getValueAt(tcl.getRow(), 3).toString());
                    Libro l = new Libro(libId, libNombre, libPub, ediId, autId, libPrecio);
                    controller.actualizarLibro(l);
                } else {
                    int libId = Integer.parseInt(table.getModel().getValueAt(tcl.getRow(), 0).toString());
                    String libNombre = table.getModel().getValueAt(tcl.getRow(), 1).toString();
                    int libPub = Integer.parseInt(table.getModel().getValueAt(tcl.getRow(), 2).toString());
                    int ediId = 1; //by now!
                    int autId = 1; //by now!
                    double libPrecio = Double.parseDouble(table.getModel().getValueAt(tcl.getRow(), 3).toString());
                    Libro l = new Libro(libId, libNombre, libPub, ediId, autId, libPrecio);
                    controller.agregarLibro(l);
                    editable = false;
                }
            }
        }
    };
}
