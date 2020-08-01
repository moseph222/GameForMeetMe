package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

//hello

import java.io.IOException;

public class Controller_start {

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
        //String team1_name = Team1.getText();
        //String team2_name = Team2.getText();
        String team1_name = "Test Team1";
        String team2_name = "Test Team2";
        boolean disableButton1 = team1_name.isEmpty() || team1_name.trim().isEmpty();
        boolean disableButton2 = team2_name.isEmpty() || team2_name.trim().isEmpty();
        //if(!Team1.getText().isEmpty() && !Team2.getText().isEmpty()){
            System.out.println("Hello  " + Team1.getText());
            System.out.println("Hello  " + Team2.getText());
            System.out.println("The following button was a " + e.getSource());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pickteams.fxml"));
            Parent root = (Parent)loader.load();
            Controller_teams pickteams = loader.getController();
            pickteams.setStageAndSetupListeners(primaryStage);
            pickteams.setTeamNames(team1_name,team2_name); //pass team names to second stage
            primaryStage.setScene(new Scene(root, 720, 640));
        //}
    }

    public void setStageAndSetupListeners(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

}
