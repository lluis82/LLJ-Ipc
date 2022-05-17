/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import model.Navegacion;
import model.Problem;
import poiupv.Poi;


     

/**
 *
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {

    //=======================================
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;

    private ListView<Poi> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    private MenuButton map_pin;
    private MenuItem pin_info;
    @FXML
    private Label posicion;
    @FXML
    private Button buttonPoint;
    @FXML
    private Button buttonLine;
    @FXML
    private ImageView buttonArch;
    @FXML
    private ImageView buttonText;
    @FXML
    private ImageView imageviewCarta;
    double x;
    double y;
    @FXML
    private Label labelSelected;
    private AnchorPane paneMenu;
    @FXML
    private ImageView imageviewBurger;
    Navegacion t;
    private List<Problem> l;
    private ObservableList<Problem> listaObservable;
    @FXML
    private Pane paneCarta;
    private Circle c;
    @FXML
    private ListView<Problem> lvProblemas;

    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += ((0.8-0.12)/10));
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -((0.8-0.12)/10));
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    void listClicked(MouseEvent event) {
        Poi itemSelected = map_listview.getSelectionModel().getSelectedItem();

        // Animación del scroll hasta la posicion del item seleccionado
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double scrollH = itemSelected.getPosition().getX() / mapWidth;
        double scrollV = itemSelected.getPosition().getY() / mapHeight;
        final Timeline timeline = new Timeline();
        final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
        final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        // movemos el objto map_pin hasta la posicion del POI
        double pinW = map_pin.getBoundsInLocal().getWidth();
        double pinH = map_pin.getBoundsInLocal().getHeight();
        map_pin.setLayoutX(itemSelected.getPosition().getX());
        map_pin.setLayoutY(itemSelected.getPosition().getY());
        pin_info.setText(itemSelected.getDescription());
        map_pin.setVisible(true);
    }

    private void initData() {
        //hm.put("ELIMINAR_1", new Poi("ELIMINAR_1", "eliminar1", 325, 225));
        //hm.put("ELIMINAR_2", new Poi("ELIMINAR_2", "eliminar2", 600, 360));
        //map_listview.getItems().add(hm.get("ELIMINAR_1"));
        //map_listview.getItems().add(hm.get("ELIMINAR_2"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initData();
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(0.12);
        zoom_slider.setMax(0.8);
        zoom_slider.setValue(0.46);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
        
        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        
        buttonPoint.setOnMouseClicked(this::colocarPunto);
        
        try {
            t = Navegacion.getSingletonNavegacion();
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(FXMLoginController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error");
        }
        l = t.getProblems();
        listaObservable = FXCollections.observableList(l);
        
        //Stage.setOnCloseRequest(this::cerrarAplicacion);
    }

    @FXML
    private void muestraPosicion(MouseEvent event) {
        posicion.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
    }

    @FXML
    private void cerrarAplicacion(ActionEvent event) {
        // Lanza aviso solamente si el usuario está realizando un test o el mapa tiene algo dibujado/escrito en él
        /* Solo si está en mitad de un test || si el mapa tiene algo dibujado/escrito */
//        if (isOnTest || isMapDrawn) {
            Alert mensaje = new Alert(Alert.AlertType.CONFIRMATION);
            mensaje.setTitle("Cerrar aplicación");
            mensaje.setHeaderText("¿Seguro que quiere cerrar la aplicación?");
            mensaje.setContentText("Podría llegar a perder información si está realizando un test.\n");
            Optional<ButtonType> result = mensaje.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ((Stage) zoom_slider.getScene().getWindow()).close();
            }
//        } else {
//            ((Stage) zoom_slider.getScene().getWindow()).close();
//        }
    }
    
    @FXML
    private void acercaDe(ActionEvent event) {
        Alert mensaje= new Alert(Alert.AlertType.INFORMATION);
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2022");
        mensaje.showAndWait();
    }

    @FXML
    private void borrarMapa(ActionEvent event) {
        /* Solo si el mapa tiene algo dibujado/escrito */
//      if (isMapDrawn) {
            Alert mensaje = new Alert(Alert.AlertType.CONFIRMATION);
            mensaje.setTitle("Borrar dibujado");
            mensaje.setHeaderText("¿Seguro que quiere borrar todo lo apuntado en el mapa?");
            Optional<ButtonType> result = mensaje.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //Borrar todo lo dibujado/escrito
            }
//      }
    }

    private void getCoords(MouseEvent event) {
        x = event.getX();
        y = event.getY();
        labelSelected.setText("X: " + x + "\n" + "Y: " + y);
    }
    
//    private void handleStageClosed(WindowEvent event) {
//        // Lanza aviso solamente si el usuario está realizando un test o el mapa tiene algo dibujado/escrito en él
////        if (/* Solo si está en mitad de un test || si el mapa tiene algo dibujado/escrito */) {
//        Alert mensaje = new Alert(Alert.AlertType.CONFIRMATION);
//        mensaje.setTitle("Cerrar aplicación");
//        mensaje.setHeaderText("¿Seguro que quiere cerrar la aplicación?");
//        mensaje.setContentText("Podría llegar a perder información si está realizando un test.\n");
//        Optional<ButtonType> result = mensaje.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            Platform.exit();
//        }
////        } else {
////            Platform.exit();
////        }
//    }    

    private void loadStage(String fxmlDocumentfxml, ActionEvent event) throws IOException {
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

    private void cerrarSesion(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Diálogo de confirmación");
        alert.setHeaderText("Cabecera");
        alert.setContentText("¿Seguro que quieres continuar?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            loadStage("/FXML/FXMLLogIn.fxml", event);
        } else {
            System.out.println("CANCEL");
        }
    }


    @FXML
    private void handleBLine(ActionEvent event) {
    }

    @FXML
    private void editarPerfil(MouseEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow();

        Object eventSource = event.getSource();
        Node sourceAsNode = (Node) eventSource;
        Scene oldScene = sourceAsNode.getScene();
        Window window = oldScene.getWindow();
        Stage stage = (Stage) window;
        

        Parent root = FXMLLoader.load(getClass().getResource("/FXML/FXMLEditarPerfil.fxml"));
        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        
        newStage.getIcons().add(new Image("/resources/icons/signup.png"));
        newStage.setTitle("Edit Profile");
        newStage.show();
    }

    @FXML
    private void problemas(ActionEvent event) {
        lvProblemas.getItems().addAll(listaObservable);
        System.out.println(listaObservable);
    }


    @FXML
    private void colocarPunto1(ActionEvent event) {
        c = new Circle(0, 0, 15);
        imageviewCarta.setOnMousePressed(ev -> {
            
            x = ev.getSceneX()-ev.getX();
            y = ev.getSceneY()-ev.getY();
            labelSelected.setText("X: " + x + "\n" + "Y: " + y);
            
            paneCarta.getChildren().add(c);
            c.setLayoutX(x);
            c.setLayoutY(y);
            c.setVisible(true);
        });
        
        c.setOnMouseDragged(e ->{
            //double baseX = c.getTranslateX();
            //double baseY = c.getTranslateY();
//            c.setTranslateX(despX + baseX);
//            c.setTranslateY(despY + baseY);
            Point2D newLocation = new Point2D(e.getX(), e.getY());
            c.setLayoutX(newLocation.getX());
            c.setLayoutY(newLocation.getY());

        });
        
        

    }

    @FXML
    private void colocarPunto(MouseEvent event) {
        
    }
}
