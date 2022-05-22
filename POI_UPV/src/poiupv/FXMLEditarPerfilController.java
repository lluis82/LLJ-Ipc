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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.DateCell;
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
import javafx.util.converter.LocalDateStringConverter;
import model.Navegacion;
import model.User;


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
   
    User usuario;
    @FXML
    private Label labperfil;
   

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
//        tfNombre.setText(usuario.getNickName());
        
       
        
        
        // Formato -> https://acodigo.blogspot.com/2017/08/datepicker-control-javafx-para-manejar.html
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        datepicker.setConverter(new LocalDateStringConverter(formatter, null));
        
        SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
        
        datepicker.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                   LocalDate today = LocalDate.now().minusYears(16);
                   setDisable(empty || date.compareTo(today) > 0 );
                }
            };
        });
        
        datepicker.setShowWeekNumbers(false);
        
        
       
//        String user = t.toString();
        //System.out.println(usuario);
        
        
      
        
        //System.out.println(pr.toString());
    }    

    @FXML
    private void cerrarSesion(MouseEvent event) throws IOException, Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesión");
        alert.setHeaderText("Cerrar Sesión");
        alert.setContentText("¿Seguro que quieres cerrar la sesión?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
//            Session e = usuario.getSession
//            usuario.getSessions().add(e);
            
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
    
    

    @FXML
    private void acercaDe(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca De");
        alert.setHeaderText("Acerca De");
        alert.setContentText("Proyecto IPC 2022 - Javier Pérez Asensio & Lluis Terol Martin");
        alert.showAndWait();
    }

    
    //codigo de https://teamtreehouse.com/community/setting-selected-image-file-to-imageview-in-javafx
    @FXML
    private void getAvatar(MouseEvent event) throws MalformedURLException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.gif"));
        
        String path = "src/resources/avatars";
        File recordsDir = new File(path);
        chooser.setInitialDirectory(recordsDir);
        
        File file = chooser.showOpenDialog(new Stage());
        
        if (file != null) {
//            String imagepath = file.getPath();
//            System.out.println("file:" + imagepath);
            String imagepath = file.toURI().toURL().toString();
            Image image = new Image(imagepath);
            if(image.getHeight()>800 || image.getWidth()>800){
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

    @FXML
    private void confirmChanges(ActionEvent event) {
    }
    
    public void setUsuario(User user){
        usuario = user;
        imageviewAvatar.setImage(usuario.getAvatar());
//        tfNombre.setText(usuario.getNickName());
        System.out.println(usuario);
    }
    
    public void setTfs(){
        tfNombre.setText(usuario.getNickName());
    }

    @FXML
    private void setperfil(MouseEvent event) {
        
        System.out.println(usuario);
    }

    @FXML
    private void atras(MouseEvent event) throws IOException {
        loadStage("/FXML/FXMLDocument.fxml", event);
    }
}
