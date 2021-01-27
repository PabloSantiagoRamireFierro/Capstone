/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cupcakes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Pablo Ramirez
 */
public class CajaController implements Initializable {

    @FXML
    private TableView<?> table;
    @FXML
    private TableColumn<?, ?> col_pro;
    @FXML
    private TableColumn<?, ?> col_total;
    @FXML
    private TableColumn<?, ?> col_ingresos;
    @FXML
    private TableColumn<?, ?> col_fecha;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnRefrescar;
    @FXML
    private Label IngresoTotal;
    @FXML
    private VBox choiceProducto;
    @FXML
    private ChoiceBox<String> choiceReserva;
    @FXML
    private DatePicker dateIngreso;
    @FXML
    private Button btnConsultar;
    @FXML
    private ChoiceBox<?> choiceIR;
    @FXML
    private TextField txtIR;
    @FXML
    private Button btnRegistrar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choiceReserva.getItems().addAll("Cupcakes","Popcakes","Bebidas Frias", "Decoraciones");
        // TODO
    }    

    @FXML
    private void btnVolver_click(ActionEvent event) throws IOException {
        Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("MenuInicio.fxml"));
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
    private void btnRefrescar_click(ActionEvent event) {
    }

    @FXML
    private void btnConsultar_click(ActionEvent event) {
    }

    @FXML
    private void btnRegistrar_click(ActionEvent event) {
    }
    
}
