package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

//hello

import java.io.IOException;

public class Controller {

    @FXML
    private TextField Team1;
    @FXML
    private TextField Team2;
    @FXML
    private Label StartButton;
    Stage primaryStage;

    public void initialize(){
    }

    @FXML
    public void onButtonClicked(MouseEvent e) throws IOException {
        String text1 = Team1.getText();
        String text2 = Team2.getText();
        boolean disableButton1 = text1.isEmpty() || text1.trim().isEmpty();
        boolean disableButton2 = text2.isEmpty() || text2.trim().isEmpty();
        if(!Team1.getText().isEmpty() && !Team2.getText().isEmpty()){
            System.out.println("Hello  " + Team1.getText());
            System.out.println("Hello  " + Team2.getText());
            System.out.println("The following button was a " + e.getSource());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("stage2.fxml"));
            Parent root = (Parent)loader.load();
            primaryStage.setScene(new Scene(root, 720, 640));
        }
    }

    public void setStageAndSetupListeners(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

}
