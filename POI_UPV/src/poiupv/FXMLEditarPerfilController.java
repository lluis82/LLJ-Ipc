/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import model.Navegacion;
import poiupv.Transfer.*;

/**
 * FXML Controller class
 *
 * @author lluis
 * basado en este video: https://www.youtube.com/watch?v=hvsvb-BVNoE
 * 
 */
public class FXMLEditarPerfilController implements Initializable {

    @FXML
    private ImageView imageviewAvatar;
    @FXML
    private Label labelCerrarSesion;
    Navegacion t;
    @FXML
    private Button buttonAtras;
    @FXML
    private Label labelAD;
    @FXML
    private Label labelAvatar;
    @FXML
    private TextField tfNombre;
    @FXML
    private PasswordField tfContra;
    @FXML
    private PasswordField tfContra2;
    @FXML
    private TextField tfCorreo;
    @FXML
    private DatePicker datepicker;
    @FXML
    private Button buttonConfirmar;
    Transfer transfer = new Transfer();
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            t = Navegacion.getSingletonNavegacion();
        } catch (NavegacionDAOException ex) {
            java.util.logging.Logger.getLogger(FXMLoginController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        tfNombre.setText(transfer.getUser());
    }    

    @FXML
    private void cerrarSesion(MouseEvent event) throws IOException, Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesión");
        alert.setHeaderText("Cerrar Sesión");
        alert.setContentText("¿Seguro que quieres cerrar la sesión?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            
            loadStage("/FXML/FXMLLogIn.fxml", event);
        } else {
            System.out.println("CANCEL");
        }
        
        
    }
    
    private void loadStage(String fxmlDocumentfxml, MouseEvent event) throws IOException {
        // El cambio de ventanas lo hemos implementado con la ayuda del siguiente video https://www.youtube.com/watch?v=tibw7d1DjEI
        ((Node)(event.getSource())).getScene().getWindow().hide();    
            
            
        Object eventSource = event.getSource(); 
        Node sourceAsNode = (Node) eventSource ;
        Scene oldScene = sourceAsNode.getScene();
        Window window = oldScene.getWindow();
        Stage stage = (Stage) window ;
        stage.hide();

        Parent root = FXMLLoader.load(getClass().getResource(fxmlDocumentfxml));
        Scene scene = new Scene(root);              
        Stage newStage = new Stage();
        newStage.setScene(scene);
        
        switch(fxmlDocumentfxml) {
            case "/FXML/FXMLDocument.fxml":
                newStage.getIcons().add(new Image("/resources/icons/app.png"));
                newStage.setTitle("APP");
                break;
                
            case "/FXML/FXMLSignUp.fxml":
                newStage.getIcons().add(new Image("/resources/icons/signup.png"));
                newStage.setTitle("Sign Up");
                break;
                
            default:
                newStage.setTitle("ERROR");
                break;
        }
        
        newStage.show();  

        newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
            }
        });
    }
    
    private void closebackuptaskandshowmaintask(Event event) throws Exception {
        // Decalaration of Variables
        final Stage stage, stage1;
        FXMLLoader pane;
        Parent taskselectwindow;
        String eventstring;

        // Execution Block
        eventstring = event.getEventType().toString();
        if ("ACTION".equals(eventstring)) {
            stage = (Stage) labelCerrarSesion.getScene().getWindow();
            stage.close();
        } else if ("WINDOW_CLOSE_REQUEST".equals(eventstring)) {
            event.consume();
            stage = (Stage) event.getSource();
            stage.close();
        }
        pane = new FXMLLoader(getClass().getResource("FXMLLogin.fxml"));
        taskselectwindow = (Parent) pane.load();
        stage1 = new Stage();
        stage1.setScene(new Scene(taskselectwindow));
        stage1.setTitle("LogIn");
        stage1.show();
}

    @FXML
    private void atras(ActionEvent event) {
         Stage stage = (Stage) buttonAtras.getScene().getWindow();
         stage.close();
    }

    @FXML
    private void acercaDe(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca De");
        alert.setHeaderText("Acerca De");
        alert.setContentText("Proyecto IPC 2022 - Javier Pérez Asensio & Lluis Terol Martin");
        alert.show();
    }

    
    //codigo de https://teamtreehouse.com/community/setting-selected-image-file-to-imageview-in-javafx
    @FXML
    private void getAvatar(MouseEvent event) throws MalformedURLException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(new Stage());
        String xd="file:/Users/lluis/Documents/Carrera/2ndo/2ndo%20cuatri/IPC/LLJ-Ipc/POI_UPV/build/classes/resources/avatars";
        //String xd = imageviewAvatar.getImage().getUrl();
        File recordsDir = new File(xd);
        chooser.setInitialDirectory(recordsDir);
        System.out.print(chooser.getInitialDirectory());
        if (file != null) {
//            String imagepath = file.getPath();
//            System.out.println("file:" + imagepath);
            String imagepath = file.toURI().toURL().toString();
            Image image = new Image(imagepath);
            if(image.getHeight()>500 || image.getWidth()>500){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error imagen");
                alert.setHeaderText("Por favor selecciona una foto que no supere 500x500");
                /*alert.setContentText("You didn't select a file!");*/
                alert.showAndWait();
                getAvatar(event);
            }
            else {
                //falta guardar en base de datos
                imageviewAvatar.setImage(image);
            } 
            
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info Imagen");
            alert.setHeaderText("Por favor selecciona un archivo");
            /*alert.setContentText("You didn't select a file!");*/
            alert.showAndWait();
        }
    }
}
