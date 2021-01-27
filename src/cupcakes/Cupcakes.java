package cupcakes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Pablo Ramirez
 */
public class Cupcakes extends Application {
    
    @Override
    public void start(Stage stage) throws Exception  {
        stage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("Login_Covita.fxml"));
        
        Scene scene = new Scene(root);
        stage.show();
        stage.setTitle("Covita");
        stage.setScene(scene);
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
