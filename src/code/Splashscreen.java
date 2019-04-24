package code;

import borderless.BorderlessScene;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Splashscreen implements Initializable {

    @FXML
    AnchorPane background;

    BorderlessScene sc;
    Stage stage;

    private static Splashscreen instance;
    public Splashscreen(){ instance = this; }
    public static Splashscreen getInstance(){
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage = new Stage();
        new SplashScreen().start();
    }
    public class SplashScreen extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(5000);
                Platform.runLater(() -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sc = new BorderlessScene(stage, root);
                    stage.setScene(sc);
                    sc.setMoveControl(root);
                    stage.show();

                    background.getScene().getWindow().hide();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
