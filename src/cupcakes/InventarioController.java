/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cupcakes;

import DataBase.ConexionMySQL;
import DataBase.JdbcHelper;
import Entidades.Insumo;
import Fecha.Fecha;
import Repositorio.InsumoRepo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Pablo Ramirez
 */
public class InventarioController implements Initializable {
    ObservableList<Insumo> data = FXCollections.observableArrayList();
    @FXML
    private Button btnSalir;
    @FXML
    private Button btnVolver;
    @FXML
    private TableView<Insumo> DataInventario;
    @FXML
    private TableColumn<Insumo, String> col_nombre;
    @FXML
    private TableColumn<Insumo, String> col_marca;
    @FXML
    private TableColumn<Insumo, Integer> col_cantidad;
    @FXML
    private TableColumn<Insumo, Integer> col_costo;
    @FXML
    private TableColumn<Insumo, String> col_fecha;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtMarca;
    @FXML
    private ChoiceBox<String> ChoiceMetrica;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtCosto;
    @FXML
    private DatePicker Fecha_Crear;
    @FXML
    private Button btnCrear;
    @FXML
    private TextField txtNombre_Eliminar;
    @FXML
    private TextField txtMarca_Eliminar;
    @FXML
    private DatePicker Fecha_Eliminar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Font x1;
    @FXML
    private Button btnActualizar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ChoiceMetrica.getItems().addAll("Kilogramos","Litros","Unidad");
        configurarTabla();
        try {
            rellenarTabla();
        } catch (SQLException ex) {
            Logger.getLogger(InventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void btnSalir_click(ActionEvent event) {
        String query = "DELETE * FROM admin";
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        System.exit(0);
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
    private void btnCrear_click(ActionEvent event) throws SQLException {
        Insumo insumoFinal;
        String Nombre, Marca, Metrica, Fecha_string;
        int Costo, Cantidad;
        long Fecha_long;
        Connection conn = ConexionMySQL.conectar();
        Nombre= txtNombre.getText();
        Marca= txtMarca.getText();
        Metrica= ChoiceMetrica.getValue();
        
        String costo = txtCosto.getText();
        String cantidad = txtCantidad.getText();
        boolean esEnteroCO = isInteger(costo);
        boolean esEnteroCA = isInteger(cantidad);
        
        int CostoNumero = (int) Long.parseLong(txtCosto.getText());
        int CantidadNumero = (int) Long.parseLong(txtCantidad.getText());
        
        //datos vacios
        if((Fecha_Crear.getValue())== null){
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("Ingrese una Fecha de Vencimiento");
            return;
        }
        else if("".equals(Nombre) ||"".equals(Marca)||"".equals(costo)||"".equals(cantidad)){
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("Llene todos los campos");
            return;
        }
        else if(Metrica != "Kilogramos" && 
            Metrica != "Unidad" &&
            Metrica != "Litros" ){
            
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("Seleccione alguna opción de metrica");
            return;
        }
        
        Fecha_long = Fecha.getFechaLong(Fecha_Crear.getValue());
        Fecha_string = Fecha.getFechaString(Fecha_long);
        
        Date tiempofecha = new Date();
        Calendar now = Calendar.getInstance();
        int añoActual = (now.get(Calendar.YEAR));
        int mesActual = (now.get(Calendar.MONTH)+1);
        int diaActual = (now.get(Calendar.DAY_OF_MONTH));
        //Fechas   
        String año = String.valueOf(Fecha_string.charAt(6)) + String.valueOf(Fecha_string.charAt(7)) + String.valueOf(Fecha_string.charAt(8)) + String.valueOf(Fecha_string.charAt(9));
        String mes = String.valueOf(Fecha_string.charAt(3)) + String.valueOf(Fecha_string.charAt(4));
        String dia = String.valueOf(Fecha_string.charAt(0)) + String.valueOf(Fecha_string.charAt(1));
        
        if(Integer.parseInt(año) < añoActual){
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("El año no es correcto. Ingresa otra fecha"); 
        }
        else if(Integer.parseInt(año) == añoActual && Integer.parseInt(mes) < mesActual){
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("El mes no es correcto. Ingresa otra fecha");
        }
        else if(Integer.parseInt(año) == añoActual && Integer.parseInt(mes) == mesActual && Integer.parseInt(dia) < diaActual){
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("El día no es correcto. Ingresa otra fecha"); 
        }
        //restriccion para que escriba numero el aweonao culiao
        else if(esEnteroCA == false){
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("Ingrese cantidades numericas en el campo solicitado");
        }
        else if(esEnteroCO == false){
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("Ingrese cantidades numericas en el campo solicitado");
        }
        else if(CostoNumero <=0){
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("Ingrese numeros validos en el campo solicitado");
        }
        else if(CantidadNumero <=0){
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("Ingrese numeros validos en el campo solicitado");
        }
        else{
            insumoFinal = new Insumo(Nombre,Marca,Metrica,CostoNumero, CantidadNumero,Fecha_long);
            InsumoRepo insumo = new InsumoRepo();
            insumo.crear(insumoFinal);
            rellenarTabla();
            vaciarCampos();
        }
        
    }

    @FXML
    private void btnEliminar_click(ActionEvent event) throws SQLException {
        Connection conn = ConexionMySQL.conectar();
        String Fecha_string;
        long Fecha_long;
        String nombre = txtNombre_Eliminar.getText();
        String marca = txtMarca_Eliminar.getText();
        Fecha_long = Fecha.getFechaLong(Fecha_Eliminar.getValue());
        Fecha_string = Fecha.getFechaString(Fecha_long);
        
        if(dbexisteRegistro(conn, nombre, marca) == false){ 
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("No existe el insumo");
        }
        else{
            int opcion = JOptionPane.showConfirmDialog(null,"¿Desea Eliminar el registro?",
                    "Confirmacion",JOptionPane.YES_NO_OPTION, 2);
            if(opcion == JOptionPane.YES_OPTION){
                InsumoRepo insumo = new InsumoRepo();
                insumo.eliminar(nombre,marca,Fecha_long);
                JOptionPane.showMessageDialog(null,"Se ha eliminado el registro",
                        "Aviso",JOptionPane.INFORMATION_MESSAGE);

                rellenarTabla();
                vaciarCampos();
            }
            }
    }
    public void configurarTabla(){
        col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_marca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        col_costo.setCellValueFactory(new PropertyValueFactory<>("costo"));
        col_cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        col_fecha.setCellValueFactory(new PropertyValueFactory<>("fechastring"));
        DataInventario.setItems(data);
    }
    
    public void rellenarTabla() throws SQLException{
        data.clear();
        InsumoRepo insumo = new InsumoRepo();
        ObservableList<Insumo> insumos = insumo.buscarTodos();
        data.setAll(insumos);
    }
    
    private void vaciarCampos(){
        txtNombre.setText("");
        txtNombre_Eliminar.setText("");
        txtMarca.setText("");
        txtMarca_Eliminar.setText("");
        txtCosto.setText("");
        txtCantidad.setText("");
        Fecha_Crear.setValue(null);
        Fecha_Eliminar.setValue(null);
                
    }
    
    public static boolean isInteger(String s) {
    try { //Try to make the input into an integer
        Integer.parseInt(s);
        return true; //Return true if it works
    }
    catch( Exception e ) { 
        return false; //If it doesn't work return false
    }
    }
    public  boolean dbexisteRegistro(Connection Conn, String nombre, String marca){
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             sSQL = "SELECT * FROM insumo WHERE nombre ='"+nombre+"' AND marca = '"+ marca+"'" ;


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

    @FXML
    private void btnActualizar_click(ActionEvent event) throws IOException {
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
}
