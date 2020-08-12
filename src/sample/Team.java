package sample;

import java.util.Collections;
import java.util.List;

public class Team {

    private String name;
    private List<Player> players;
    private int totalPoints = 0;
    private int stolenRounds = 0;
    private int stolenPoints = 0;
    private int roundsWon = 0;
    private int slamDunks = 0;
    private boolean isWinner = false;

    public Team(String name, List<Player> players) {
        this.name = name;
        this.players = players;
    }

    public void setWinner() {
        this.isWinner = true;
    }

    public void addPoints(int points) {
        this.totalPoints += points;
        this.increaseRoundsWon();
    }

    public void setTotalPoints(int points) {
        this.totalPoints = points;
    }

    public void addStolenPoints(int points) {
        this.stolenRounds ++;
        this.stolenPoints += points;
        this.addPoints(points);
    }

    public void increaseRoundsWon() {
        this.roundsWon ++;
    }

    public void increaseSlamDunks() {
        this.slamDunks ++;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int index) {
        return getPlayers().get(index);
    }

    public int getTotalPoints() {
        return this.totalPoints;
    }

    public int getStolenRounds() {
        return this.stolenRounds;
    }

    public int getStolenPoints() {
        return this.stolenPoints;
    }

    public boolean isWinner() {
        return this.isWinner;
    }

    public String calculateMVP() {
        int MvpIndex = 0;
        double mostMvpPoints = this.getPlayer(0).getMvpPoints();
        for(int i = 0; i < this.getPlayers().size(); i++) {
            if (this.getPlayer(i).getMvpPoints() > this.getPlayer(MvpIndex).getMvpPoints()) {
                MvpIndex = i;
                mostMvpPoints = this.getPlayer(i).getMvpPoints();
            }
        }

        if(mostMvpPoints == 0)
            return "No MVP";
        else {
            this.getPlayer(MvpIndex).setMVPStatus(true);
            return this.getPlayer(MvpIndex).toString();
        }
    }

    public void rotatePlayers() {
        Collections.rotate(this.players,1);
    }

    public String getStats() {
        String output = "";
        output += this.toString()+":\n";
        output += "\t"+this.getPlayers()+"\n";
        output += "\tMVP: "+this.calculateMVP()+"\n";
        output += "\t"+this.getTotalPoints()+" total points\n";
        output += "\t"+this.getStolenPoints()+" points stolen from ";
        output += this.getStolenRounds()+" rounds\n";
        output += "\t"+this.roundsWon+" rounds won\n";
        output += "\t"+this.slamDunks+" slam dunks\n";
        if(this.isWinner())
            output += "\t"+this.toString()+" win!";
        return output;
    }

    public String toString() {
        return this.name;
    }

}