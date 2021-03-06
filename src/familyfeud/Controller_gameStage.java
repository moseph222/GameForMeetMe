package familyfeud;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Controller_gameStage {
    @FXML private Label question;
    @FXML private Label gameRound;
    @FXML private Label roundPoints;
    @FXML private Label team1Name;
    @FXML private Label team2Name;
    @FXML private Label currentPlayer;
    @FXML private Label currentTeam;
    @FXML private Label team1Points;
    @FXML private Label team2Points;
    @FXML private Label Team1Timer;
    @FXML private Label Team2Timer;
    @FXML private VBox ansVBox;
    @FXML private VBox hostView;
    @FXML private GridPane team1StrikeIndicators;
    @FXML private GridPane team2StrikeIndicators;
    @FXML private Button timerButton;
    @FXML private Button nextQuesButton;
    @FXML private Button strikeButton;
    @FXML private Button revealButton;

    private Stage primaryStage;
    private int round = 0;
    private int roundPointsInteger;
    private int maxNumAnswers;
    private int currentAmountAnswered = 0;
    private int strikes = 0;
    private Team team1;
    private Team team2;
    private int gameFile = 0;

    private boolean revealed;
    private boolean noMorePoints = false;
    private boolean timerShouldRun = false;

    // Media credit: https://freesound.org/people/Fachii/sounds/338229/
    private AudioClip hoverEffect = new AudioClip(new File("src/familyfeud/sounds/hover.wav").toURI().toString());
    // Media credit: https://www.myinstants.com/instant/family-feud-yes-ding-24818/
    private AudioClip bellEffect = new AudioClip(new File("src/familyfeud/sounds/bell.mp3").toURI().toString());
    // Media credit: https://www.youtube.com/watch?v=NtKEMWX8OqU
    private AudioClip strikeEffect = new AudioClip(new File("src/familyfeud/sounds/strike.mp3").toURI().toString());
    // Media credit: https://freesound.org/people/f4ngy/sounds/240776/
    private AudioClip cardEffect = new AudioClip(new File("src/familyfeud/sounds/card.wav").toURI().toString());

    private LinkedList<Round> Game = new LinkedList<Round>();

    public void setStageAndSetupListeners(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void LoadQuestions(MouseEvent event){
        ((Button) event.getSource()).setDisable(true);
        timerButton.setDisable(false);
        strikeButton.setDisable(false);
        int turn = 0;
        round = 1;
        roundPointsInteger = 0;

        // Reading questions from a file into a LinkedList of questions
        try {
            File game = new File("src/familyfeud/games/Game"+ gameFile +".txt");
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

        cleanAnswerButtons();

        //Populate with all answers
        populateAnsFromCurQuestion(round);

        // Decide who goes first
        turn = (Math.random() <= 0.5) ? 1 : 2;
        if (turn == 1)
            Game.get(0).setCurrentTeam(team2);
        else if (turn == 2)
            Game.get(0).setCurrentTeam(team1);
        switchTeams();

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
        refreshLabels();
    }

    public void onAnswerChosen(MouseEvent e) {
        // If round is over, do nothing
        if(noMorePoints) {
            return;
        }

        bellEffect.play();
        BorderPane front = (BorderPane) e.getSource();
        StackPane stack = (StackPane) ((BorderPane)e.getSource()).getParent();
        BorderPane back = (BorderPane) stack.getChildren().get(0);

        animateFlip(stack);

        Label points = (Label) back.getRight();

        // If button has already been pressed, do nothing
        if(((BorderPane) e.getSource()).getChildren().get(0).getOpacity() == 100)
            return;

        timerShouldRun = false;
        currentAmountAnswered++;

        if (noMorePoints == true) {
            roundPointsInteger = 0;
        }
        else if(Game.get(round-1).aTeamHasChanceToSteal() == false){
            roundPointsInteger += Integer.parseInt(points.getText());
            Game.get(round-1).getCurrentPlayer().storeTempPoints(roundPointsInteger, 1);
            Game.get(round-1).getCurrentTeam().rotatePlayers();
        }

        // Handle stealing
        if(Game.get(round-1).aTeamHasChanceToSteal()) {
            roundPointsInteger += Integer.parseInt(points.getText());
            Game.get(round-1).getCurrentTeam().addStolenPoints(roundPointsInteger);
            Game.get(round-1).getOtherTeam().clearTeamTempPoints();
            noMorePoints = true;
        }

        // Handle slam dunk
        if (currentAmountAnswered == maxNumAnswers && noMorePoints == false) {
            Game.get(round-1).getCurrentTeam().addPoints(roundPointsInteger);
            Game.get(round-1).getCurrentTeam().increaseSlamDunks();
            revealed = true;
            noMorePoints = true;
            if(Main.Debugging())
                System.out.println("Slam Dunk occurred: "+roundPointsInteger+" points.");
        }

        refreshLabels();
    }

    public void cleanAnswerButtons() {
        ansVBox.getChildren().clear();
        hostView.getChildren().clear();
    }

    public void populateAnsFromCurQuestion(int round) {
        int numOfAnswers = Game.get(round - 1).getAnswers().size();
        // Volume range 0.0 - 1.0
        bellEffect.setVolume(0.35);
        //TODO change? was 200
        double maxAnsWidth = 100;
        double ansWidth;
        double fontSize;
        StackPane stack;
        BorderPane front;
        BorderPane back;
        BorderPane hostPane;
        Text answer;
        Label points;
        Text hostAns;
        Label hostPnts;
        Text placeholder;

        for (int i = 0; i < numOfAnswers; i++) {
            stack = new StackPane();
            front = new BorderPane();
            back = new BorderPane();
            answer = new Text(Game.get(round - 1).getAnswers().get(i).toUpperCase());
            points = new Label(String.valueOf(Game.get(round - 1).getPoints().get(i)));

            hostPane = new BorderPane();
            hostAns = new Text(Game.get(round - 1).getAnswers().get(i).toUpperCase());
            hostPnts = new Label(String.valueOf(Game.get(round - 1).getPoints().get(i)));
            placeholder = new Text("");

            points.setPrefWidth(30);
            points.setAlignment(Pos.CENTER);

            hostPnts.setPrefWidth(30);
            hostPnts.setAlignment(Pos.CENTER);

            hostPane.getStyleClass().add("answer");
            hostAns.getStyleClass().add("button");
            hostPnts.getStyleClass().add("points");

            front.getStyleClass().add("answer");
            back.getStyleClass().add("answer");
            answer.getStyleClass().add("button");
            points.getStyleClass().add("points");

            hostPane.setMinWidth(200);
            hostPane.setMaxWidth(200);
            back.setMinWidth(200);
            back.setMaxWidth(200);
            front.setMaxWidth(200);
            front.setMinWidth(200);

            hostPane.setLeft(hostAns);
            hostPane.setRight(hostPnts);

            back.setLeft(answer);
            back.setRight(points);
            back.setId("coverFace");
            front.setId("answerFace");
            front.setCenter(placeholder);
            front.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    onAnswerChosen(e);
                    BorderPane pane = (BorderPane) e.getSource();
                    System.out.println(pane.getId()+" clicked");
                }
            });
            front.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    primaryStage.getScene().setCursor(Cursor.HAND);
                    BorderPane pane = (BorderPane) e.getSource();
                    pane.getStyleClass().add("answerHover");
                    hoverEffect.play();
                }
            });
            front.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    primaryStage.getScene().setCursor(Cursor.DEFAULT);
                    BorderPane pane = (BorderPane) e.getSource();
                    pane.getStyleClass().remove("answerHover");
                }
            });

            BorderPane.setAlignment(back.getChildren().get(0), Pos.CENTER);
            BorderPane.setMargin(back.getChildren().get(0), new Insets(0,10,0,10));

            BorderPane.setAlignment(hostPane.getChildren().get(0), Pos.CENTER);
            BorderPane.setMargin(hostPane.getChildren().get(0), new Insets(0,10,0,10));

            stack.getChildren().add(back);
            stack.getChildren().add(front);
            ansVBox.getChildren().add(stack);
            hostView.getChildren().add(hostPane);

            // Styling
            ansWidth = answer.getLayoutBounds().getWidth();
            fontSize = 16;
            // Text Scaling
            if(ansWidth > maxAnsWidth) {
                fontSize = 16 * maxAnsWidth / ansWidth;
            }
            hostAns.setFont(Font.font("sans-serif", FontWeight.BOLD, fontSize));
            answer.setFont(Font.font("sans-serif", FontWeight.BOLD, fontSize));
            // End of styling
        }
    }

    public void goToNextQuestion() throws IOException {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                flipAllAnswers((StackPane) ansVBox.getChildren().get(0));
                FadeTransition fadeOut = new FadeTransition(Duration.millis(500),question.getParent());
                fadeOut.setFromValue(10);
                fadeOut.setToValue(0);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(500),question.getParent());
                fadeIn.setFromValue(0);
                fadeIn.setToValue(10);

                fadeOut.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        round++;
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
                        fadeIn.play();
                    }
                });
                fadeOut.play();
                Thread.sleep(200*maxNumAnswers);
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                timerShouldRun = false;
                roundPointsInteger = 0;
                noMorePoints = false;
                cleanAnswerButtons();
                populateAnsFromCurQuestion(round);
                maxNumAnswers = Game.get(round - 1).getAnswers().size();
                currentAmountAnswered = 0;
                revealed = false;
                // Set the current team of the new round to the last playing team
                Game.get(round-1).setCurrentTeam(Game.get(round-2).getCurrentTeam());
                // If the team previously playing was stealing, they should go first
                // Otherwise, the teams should switch for the new round
                if(Game.get(round-2).aTeamHasChanceToSteal() == false)
                    switchTeams();
                refreshLabels();
            }
        });

        //if game is over, proceed to end screen
        if(round == Game.size())
            endGame();
        else
            new Thread(sleeper).start();
    }

    public void keepPoints() {
        // Upon failure to steal, and host presses "keep points" button
        if(Game.get(round-1).aTeamHasChanceToSteal()) {
            // The current player is the team set to steal, so we must switch
            switchTeams();
            Game.get(round-1).setChanceToSteal(false);
            // After switching we can add points to the correct team
            Game.get(round-1).getCurrentTeam().addPoints(roundPointsInteger);
            noMorePoints = true;
        }
        refreshLabels();
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
        if(!timerShouldRun) {
            Team1Timer.setText("0");
            Team2Timer.setText("0");
        }

        // Refresh current team and player labels for host
        setCurrentTeamAndPlayerLabels();

        // Refresh strike indicators and host buttons
        if(noMorePoints) {
            strikes = 0;
            strikeButton.setDisable(true);
            if(revealButton.isDisable() && !revealed) {
                revealButton.setDisable(false);
            }
            else
            {
                nextQuesButton.setDisable(false);
            }
        }
        else {
            strikeButton.setDisable(false);
            nextQuesButton.setDisable(true);
        }

        for(int i = 0; i < 3; i++) {
            Circle indicator1 = new Circle(0,0,6);
            if((strikes > i && Game.get(round-1).getCurrentTeam().equals(team1) && !(Game.get(round-1).aTeamHasChanceToSteal())) || (Game.get(round-1).aTeamHasChanceToSteal() && Game.get(round-1).getCurrentTeam().equals(team2)))
                indicator1.setFill(Color.RED);
            else
                indicator1.setFill(Color.GREEN);
            GridPane.setColumnIndex(indicator1,i);
            team1StrikeIndicators.getChildren().add(indicator1);

            Circle indicator2 = new Circle(0,0,6);
            if((strikes > i && Game.get(round-1).getCurrentTeam().equals(team2) && !(Game.get(round-1).aTeamHasChanceToSteal())) || (Game.get(round-1).aTeamHasChanceToSteal() && Game.get(round-1).getCurrentTeam().equals(team1)))
                indicator2.setFill(Color.RED);
            else
                indicator2.setFill(Color.GREEN);
            GridPane.setColumnIndex(indicator2,i);
            team2StrikeIndicators.getChildren().add(indicator2);
        }
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
        team1 = new Team(team1title, team1playerslist);
        team1.setFirstTeam(true);
        team2 = new Team(team2title, team2playerslist);
        team1Name.setText(team1title);
        team2Name.setText(team2title);
    }

    public void switchTeams() {
        if(strikes == 3) {
            Game.get(round - 1).setChanceToSteal(true);
        }
        if(Game.get(round-1).getCurrentTeam().equals(team1)) {
            Game.get(round-1).setCurrentTeam(team2);
        }
        else if(Game.get(round-1).getCurrentTeam().equals(team2)) {
            Game.get(round-1).setCurrentTeam(team1);
        }
        else {
            // This should never happen
            System.err.print("ERROR: "+Game.get(round-1).getCurrentTeam());
            System.err.println(" is the current team. This should not happen.");
        }

        refreshLabels();
    }

    public void setCurrentTeamAndPlayerLabels() {
        currentPlayer.setText(Game.get(round-1).getCurrentPlayer().toString());
        currentTeam.setText(Game.get(round-1).getCurrentTeam().toString());
        if(Game.get(round-1).aTeamHasChanceToSteal())
            currentPlayer.setText("STEALING");
    }

    public void startTiming(MouseEvent mouseEvent) throws FileNotFoundException {
        ((Button)mouseEvent.getSource()).setText("Stop timer");
        if(timerShouldRun == true) {
            ((Button)mouseEvent.getSource()).setText("Start timer");
            timerShouldRun = false;
        }
        else {
            if (Game.get(round-1).getCurrentTeam().equals(team1)) {
                if(Game.get(round-1).aTeamHasChanceToSteal())
                    startTimer(60,Team1Timer);
                else
                    startTimer(20,Team1Timer);
            }
            else if (Game.get(round-1).getCurrentTeam().equals(team2)) {
                if(Game.get(round-1).aTeamHasChanceToSteal())
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

    public void animateFlip(StackPane stack) {
        Duration speed = Duration.millis(100);
        BorderPane back = (BorderPane) stack.getChildren().get(0);
        BorderPane front = (BorderPane) stack.getChildren().get(1);
        back.setScaleY(0);
        ScaleTransition stHideFront = new ScaleTransition(speed, front);
        stHideFront.setFromY(1);
        stHideFront.setToY(0);

        ScaleTransition stShowBack = new ScaleTransition(speed, back);
        stShowBack.setFromY(0);
        stShowBack.setToY(1);

        stHideFront.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stShowBack.play();
                front.toBack();
            }
        });

        stHideFront.play();
    }

    public void strike(MouseEvent e) throws InterruptedException {
        strikeButton.setDisable(true);
        timerShouldRun = false;
        Game.get(round-1).getCurrentPlayer().addStrike();
        Game.get(round-1).getCurrentTeam().rotatePlayers();
        strikes++;
        strikeEffect.play();
        StackPane stack = (StackPane) ansVBox.getParent();
        Label imgLabel = new Label();
        ImageView img = new ImageView(new Image(getClass().getResourceAsStream("images/strike.png")));
        imgLabel.setGraphic(img);
        stack.getChildren().add(imgLabel);
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(1000);
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                stack.getChildren().remove(imgLabel);
                strikeButton.setDisable(false);
                if(Game.get(round-1).aTeamHasChanceToSteal())
                    keepPoints();
                else if(strikes == 3)
                    switchTeams();
                else
                    refreshLabels();
            }
        });
        new Thread(sleeper).start();
    }

    public void endGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("xml/endScreen.fxml"));
        Parent root = (Parent)loader.load();
        Controller_endScreen endgame = (Controller_endScreen)loader.getController();
        endgame.setStageAndSetupListeners(primaryStage);
        endgame.passGame(Game,gameFile);
        primaryStage.setScene(new Scene(root, 720, 640));
        primaryStage.getScene().getStylesheets().add("style.css");
    }

    public void chooseGameFile(int gameFile){
        System.out.println("Game #"+gameFile+" selected.");
        this.gameFile = gameFile;
    }

    public void revealAnswers(MouseEvent e) {
        flipAllAnswers((StackPane) ansVBox.getChildren().get(0), "answerFace");
        revealed = true;
        ((Button) e.getSource()).setDisable(true);
        refreshLabels();
    }

    public void flipAllAnswers(StackPane first) {
        cardEffect.play();
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if(ansVBox.getChildren().indexOf(first) != 0)
                    Thread.sleep(150);
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                animateFlip(first);
                int oldIndex = ansVBox.getChildren().indexOf(first);
                if(ansVBox.getChildren().size() != oldIndex+1)
                    flipAllAnswers((StackPane) ansVBox.getChildren().get(oldIndex+1));
            }
        });
        new Thread(sleeper).start();
    }

    public void flipAllAnswers(StackPane first, String exceptionID) {
        cardEffect.play();
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if(ansVBox.getChildren().indexOf(first) != 0)
                    Thread.sleep(150);
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                int oldIndex = ansVBox.getChildren().indexOf(first);
                if(!first.getChildren().get(0).getId().equals(exceptionID))
                    animateFlip(first);
                //Find the next non-excepted node
                if(ansVBox.getChildren().size() != oldIndex+1)
                {
                    for(int i = oldIndex+1; i < maxNumAnswers; i++) {
                        if(Main.Debugging())
                            System.out.println("Checking node "+i);
                        if(((StackPane) ansVBox.getChildren().get(i)).getChildren().get(0).getId().equals(exceptionID) == false) {
                            if(Main.Debugging())
                                System.out.println(((StackPane) ansVBox.getChildren().get(i)).getChildren().get(0).getId() + " is id of Node "+i+", flipping");
                            flipAllAnswers((StackPane) ansVBox.getChildren().get(i),exceptionID);
                            break;
                        }
                    }
                }
            }
        });
        new Thread(sleeper).start();
    }
}