package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
        Parent root = (Parent)loader.load();
        Controller_start controller = (Controller_start) loader.getController();
        controller.setStageAndSetupListeners(primaryStage);
        primaryStage.setTitle("Fuck Moseph");
        primaryStage.setScene(new Scene(root, 360, 640));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    // return true if debugging
    public static boolean Debugging() {
        return true;
    }
}
