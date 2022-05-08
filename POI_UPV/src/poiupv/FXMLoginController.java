/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
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
    Navegacion n;
    @FXML
    private Button buttonCreateAccount;
    Navegacion t;
    
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
        
        try {
            t = Navegacion.getSingletonNavegacion();
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(FXMLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        buttonCancel.setOnAction((event) -> {
            buttonCancel.getScene().getWindow().hide();
        });
    }    

    @FXML
    private void handleBAcceptonAction(ActionEvent event) throws IOException {
        String nick = eemail.getText();
        if (t.loginUser(nick, epassword.getText())==null){lIncorrectPass.setVisible(true); lIncorrectEmail.setVisible(true);}
        else loadStage("/FXML/FXMLDocument.fxml", event);
    }

    @FXML
    private void handleBCreateAccount(ActionEvent event) throws IOException {
        loadStage("/FXML/FXMLSignUp.fxml", event);
    }
    
    private void loadStage(String fxmlDocumentfxml, ActionEvent event) throws IOException {
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
            newStage.show();  
                                    
            newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                }
            });
    }
    
}
