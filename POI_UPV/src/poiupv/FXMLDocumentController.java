/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
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
import model.Session;
import model.User;
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
    Line linePainting_1;
    Line linePainting_2;
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
    private ObservableList<Node> deleteObject;
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
    ArrayList<String> probs;
    List<Answer> ans;
    List<Problem> l;
    @FXML
    private ToggleButton btrans;
    @FXML
    private ToggleButton buttonBorder;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private MenuButton strokePicker;
    @FXML
    private ToggleGroup strokeGroup;
    @FXML
    private RadioButton stroke25;
    @FXML
    private RadioButton stroke50;
    @FXML
    private RadioButton stroke75;
    @FXML
    private RadioButton stroke100;
    @FXML
    private ToggleGroup radios;
    @FXML
    private RadioButton radio2;
    @FXML
    private RadioButton radio3;
    @FXML
    private RadioButton radio4;
    @FXML
    private Button buttonPreview;
    @FXML
    private Button buttonSelect;
    List<Answer> a;
    int aciertos;
    int fallos;
    User usuarioLogeado;
    @FXML
    private Button buttonSession;
    @FXML
    private DatePicker datePickerStats;
    @FXML
    private Label labelAciertos;
    @FXML
    private Label labelFallos;
    
    
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
        
        fallos = 0;
        aciertos =0;
        
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
        usuarioLogeado = FXMLoginController.usuarioLogeado;
        
        //Stage.setOnCloseRequest(this::cerrarAplicacion);
        
//        linePainting.setOnMouseEntered(event -> {
//            linePainting.setFill(colorPicker.getValue());
//        });
//        
//        colorPicker.setOnAction(event -> {
//            linePainting.setFill(colorPicker.getValue());
//        });
        datePickerStats.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) >= 0);
                }
            };
        });

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
      if (zoomGroup.getChildren().get(1) != null) {
            Alert mensaje = new Alert(Alert.AlertType.CONFIRMATION);
            mensaje.setTitle("Borrar dibujado");
            mensaje.setHeaderText("¿Seguro que quiere borrar todo lo apuntado en el mapa?");
            Optional<ButtonType> result = mensaje.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                zoomGroup.getChildren().clear();
                zoomGroup.getChildren().add(paneCarta);
            }
      }
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
        stage.close();
        

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
            
            linePainting.setStroke(colorPicker.getValue());
            
            if (stroke25.isSelected()) {
                linePainting.setStrokeWidth(3);
            } else if (stroke75.isSelected()) {
                linePainting.setStrokeWidth(9);
            } else if (stroke100.isSelected()) {
                linePainting.setStrokeWidth(15);
            } else {
                linePainting.setStrokeWidth(6);
            }
            
            zoomGroup.getChildren().add(linePainting);

            linePainting.setOnContextMenuRequested(e -> {
                ContextMenu menuContext = new ContextMenu();
                MenuItem strokeItem = new MenuItem("Tamaño");
                MenuItem colorItem = new MenuItem("Color");
                MenuItem borrarItem = new MenuItem("Eliminar");
                menuContext.getItems().add(strokeItem);
                menuContext.getItems().add(colorItem);
                menuContext.getItems().add(borrarItem);
                strokeItem.setOnAction(ev -> {
                    if (stroke25.isSelected()) {
                        ((Line) e.getSource()).setStrokeWidth(3);
                    } else if (stroke75.isSelected()) {
                        ((Line) e.getSource()).setStrokeWidth(9);
                    } else if (stroke100.isSelected()) {
                        ((Line) e.getSource()).setStrokeWidth(15);
                    } else {
                        ((Line) e.getSource()).setStrokeWidth(6);
                    }
                    ev.consume();
                });
                colorItem.setOnAction(ev -> {
                    ((Line) e.getSource()).setStroke(colorPicker.getValue());
                    ev.consume();
                });
                borrarItem.setOnAction(ev -> {
                    zoomGroup.getChildren().remove((Node) e.getSource());
                    ev.consume();
                });
                menuContext.show(((Line) e.getSource()), e.getSceneX(), e.getSceneY());
                e.consume();
            });
        }
        
        
        
        
        
        /*-----------ARCO-----------*/
        if (buttonArc.isSelected() && event.isPrimaryButtonDown()) {
            circlePainting = new Circle(1);
            circlePainting.setStroke(colorPicker.getValue());
            
            if (stroke25.isSelected()) {
                circlePainting.setStrokeWidth(3);
            } else if (stroke75.isSelected()) {
                circlePainting.setStrokeWidth(9);
            } else if (stroke100.isSelected()) {
                circlePainting.setStrokeWidth(15);
            } else {
                circlePainting.setStrokeWidth(6);
            }
            
            circlePainting.setFill(Color.TRANSPARENT);
            
            zoomGroup.getChildren().add(circlePainting);
            
            circlePainting.setCenterX(event.getX());
            circlePainting.setCenterY(event.getY());
            inicioXArc = event.getX();
            
            circlePainting.setOnContextMenuRequested(e -> {
                ContextMenu menuContext = new ContextMenu();
                MenuItem strokeItem = new MenuItem("Tamaño");
                MenuItem colorItem = new MenuItem("Color");
                MenuItem borrarItem = new MenuItem("Eliminar");
                menuContext.getItems().add(strokeItem);
                menuContext.getItems().add(colorItem);
                menuContext.getItems().add(borrarItem);
                strokeItem.setOnAction(ev -> {
                    if (stroke25.isSelected()) {
                        ((Circle) e.getSource()).setStrokeWidth(3);
                    } else if (stroke75.isSelected()) {
                        ((Circle) e.getSource()).setStrokeWidth(9);
                    } else if (stroke100.isSelected()) {
                        ((Circle) e.getSource()).setStrokeWidth(15);
                    } else {
                        ((Circle) e.getSource()).setStrokeWidth(6);
                    }
                    ev.consume();
                });
                colorItem.setOnAction(ev -> {
                    ((Circle) e.getSource()).setStroke(colorPicker.getValue());
                    ev.consume();
                });
                borrarItem.setOnAction(ev -> {
                    zoomGroup.getChildren().remove((Node) e.getSource());
                    ev.consume();
                });
                menuContext.show(((Circle) e.getSource()), e.getSceneX(), e.getSceneY());
                e.consume();
            });
        }
        
        
        
        
        
        /*-----------TEXTO-----------*/
        if (buttonText.isSelected() && event.isPrimaryButtonDown()) {
            zoomGroup.getChildren().add(texto);
            texto.setLayoutX(event.getX());
            texto.setLayoutY(event.getY());
            if (zoom_slider.getValue() < 0.3) {
                texto.setPrefSize(700, 30);
                texto.setStyle("-fx-font-size: 60;");
            } else {
                texto.setPrefSize(200, 20);
                texto.setStyle("-fx-font-size: 20;");
            }
            texto.requestFocus();
            
            
            texto.setOnAction(e -> {
                Text textoT = new Text(texto.getText());
                textoT.setX(texto.getLayoutX());
                textoT.setY(texto.getLayoutY());
                textoT.setStyle("-fx-font-family: Gafata;");
                textoT.setFill(colorPicker.getValue());
                
                if (stroke25.isSelected()) {
                    textoT.setStyle("-fx-font-size: 20;");
                } else if (stroke75.isSelected()) {
                    textoT.setStyle("-fx-font-size: 60;");
                } else if (stroke100.isSelected()) {
                    textoT.setStyle("-fx-font-size: 80;");
                } else {
                    textoT.setStyle("-fx-font-size: 40;");
                }
                
                zoomGroup.getChildren().add(textoT);
                
                textoT.setOnContextMenuRequested(ev -> {
                    ContextMenu menuContext = new ContextMenu();
                    menuContext.setStyle("-fx-font-size: 12;");
                    MenuItem strokeItem = new MenuItem("Tamaño");
                    MenuItem colorItem = new MenuItem("Color");
                    MenuItem borrarItem = new MenuItem("Eliminar");
                    menuContext.getItems().add(strokeItem);
                    menuContext.getItems().add(colorItem);
                    menuContext.getItems().add(borrarItem);
                    
                    strokeItem.setOnAction(eve -> {
                        if (stroke25.isSelected()) {
                            ((Text) ev.getSource()).setStyle("-fx-font-size: 20;");
                        } else if (stroke75.isSelected()) {
                            ((Text) ev.getSource()).setStyle("-fx-font-size: 60;");
                        } else if (stroke100.isSelected()) {
                            ((Text) ev.getSource()).setStyle("-fx-font-size: 80;");
                        } else {
                            ((Text) ev.getSource()).setStyle("-fx-font-size: 40;");
                        }
                        eve.consume();
                    });
                    colorItem.setOnAction(eve -> {
                        ((Text) ev.getSource()).setFill(colorPicker.getValue());
                        eve.consume();
                    });
                    borrarItem.setOnAction(eve -> {
                        zoomGroup.getChildren().remove((Node) ev.getSource());
                        eve.consume();
                    });
                    menuContext.show(((Text) ev.getSource()), ev.getSceneX(), ev.getSceneY());
                    ev.consume();
                });
                
                texto.setText("");
                zoomGroup.getChildren().remove(texto);
                e.consume();
                
                buttonText.setSelected(false);
            });
            
        }
        
        /*-----------PUNTO-----------*/
        if (buttonPoint.isSelected() && event.isPrimaryButtonDown()) {
            c = new Circle(10);
            c.setStroke(colorPicker.getValue().darker());
            c.setFill(colorPicker.getValue());
            
            if (stroke25.isSelected()) {
                c.setRadius(6);
            } else if (stroke75.isSelected()) {
                c.setRadius(15);
            } else if (stroke100.isSelected()) {
                c.setRadius(20);
            } else {
                c.setRadius(10);
            }
            
            zoomGroup.getChildren().add(c);
            
            c.setCenterX(event.getX());
            c.setCenterY(event.getY());
            
            
            c.setOnContextMenuRequested(e -> {
                ContextMenu menuContext = new ContextMenu();
                MenuItem strokeItem = new MenuItem("Tamaño");
                MenuItem colorItem = new MenuItem("Color");
                MenuItem borrarItem = new MenuItem("Eliminar");
                menuContext.getItems().add(strokeItem);
                menuContext.getItems().add(colorItem);
                menuContext.getItems().add(borrarItem);
                strokeItem.setOnAction(ev -> {
                    if (stroke25.isSelected()) {
                        ((Circle) e.getSource()).setRadius(6);
                    } else if (stroke75.isSelected()) {
                        ((Circle) e.getSource()).setRadius(15);
                    } else if (stroke100.isSelected()) {
                        ((Circle) e.getSource()).setRadius(20);
                    } else {
                        ((Circle) e.getSource()).setRadius(10);
                    }
                    ev.consume();
                });
                colorItem.setOnAction(ev -> {
                    ((Circle) e.getSource()).setStroke(colorPicker.getValue().darker());
                    ((Circle) e.getSource()).setFill(colorPicker.getValue());
                    ev.consume();
                });
                borrarItem.setOnAction(ev -> {
                    zoomGroup.getChildren().remove((Node) e.getSource());
                    ev.consume();
                });
                menuContext.show(((Circle) e.getSource()), e.getSceneX(), e.getSceneY());
                e.consume();
            });
            
            
            buttonPoint.setSelected(false);
        }
        
        
        
        /*-----------TRANSPORTADOR-----------*/
        if(btrans.isSelected() && event.isPrimaryButtonDown()){
            transportador.setVisible(true);
            transportador.setDisable(false);
            transportador.setMouseTransparent(false);
            X_inicial = event.getX();
            Y_inicial = event.getY();
            tbaseX = transportador.getTranslateX() - X_inicial;
            tbaseY = transportador.getTranslateY() - Y_inicial;
            
            //zoomGroup.getChildren().add(transportador);
            
            transportador.setOnContextMenuRequested(e -> {
                ContextMenu menuContext = new ContextMenu();
                MenuItem borrarItem = new MenuItem("Eliminar");
                menuContext.getItems().add(borrarItem);
                borrarItem.setOnAction(ev -> {
                    //zoomGroup.getChildren().remove((Node) e.getSource());
                    transportador.setVisible(false);
                    ev.consume();
                    btrans.setSelected(false);
                });
                menuContext.show(((ImageView) e.getSource()), e.getSceneX(), e.getSceneY());
                e.consume();
            });
        }
        else if(!btrans.isSelected()){
            transportador.setDisable(true);
            transportador.setMouseTransparent(true);
        }
        
        
        
        /*-----------RAYAS_ORTOGONALES-----------*/
        if (buttonBorder.isSelected() && event.isPrimaryButtonDown()) {
            linePainting_1 = new Line(0, event.getY(), 8500, event.getY());
            linePainting_2 = new Line(event.getX(), 0, event.getX(), 5300);
            
            linePainting_1.setStroke(colorPicker.getValue());
            linePainting_2.setStroke(colorPicker.getValue());
            
            if (stroke25.isSelected()) {
                linePainting_1.setStrokeWidth(3);
                linePainting_2.setStrokeWidth(3);
            } else if (stroke75.isSelected()) {
                linePainting_1.setStrokeWidth(9);
                linePainting_2.setStrokeWidth(9);
            } else if (stroke100.isSelected()) {
                linePainting_1.setStrokeWidth(15);
                linePainting_2.setStrokeWidth(15);
            } else {
                linePainting_1.setStrokeWidth(6);
                linePainting_2.setStrokeWidth(6);
            }
            
            zoomGroup.getChildren().add(linePainting_1);
            zoomGroup.getChildren().add(linePainting_2);
            
            event.consume();
            
            buttonBorder.setSelected(false);
            
            
            linePainting_1.setOnContextMenuRequested(e -> {
                ContextMenu menuContext = new ContextMenu();
                MenuItem strokeItem = new MenuItem("Tamaño");
                MenuItem colorItem = new MenuItem("Color");
                MenuItem borrarItem = new MenuItem("Eliminar");
                menuContext.getItems().add(strokeItem);
                menuContext.getItems().add(colorItem);
                menuContext.getItems().add(borrarItem);
                strokeItem.setOnAction(ev -> {
                    if (stroke25.isSelected()) {
                        ((Line) e.getSource()).setStrokeWidth(3);
//                        linePainting_2.setStrokeWidth(3);
                    } else if (stroke75.isSelected()) {
                        ((Line) e.getSource()).setStrokeWidth(9);
//                        linePainting_2.setStrokeWidth(9);
                    } else if (stroke100.isSelected()) {
                        ((Line) e.getSource()).setStrokeWidth(15);
//                        linePainting_2.setStrokeWidth(15);
                    } else {
                        ((Line) e.getSource()).setStrokeWidth(6);
//                        linePainting_2.setStrokeWidth(6);
                    }
                    ev.consume();
                });
                colorItem.setOnAction(ev -> {
                    ((Line) e.getSource()).setStroke(colorPicker.getValue());
//                    linePainting_2.setStroke(colorPicker.getValue());
                    ev.consume();
                });
                borrarItem.setOnAction(ev -> {
                    zoomGroup.getChildren().remove((Node) e.getSource());
//                    zoomGroup.getChildren().remove(linePainting_2);
                    ev.consume();
                });
                menuContext.show(((Line) e.getSource()), e.getSceneX(), e.getSceneY());
                e.consume();
            });
            
            
            
            linePainting_2.setOnContextMenuRequested(e -> {
                ContextMenu menuContext = new ContextMenu();
                MenuItem strokeItem = new MenuItem("Tamaño");
                MenuItem colorItem = new MenuItem("Color");
                MenuItem borrarItem = new MenuItem("Eliminar");
                menuContext.getItems().add(strokeItem);
                menuContext.getItems().add(colorItem);
                menuContext.getItems().add(borrarItem);
                strokeItem.setOnAction(ev -> {
                    if (stroke25.isSelected()) {
//                        linePainting_1.setStrokeWidth(3);
                        ((Line) e.getSource()).setStrokeWidth(3);
                    } else if (stroke75.isSelected()) {
//                        linePainting_1.setStrokeWidth(9);
                        ((Line) e.getSource()).setStrokeWidth(9);
                    } else if (stroke100.isSelected()) {
//                        linePainting_1.setStrokeWidth(15);
                        ((Line) e.getSource()).setStrokeWidth(15);
                    } else {
//                        linePainting_1.setStrokeWidth(6);
                        ((Line) e.getSource()).setStrokeWidth(6);
                    }
                    ev.consume();
                });
                colorItem.setOnAction(ev -> {
//                    linePainting_1.setStroke(colorPicker.getValue());
                    ((Line) e.getSource()).setStroke(colorPicker.getValue());
                    ev.consume();
                });
                borrarItem.setOnAction(ev -> {
//                    zoomGroup.getChildren().remove(linePainting_1);
                    zoomGroup.getChildren().remove(((Node) e.getSource()));
                    ev.consume();
                });
                menuContext.show(((Line) e.getSource()), e.getSceneX(), e.getSceneY());
                e.consume();
            });
            
            
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
            double despX = event.getX();
            double despY = event.getY();
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
        probs = new ArrayList<>();
        for(int j=0; j< listaObservable.size(); j++){
            String e = l.get(j).getText();
            probs.add(e);
        }
        System.out.println(probs);
        listaObservableProblemsString = FXCollections.observableList(probs);
        listviewProblemas.setItems(listaObservableProblemsString);
        buttonPreview.setDisable(false);
        buttonSelect.setDisable(false);
        
    }

    @FXML
    private void getRandomProblem(ActionEvent event) {
        
        int i = rnd.nextInt(listaObservable.size()+1);
        System.out.println(i);
        enunciado.setText(listaObservable.get(i).getText());
        //enunciado.setText(listaObservable.get(i).toString());
        enunciado.setVisible(true);
//        pruebaAns.setText(listaObservable.get(i).getAnswers());
        a = listaObservable.get(i).getAnswers();
        radio1.setText(a.get(0).getText());
        
        radio2.setText(a.get(1).getText());
        
        radio3.setText(a.get(2).getText());
        
        radio4.setText(a.get(3).getText());
        
        radios.getSelectedToggle().setSelected(false);
        buttonEnviar.setDisable(false);
        
    }

    @FXML
    private void enviarRespuesta(ActionEvent event) {
        if(a.get(0).getValidity() && radio1.isSelected()){
            aciertos++;
            pruebaAns.setText("Correcto. Aciertos: " + aciertos + " Fallos: " + fallos);
            fallos--;
        }
        if(a.get(1).getValidity() && radio2.isSelected()){
            aciertos++;
            pruebaAns.setText("Correcto. Aciertos: " + aciertos + " Fallos: " + fallos);
            fallos--;
        }
        if(a.get(2).getValidity() && radio3.isSelected()){
            aciertos++;
            pruebaAns.setText("Correcto. Aciertos: " + aciertos + " Fallos: " + fallos);
            fallos--;
        }
        if(a.get(3).getValidity() && radio4.isSelected()){
            aciertos++;
            pruebaAns.setText("Correcto. Aciertos: " + aciertos + " Fallos: " + fallos);
            fallos--;
            }
        else {
            fallos++;
            pruebaAns.setText("Correcto. Aciertos: " + aciertos + " Fallos: " + fallos);
            
        }
        buttonEnviar.setDisable(true);
    }

    @FXML
    private void previewProblempreviewProblem(ActionEvent event) {
        Alert mensaje= new Alert(Alert.AlertType.INFORMATION);
        mensaje.setTitle("MostrarProblema");
        mensaje.setHeaderText("Preview");
        int xd = listviewProblemas.getSelectionModel().getSelectedIndex();
        mensaje.setContentText(listaObservable.get(xd).getText());
        mensaje.showAndWait();
    }

    @FXML
    private void selectProblem(ActionEvent event) {
        Alert mensaje= new Alert(Alert.AlertType.CONFIRMATION);
        mensaje.setTitle("Seleccionar Problema");
        mensaje.setContentText("¿Quieres realizar este problema?");
        Optional<ButtonType> result = mensaje.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            enunciado.setVisible(true);
            
            int xd = listviewProblemas.getSelectionModel().getSelectedIndex();
            System.out.println(xd);
//            enunciado.setText(listviewProblemas.getSelectionModel().getSelectedItems().toString());
            a = listaObservable.get(xd).getAnswers();
            enunciado.setText(listaObservable.get(xd).getText());
            radio1.setText(a.get(0).getText());

            radio2.setText(a.get(1).getText());

            radio3.setText(a.get(2).getText());

            radio4.setText(a.get(3).getText());
            
        }
        radios.getSelectedToggle().setSelected(false);
        buttonEnviar.setDisable(false);
    }
    
    

    @FXML
    private void getSessionInfo(ActionEvent event) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Sesiones");
//        alert.setHeaderText("Sesiones previas");
//        alert.setContentText(usuarioLogeado.getSessions().toString());
//        alert.showAndWait();
        int a=0;
        int f=0;
       List<Session> l = usuarioLogeado.getSessions();
       LocalDate ld = datePickerStats.getValue();
       for(int v=0;v<l.size();v++){
           if(l.get(v).getLocalDate().isBefore(ld)){
             a += l.get(v).getHits();
             f += l.get(v).getFaults();
           }
       }
       labelAciertos.setText("Aciertos: "+ a);
       labelFallos.setText("Fallos: "+ f);
       
    }
    
    
}
