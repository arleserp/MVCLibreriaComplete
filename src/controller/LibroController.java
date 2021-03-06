/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.ILibroDAO;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Libro;

/**
 * Ahora creas el controlador, el controlador contiene 2 objetos el modelo, la
 * vista así como los getters y setters para llenar las propiedades del modelo y
 * un método(actualizarVista()) que llama a la vista que a su vez imprime las
 * propiedades del modelo cliente.
 *
 * @author Arles
 */
public class LibroController {

    private ILibroDAO libroDAO;

    public LibroController(ILibroDAO libroDAO) {
        this.libroDAO = libroDAO;
    }

    public DefaultTableModel consultarLibros() {
        String[] titulos = {"Id", "Título", "Año", "Precio"};
        DefaultTableModel modelo = new DefaultTableModel(null, titulos);

        List<Libro> libros = libroDAO.obtenerLibros();
        for (Libro libro : libros) {
            String[] registro = new String[4];
            registro[0] = libro.getLibId() + "";
            registro[1] = libro.getLibNombre();
            registro[2] = libro.getLibPub() + "";
            registro[3] = libro.getPrecio() + "";
            //System.out.println("libro: " + libro);
            modelo.addRow(registro);
        }
        return modelo;
    }

    public void actualizarLibro(Libro l) {
        libroDAO.actualizarLibro(l);
    }

    public void agregarLibro(Libro l) {
        libroDAO.agregarLibro(l);
    }
    
    public void eliminarLibro(int id){
        libroDAO.eliminarLibro(id);
    }
}
