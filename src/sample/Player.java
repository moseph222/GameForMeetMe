package sample;

import java.util.List;

public class Player {

    private String name;
    private int points = 0;
    private int strikes = 0;
    private int bestRound = 0;
    private int correctAns = 0;
    private int tempPoints = 0;
    private boolean isMVP = false;
    private List<Double> times;

    public Player(String name) {
        this.name = name;
    }

    public void addPoints(int points) {
        this.points += points;
        addAnswer();
    }

    public void addStrike() {
        strikes++;
    }

    public void setBestRound(int bestRound) {
        this.bestRound = bestRound;
    }

    public void addAnswer() {
        correctAns++;
    }

    public void setMVPStatus(boolean status) {
        isMVP = status;
    }

    public void addTime(double time) {
        times.add(time);
    }

    public void storeTempPoints(int tempPoints) {
        this.tempPoints += tempPoints;
    }

    public void submitTempPoints() {
        addPoints(tempPoints);
        clearTempPoints();
    }

    public void clearTempPoints() {
        tempPoints = 0;
    }

    public int getTotalPoints() {
        return points;
    }

    public int getTotalStrikes() {
        return strikes;
    }

    public int getBestRound() {
        return bestRound;
    }

    public int getTotalAnswers() {
        return correctAns;
    }

    public boolean isMVP() {
        return isMVP;
    }

    public double getAvgTime() {
        double totalTime = 0;
        for(int i = 0; i < times.size(); i++)
            totalTime += times.get(i);
        return totalTime/times.size();
    }

    public double getMedianTime() {
        if(times.size() % 2 == 0) //even
            return times.get(times.size()/2);
        else //odd
            return times.get((times.size()+1)/2);
    }

    public double getMvpPoints() {
        double ansMult = 0.5; // every 2 correct answers rewards a point
        double pntsDiv = 10; // every 10 points rewards a point

        return getTotalAnswers()*ansMult + getTotalPoints()/pntsDiv;
    }

    public String getStats() {
        String output = "";
        output += "Player "+name+" stats:\n";
        output += "\t"+getTotalPoints()+" points\n";
        output += "\t"+getTotalStrikes()+" strikes\n";
        output += "\t"+getTotalAnswers()+" answers\n";
        if(times != null) {
            output += "\tAvg Time: " + getAvgTime() + "\n";
            output += "\tMed. Time: " + getMedianTime() + "\n";
        }
        if(bestRound > 0)
            output += "\tBest Round: "+getBestRound()+"\n";
        if(isMVP)
            output += "\t Player is MVP!\n";
        return output;
    }

    public String toString() {
        return name;
    }
}