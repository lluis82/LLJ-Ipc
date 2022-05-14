/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author jose
 */
public class PoiUPVApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/FXML/FXMLLogIn.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        // Solamente el codigo para añadir el icono ha sido sacado de: https://stackoverflow.com/questions/10275841/how-to-change-the-icon-on-the-title-bar-of-a-stage-in-java-fx-2-0-of-my-applicat
        stage.getIcons().add(new Image("/resources/icons/LogIn.png"));
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();
        //stage.setOnCloseRequest(this::handleStageClosed);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

//    private void handleStageClosed(WindowEvent event) {
//        // Lanza aviso solamente si el usuario está realizando un test o el mapa tiene algo dibujado/escrito en él
////        if (/* Solo si está en mitad de un test || si el mapa tiene algo dibujado/escrito */) {
//            Alert mensaje = new Alert(Alert.AlertType.CONFIRMATION);
//            mensaje.setTitle("Cerrar aplicación");
//            mensaje.setHeaderText("¿Seguro que quiere cerrar la aplicación?");
//            mensaje.setContentText("Podría llegar a perder información si está realizando un test.\n");
//            Optional<ButtonType> result = mensaje.showAndWait();
//            if (result.isPresent() && result.get() == ButtonType.OK) {
//                Platform.exit();
//            }
////        } else {
////            Platform.exit();
////        }
//    }
    
}
