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
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Pablo Ramirez
 */
public class MenuEstadisticas implements Initializable {

    @FXML
    private Button btnEInsumos;
    @FXML
    private Button btnEVentas;
    @FXML
    private Button btnPredicciones;
    @FXML
    private Button btnVolver;
    private Button btnReporte;
    @FXML
    private Font x3;
    @FXML
    private Font x11;
    @FXML
    private BarChart<?, ?> barChart;
    @FXML
    private ChoiceBox<?> choiceaño;
    @FXML
    private ChoiceBox<?> choicetiempo;
    @FXML
    private Button btnCalcular;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    private void btnEInsumos_click(ActionEvent event) {
    }

    @FXML
    private void btnEVentas_click(ActionEvent event) {
    }

    @FXML
    private void btnPredicciones(ActionEvent event) {
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
    private void btnCalcular_click(ActionEvent event) {
    }
    
}
