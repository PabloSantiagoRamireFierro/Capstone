/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cupcakes;

import DataBase.ConexionMySQL;
import DataBase.JdbcHelper;
import Entidades.Producto;
import Repositorio.ProductoRepo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Pablo Ramirez
 */
public class GestionProductosController implements Initializable {
    
    ObservableList<Producto> dataProducto = FXCollections.observableArrayList();
    @FXML
    private TextField txtNombre_Crear;
    @FXML
    private TextField txtTipo_Crear;
    @FXML
    private TextField txtId_Eliminar;
    @FXML
    private TextField txtPrecio_Crear;
    @FXML
    private TextField txtAtributo_Editar;
    @FXML
    private TextField txtId_Editar;
    @FXML
    private Button btnCrear;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private TableView<Producto> TablaProducto;
    @FXML
    private TableColumn<Producto, Integer> ColId;
    @FXML
    private TableColumn<Producto, String> ColNombreProducto;
    @FXML
    private TableColumn<Producto, String> ColTipo;
    @FXML
    private TableColumn<Producto, Integer> ColStock;
    @FXML
    private TableColumn<Producto, Integer> ColPrecio;
    @FXML
    private TableColumn<Producto, String> ColFecha;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnSalir;
    @FXML
    private ChoiceBox<String> ChoiceAtributo_Editar;
    @FXML
    private Button btnActualizarStock;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        try {
            rellenarTablaHabitacion();
        } catch (SQLException ex) {
            Logger.getLogger(GestionProductosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ChoiceAtributo_Editar.getItems().addAll("Nombre","Tipo","Precio");
        
    }    

    @FXML
    private void btnCrear_click(ActionEvent event) throws SQLException {
        Connection conn = ConexionMySQL.conectar();
        Producto producto;
        String p = txtPrecio_Crear.getText();
        String nombre = txtNombre_Crear.getText();
        String tipo = txtTipo_Crear.getText();
        boolean esEnteroP = isInteger(p);
 
        if(nombre.equals("") || p.equals("")|| tipo.equals("") ){
            ProductoRepo method = new ProductoRepo();
            method.alerta("Llene todos los campos");
            return; 
        }
        
        int precio = (int) Long.parseLong(txtPrecio_Crear.getText());
        //restriccion para que escriba numero el aweonao culiao
        if(esEnteroP == false){
            ProductoRepo productoRepo = new ProductoRepo();
            productoRepo.alerta("Ingresar un numero de habitacion correcto");
        }
        //Numeros negativos
        else if(precio <=0){
            ProductoRepo productoRepo = new ProductoRepo();
            productoRepo.alerta("Ingrese un precio válido");
        }
        //si existe el registro
        else if(dbexisteRegistroInicial(conn, nombre) == true){
            ProductoRepo productoRepo = new ProductoRepo();
            productoRepo.alerta("Ya existe el producto" + nombre);
        }
        else{
            producto = new Producto(nombre, tipo, precio);
        
            ProductoRepo productoRepo = new ProductoRepo();
            productoRepo.crear(producto);
        
            rellenarTablaHabitacion();
            vaciarCampos();
        }
    }

    @FXML
    private void btnEditar_click(ActionEvent event) throws SQLException {
        Connection conn = ConexionMySQL.conectar();
        Producto producto;
        String n = txtId_Editar.getText();
        int id = (int) Long.parseLong(txtId_Editar.getText());   
        String tipo = ChoiceAtributo_Editar.getValue();
        String algo = txtAtributo_Editar.getText();
        boolean esEnteroN = isInteger(n);
        
        if(esEnteroN == false){
            ProductoRepo productoRepo = new ProductoRepo();
            productoRepo.alerta("Ingresar una ID valida");
        }
        else if(dbexisteRegistro(conn, n) == false){
            ProductoRepo method = new ProductoRepo();
            method.alerta("No existe el producto");
        }
        else{
            producto = new Producto(tipo, id,algo);
        
            ProductoRepo productoRepo = new ProductoRepo();
            productoRepo.modificar(producto);
        
            rellenarTablaHabitacion();
            vaciarCampos();
        }
    
    }

    @FXML
    private void btnEliminar_click(ActionEvent event) throws SQLException {
        
        Connection conn = ConexionMySQL.conectar();
        String nHab = txtId_Eliminar.getText();
        boolean esEnteroNHAB = isInteger(nHab);
        
        if(esEnteroNHAB == false){
            ProductoRepo productoRepo = new ProductoRepo();
            productoRepo.alerta("Ingresar una ID valida");
        }
        else if(dbexisteRegistro(conn, nHab) == false){
            ProductoRepo method = new ProductoRepo();
            method.alerta("No existe el producto");
        }
        else{
            int opcion = JOptionPane.showConfirmDialog(null,"¿Desea Eliminar el producto?",
                    "Confirmacion",JOptionPane.YES_NO_OPTION, 2);
            if(opcion == JOptionPane.YES_OPTION){
                String numero = txtId_Eliminar.getText();
                int id = Integer.parseInt(numero);
                ProductoRepo productoRepo = new ProductoRepo();
                productoRepo.eliminar(id);
                JOptionPane.showMessageDialog(null,"Se ha eliminado el registro",
                        "Aviso",JOptionPane.INFORMATION_MESSAGE);

                rellenarTablaHabitacion();
                vaciarCampos();
            }
        }
    }


    @FXML
    private void btnVolver_click(ActionEvent event) throws IOException {
        Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("MenuInventario.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Menu");
           
            
            //al cerrar la ventana de Libros
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent e){
                    Platform.exit();
                    System.exit(0);
                }
            });

            stage.setScene(scene);
            stage.show();
            
            //ocultar la ventana de Login
            ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void btnSalir_click(ActionEvent event) {
        String query = "DELETE FROM admin";
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        System.exit(0);
    }

    @FXML
    private void btnActualizarStock_click(ActionEvent event) throws IOException {
        Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Actualizar.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Actualizar");
           
            
            //al cerrar la ventana de Libros
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent e){
                    Platform.exit();
                    System.exit(0);
                }
            });

            stage.setScene(scene);
            stage.show();
            
            //ocultar la ventana de Login
            ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    public void configurarTabla(){
        ColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        ColTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        ColPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        ColFecha.setCellValueFactory(new PropertyValueFactory<>("fecha_long"));
        ColStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TablaProducto.setItems(dataProducto);
    }
    
    public void rellenarTablaHabitacion() throws SQLException{
        dataProducto.clear();
        ProductoRepo habitacionRepo = new ProductoRepo();
        ObservableList<Producto> productos = habitacionRepo.buscarTodos();
        dataProducto.setAll(productos);
    }
    
    private void vaciarCampos(){
        txtNombre_Crear.setText("");
        txtTipo_Crear.setText("");
        txtId_Eliminar.setText("");
        txtPrecio_Crear.setText("");
        txtAtributo_Editar.setText("");
        txtId_Editar.setText("");
                
    }
    
    public static boolean isInteger(String s) {
    return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
               if(s.length() == 1) return false;
               else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
    public  boolean dbexisteRegistro(Connection Conn, String id_a_buscar){
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             sSQL = "SELECT * FROM producto WHERE id ='" + id_a_buscar + "'";


             oSt = Conn.createStatement();
             oRs = oSt.executeQuery(sSQL);

             if(oRs.next()){
                if(oRs.getRow() > 0){
                    dbexisteRegistro= true;
                }
             }

             if (oSt != null) {oSt.close();oSt = null;}
             if (oRs != null) {oRs.close();oRs = null;}
         }catch(SQLException err){

             oSt = null;
             oRs = null;
             sSQL=null;
         }catch(Exception err){

             oSt = null;
             oRs = null;
             sSQL=null;  
         }finally{
             oSt = null;
             oRs = null;
             sSQL=null;
         }
         return dbexisteRegistro;
    }
    public  boolean dbexisteRegistroInicial(Connection Conn, String nombre){
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             sSQL = "SELECT * FROM producto WHERE nombre ='" + nombre + "'";


             oSt = Conn.createStatement();
             oRs = oSt.executeQuery(sSQL);

             if(oRs.next()){
                if(oRs.getRow() > 0){
                    dbexisteRegistro= true;
                }
             }

             if (oSt != null) {oSt.close();oSt = null;}
             if (oRs != null) {oRs.close();oRs = null;}
         }catch(SQLException err){

             oSt = null;
             oRs = null;
             sSQL=null;
         }catch(Exception err){

             oSt = null;
             oRs = null;
             sSQL=null;  
         }finally{
             oSt = null;
             oRs = null;
             sSQL=null;
         }
         return dbexisteRegistro;
 }
}
