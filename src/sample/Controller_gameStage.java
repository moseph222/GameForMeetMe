package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Controller_gameStage {

    Stage primaryStage;
    private LinkedList<Text> buttons;
    @FXML
    private Text button1;
    @FXML
    private Text button2;
    @FXML
    private Text button3;
    @FXML
    private Text button4;
    @FXML
    private Text button5;
    @FXML
    private Text button6;
    @FXML
    private Text button7;
    @FXML
    private Text button8;
    @FXML
    private Text point1;
    @FXML
    private Text point2;
    @FXML
    private Text point3;
    @FXML
    private Text point4;
    @FXML
    private Text point5;
    @FXML
    private Text point6;
    @FXML
    private Text point7;
    @FXML
    private Text point8;
    @FXML
    private Label question;
    @FXML
    private Text ans1;
    @FXML
    private Text ans2;
    @FXML
    private Text ans3;
    @FXML
    private Text ans4;
    @FXML
    private Text ans5;
    @FXML
    private Text ans6;
    @FXML
    private Text ans7;
    @FXML
    private Text ans8;
    @FXML
    private Label gameRound;
    @FXML
    private ImageView team1Attempt1;
    @FXML
    private ImageView team1Attempt2;
    @FXML
    private ImageView team1Attempt3;
    @FXML
    private ImageView team2Attempt1;
    @FXML
    private ImageView team2Attempt2;
    @FXML
    private ImageView team2Attempt3;
    @FXML
    private Label roundPoints;
    @FXML
    private Label team1Name;
    @FXML
    private Label team2Name;
    @FXML
    private Label currentPlayer;
    @FXML
    private Label currentTeam;
    @FXML
    private Label team1Points;
    @FXML
    private Label team2Points;
    @FXML
    private Label Team1Timer;
    @FXML
    private Label Team2Timer;

    private int round = 0;
    private int roundPointsInteger;
    private int maxNumAnswers;
    private int currentAmountAnswered = 0;
    private Team team1;
    private Team team2;
    private int[] team1NumAttempts = new int[3]; // array of trials per team
    private int[] team2NumAttempts = new int[3];
    private LinkedList<Text> allButtons = new LinkedList<Text>();
    private LinkedList<Text> points = new LinkedList<Text>();
    private LinkedList<Text> answers = new LinkedList<Text>();
    private int Gamefile = 0;

    private boolean noMorePoints = false;
    private boolean timerShouldRun = false;

    private LinkedList<Round> Game = new LinkedList<Round>();


    // Initializing variables, arrays, and setting labels as empty or lower opacity

    public void initialize(){


    }
    public void LoadQuestions(){
        int turn = 0;
        round = 1;
        if(Gamefile == 0){
            Gamefile = 3;
        }
        roundPointsInteger = 0;

        // Reading questions from a file into a LinkedList of questions
        try {
            File game = new File("src/sample/Game"+Gamefile+".txt");
            Scanner myReader = new Scanner(game);
            String input;
            String question = null;
            LinkedList<String> answers = new LinkedList<String>();
            LinkedList<Integer> points = new LinkedList<Integer>();
            int id = 1;

            int lineNum = 1;

            while (myReader.hasNext()) {
                input = myReader.nextLine();
                if (input.compareTo("NEW QUESTION") != 0) {
                    if (lineNum == 1) {
                        question = input;
                    } else if (lineNum % 2 == 0) {
                        answers.add(input);
                    } else if (lineNum % 2 != 0) {
                        points.add(Integer.parseInt(input));
                    }
                    lineNum++;
                } else {
                    lineNum = 1;
                    Game.add(new Round(question, answers, points, id));
                    answers = new LinkedList<String>();
                    points = new LinkedList<Integer>();
                    id++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // Creating a linkedList for all the buttons and point buttons

        allButtons.add(button1);
        allButtons.add(button2);
        allButtons.add(button3);
        allButtons.add(button4);
        allButtons.add(button5);
        allButtons.add(button6);
        allButtons.add(button7);
        allButtons.add(button8);

        answers.add(ans1);
        answers.add(ans2);
        answers.add(ans3);
        answers.add(ans4);
        answers.add(ans5);
        answers.add(ans6);
        answers.add(ans7);
        answers.add(ans8);


        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);
        points.add(point6);
        points.add(point7);
        points.add(point8);

        cleanAnswerButtons();

        //Populate with all answers
        populateAnsFromCurQuestion(round);

        // Decide who goes first
        turn = (Math.random() <= 0.5) ? 1 : 2;
        if (turn == 1)
            Game.get(0).setCurrentTeam(team2);
        else if (turn == 2)
            Game.get(0).setCurrentTeam(team1);
        this.switchTeams();

        if(team1Name.getText() == null){
            team1Name.setText("Team 1");
        }

        if(team2Name.getText() == null){
            team2Name.setText("Team 2");
        }

        if(Main.Debugging()) {
            System.out.println(Game.get(round-1).getCurrentTeam()+" selected to go first.");
        }

        //Gets the answers of the first game
        maxNumAnswers = Game.get(round-1).getAnswers().size();

        this.refreshLabels();
    }

    public static int getRandomInteger(int maximum, int minimum) {
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }

    public void onButtonClicked(MouseEvent e) throws IOException {
        StackPane stacknum = (StackPane) e.getSource();
        int idnum = Integer.parseInt(stacknum.getId());

        // If button has already been pressed, do nothing
        if(((StackPane) e.getSource()).getChildren().get(1).getOpacity() == 100)
            return;

        timerShouldRun = false;
        currentAmountAnswered++;

        if (noMorePoints == true) {
            roundPointsInteger = 0;
        }
        else if(Game.get(round-1).hasChanceToSteal() == false){
            roundPointsInteger += Integer.parseInt(this.points.get(idnum-1).getText());
            //TODO need a better way of handling this
            Game.get(round-1).getCurrentPlayer().addPoints(roundPointsInteger);
            Game.get(round-1).getCurrentTeam().rotatePlayers();
        }

        ((StackPane) e.getSource()).getChildren().get(1).setOpacity(100);
        this.points.get(idnum-1).setOpacity(100);

        // Handle stealing
        if(Game.get(round-1).hasChanceToSteal()) {
            roundPointsInteger += Integer.parseInt(this.points.get(idnum-1).getText());
            Game.get(round-1).getCurrentTeam().addStolenPoints(roundPointsInteger);
            //Game.get(round-1).getOtherTeam().rollBackPlayers(...);
            noMorePoints = true;
        }

        // Handle slam dunk
        if (currentAmountAnswered == maxNumAnswers && noMorePoints == false) {
            Game.get(round-1).getCurrentTeam().addPoints(roundPointsInteger);
            Game.get(round-1).getCurrentTeam().increaseSlamDunks();
            noMorePoints = true;
        }

        this.refreshLabels();
    }


    public void AttemptHandler(MouseEvent e) {

        Label currentLabel = (Label) e.getSource();
        String buttonId = currentLabel.getId();

        if (buttonId.compareTo("team1attemptButton1") == 0) {
            Image newimg = null;
            if (team1NumAttempts[0] == 0) {
                newimg = new Image(getClass().getResourceAsStream("RedButton.png"));
                team1NumAttempts[0] = 1;
            } else if (team1NumAttempts[0] == 1) {
                newimg = new Image(getClass().getResourceAsStream("GreenButton.png"));
                team1NumAttempts[0] = 0;
            }
            team1Attempt1.setImage(newimg);
            team1Attempt1.setFitHeight(10);
            team1Attempt1.setFitWidth(10);
        } else if (buttonId.compareTo("team1attemptButton2") == 0) {
            Image newimg = null;
            if (team1NumAttempts[1] == 0) {
                newimg = new Image(getClass().getResourceAsStream("RedButton.png"));
                team1NumAttempts[1] = 1;
            } else {
                newimg = new Image(getClass().getResourceAsStream("GreenButton.png"));
                team1NumAttempts[1] = 0;
            }
            team1Attempt2.setImage(newimg);
            team1Attempt2.setFitHeight(10);
            team1Attempt2.setFitWidth(10);
        } else if (buttonId.compareTo("team1attemptButton3") == 0) {
            Image newimg = null;
            if (team1NumAttempts[2] == 0) {
                newimg = new Image(getClass().getResourceAsStream("RedButton.png"));
                team1NumAttempts[2] = 1;
            } else {
                newimg = new Image(getClass().getResourceAsStream("GreenButton.png"));
                team1NumAttempts[2] = 0;
            }
            team1Attempt3.setImage(newimg);
            team1Attempt3.setFitHeight(10);
            team1Attempt3.setFitWidth(10);
        } else if (buttonId.compareTo("team2attemptButton1") == 0) {
            Image newimg = null;
            if (team2NumAttempts[0] == 0) {
                newimg = new Image(getClass().getResourceAsStream("RedButton.png"));
                team2NumAttempts[0] = 1;
            } else {
                newimg = new Image(getClass().getResourceAsStream("GreenButton.png"));
                team2NumAttempts[0] = 0;
            }
            team2Attempt1.setImage(newimg);
            team2Attempt1.setFitHeight(10);
            team2Attempt1.setFitWidth(10);
        } else if (buttonId.compareTo("team2attemptButton2") == 0) {
            Image newimg = null;
            if (team2NumAttempts[1] == 0) {
                newimg = new Image(getClass().getResourceAsStream("RedButton.png"));
                team2NumAttempts[1] = 1;
            } else {
                newimg = new Image(getClass().getResourceAsStream("GreenButton.png"));
                team2NumAttempts[1] = 0;
            }
            team2Attempt2.setImage(newimg);
            team2Attempt2.setFitHeight(10);
            team2Attempt2.setFitWidth(10);
        } else if (buttonId.compareTo("team2attemptButton3") == 0) {
            Image newimg = null;
            if (team2NumAttempts[2] == 0) {
                newimg = new Image(getClass().getResourceAsStream("RedButton.png"));
                team2NumAttempts[2] = 1;
            } else {
                newimg = new Image(getClass().getResourceAsStream("GreenButton.png"));
                team2NumAttempts[2] = 0;
            }
            team2Attempt3.setImage(newimg);
            team2Attempt3.setFitHeight(10);
            team2Attempt3.setFitWidth(10);
        }

        // If a team has 3 strikes, switch teams
        if(team1NumAttempts[0] == 1 && team1NumAttempts[1] == 1 && team1NumAttempts[2] == 1) {
            switchTeams();
        }
        else if(team2NumAttempts[0] == 1 && team2NumAttempts[1] == 1 && team2NumAttempts[2] == 1) {
            switchTeams();
        }


    }

    public void cleanAnswerButtons() {
        //Clear up all text in case there's an extra answers
        for (int i = 0; i < allButtons.size(); i++) {
            answers.get(i).setText("");
            allButtons.get(i).setText("");
            points.get(i).setText("");
        }
    }

    public void populateAnsFromCurQuestion(int round) {
        int numOfAnswers = Game.get(round - 1).getAnswers().size();
        double maxAnsWidth = 130;
        double ansWidth;
        double fontSize;

        for (int i = 0; i < numOfAnswers; i++) {
            allButtons.get(i).setText("   "+Game.get(round - 1).getAnswers().get(i).toUpperCase()+"   ");
            answers.get(i).setText(Game.get(round - 1).getAnswers().get(i));
            points.get(i).setText(String.valueOf(Game.get(round - 1).getPoints().get(i)));
            // Styling
            Text answer = new Text(Game.get(round - 1).getAnswers().get(i).toUpperCase());
            ansWidth = answer.getLayoutBounds().getWidth();
            fontSize = 16;
            // Text Scaling
            if(ansWidth > maxAnsWidth) {
                fontSize = 16 * maxAnsWidth / ansWidth;
            }
            allButtons.get(i).setFont(Font.font("sans-serif", FontWeight.BOLD, fontSize));
            // End of styling
        }

        // For all buttons and points that don't get populated, hide
        for (int i = numOfAnswers; i < 8; i++) {
            allButtons.get(i).getParent().setOpacity(0);
            points.get(i).getParent().setOpacity(0);
        }
    }


    public void zeroOpacityAnsFromCurQuestion(int round) {
        for (int i = 0; i < Game.get(round - 1).getAnswers().size(); i++) {
            allButtons.get(i).setOpacity(0);
            points.get(i).setOpacity(0);
        }
    }

    public void goToNextQuestion() throws IOException {
        timerShouldRun = false;

        //if game is over, proceed to end screen
        if(round == Game.size()){
            endGame();
        }

        roundPointsInteger = 0;
        noMorePoints = false;
        round++;
        cleanAnswerButtons();
        populateAnsFromCurQuestion(round);
        zeroOpacityAnsFromCurQuestion(round);
        maxNumAnswers = Game.get(round - 1).getAnswers().size();
        currentAmountAnswered = 0;
        resetAttempts();

        // Set the current team of the new round to the last playing team
        Game.get(round-1).setCurrentTeam(Game.get(round-2).getCurrentTeam());
        // If the team previously playing was stealing, they should go first
        // Otherwise, the teams should switch for the new round
        if(Game.get(round-2).hasChanceToSteal() == false)
            switchTeams();
        refreshLabels();
    }

    public void keepPoints() {
        // Upon failure to steal, and host presses "keep points" button
        if(Game.get(round-1).hasChanceToSteal()) {
            // The current player is the team set to steal, so we must switch
            switchTeams();
            Game.get(round-1).setChanceToSteal(false);
            // After switching we can add points to the correct team
            Game.get(round-1).getCurrentTeam().addPoints(roundPointsInteger);
            noMorePoints = true;
        }
        refreshLabels();
    }

    public void resetAttempts() {
        team1NumAttempts = new int[3];
        team2NumAttempts = new int[3];

        Image newimg = new Image(getClass().getResourceAsStream("GreenButton.png"));

        team1Attempt1.setImage(newimg);
        team1Attempt1.setFitHeight(10);
        team1Attempt1.setFitWidth(10);
        team1Attempt2.setImage(newimg);
        team1Attempt2.setFitHeight(10);
        team1Attempt2.setFitWidth(10);
        team1Attempt2.setImage(newimg);
        team1Attempt2.setFitHeight(10);
        team1Attempt2.setFitWidth(10);
        team2Attempt1.setImage(newimg);
        team2Attempt1.setFitHeight(10);
        team2Attempt1.setFitWidth(10);
        team2Attempt2.setImage(newimg);
        team2Attempt2.setFitHeight(10);
        team2Attempt2.setFitWidth(10);
        team2Attempt3.setImage(newimg);
        team2Attempt3.setFitHeight(10);
        team2Attempt3.setFitWidth(10);
    }

    public void refreshLabels() {
        // Refresh question
        Text ques = new Text(Game.get(round-1).getQuestion());
        double maxQuesWidth = 445;
        double quesWidth = ques.getLayoutBounds().getWidth();
        double fontSize = 18;
        if(quesWidth > maxQuesWidth) {
            fontSize = fontSize * maxQuesWidth / quesWidth;
        }
        question.setFont(Font.font("sans-serif", FontWeight.BOLD, fontSize));
        question.setText(Game.get(round-1).getQuestion());

        // Refresh round counter
        gameRound.setText("Round " + String.valueOf(round));

        // Refresh team names
        team1Name.setText(team1.toString());
        team2Name.setText(team2.toString());

        // Refresh current team indication
        if(Game.get(round-1).getCurrentTeam().equals(team1)) {
            team2Name.setBackground(Background.EMPTY);
            team1Name.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 80, 0.7), new CornerRadii(5.0), new Insets(-2.0))));
        }
        else if(Game.get(round-1).getCurrentTeam().equals(team2)) {
            team1Name.setBackground(Background.EMPTY);
            team2Name.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 80, 0.7), new CornerRadii(5.0), new Insets(-2.0))));
        }

        // Refresh point counters
        roundPoints.setText(roundPointsInteger+"");
        team1Points.setText(team1.getTotalPoints()+"");
        team2Points.setText(team2.getTotalPoints()+"");

        // Refresh timers
        if(timerShouldRun == false) {
            Team1Timer.setText("0");
            Team2Timer.setText("0");
        }

        // Refresh current team and player labels for host
        setCurrentTeamAndPlayerLabels();
    }


    public void setStageAndSetupListeners(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Set team names and player lists.
     *
     * @param team1title       Team 1's name
     * @param team2title       Team 2's name
     * @param team1playerslist Team 1's list of players
     * @param team2playerslist Team 2's list of players
     * @author Benjamin
     */
    public void setTeams(String team1title, String team2title, List<Player> team1playerslist, List<Player> team2playerslist) {
        this.team1 = new Team(team1title, team1playerslist);
        this.team2 = new Team(team2title, team2playerslist);
        team1Name.setText(team1title);
        team2Name.setText(team2title);
    }

    public void switchTeams() {
        if(Game.get(round-1).getCurrentTeam().equals(team1)) {
            Game.get(round-1).setCurrentTeam(team2);
            if(team1NumAttempts[0] == 1 && team1NumAttempts[1] == 1 && team1NumAttempts[2] == 1)
                Game.get(round-1).setChanceToSteal(true);
        }
        else if(Game.get(round-1).getCurrentTeam().equals(team2)) {
            Game.get(round-1).setCurrentTeam(team1);
            if(team2NumAttempts[0] == 1 && team2NumAttempts[1] == 1 && team2NumAttempts[2] == 1)
                Game.get(round-1).setChanceToSteal(true);
        }
        else {
            // This should never happen
            System.err.print("ERROR: "+Game.get(round-1).getCurrentTeam());
            System.err.println(" is the current team. This should not happen.");
        }

        refreshLabels();
    }

//    /**
//     * Method to return the team digit of whom is currently playing
//     * (either team 1 or team 2)
//     *
//     * @return number of current team
//     * @author Benjamin
//     */
//    public int getCurrentTeamNum() {
//        if (setToSteal)
//            return thieves;
//        else
//            return gamers;
//    }



    /**
     * Method to return the name of the current player.
     * If team 1 is playing, pulls from team 1's player list.
     * Else, pulls from team 2's.
     * Before a player name is returned, the respective
     * player list is rotated by 1.
     *
     * @return name of current player
     * @author Benjamin
     */
//    public String getCurrentPlayer() {
//        if (getCurrentTeamNum() == 1) {
//            Collections.rotate(team1playerslist, 1);
//            return this.team1playerslist.get(0);
//        } else {
//            Collections.rotate(team2playerslist, 1);
//            return this.team2playerslist.get(0);
//        }
//    }
//
    public void setCurrentTeamAndPlayerLabels() {
        currentPlayer.setText(Game.get(round-1).getCurrentPlayer().toString());
        currentTeam.setText(Game.get(round-1).getCurrentTeam().toString());
        if(Game.get(round-1).hasChanceToSteal())
            currentPlayer.setText("STEALING");
    }

    public void startTiming(MouseEvent mouseEvent) throws FileNotFoundException {
        if(timerShouldRun == true) {
            timerShouldRun = false;
        }
        else {
            if (Game.get(round-1).getCurrentTeam().equals(team1)) {
                if(Game.get(round-1).hasChanceToSteal())
                    startTimer(60,Team1Timer);
                else
                    startTimer(20,Team1Timer);
            }
            else if (Game.get(round-1).getCurrentTeam().equals(team2)) {
                if(Game.get(round-1).hasChanceToSteal())
                    startTimer(60,Team2Timer);
                else
                    startTimer(20,Team2Timer);
            }
        }
    }

    public void startTimer(int seconds, Label clock) {
        timerShouldRun = true;
        Thread one = new Thread() {
            private double i = seconds * 10;
            public void run() {
                try {
                    while (i + 1 > 0 && timerShouldRun) {
                        i--;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (i <= 0)
                                    clock.setText("0");
                                else
                                    clock.setText("" + i / 10);
                            }
                        });

                        Thread.sleep(100);
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        one.start();
    }

    public void endGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("endScreen.fxml"));
        Parent root = (Parent)loader.load();
        Controller_endScreen endgame = (Controller_endScreen)loader.getController();
        endgame.setStageAndSetupListeners(primaryStage);
        endgame.passTeams(team1, team2);
//            int t1 = Integer.parseInt(team1Points.getText());
//            int t2 = Integer.parseInt(team2Points.getText());
//            if(t1 > t2){
//                endgame.setWinnerTeam(team1title, team1playerslist);
//            } else {
//                endgame.setWinnerTeam(team2title, team2playerslist);
//            }
        primaryStage.setScene(new Scene(root, 720, 640));
        return;
    }

    public void chooseGameFile(int gameFile){
        System.out.println("Inside gameStage: " + gameFile);
        this.Gamefile = gameFile;
    }
}