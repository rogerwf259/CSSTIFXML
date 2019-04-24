package helper;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MessageBox {
    public static void show(StackPane root, String message, String title) {

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(new Text(title));
        dialogLayout.setBody(new Text(message));
        JFXButton buttonOk = new JFXButton();
        buttonOk.setText("Ok");
        dialogLayout.setActions(buttonOk);

        JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.BOTTOM);
        buttonOk.setOnAction(e -> dialog.close());
        dialog.setAlignment(Pos.CENTER);
        dialog.setMaxWidth(150);
        dialog.setMaxHeight(100);
        dialog.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        dialog.show();
    }
}
