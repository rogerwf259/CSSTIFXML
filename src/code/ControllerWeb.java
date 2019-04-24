package code;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerWeb implements Initializable {

    @FXML
    WebView webView;
    @FXML
    BorderPane video;
    @FXML
    JFXToggleNode  backBtn;
    @FXML
    JFXButton minimizeBtn, closeBtn;
    @FXML
    ImageView camfeed;
    @FXML
    StackPane visualizador;


    WebEngine webEngine;
    Webcam webcam;
    BufferedImage capturedImage;
    Image image;
    HttpURLConnection http;
    URL urlS;
    String oldValue="";
    boolean isRunning=false, isCamRecording=false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Splashscreen.getInstance().sc.setMoveControl(visualizador);
        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open();
        webEngine = webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((observableValue, oldVal, newVal) -> {
            if (Worker.State.SUCCEEDED.equals(newVal)){
                System.out.println("Done Loading");
                try {
                    http = (HttpURLConnection) urlS.openConnection();
                    oldValue = http.getHeaderField("Last-Modified");
                    System.out.println("Old Value:"+oldValue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                initFetch();
                initFeed();
            }
        });
        backBtn.setOnAction(e -> Btn_Clicked(e));
        minimizeBtn.setOnAction(e-> Btn_Clicked(e));
        closeBtn.setOnAction(e -> Btn_Clicked(e));
    }
    public void sendURL(String url) {
        webEngine.load(url);
        try {
            this.urlS = new URL(url);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void Btn_Clicked(Event event) {
        switch (((Node) event.getSource()).getId()){
            case "backBtn":
                System.out.println("Yo back");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isRunning = false;
                isCamRecording = false;
                webcam.close();
                Splashscreen.getInstance().sc.setContent(root);
                break;
            case "minimizeBtn":
                System.out.println("Yo minimze");
                Splashscreen.getInstance().sc.minimise();
                break;
            case "closeBtn":
                System.out.println("Yo close");
                if (isRunning)
                    isRunning=false;
                if (isCamRecording)
                    isCamRecording=false;
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.close();
                break;
            default:break;
        }
    }
    public void initFetch() {
        //webcam.open();
        Runnable task = () -> {
          isRunning = true;
          websiteChange();
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }
    public void websiteChange() {
        String newValue = "";
        while (isRunning){
            try {
                http = (HttpURLConnection) urlS.openConnection();
                newValue = http.getHeaderField("Last-Modified");
                System.out.println("New Value: "+newValue);
                if (oldValue.equals(newValue)){
                    System.out.println("Same");
                } else {
                    System.out.println("Changed");
                    Platform.runLater(() -> webEngine.reload());
                }
                Thread.sleep(5000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initFeed() {
        if (!isCamRecording) {
            isCamRecording = true;
            new VideoFeed().start();
        }
    }

    public class VideoFeed extends Thread {
        @Override
        public void run() {
            while (isCamRecording){
                try {
                    super.run();
                    capturedImage = webcam.getImage();
                    image = SwingFXUtils.toFXImage(capturedImage, null);
                    camfeed.setImage(image);
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
