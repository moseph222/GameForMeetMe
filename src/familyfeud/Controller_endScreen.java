package familyfeud;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Controller_endScreen {

    @FXML private Label winnerTeam;
    @FXML private Label teamPlayers;
    @FXML private TextArea GameNumber;
    private String winnerTeamStr;
    private Stage primaryStage;
    private List<String> winnerTeamPlayerList;
    private LinkedList<Round> game;
    private int gameFile = 0;

    public void initialize(){

    }

    public void setStageAndSetupListeners(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.getScene().getStylesheets().add("style.css");
    }

    public void passGame(LinkedList<Round> game, int gameFile) {
        this.game = game;
        this.gameFile = gameFile;
        Team teamA = game.get(0).getCurrentTeam();
        Team teamB = game.get(0).getOtherTeam();
        if(teamA.getTotalPoints() > teamB.getTotalPoints())
            teamA.setWinner();
        else
            teamB.setWinner();

        if(Main.Debugging()) {
            System.out.println(teamA.getStats());
            for(int i = 0; i < teamA.getPlayers().size(); i++)
            {
                System.out.println(teamA.getPlayer(i).getStats());
            }
            System.out.println(teamB.getStats());
            for(int i = 0; i < teamB.getPlayers().size(); i++)
            {
                System.out.println(teamB.getPlayer(i).getStats());
            }
        }
    }

    public void startNewGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("xml/start.fxml"));
        Parent root = (Parent)loader.load();
        Controller_start startStage = loader.getController();
        startStage.setStageAndSetupListeners(primaryStage);
        startStage.passPastVariables(gameFile, game.get(0).getCurrentTeam(), game.get(0).getOtherTeam());
        primaryStage.setScene(new Scene(root, 360, 640));
        primaryStage.getScene().getStylesheets().add("style.css");
    }

    //TODO deprecated
    public void setWinnerTeam(String winnerTeam, List<String> winnerTeamPlayerList1) {
//        this.winnerTeamStr = winnerTeam;
//        this.winnerTeamPlayerList = winnerTeamPlayerList1;
//        this.winnerTeam.setText(winnerTeamStr);
//        String team = "";
//
//        for(int i = 0; i < winnerTeamPlayerList.size(); i++){
//            team = team + winnerTeamPlayerList.get(i) + "  ";
//        }
//
//        teamPlayers.setText(team);
    }
}
