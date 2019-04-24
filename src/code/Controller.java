package code;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import helper.Config;
import helper.HelperMethods;
import helper.MessageBox;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    StackPane stack;
    @FXML
    JFXTextField url;
    @FXML
    JFXTextField token;
    @FXML
    JFXTextField width;
    @FXML
    JFXTextField height;
    @FXML
    JFXButton requestButton, cancelButton;
    @FXML
    JFXCheckBox checkbox;
    @FXML
    BorderPane rootPane;
    @FXML
    VBox request;

    Boolean isMeasuring = true;
    String uText="", tText="";
    Double wP=0.0, hP=0.0;
    Config config = new Config();
    File file;

    public static String color = "-fx-background-color: linear-gradient(#42a5f5 0%, #1E88E5 50%, #1565C0 100%)";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Splashscreen.getInstance().sc.setMoveControl(request);
        rootPane.setStyle(color);
        rootPane.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            if (newScene == null){
                isMeasuring = false;
            } else {
                showing();
            }
        });
        requestButton.setOnAction(e -> requestButton_Clicked(e));
        cancelButton.setOnAction(e -> cancelButton_Clicked(e));
    }

    public void requestButton_Clicked(Event event) {
        String URLText = "", errorMessage = "";
        if (url.getText().equals(""))
            errorMessage += "URL es un campo requerido\n";
        if (token.getText().equals(""))
            errorMessage += "Token es un campo requerido\n";
        if (!url.getText().equals("") && !token.getText().equals("")) {
            if (HelperMethods.isMatch(url.getText())) {
                URLText += url.getText() + token.getText();
                //URLText = insertToken(url.getText(), token.getText());
                System.out.println("URLText: " + URLText);
                if (checkbox.isSelected()){
                    saveProperties(url.getText(), token.getText(), rootPane.getWidth(), rootPane.getHeight());
                }
                loadSecondAndSendURL(URLText, event);
            } else {
                MessageBox.show(stack,"URL invalida", "Error");
            }
        } else {
            MessageBox.show(stack, errorMessage, "Datos Faltantes");
        }
    }
    public void loadSecondAndSendURL(String url, Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("web.fxml"));
            Parent root = loader.load();
            ControllerWeb controllerWeb = loader.getController();
            controllerWeb.sendURL(url);
            /*Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene2 = new Scene(root, wP, hP);
            window.setScene(scene2);
            window.show();*/
            //Main.getInstance().sc.setContent(root);
            Splashscreen.getInstance().sc.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProperties(String url, String token, Double width, Double height) {
        config.saveProperty("URL", url);
        config.saveProperty("Token", token);
        if (width != null && height != null){
            config.saveProperty("Width", width.toString());
            config.saveProperty("Height", height.toString());
        }
    }

    public void showing() {
        file = new File("config.ini");
        if (file.exists()){
            uText = config.readProperties("URL");
            this.url.setText(uText);
            this.url.setStyle("-fx-text-fill: white");
            tText = config.readProperties("Token");
            this.token.setText(tText);
            wP = Double.valueOf(config.readProperties("Width"));
            rootPane.setPrefWidth(wP);
            hP = Double.valueOf(config.readProperties("Height"));
            rootPane.setPrefHeight(hP);
        }
        System.out.println("CONFIG:"+file.exists());
        measureUpdate();
    }

    public void measureUpdate() {
        Runnable mUpdate = () -> measure();
        Thread measuresBackground = new Thread(mUpdate);
        measuresBackground.setDaemon(true);
        measuresBackground.start();
    }
    public void measure(){
        while (isMeasuring){
            Platform.runLater(() -> {
                width.setText(String.valueOf(rootPane.getWidth()));
                height.setText(String.valueOf(rootPane.getHeight()));
            });
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    public void cancelButton_Clicked(Event event){
        isMeasuring = false;
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }
    public String insertToken(String base, String tkn){
        return (base.substring(0, base.indexOf('*'))+tkn+base.substring(base.lastIndexOf('*')+1));
    }
}
