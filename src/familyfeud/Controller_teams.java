package familyfeud;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller_teams {

    @FXML private Label Team1Title;
    @FXML private TextArea Team1Players;
    @FXML private Label Team2Title;
    @FXML private TextArea Team2Players;
    @FXML private Button submit;
    @FXML private ComboBox comboBox;

    private Stage primaryStage;
    private List<Player> team1playerslist;
    private List<Player> team2playerslist;
    private List<Player> totalPlayers;
    private String team1Name;
    private String team2Name;
    private int gameFile = 0;

    public void initialize() throws FileNotFoundException {
        team1playerslist = new ArrayList<Player>();
        team2playerslist = new ArrayList<Player>();
        totalPlayers = new ArrayList<Player>();

        Team1Players.setMaxHeight(200);
        Team1Players.setMaxWidth(300);
        Team1Players.setWrapText(true);

        Team2Players.setMaxHeight(200);
        Team2Players.setMaxWidth(300);
        Team2Players.setWrapText(true);

        submit.setDisable(true);

        File folder = new File("src/familyfeud/games");
        for(File gameFile : folder.listFiles()) {
            String gameName = gameFile.getName();
            gameName = gameName.substring(0,gameName.indexOf('.'));
            gameName = gameName.substring(0,4)+" "+gameName.substring(4);
            comboBox.getItems().add(gameName);
        }
        comboBox.setValue("Game "+gameFile);

        if(Main.Debugging()) {
            Team1Players.setText("Eden, Mcluvin, Moseph");
            Team2Players.setText("Emma, Benjamin, Joseph");
            submit.setDisable(false);
        }
    }

    public void setStageAndSetupListeners(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    @FXML
    public void startGame(ActionEvent e) throws IOException {
        team1playerslist.clear();
        team2playerslist.clear();
        totalPlayers.clear();

        parsePlayers(Team1Players,team1playerslist);
        parsePlayers(Team2Players,team2playerslist);
        totalPlayers.addAll(team1playerslist);
        totalPlayers.addAll(team2playerslist);

        // Debugging
        if(Main.Debugging()) {
            System.out.println("Team 1 Players");
            for (int i = 0; i < team1playerslist.size(); i++) {
                System.out.println("[" + team1playerslist.get(i) + "]");
            }
            System.out.println("Team 2 Players");
            for (int i = 0; i < team2playerslist.size(); i++) {
                System.out.println("[" + team2playerslist.get(i) + "]");
            }
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("xml/gamestage.fxml"));
        Parent root = (Parent)loader.load();
        Controller_gameStage gameStage = loader.getController();
        gameStage.setTeams(team1Name, team2Name, team1playerslist, team2playerslist);
        gameStage.chooseGameFile(Integer.parseInt(comboBox.getValue().toString().substring(5)));
        gameStage.setStageAndSetupListeners(primaryStage);
        primaryStage.setScene(new Scene(root, 720, 640));
        primaryStage.getScene().getStylesheets().add("style.css");
    }

    /**
     * Clears both team's player lists and
     * resets the text areas of
     * the team selection stage.
     * Also clears the total players list.
     *
     * @aurthor Benjamin
     * @param actionEvent UNUSED
     */
    public void clearTeams(ActionEvent actionEvent) {
        team1playerslist.clear();
        team2playerslist.clear();
        totalPlayers.clear();
        Team1Players.clear();
        Team2Players.clear();
        this.checkSubmitStatus();
    }

    @FXML
    public void randomizeTeams(ActionEvent e) {
        String team1Names = "";
        String team2Names = "";

        System.out.println("Randomize?");
        team1playerslist.clear();
        team2playerslist.clear();
        totalPlayers.clear();

        parsePlayers(Team1Players,totalPlayers);
        parsePlayers(Team2Players,totalPlayers);

        // Randomize players
        Collections.shuffle(totalPlayers);

        // Split players into teams
        // if there are an uneven number of total players,
        // team 2 will always have one more
        for(int i = 0; i < totalPlayers.size(); i++)
        {
            if(i < totalPlayers.size()/2) {
                team1playerslist.add(totalPlayers.get(i));
                team1Names += totalPlayers.get(i)+"\n";
            }
            else {
                team2playerslist.add(totalPlayers.get(i));
                team2Names += totalPlayers.get(i)+"\n";
            }
        }

        Team1Players.replaceText(0,Team1Players.getLength(),team1Names.trim());
        Team2Players.replaceText(0,Team2Players.getLength(),team2Names.trim());

        // Debugging
        if(Main.Debugging()) {
            System.out.println("\nRandomized Players");
            System.out.println("\nTeam 1 Players\n");
            for (int i = 0; i < team1playerslist.size(); i++) {
                System.out.println("[" + team1playerslist.get(i) + "]");
            }
            System.out.println("\nTeam 2 Players\n");
            for (int i = 0; i < team2playerslist.size(); i++) {
                System.out.println("[" + team2playerslist.get(i) + "]");
            }
        }

        this.checkSubmitStatus();
    }

    //TODO parsePlayers needs work
    public void parsePlayers(TextArea players, List<Player> list) {
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
                    list.add(new Player(name));
            }
        }

        // Add last entry
        name = players.getText().substring(index,input.length());
        if(name.matches("[a-zA-Z]+"))
            list.add(new Player(name));
    }

    public void setTeamNames(String team1_name, String team2_name) {
        Team1Title.setText(""+team1_name+":");
        Team2Title.setText(""+team2_name+":");
        team1Name = team1_name;
        team2Name = team2_name;
        //System.out.println("setTeamNames called");
    }

    public void passPastVariables(int gameFile, Team team1, Team team2){
        this.gameFile = gameFile;
        System.out.println("Gamefile: "+gameFile);
        String team1Players = team1.getPlayers().toString();
        String team2Players = team2.getPlayers().toString();
        Team1Players.setText(team1Players.substring(1,team1Players.length()-1));
        Team2Players.setText(team2Players.substring(1,team2Players.length()-1));
        comboBox.setValue("Game "+gameFile);
    }

    public void onKeyTyped(KeyEvent keyEvent) {
        this.checkSubmitStatus();
    }

    public void checkSubmitStatus()
    {
        if(Team1Players.getText().isBlank() || Team2Players.getText().isBlank())
            submit.setDisable(true);
        else
            submit.setDisable(false);
    }
}