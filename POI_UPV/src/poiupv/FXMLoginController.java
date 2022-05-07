/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Navegacion;
import static model.Navegacion.getSingletonNavegacion;

/**
 * FXML Controller class
 *
 * @author lluis
 */
public class FXMLoginController implements Initializable {

    @FXML
    private Button buttonAccept;
    @FXML
    private Button buttonCancel;
    @FXML
    private TextField eemail;
    @FXML
    private Label lIncorrectEmail;
    @FXML
    private PasswordField epassword;
    @FXML
    private Label lIncorrectPass;
    @FXML
    private Label lPassDifferent;
    Navegacion n;
    
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
     * Changes to red the background of the edit and
     * makes the error label visible
     * @param errorLabel
     * @param textField 
     */
    private void showErrorMessage(Label errorLabel,TextField textField)
    {
        errorLabel.visibleProperty().set(true);
        textField.styleProperty().setValue("-fx-background-color: #FCE5E0");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        eemail.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                checkUserExists();
            }
        });
        
        try {
            n = getSingletonNavegacion();
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(FXMLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleBAcceptonAction(ActionEvent event) {
        
    }

    private void checkUserExists() {
        String nick = eemail.getText();
        //boolean exists = n.existsNickName(nick);
    }
    
}
