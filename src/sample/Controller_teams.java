package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//hello

public class Controller_teams {

    @FXML
    private Label Team1Title;
    @FXML
    private TextArea Team1Players;
    @FXML
    private Label Team2Title;
    @FXML
    private TextArea Team2Players;

    Stage primaryStage;
    List<String> team1playerslist;
    List<String> team2playerslist;
    List<String> totalPlayers;


    public void initialize(){
        team1playerslist = new ArrayList<String>();
        team2playerslist = new ArrayList<String>();
        totalPlayers = new ArrayList<String>();

        Team1Players.setMaxHeight(100);
        Team1Players.setMaxWidth(150);
        Team1Players.setWrapText(true);

        Team2Players.setMaxHeight(100);
        Team2Players.setMaxWidth(150);
        Team2Players.setWrapText(true);
    }

    @FXML
    public void submitTeams(ActionEvent e) {
        System.out.println("");
        System.out.println("->Button pressed<-");
        System.out.println("");

        team1playerslist.clear();
        team2playerslist.clear();
        totalPlayers.clear();

        parsePlayers(Team1Players,team1playerslist);
        parsePlayers(Team2Players,team2playerslist);
        totalPlayers.addAll(team1playerslist);
        totalPlayers.addAll(team2playerslist);

        // Debugging

        System.out.println("Team 1 Players");
        for(int i = 0; i < team1playerslist.size(); i++)
        {
            System.out.println("["+team1playerslist.get(i)+"]");
        }
        System.out.println("Team 2 Players");
        for(int i = 0; i < team2playerslist.size(); i++)
        {
            System.out.println("["+team2playerslist.get(i)+"]");
        }
    }

    @FXML
    public void randomizeTeams(ActionEvent e) {
        System.out.println("Randomize?");
        team1playerslist.clear();
        team2playerslist.clear();
        totalPlayers.clear();

        parsePlayers(Team1Players,team1playerslist);
        parsePlayers(Team2Players,team2playerslist);
        totalPlayers.addAll(team1playerslist);
        totalPlayers.addAll(team2playerslist);
        Collections.shuffle(totalPlayers);

        System.out.println("Randomized Players");
        for(int i = 0; i < totalPlayers.size(); i++)
        {
            System.out.println("["+totalPlayers.get(i)+"]");
        }
    }

    public void parsePlayers(TextArea players, List<String> list) {
        String input = players.getText();
        String name = "";
        int index = 0;

        for(int i = 0; i < players.getText().length()-1; i++)
        {
            if(players.getText().charAt(i)==','||players.getText().charAt(i)==' '||players.getText().charAt(i)=='\n')
            {
                name = players.getText().substring(index,i);
                index = i+1;
                if(name.matches("[a-zA-Z]+"))
                    list.add(name);
            }
        }

        // Add last entry
        name = players.getText().substring(index,input.length());
        if(name.matches("[a-zA-Z]+"))
            list.add(name);
    }

    public void setTeamNames(String team1_name, String team2_name) {
        Team1Title.setText("Team "+team1_name);
        Team2Title.setText("Team "+team2_name);
    }

    public void setStageAndSetupListeners(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    @FXML
    public void onButtonClicked(MouseEvent e) throws IOException {

    }

}
