package familyfeud;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class Controller_start {

    @FXML private TextField Team1;
    @FXML private TextField Team2;
    @FXML private Label StartButton;
    private Stage primaryStage;
    private Team oldTeam1;
    private Team oldTeam2;
    private int gameFile = 0;

    public void initialize(){
        Team1.setMaxWidth(250);
        Team2.setMaxWidth(250);

        if(Main.Debugging()) {
            Team1.setText("Minecraft Manhunters");
            Team2.setText("The Ross Racers");
        }
    }

    public void setStageAndSetupListeners(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    @FXML
    public void onButtonClicked(MouseEvent e) throws IOException {
        if(!Team1.getText().isEmpty() && !Team2.getText().isEmpty()){
            System.out.println("The following button was a " + e.getSource());
            handleEvent();
        }
    }

    public void onKeyPress (KeyEvent keyEvent) throws IOException {
        System.out.println("Key Pressed");
        if(keyEvent.getCode() == KeyCode.ENTER && !Team1.getText().isEmpty() && !Team2.getText().isEmpty())
        {
            handleEvent();
        }
    }

    public void handleEvent() throws IOException {
        System.out.println("Hello " + Team1.getText());
        System.out.println("Hello " + Team2.getText());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("xml/pickteams.fxml"));
        Parent root = (Parent)loader.load();
        Controller_teams pickteams = loader.getController();
        pickteams.setStageAndSetupListeners(primaryStage);
        if(oldTeam1 != null && oldTeam2 != null)
            pickteams.passPastVariables(gameFile, oldTeam1, oldTeam2);
        pickteams.setTeamNames(Team1.getText(),Team2.getText()); //pass team names to second stage
        primaryStage.setScene(new Scene(root, 360, 640));
        primaryStage.getScene().getStylesheets().add("style.css");
    }

    public void passPastVariables(int gameFile, Team team1, Team team2){
        this.gameFile = gameFile;
        System.out.println("Gamefile: "+gameFile);
        if(team1.isFirstTeam()) {
            oldTeam1 = team1;
            oldTeam2 = team2;
        }
        else {
            oldTeam1 = team2;
            oldTeam2 = team1;
        }
        Team1.setText(oldTeam1.toString());
        Team2.setText(oldTeam2.toString());
    }
}
