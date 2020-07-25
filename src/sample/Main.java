package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = (Parent)loader.load();
        Controller controller = (Controller) loader.getController();
        controller.setStageAndSetupListeners(primaryStage);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 720, 640));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
