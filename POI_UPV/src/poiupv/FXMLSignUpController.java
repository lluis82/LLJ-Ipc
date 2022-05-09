/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import com.sun.javafx.logging.PlatformLogger.Level;
import java.io.IOException;
import java.lang.System.Logger;
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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.converter.LocalDateStringConverter;


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
         

//        eemail.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue) {
//                checkEditEmail();
//            }
//        });
//        
//        epassword.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue) {
//                checkEditPass();
//            }
//        });
//        
//        epassword2.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue) {
//                checkEquals();
//            }
//        });
        
//        bAccept.disableProperty().bind(
//                Bindings.not(validFields)
//        );
        
//        bCancel.setOnAction((event) -> {
//            bCancel.getScene().getWindow().hide();
//        });

        // Formato -> https://acodigo.blogspot.com/2017/08/datepicker-control-javafx-para-manejar.html
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        datePicker.setConverter(new LocalDateStringConverter(formatter, null));
        
        try{
            SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdformat.parse("01/02/2018");
            } catch (ParseException ex) {
        }
    } 
   
    private boolean checkEditEmail() {
        boolean correct = true;
        if (!Utils.checkEmail(eemail.textProperty().getValueSafe())) {
            manageError(lIncorrectEmail, eemail, validEmail);
            correct = false;
        }
        else { manageCorrect(lIncorrectEmail, eemail, validEmail); }
        return correct;
    }
    
    private boolean checkEditPass() {
        boolean correct = true;
        if (!Utils.checkPassword(epassword.textProperty().getValueSafe())) {
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

        @FXML
    private void changeAvatarButton(ActionEvent event) {
    }

    @FXML
    private boolean datePickerButton(ActionEvent event) {
        boolean correct = true;
        LocalDate date = datePicker.getValue();
        String dateS = date.toString();
        String date2 = "05-06-2017";
        
        if (dateS.compareTo(date2) > 0) {
            showErrorMessage(lIncorrectDate, datePicker, validDate);
            datePicker.DatePicker();
            datePicker.requestFocus();
            correct = false;
        } else { manageCorrect(lIncorrectDate, datePicker, validDate); }
        
        return correct;
    }
    
    @FXML
    private void handleBAcceptonAction(ActionEvent event) throws IOException {
        
        if (checkEditEmail() ==  true) {
            if (checkEditPass() == true) {
                if (checkEquals() == true) {
                    loadStage("/FXML/FXMLDocument.fxml", event);
                }
            }
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