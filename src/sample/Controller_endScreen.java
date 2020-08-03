package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Controller_endScreen {

    @FXML
    private Label winnerTeam;
    @FXML
    private Label teamPlayers;
    private String winnerTeamStr;
    private Stage primaryStage;
    @FXML
    private TextArea GameNumber;
    private List<String> winnerTeamPlayerList;

    public void initialize(){

    }

    public void setStageAndSetupListeners(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setWinnerTeam(String winnerTeam, List<String> winnerTeamPlayerList1) {
        this.winnerTeamStr = winnerTeam;
        this.winnerTeamPlayerList = winnerTeamPlayerList1;
        this.winnerTeam.setText(winnerTeamStr);
        String team = "";

        for(int i = 0; i < winnerTeamPlayerList.size(); i++){
            team = team + winnerTeamPlayerList.get(i) + "  ";
        }

        teamPlayers.setText(team);
    }

    public void startNewGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pickteams.fxml"));
        Parent root = (Parent)loader.load();
        Controller_teams teamStage = loader.getController();
        teamStage.setStageAndSetupListeners(primaryStage);
        System.out.println("Inside endScreen: " + Integer.parseInt(GameNumber.getText()));
        teamStage.chooseGameFile(Integer.parseInt(GameNumber.getText()));
        primaryStage.setScene(new Scene(root, 360, 640));
    }
}
