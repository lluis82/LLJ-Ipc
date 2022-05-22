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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import model.Answer;
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
    private ToggleButton buttonPoint;
    @FXML
    private ToggleButton buttonLine;
    @FXML
    private ToggleButton buttonText;
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
    private ObservableList<Problem> listaObservable;
    @FXML
    private Pane paneCarta;
    Circle c;
    private ListView<Problem> lvProblemas;
    Line linePainting;
    Circle circlePainting;
    TextField texto = new TextField();
    double inicioXArc;
    @FXML
    private Label labelEnucnciado;
    @FXML
    private ImageView transportador;
    double X_inicial;
    double Y_inicial;
    double xt;
    double yt;
    double tbaseX;
    double tbaseY;
    @FXML
    private Button buttonListProblems;
    @FXML
    private Button buttonRandom;
    @FXML
    private Label enunciado;
    @FXML
    private ToggleButton buttonArc;
    @FXML
    private ToggleGroup toggleGroup;
    private ObservableList<Object> deleteObject;
    private ObservableList<String> listaObservableProblemsString;
    Random rnd;
    @FXML
    private Label pruebaAns;
    @FXML
    private Button buttonEnviar;
    @FXML
    private RadioButton radio1;
    @FXML
    private ListView<String> listviewProblemas;
    List<String> probs;
    List<Answer> ans;
    List<Problem> l;
    @FXML
    private ToggleButton btrans;
    
    
    
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
        
        
        
        try {
            l = Navegacion.getSingletonNavegacion().getProblems();
//            System.out.println(l);
            listaObservable = FXCollections.observableList(l);

        } catch (NavegacionDAOException ex) {
            Logger.getLogger(FXMLoginController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error");
        }
        
        rnd = new Random();
        //lvProblemas.setItems(listaObservable);
     
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
        mensaje.setContentText("Proyecto IPC 2022 - Javier Pérez Asensio & Lluis Terol Martin");
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
                //zoomGroup.getChildren().removeAll(deleteObject);
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
        newStage.showAndWait();
        
    }
    
    @FXML
    private void zoomControl(ScrollEvent event) {
        if (event.isControlDown()) {
            if (event.getDeltaY() < 0) {
                double sliderVal = zoom_slider.getValue();
                zoom_slider.setValue(sliderVal += -((0.8-0.12)/10));
            }
            else {
                double sliderVal = zoom_slider.getValue();
                zoom_slider.setValue(sliderVal + ((0.8-0.12)/10));
            }
        }
    }
    
    @FXML
    private void mousePressedAction(MouseEvent event) {
        
        /*-----------LINEA-----------*/
        if (buttonLine.isSelected() && event.isPrimaryButtonDown()) {
            linePainting = new Line(event.getX(), event.getY(), event.getX(), event.getY());
            zoomGroup.getChildren().add(linePainting);
            //deleteObject.add(linePainting);

            linePainting.setOnContextMenuRequested(e -> {
            ContextMenu menuContext = new ContextMenu();
            MenuItem strokeItem = new MenuItem("Tamaño");
            MenuItem colorItem = new MenuItem("Color");
            MenuItem borrarItem = new MenuItem("Eliminar");
            menuContext.getItems().add(borrarItem);
            borrarItem.setOnAction(ev -> {
                zoomGroup.getChildren().remove((Node) e.getSource());
                //deleteObject.remove((Node) e.getSource());
                ev.consume();
            });
            menuContext.show(linePainting, e.getSceneX(), e.getSceneY());
            e.consume();
        });
        }
        
        
        
        
        
        /*-----------ARCO-----------*/
        if (buttonArc.isSelected() && event.isPrimaryButtonDown()) {
            circlePainting = new Circle(1);
            circlePainting.setStroke(Color.RED);
            circlePainting.setFill(Color.TRANSPARENT);
            
            zoomGroup.getChildren().add(circlePainting);
            //deleteObject.add(circlePainting);
            
            circlePainting.setCenterX(event.getX());
            circlePainting.setCenterY(event.getY());
            inicioXArc = event.getX();
            
            circlePainting.setOnContextMenuRequested(e -> {
                ContextMenu menuContext = new ContextMenu();
                MenuItem strokeItem = new MenuItem("Tamaño");
                MenuItem colorItem = new MenuItem("Color");
                MenuItem borrarItem = new MenuItem("Eliminar");
                menuContext.getItems().add(borrarItem);
                borrarItem.setOnAction(ev -> {
                    zoomGroup.getChildren().remove((Node) e.getSource());
                    //deleteObject.remove((Node) e.getSource());
                    ev.consume();
                });
                menuContext.show(circlePainting, e.getSceneX(), e.getSceneY());
                e.consume();
            });
        }
        
        
        
        
        
        /*-----------TEXTO-----------*/
        if (buttonText.isSelected() && event.isPrimaryButtonDown()) {
            zoomGroup.getChildren().add(texto);
            //deleteObject.add(texto);
            texto.setLayoutX(event.getX());
            texto.setLayoutY(event.getY());
            texto.requestFocus();
            
            
            texto.setOnAction(e -> {
                Text textoT = new Text(texto.getText());
                textoT.setX(texto.getLayoutX());
                textoT.setY(texto.getLayoutY());
                textoT.setStyle("-fx-font-family: Gafata; -fx-font-size: 40;");
                zoomGroup.getChildren().add(textoT);
                texto.setText("");
                zoomGroup.getChildren().remove(texto);
                //deleteObject.remove(texto);
                e.consume();
                
                buttonText.setSelected(false);
            });
        }
        
        /*-----------PUNTO-----------*/
        if (buttonPoint.isSelected() && event.isPrimaryButtonDown()) {
            c = new Circle(10);
            c.setStroke(Color.BLUE);
            c.setFill(Color.LIGHTBLUE);
            
            zoomGroup.getChildren().add(c);
            //deleteObject.add(c);
            
            c.setCenterX(event.getX());
            c.setCenterY(event.getY());
            
            buttonPoint.setSelected(false);
        }
        
        /*-----------TRANSPORTADOR-----------*/
        if(btrans.isSelected() && event.isPrimaryButtonDown()){
            transportador.setVisible(true);
            transportador.setDisable(false);
            transportador.setMouseTransparent(false);
            X_inicial = event.getSceneX();
            Y_inicial = event.getSceneY();
            tbaseX = transportador.getTranslateX();
            tbaseY = transportador.getTranslateY();
            event.consume();
        }
        else if(!btrans.isSelected()){
            transportador.setDisable(true);
            transportador.setMouseTransparent(true);
        }
    }

    @FXML
    private void mouseDraggedAction(MouseEvent event) {
        
        /*-----------LINEA-----------*/
        if (buttonLine.isSelected() && event.isPrimaryButtonDown()) {
            linePainting.setEndX(event.getX());
            linePainting.setEndY(event.getY());
            event.consume();
        }
        
        
        /*-----------ARCO-----------*/
        if (buttonArc.isSelected() && event.isPrimaryButtonDown()) {
            double radio = Math.abs(event.getX() - inicioXArc);
            circlePainting.setRadius(radio);
            event.consume();
        }
        
        /*-----------TRANSPORTADOR-----------*/
        if (btrans.isSelected() && event.isPrimaryButtonDown()){
            double despX = event.getSceneX();
            double despY = event.getSceneY();
            transportador.setTranslateX(despX - X_inicial);
            transportador.setTranslateY(despY - Y_inicial);
            
        }
    }

    @FXML
    private void mouseReleasedAction(MouseEvent event) {
        buttonLine.setSelected(false);
        buttonArc.setSelected(false);
    }

    @FXML
    private void mouseMovedAction(MouseEvent event) {
        
    }

//    @FXML
//    private void moverTransportador(MouseEvent event) {
//        transportador.setDisable(false);
//        transportador.setMouseTransparent(true);
//        X_inicial = event.getSceneX();
//        Y_inicial = event.getSceneY();
//        tbaseX = transportador.getTranslateX();
//        tbaseY = transportador.getTranslateY();
////        double despX = event.getSceneX() - X_inicial;
////        double despY = event.getSceneY() - Y_inicial;
////        transportador.setTranslateX(despX - tbaseX);
////        transportador.setTranslateY(despY - tbaseY);
//        event.consume();
//
//
//
//
//    }
//
//    @FXML
//    private void pressedTransportador(MouseEvent event) {
////        X_inicial = event.getSceneX();
////        Y_inicial = event.getSceneY();
////        tbaseX = transportador.getTranslateX();
////        tbaseY = transportador.getTranslateY();
////        event.consume();
//    }

    @FXML
    private void SelectProblem(ActionEvent event) {
        listaObservableProblemsString = FXCollections.observableList(probs);
        for(int j=0; j< listaObservable.size(); j++){
            
            probs.add(listaObservable.get(j).getText());
//            listaObservableProblemsString = FXCollections.observableList(probs);
//            listviewProblemas.setItems(listaObservableProblemsString);
            System.out.println(listaObservableProblemsString);
        }
        listviewProblemas.setItems(listaObservableProblemsString);
    }

    @FXML
    private void getRandomProblem(ActionEvent event) {
        int i = rnd.nextInt(listaObservable.size()+1);
        System.out.println(i);
        enunciado.setText(listaObservable.get(i).getText());
        //enunciado.setText(listaObservable.get(i).toString());
        enunciado.setVisible(true);
//        pruebaAns.setText(listaObservable.get(i).getAnswers());
        List<Answer> a = listaObservable.get(i).getAnswers();
        radio1.setText(a.get(0).getText());
        
        
    }

    @FXML
    private void enviarRespuesta(ActionEvent event) {
        
    }

    
}