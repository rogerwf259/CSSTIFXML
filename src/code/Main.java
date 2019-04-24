package code;

import borderless.BorderlessScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    BorderlessScene sc;

    private static Main instance;
    public Main(){
        instance = this;
    }
    public static Main getInstance(){
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("splashscreen.fxml"));
        //sc = new BorderlessScene(primaryStage, root);
        primaryStage.setScene(new Scene(root));
        //sc.setMoveControl(root);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
