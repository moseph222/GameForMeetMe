package sample;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

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
    private int round = 0;
    @FXML
    private Label roundPoints;
    private int roundPointsInteger;
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

    private int maxNumAnswers;
    private int currentAmountAnswers;
    private List<String> team1playerslist;
    private List<String> team2playerslist;
    private String team1title;
    private String team2title;
    private int[] team1NumAttempts = new int[3]; // array of trials per team
    private int[] team2NumAttempts = new int[3];
    private LinkedList<Text> allButtons = new LinkedList<Text>();
    private LinkedList<Text> points = new LinkedList<Text>();
    private LinkedList<Text> answers = new LinkedList<Text>();

    int turn = 0;

    private int gamers = 1;   // players answering are by default team 1
    private int thieves = 2;  // players set to steal are by default team 2
    private boolean setToSteal = false;
    private boolean setToStealAB = false; // Team 1 to Team2
    private boolean setToStealBA = false; // Team 2 to Team1
    private boolean noMorePoints = false;

    private LinkedList<Question> Game = new LinkedList<Question>();


    // Initializing variables, arrays, and setting labels as empty or lower opacity

    public void initialize(){
        round = 1;
        int randomNumber = 1;
        roundPointsInteger = 0;

        // Reading questions from a file into a LinkedList of questions
        try {
            File game = new File("src/sample/Game"+randomNumber+".txt");
            Scanner myReader = new Scanner(game);
            String input;
            String question = null;
            LinkedList<String> answers = new LinkedList<String>();
            LinkedList<Integer> points = new LinkedList<Integer>();
            int id = 1;

            int lineNum = 1;

            while(myReader.hasNext()){
                input = myReader.nextLine();
                if(input.compareTo("NEW QUESTION") != 0){
                    if(lineNum == 1){
                        question = input;
                    } else if(lineNum % 2 == 0){
                        answers.add(input);
                    } else if(lineNum % 2 != 0){
                        points.add(Integer.parseInt(input));
                    }
                    lineNum++;
                } else {
                    lineNum = 1;
                    Game.add(new Question(question,answers,points,id));
                    answers = new LinkedList<String>();
                    points = new LinkedList<Integer>();
                    id++;
                }
            }
        } catch(FileNotFoundException e){
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

        // Set the round label
        gameRound.setText("Round " + String.valueOf(round));

        // Set the question
        question.setText(Game.get(round-1).getQuestion());

        cleanAnswerButtons();

        //Populate with all answers
        populateAnsFromCurQuestion(round);


        // Decide first turn
        turn = (Math.random() <= 0.5) ? 1 : 2;

        if(turn == 1){
            gamers = 1;
            thieves = 2;
            team1Name.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 80, 0.7), new CornerRadii(5.0), new Insets(-2.0))));
        } else if(turn == 2){
            gamers = 2;
            thieves = 1;
            team2Name.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 80, 0.7), new CornerRadii(5.0), new Insets(-2.0))));
        }

        System.out.println("gamers: " + gamers);
        System.out.println("thieves: " + thieves);
        //Gets the answers of the first game
        maxNumAnswers = Game.get(0).getAnswers().size();
        System.out.println("Max number of answer" + maxNumAnswers);
        currentAmountAnswers = 0;



    }




    public static int getRandomInteger(int maximum, int minimum){
        return ((int) (Math.random()*(maximum - minimum))) + minimum;
    }

    public void onButtonClicked(MouseEvent e) throws IOException {
        StackPane stacknum = (StackPane) e.getSource();
        int idnum = Integer.parseInt(stacknum.getId());

        if(idnum == 1){
            button1.setOpacity(100);
            point1.setOpacity(100);
            if(noMorePoints == true){
                roundPointsInteger = 0;
            } else {
                roundPointsInteger += Integer.parseInt(point1.getText());
            }

            currentAmountAnswers++;
        } else if(idnum == 2){
            button2.setOpacity(100);
            point2.setOpacity(100);
            if(noMorePoints == true){
                roundPointsInteger = 0;
            } else {
                roundPointsInteger += Integer.parseInt(point2.getText());
            }

            currentAmountAnswers++;
        } else if (idnum == 3){
            button3.setOpacity(100);
            point3.setOpacity(100);
            if(noMorePoints == true){
                roundPointsInteger = 0;
            } else {
                roundPointsInteger += Integer.parseInt(point3.getText());
            }

            currentAmountAnswers++;
        } else if(idnum ==4){
            button4.setOpacity(100);
            point4.setOpacity(100);
            if(noMorePoints == true){
                roundPointsInteger = 0;
            } else {
                roundPointsInteger += Integer.parseInt(point4.getText());
            }
            currentAmountAnswers++;
        } else if(idnum == 5){
            button5.setOpacity(100);
            point5.setOpacity(100);
            if(noMorePoints == true){
                roundPointsInteger = 0;
            } else {
                roundPointsInteger += Integer.parseInt(point5.getText());
            }
            currentAmountAnswers++;
        } else if(idnum == 6){
            button6.setOpacity(100);
            point6.setOpacity(100);
            if(noMorePoints == true){
                roundPointsInteger = 0;
            } else {
                roundPointsInteger += Integer.parseInt(point6.getText());
            }
            currentAmountAnswers++;
        } else if(idnum == 7){
            button7.setOpacity(100);
            point7.setOpacity(100);
            if(noMorePoints == true){
                roundPointsInteger = 0;
            } else {
                roundPointsInteger += Integer.parseInt(point7.getText());
            }
            currentAmountAnswers++;
        } else if(idnum ==8) {
            button8.setOpacity(100);
            point8.setOpacity(100);
            if(noMorePoints == true){
                roundPointsInteger = 0;
            } else {
                roundPointsInteger += Integer.parseInt(point8.getText());
            }
            currentAmountAnswers++;

        }

        if(!noMorePoints){
            roundPoints.setText(String.valueOf(roundPointsInteger));
        }


        if(setToStealAB == true){
            int currentPoints = Integer.parseInt(team2Points.getText());
            team2Points.setText(String.valueOf(currentPoints + roundPointsInteger));
            // Now you can't earn any more points
            noMorePoints = true;
            // ==============ROUND OVER==================




        } else if(setToStealBA == true){
            int currentPoints = Integer.parseInt(team1Points.getText());
            team1Points.setText(String.valueOf(currentPoints + roundPointsInteger));
            noMorePoints = true;
            //==================== ROUND OVER======================


        } if(currentAmountAnswers == maxNumAnswers){
            if(noMorePoints == false){
                if(gamers == 1){
                    int currentPoints = Integer.parseInt(team1Points.getText());
                    team1Points.setText(String.valueOf(currentPoints + roundPointsInteger));
                } else{
                    int currentPoints = Integer.parseInt(team2Points.getText());
                    team2Points.setText(String.valueOf(currentPoints + roundPointsInteger));
                }

                roundPointsInteger = 0;

            }

            // ======================ROUND OVER==============


        } else {
            if(team1NumAttempts[0] == 1 && team1NumAttempts[1] == 1 && team1NumAttempts[2]==1){
                setToStealAB = true;
            } else if(team2NumAttempts[0] == 1 && team2NumAttempts[1] == 1 && team2NumAttempts[2]==1){
                setToStealBA = true;
            }
        }




//        boolean steal = true;
//        boolean nextMatch = (currentAmountAnswers == maxNumAnswers) || steal;

//        if(nextMatch){
//            round++;
//            gameRound.setText("Round " + String.valueOf(round));
//            // Move to next round
//        }
    }



    public void AttemptHandler(MouseEvent e){

        Label currentLabel = (Label) e.getSource();
        String buttonId = currentLabel.getId();

        if(buttonId.compareTo("team1attemptButton1") == 0){
            Image newimg = null;
            if(team1NumAttempts[0] == 0){
                newimg = new Image(getClass().getResourceAsStream("RedButton.png"));
                team1NumAttempts[0] = 1;
            } else if(team1NumAttempts[0] == 1){
                newimg = new Image(getClass().getResourceAsStream("GreenButton.png"));
                team1NumAttempts[0] = 0;
            }

            team1Attempt1.setImage(newimg);
            team1Attempt1.setFitHeight(10);
            team1Attempt1.setFitWidth(10);
        } else if(buttonId.compareTo("team1attemptButton2")==0){
            Image newimg = null;
            if(team1NumAttempts[1] == 0){
                newimg = new Image(getClass().getResourceAsStream("RedButton.png"));
                team1NumAttempts[1] = 1;
            } else {
                newimg = new Image(getClass().getResourceAsStream("GreenButton.png"));
                team1NumAttempts[1] = 0;
            }
            team1Attempt2.setImage(newimg);
            team1Attempt2.setFitHeight(10);
            team1Attempt2.setFitWidth(10);
        } else if(buttonId.compareTo("team1attemptButton3")==0){
            Image newimg = null;
            if(team1NumAttempts[2] == 0){
                newimg = new Image(getClass().getResourceAsStream("RedButton.png"));
                team1NumAttempts[2] = 1;
            } else {
                newimg = new Image(getClass().getResourceAsStream("GreenButton.png"));
                team1NumAttempts[2] = 0;
            }
            team1Attempt3.setImage(newimg);
            team1Attempt3.setFitHeight(10);
            team1Attempt3.setFitWidth(10);
        } else if(buttonId.compareTo("team2attemptButton1")==0){
            Image newimg = null;
            if(team2NumAttempts[0] == 0){
                newimg = new Image(getClass().getResourceAsStream("RedButton.png"));
                team2NumAttempts[0] = 1;
            } else {
                newimg = new Image(getClass().getResourceAsStream("GreenButton.png"));
                team2NumAttempts[0] = 0;
            }
            team2Attempt1.setImage(newimg);
            team2Attempt1.setFitHeight(10);
            team2Attempt1.setFitWidth(10);
        } else if(buttonId.compareTo("team2attemptButton2")==0){
            Image newimg = null;
            if(team2NumAttempts[1] == 0){
                newimg = new Image(getClass().getResourceAsStream("RedButton.png"));
                team2NumAttempts[1] = 1;
            } else {
                newimg = new Image(getClass().getResourceAsStream("GreenButton.png"));
                team2NumAttempts[1] = 0;
            }
            team2Attempt2.setImage(newimg);
            team2Attempt2.setFitHeight(10);
            team2Attempt2.setFitWidth(10);
        } else if(buttonId.compareTo("team2attemptButton3")==0){
            Image newimg = null;
            if(team2NumAttempts[2] == 0){
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



        //
        switchTeamHighlight();


    }




    public void cleanAnswerButtons(){
        //Clear up all text in case there's an extra answers
        for(int i = 0; i < allButtons.size();i++){
            answers.get(i).setText("");
            allButtons.get(i).setText("");
            points.get(i).setText("");
        }
    }

    public void populateAnsFromCurQuestion(int round){
        for(int i = 0; i < Game.get(round-1).getAnswers().size();i++){
            allButtons.get(i).setText(Game.get(round-1).getAnswers().get(i));
            answers.get(i).setText(Game.get(round-1).getAnswers().get(i));
            points.get(i).setText(String.valueOf(Game.get(round-1).getPoints().get(i)));
        }
    }
    public void zeroOpacityAnsFromCurQuestion(int round){
        for(int i = 0; i < Game.get(round-1).getAnswers().size();i++){
            allButtons.get(i).setOpacity(0);
            points.get(i).setOpacity(0);
        }
    }

    // MAKE ONE MORE BUTTON FOR STEALING FAILED

    public void goToNextQuestion(){
        setToStealAB = false;
        setToStealBA = false;
        System.out.println(Game.size());
        roundPointsInteger = 0;
        noMorePoints = false;
        round++;
        gameRound.setText("Round " + String.valueOf(round));
        swapTeamRoles();
        System.out.println ("NEW ROUND");
        System.out.println("gamers: " + gamers);
        System.out.println("thieves: " + thieves);
        cleanAnswerButtons();
        populateAnsFromCurQuestion(round);
        zeroOpacityAnsFromCurQuestion(round);
        maxNumAnswers = Game.get(round-1).getAnswers().size();
        currentAmountAnswers=0;
        resetAttempts();
        roundPoints.setText("000");

        if(gamers == 1){
            team2Name.setBackground(Background.EMPTY);
            team1Name.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 80, 0.7), new CornerRadii(5.0), new Insets(-2.0))));
        } else if(thieves == 1){
            team1Name.setBackground(Background.EMPTY);
            team2Name.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 80, 0.7), new CornerRadii(5.0), new Insets(-2.0))));
        }

    }

    public void keepPoints(){
        if(setToStealAB){
            int currentPoints = Integer.parseInt(team1Points.getText());
            team1Points.setText(String.valueOf(currentPoints + roundPointsInteger));
        } else if(setToStealBA){
            int currentPoints = Integer.parseInt(team2Points.getText());
            team2Points.setText(String.valueOf(currentPoints + roundPointsInteger));
        }
    }

    public void resetAttempts(){
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


    public void switchTeamHighlight(){
        if(team1NumAttempts[0] == 1 && team1NumAttempts[1] == 1 && team1NumAttempts[2]==1){
            team1Name.setBackground(Background.EMPTY);
            team2Name.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 80, 0.7), new CornerRadii(5.0), new Insets(-2.0))));
            setToStealAB = true;
        }

        if(team2NumAttempts[0] == 1 && team2NumAttempts[1] == 1 && team2NumAttempts[2]==1){
            team2Name.setBackground(Background.EMPTY);
            team1Name.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 80, 0.7), new CornerRadii(5.0), new Insets(-2.0))));
            setToStealBA = true;
        }
    }



    public void setStageAndSetupListeners(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    /**
     * Set team names and player lists.
     *
     * @author Benjamin
     * @param team1title Team 1's name
     * @param team2title Team 2's name
     * @param team1playerslist Team 1's list of players
     * @param team2playerslist Team 2's list of players
     */
    public void setTeams(String team1title, String team2title, List<String> team1playerslist, List<String> team2playerslist) {
        this.team1title = team1title;
        this.team2title = team2title;
        this.team1playerslist = team1playerslist;
        this.team2playerslist = team2playerslist;
        team1Name.setText(team1title);
        team2Name.setText(team2title);;

        setCurrentTeamAndPlayer();
    }

    /**
     * Swap team roles.
     * Gamers will become thieves and
     * thieves will become gamers.
     *
     * Gamers represent the team answering,
     * thieves represent the team that may be
     * given a chance to steal.
     *
     * @author Benjamin
     */
    public void swapTeamRoles(){
        if(this.gamers == 1){
            this.gamers = 2;
            this.thieves = 1;
        }
        else if(this.gamers == 2){
            this.gamers = 1;
            this.thieves = 2;
        }
    }

    /**
     * Method to return the team digit of whom is currently playing
     * (either team 1 or team 2)
     *
     * @author Benjamin
     * @return number of current team
     */
    public int getCurrentTeamNum() {
        if(setToSteal)
            return thieves;
        else
            return gamers;
    }

    /**
     * Method to return the team name of whom is currently playing
     *
     * @author Benjamin
     * @return name of current team
     */
    public String getCurrentTeam() {
        if(setToSteal) {
            if(thieves == 1)
                return team1title;
            else
                return team2title;
        }
        else {
            if(gamers == 1)
                return team1title;
            else
                return team2title;
        }
    }

    /**
     * Method to return the name of the current player.
     * If team 1 is playing, pulls from team 1's player list.
     * Else, pulls from team 2's.
     * Before a player name is returned, the respective
     * player list is rotated by 1.
     *
     * @author Benjamin
     * @return name of current player
     */
    public String getCurrentPlayer() {
        if(getCurrentTeamNum() == 1) {
            Collections.rotate(team1playerslist,1);
            return this.team1playerslist.get(0);
        }
        else {
            Collections.rotate(team2playerslist,1);
            return this.team2playerslist.get(0);
        }
    }

    public void setCurrentTeamAndPlayer() {
        currentPlayer.setText(this.getCurrentPlayer());
        currentTeam.setText(this.getCurrentTeam());
    }
}
