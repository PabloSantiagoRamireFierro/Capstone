/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cupcakes;

import DataBase.ConexionMySQL;
import Entidades.Insumo;
import Entidades.Producto;
import Fecha.Fecha;
import Repositorio.InsumoRepo;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Pablo Ramirez
 */
public class ActualizarController implements Initializable {
    
    ObservableList<Producto> dataProducto = FXCollections.observableArrayList();
    ObservableList<Insumo> dataInsumo = FXCollections.observableArrayList();
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
    private TableView<Insumo> TablaInventario;
    @FXML
    private TableColumn<Insumo, Integer> col_id;
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
    private ChoiceBox<String> ChoiceSeleccion;
    @FXML
    private TextField txtStock;
    @FXML
    private Label LabelUtil;
    @FXML
    private Button btnInsumos;
    @FXML
    private Button btnProductos;
    @FXML
    private Button btnActualizar;
    @FXML
    private TextField txtId;
    @FXML
    private DatePicker Fecha1;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaInsumo();
        configurarTablaProducto();
        try {
            rellenarTablaProducto();
        } catch (SQLException ex) {
            Logger.getLogger(ActualizarController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rellenarTablaInsumo();
        } catch (SQLException ex) {
            Logger.getLogger(ActualizarController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ChoiceSeleccion.getItems().addAll("Insumo","Producto");
    }    

    @FXML
    private void btnInsumos_click(ActionEvent event) throws IOException {
        Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Inventario.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Inventario");
           
            
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
    private void btnProductos_click(ActionEvent event) throws IOException {
        Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("GestionProductos.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Productos");
           
            
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
    private void btnActualizar_click(ActionEvent event) throws SQLException {
        String Decision,ids, stock;
        long Fecha_long;
        Fecha_long = Fecha.getFechaLong(Fecha1.getValue());
        Decision = ChoiceSeleccion.getValue();
        ids = txtId.getText();
        stock = txtStock.getText();
        Connection conn = ConexionMySQL.conectar();
        if(Decision != "Producto" && 
           Decision != "Insumo"  ){
            
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("Seleccione alguna opción");
            return;
        }
        else if(ids.equals("") || stock.equals("")){
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("Ingrese todos los campos");
            return;
        }
        else if((Fecha1.getValue())== null){
            InsumoRepo insumo = new InsumoRepo();
            insumo.alerta("Ingrese una Fecha");
            return;
        }
        
        if(Decision == "Insumo"){
            
            if(dbexisteRegistro1(conn, ids) == false){
                InsumoRepo insumo = new InsumoRepo();
                insumo.alerta("No existe el registro");
            }
            else{
                int id = (int)Long.parseLong(txtId.getText());
                int stocks = (int)Long.parseLong(txtStock.getText());
                Insumo insumo;
                insumo = new Insumo(stocks,Fecha_long,id);

                InsumoRepo insumoRepo = new InsumoRepo();
                insumoRepo.modificar(insumo);
                
            }
        }
        if(Decision == "Producto"){
            if(dbexisteRegistro2(conn, ids) == false){
                InsumoRepo insumo = new InsumoRepo();
                insumo.alerta("No existe el registro");
            }
            else{
                int id = (int)Long.parseLong(txtId.getText());
                int stocks = (int)Long.parseLong(txtStock.getText());
                Producto producto;
                producto= new Producto(stocks,id,Fecha_long);
                
                ProductoRepo productoRepo = new ProductoRepo();
                productoRepo.modificar2(producto);
                        
                
            }
        }
        rellenarTablaInsumo();
        rellenarTablaProducto();
    }
    
    public void configurarTablaProducto(){
        ColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        ColTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        ColPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        ColStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ColFecha.setCellValueFactory(new PropertyValueFactory<>("fechastring"));
        TablaProducto.setItems(dataProducto);
    }
    public void configurarTablaInsumo(){
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_marca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        col_cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        col_costo.setCellValueFactory(new PropertyValueFactory<>("costo"));
        col_fecha.setCellValueFactory(new PropertyValueFactory<>("fechastring"));
        TablaInventario.setItems(dataInsumo);
    }
    public void rellenarTablaInsumo() throws SQLException{
        dataInsumo.clear();
        InsumoRepo insumo = new InsumoRepo();
        ObservableList<Insumo> insumos = insumo.buscarTodos2();
        dataInsumo.setAll(insumos);
    }
    public void rellenarTablaProducto() throws SQLException{
        dataProducto.clear();
        ProductoRepo habitacionRepo = new ProductoRepo();
        ObservableList<Producto> productos = habitacionRepo.buscarTodos();
        dataProducto.setAll(productos);
    }
    public  boolean dbexisteRegistro1(Connection Conn, String id){
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             sSQL = "SELECT * FROM insumo WHERE id ="+id;


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
    public  boolean dbexisteRegistro2(Connection Conn, String id){
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             sSQL = "SELECT * FROM producto WHERE id ="+id;


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
