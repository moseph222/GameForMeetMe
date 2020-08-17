package familyfeud;

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
    private boolean isFirstTeam = false;

    public Team(String name, List<Player> players) {
        this.name = name;
        this.players = players;
    }

    public void setWinner() {
        isWinner = true;
    }

    public void setFirstTeam(boolean isFirstTeam) {
        this.isFirstTeam = isFirstTeam;
    }

    public void addPoints(int totalPoints) {
        this.totalPoints += totalPoints;
        increaseRoundsWon();
        submitTeamTempPoints();
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void addStolenPoints(int stolenPoints) {
        stolenRounds ++;
        this.stolenPoints += stolenPoints;
        addPoints(stolenPoints);
    }

    public void increaseRoundsWon() {
        roundsWon ++;
    }

    public void increaseSlamDunks() {
        slamDunks ++;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int index) {
        return getPlayers().get(index);
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getStolenRounds() {
        return stolenRounds;
    }

    public int getStolenPoints() {
        return stolenPoints;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public boolean isFirstTeam() {
        return isFirstTeam;
    }

    public String calculateMVP() {
        int MvpIndex = 0;
        double mostMvpPoints = getPlayer(0).getMvpPoints();
        for(int i = 0; i < getPlayers().size(); i++) {
            if (getPlayer(i).getMvpPoints() > getPlayer(MvpIndex).getMvpPoints()) {
                MvpIndex = i;
                mostMvpPoints = getPlayer(i).getMvpPoints();
            }
        }

        if(mostMvpPoints == 0)
            return "No MVP";
        else {
            getPlayer(MvpIndex).setMVPStatus(true);
            return getPlayer(MvpIndex).toString();
        }
    }

    public void clearTeamTempPoints() {
        for(int i = 0; i < players.size(); i++) {
            players.get(i).clearTempPoints();
        }
    }

    public void submitTeamTempPoints() {
        for(int i = 0; i < players.size(); i++) {
            players.get(i).submitTempPoints();
        }
    }

    public void rotatePlayers() {
        Collections.rotate(players,1);
    }

    public String getStats() {
        String output = "";
        output += toString()+":\n";
        output += "\t"+getPlayers()+"\n";
        output += "\tMVP: "+calculateMVP()+"\n";
        output += "\t"+getTotalPoints()+" total points\n";
        output += "\t"+getStolenPoints()+" points stolen from ";
        output += getStolenRounds()+" rounds\n";
        output += "\t"+roundsWon+" rounds won\n";
        output += "\t"+slamDunks+" slam dunks\n";
        if(isWinner())
            output += "\t"+toString()+" win!";
        return output;
    }

    public String toString() {
        return name;
    }

}