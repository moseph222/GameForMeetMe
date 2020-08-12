package sample;

import java.util.LinkedList;

public class Round {

    private String question;
    private LinkedList<String> answers;
    private LinkedList<Integer> points;
    private int id;
    private Team currentTeam;
    private Team otherTeam;
    private Team winningTeam;
    private boolean chanceToSteal = false;

    public Round(String question, LinkedList<String> answers, LinkedList<Integer> points, int id){
        this.question = question;
        this.answers = answers;
        this.points = points;
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public LinkedList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(LinkedList<String> answers) {
        this.answers = answers;
    }

    public LinkedList<Integer> getPoints() {
        return points;
    }

    public void setPoints(LinkedList<Integer> points) {
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setCurrentTeam(Team team) {
        if(this.currentTeam != null)
            this.otherTeam = this.currentTeam;
        this.currentTeam = team;
    }

    /**
     * Method to return the team name of whom is currently playing
     *
     * @return name of current team
     * @author Benjamin
     */
    public Team getCurrentTeam() {
        return this.currentTeam;
    }

    public Team getOtherTeam() {
        return this.otherTeam;
    }

    public Player getCurrentPlayer() {
        return this.currentTeam.getPlayer(0);
    }

    public void setChanceToSteal(boolean chance) {
        this.chanceToSteal = chance;
    }

    public boolean hasChanceToSteal() {
        return this.chanceToSteal;
    }

    public void setWinningTeam(Team team) {
        this.winningTeam = team;
    }
}
