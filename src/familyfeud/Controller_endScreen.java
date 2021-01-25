package familyfeud;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Controller_endScreen {

    @FXML private VBox topStatsPane;
    @FXML private VBox bottomStatsPane;
    @FXML private ComboBox winners;
    @FXML private ComboBox losers;
    private Stage primaryStage;
    private LinkedList<Round> game;
    private int gameFile = 0;
    private Team winningTeam;
    private Team losingTeam;

    public void setStageAndSetupListeners(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.getScene().getStylesheets().add("style.css");
    }

    public void passGame(LinkedList<Round> game, int gameFile) {
        this.game = game;
        this.gameFile = gameFile;

        // Determine Game Winner
        if(game.get(0).getCurrentTeam().getTotalPoints() > game.get(0).getOtherTeam().getTotalPoints()) {
            game.get(0).getCurrentTeam().setWinner();
            winningTeam = game.get(0).getCurrentTeam();
            losingTeam = game.get(0).getOtherTeam();
        }
        else {
            game.get(0).getOtherTeam().setWinner();
            winningTeam = game.get(0).getOtherTeam();
            losingTeam = game.get(0).getCurrentTeam();
        }

        // Debugging
        if (Main.Debugging()) {
            System.out.println(winningTeam.getStats());
            for (int i = 0; i < winningTeam.getPlayers().size(); i++) {
                System.out.println(winningTeam.getPlayer(i).getStats());
            }
            System.out.println(losingTeam.getStats());
            for (int i = 0; i < losingTeam.getPlayers().size(); i++) {
                System.out.println(losingTeam.getPlayer(i).getStats());
            }
        }

        // By default, show team stats
        showTeamStats();

        setUpComboBox(winners, winningTeam);
        setUpComboBox(losers, losingTeam);
    }

    /**
     * Runs through a stat log of a team and
     * populates given pane with labels detatling
     * stat information.
     *
     * @param pane - pane to be populated
     * @param text - input stats
     */
    public void populatePane(VBox pane, String text) {
        pane.getChildren().clear();
        Scanner scan = new Scanner(text);
        Label line;
        boolean firstLine = true;

        // iterates through stats and prints a label for each one
        while(scan.hasNextLine()) {
            line = new Label(scan.nextLine().trim());

            if(firstLine) {
                line.getStyleClass().add("statsTitle");
                line.setFont(new Font("sans-serif", 32));
                firstLine = false;
            }
            else {
                line.getStyleClass().add("stats");
            }
            pane.getChildren().add(line);
        }
        scan.close();
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

    public void showTeamStats(MouseEvent e) {
        VBox parent = (VBox) ((Button) e.getSource()).getParent();
        if(parent.getId().equals("hostViewTop")) {
            populatePane(topStatsPane, winningTeam.getStats());
        }
        else if(parent.getId().equals("hostViewBottom")) {
            populatePane(bottomStatsPane, losingTeam.getStats());
        }
    }

    public void showTeamStats() {
        populatePane(topStatsPane, winningTeam.getStats());
        populatePane(bottomStatsPane, losingTeam.getStats());
    }

    public void showPlayerStats(MouseEvent e) {
        VBox parent = (VBox) ((Button) e.getSource()).getParent();
        HBox container = (HBox) parent.getChildren().get(0);
        ComboBox comboBox = (ComboBox) container.getChildren().get(1);

        if(parent.getId().equals("hostViewTop")) {
            populatePane(topStatsPane, ((Player) comboBox.getValue()).getStats());
        }
        else if(parent.getId().equals("hostViewBottom")) {
            populatePane(bottomStatsPane, ((Player) comboBox.getValue()).getStats());
        }
    }

    public void setUpComboBox(ComboBox combo, Team team) {
        for(int i=0; i < team.getPlayers().size(); i++) {
            combo.getItems().add(team.getPlayer(i));
        }
    }
}
