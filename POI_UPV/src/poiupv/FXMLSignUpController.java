/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import com.sun.javafx.logging.PlatformLogger.Level;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.System.Logger;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.converter.LocalDateStringConverter;
import javax.swing.JFrame;
import model.Navegacion;
import model.User;
import static model.User.checkEmail;
import static model.User.checkNickName;
import static model.User.checkPassword;



/**
 *
 * @author svalero
 */
public class FXMLSignUpController implements Initializable {


 
    //properties to control valid fieds values. 
    private BooleanProperty validPassword;
    private BooleanProperty validEmail;
    private BooleanProperty equalPasswords;  
    private BooleanProperty validUsername;
    private BooleanProperty validDate;

    //private BooleanBinding validFields;
    
    //When to strings are equal, compareTo returns zero
    private final int EQUALS = 0;
    @FXML
    private Label lIncorrectEmail;
    @FXML
    private TextField eemail;
    @FXML
    private PasswordField epassword;
    @FXML
    private PasswordField epassword2;
    @FXML
    private Label lPassDifferent;
    @FXML
    private Label lIncorrectPass;
    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;
    @FXML
    private TextField eusername;
    @FXML
    private Button changeAvatar;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label lIncorrectDate;
    
    Navegacion t;
    
    @FXML
    private Label lIncorrectUsername;
    @FXML
    private ImageView imageAvatar;
   
   
    
    
    /**
     * Updates the boolProp to false.Changes to red the background of the edit. 
     * Makes the error label visible and sends the focus to the edit. 
     * @param errorLabel label added to alert the user
     * @param textField edit text added to allow user to introduce the value
     * @param boolProp property which stores if the value is correct or not
     */
    private void manageError(Label errorLabel,TextField textField, BooleanProperty boolProp ){
        boolProp.setValue(Boolean.FALSE);
        showErrorMessage(errorLabel,textField);
        textField.requestFocus();
 
    }
    /**
     * Updates the boolProp to true. Changes the background 
     * of the edit to the default value. Makes the error label invisible. 
     * @param errorLabel label added to alert the user
     * @param textField edit text added to allow user to introduce the value
     * @param boolProp property which stores if the value is correct or not
     */
    private void manageCorrect(Label errorLabel,TextField textField, BooleanProperty boolProp ){
        boolProp.setValue(Boolean.TRUE);
        hideErrorMessage(errorLabel,textField);
        
    }
    
    /**
     * Updates the boolProp to false.Changes to red the background of the edit. 
     * Makes the error label visible and sends the focus to the edit. 
     * @param errorLabel label added to alert the user
     * @param textField edit text added to allow user to introduce the value
     * @param boolProp property which stores if the value is correct or not
     */
    private void manageErrorDatePicker(Label errorLabel, DatePicker datepicker, BooleanProperty boolProp ){
        boolProp.setValue(Boolean.FALSE);
        showErrorMessageDatePicker(errorLabel,datepicker);
        datepicker.requestFocus();
 
    }
    /**
     * Updates the boolProp to true. Changes the background 
     * of the edit to the default value. Makes the error label invisible. 
     * @param errorLabel label added to alert the user
     * @param textField edit text added to allow user to introduce the value
     * @param boolProp property which stores if the value is correct or not
     */
    private void manageCorrectDatePicker(Label errorLabel, DatePicker datepicker, BooleanProperty boolProp ){
        boolProp.setValue(Boolean.TRUE);
        hideErrorMessageDatePicker(errorLabel,datepicker);
        
    }
    
    /**
     * Changes to red the background of the edit and
     * makes the error label visible
     * @param errorLabel
     * @param textField 
     **/
    private void showErrorMessage(Label errorLabel,TextField textField)
    {
        errorLabel.visibleProperty().set(true);
        textField.styleProperty().setValue("-fx-background-color: #FCE5E0");    
    }
    /**
     * Changes the background of the edit to the default value
     * and makes the error label invisible.
     * @param errorLabel
     * @param textField 
     */
    private void hideErrorMessage(Label errorLabel,TextField textField)
    {
        errorLabel.visibleProperty().set(false);
        textField.styleProperty().setValue("");
    }

    
    
    /**
     * Changes to red the background of the edit and
     * makes the error label visible
     * @param errorLabel
     * @param textField 
     **/
    private void showErrorMessageDatePicker(Label errorLabel, DatePicker datepicker)
    {
        errorLabel.visibleProperty().set(true);
        datepicker.styleProperty().setValue("-fx-background-color: #FCE5E0");    
    }
    /**
     * Changes the background of the edit to the default value
     * and makes the error label invisible.
     * @param errorLabel
     * @param datePicker 
     */
    private void hideErrorMessageDatePicker(Label errorLabel, DatePicker datepicker)
    {
        errorLabel.visibleProperty().set(false);
        datepicker.styleProperty().setValue("");
    }

    

    
    //=========================================================
    // you must initialize here all related with the object 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        validEmail = new SimpleBooleanProperty();
        validPassword = new SimpleBooleanProperty();   
        equalPasswords = new SimpleBooleanProperty();
        validUsername = new SimpleBooleanProperty();
        validDate = new SimpleBooleanProperty();
        
        validPassword.setValue(Boolean.FALSE);
        validEmail.setValue(Boolean.FALSE);
        equalPasswords.setValue(Boolean.FALSE);
        validUsername.setValue(Boolean.FALSE);
        validDate.setValue(Boolean.FALSE);
       
        
        
        BooleanBinding validFields = Bindings.and(validEmail, validPassword)
                 .and(equalPasswords);
        

        // Formato -> https://acodigo.blogspot.com/2017/08/datepicker-control-javafx-para-manejar.html
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        datePicker.setConverter(new LocalDateStringConverter(formatter, null));
        
        SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
        
        datePicker.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                   LocalDate today = LocalDate.now().minusYears(16);
                   setDisable(empty || date.compareTo(today) > 0 );
                }
            };
        });
        
        datePicker.setShowWeekNumbers(false);
        
        try {
            t = Navegacion.getSingletonNavegacion();
        } catch (NavegacionDAOException ex) {
            java.util.logging.Logger.getLogger(FXMLoginController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
      
        
    } 
   
    private boolean checkEditEmail() {
        boolean correct = true;
        if (!checkEmail(eemail.textProperty().getValueSafe())) {
            manageError(lIncorrectEmail, eemail, validEmail);
            correct = false;
        }
        else { manageCorrect(lIncorrectEmail, eemail, validEmail); }
        return correct;
    }
    
    private boolean checkEditPass() {
        boolean correct = true;
        if (!checkPassword(epassword.textProperty().getValueSafe())) {
            manageError(lIncorrectPass, epassword, validPassword);
            correct = false;
        }
        else { manageCorrect(lIncorrectPass, epassword, validPassword); }
        return correct;
    }
    
    private boolean checkEquals() {
        boolean correct = true;
        if (epassword.textProperty().getValueSafe().compareTo(
            epassword2.textProperty().getValueSafe()) != EQUALS) {
            
            showErrorMessage(lPassDifferent,epassword2);
            equalPasswords.setValue(Boolean.FALSE);
            epassword.textProperty().setValue("");
            epassword2.textProperty().setValue("");
            epassword.requestFocus();
            correct = false;
        }
        else { manageCorrect(lPassDifferent, epassword2, equalPasswords); }
        return correct;
    }
    
    private boolean checkEditUsername() {
        boolean correct = true;
        if (!checkNickName(eusername.textProperty().getValueSafe()) || t.exitsNickName(eusername.textProperty().getValueSafe())) {
            manageError(lIncorrectUsername, eusername, validUsername);
            correct = false;
            if (t.exitsNickName(eusername.textProperty().getValueSafe())) {
                lIncorrectUsername.setText("User already registered.");
            } else {
                lIncorrectUsername.setText("Incorrect username. It has to be between 6 and 15 characters including \"-\" and \"_\" without blanks.");
            }
        }
        else {
            manageCorrect(lIncorrectUsername, eusername, validUsername);
        }
        return correct;
    }
    
    private boolean checkEditDate() {
        boolean correct = true;
            LocalDate date = LocalDate.now().minusYears(16);
            LocalDate date2 = datePicker.getValue();
            if (date2 == null || !date.isAfter(date2)) {
                manageErrorDatePicker(lIncorrectDate, datePicker, validDate);
                datePicker.getEditor().clear();
                datePicker.setValue(null);
                datePicker.requestFocus();
                correct = false;
            } else { manageCorrectDatePicker(lIncorrectDate, datePicker, validDate); }
        return correct;
    }
    

        @FXML
    private void changeAvatarButton(ActionEvent event) throws FileNotFoundException, MalformedURLException {
        
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.getExtensionFilters().addAll(
        new ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.gif"));
        
        String path = "src/resources/avatars";
        File recordsDir = new File(path);
        chooser.setInitialDirectory(recordsDir);
        
        File file = chooser.showOpenDialog(new Stage());
        
        if (file != null) {
            String imagepath = file.toURI().toURL().toString();
            Image image = new Image(imagepath);
            if(image.getHeight()>800 || image.getWidth()>800){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error imagen");
                alert.setHeaderText("Por favor selecciona una foto que no supere 500x500");
                alert.showAndWait();
                changeAvatarButton(event);
            }
            else {
                imageAvatar.setImage(image);
            } 
            
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info Imagen");
            alert.setHeaderText("Por favor selecciona un archivo");
            alert.showAndWait();
        }
        
    }
    
    @FXML
    private void handleAcceptAction(ActionEvent event) throws IOException, NavegacionDAOException {
        
        Image avatar = imageAvatar.getImage();
        Image defAvat = new Image("/resources/avatars/default.png");
        
        if (checkEditEmail() & (checkEditPass() && checkEquals()) & checkEditUsername() & checkEditDate()) {
            // Si la foto del avatar NO ha cambiado, usamos registerUser sin el Image avatar, sino, si
            if (avatar == defAvat) {
                t.registerUser(eusername.textProperty().getValueSafe(), eemail.textProperty().getValueSafe(),
                        epassword.textProperty().getValueSafe(), datePicker.getValue());
            } else {
                t.registerUser(eusername.textProperty().getValueSafe(), eemail.textProperty().getValueSafe(),
                        epassword.textProperty().getValueSafe(), avatar, datePicker.getValue());
            }
            
            loadStage("/FXML/FXMLLogIn.fxml", event);
        }
        
        eemail.textProperty().setValue("");
        epassword.textProperty().setValue("");
        epassword2.textProperty().setValue("");
        
        validEmail.setValue(Boolean.FALSE);
        validPassword.setValue(Boolean.FALSE);
        equalPasswords.setValue(Boolean.FALSE);
    }
    
    @FXML
    private void handleCancelAction(ActionEvent event) throws IOException {
        
        loadStage("/FXML/FXMLLogIn.fxml", event);
        
        eemail.textProperty().setValue("");
        epassword.textProperty().setValue("");
        epassword2.textProperty().setValue("");
        
        validEmail.setValue(Boolean.FALSE);
        validPassword.setValue(Boolean.FALSE);
        equalPasswords.setValue(Boolean.FALSE);
    }
    
    private void loadStage(String fxmlDocumentfxml, ActionEvent event) throws IOException {
        // El cambio de ventanas lo hemos implementado con la ayuda del siguiente video https://www.youtube.com/watch?v=tibw7d1DjEI
        ((Node)(event.getSource())).getScene().getWindow().hide();
            
        Object eventSource = event.getSource(); 
        Node sourceAsNode = (Node) eventSource ;
        Scene oldScene = sourceAsNode.getScene();
        Window window = oldScene.getWindow();
        Stage stage = (Stage) window;
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
                
            case "/FXML/FXMLLogIn.fxml":
                newStage.getIcons().add(new Image("/resources/icons/login.png"));
                newStage.setTitle("Log In");
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

}