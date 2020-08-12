package sample;

import java.util.List;

public class Player {

    private String name;
    private int points = 0;
    private int strikes = 0;
    private int bestRound = 0;
    private int correctAns = 0;
    private boolean isMVP = false;
    private List<Double> times;

    public Player(String name) {
        this.name = name;
    }

    public void addPoints(int points) {
        this.points += points;
        this.addAnswer();
    }

    public void addStrike() {
        this.strikes ++;
    }

    public void setBestRound(int round) {
        this.bestRound = round;
    }

    public void addAnswer() {
        this.correctAns ++;
    }

    public void setMVPStatus(boolean status) {
        this.isMVP = status;
    }

    public void addTime(double time) {
        this.times.add(time);
    }

    // Use when a steal happens
    public void rollBackStats(int points, int ans) {
        this.points -= points;
        this.correctAns -= ans;
        for(int i = 0; i < ans; i++) {
            if(times.get(i) != null)
                times.remove(i);
        }
    }

    public int getTotalPoints() {
        return this.points;
    }

    public int getTotalStrikes() {
        return this.strikes;
    }

    public int getBestRound() {
        return this.bestRound;
    }

    public int getTotalAnswers() {
        return this.correctAns;
    }

    public boolean isMVP() {
        return this.isMVP;
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

        return this.getTotalAnswers()*ansMult + this.getTotalPoints()/pntsDiv;
    }

    public String getStats() {
        String output = "";
        output += "Player "+this.name+" stats:\n";
        output += "\t"+this.getTotalPoints()+" points\n";
        output += "\t"+this.getTotalStrikes()+" strikes\n";
        output += "\t"+this.getTotalAnswers()+" answers\n";
        if(times != null) {
            output += "\tAvg Time: " + this.getAvgTime() + "\n";
            output += "\tMed. Time: " + this.getMedianTime() + "\n";
        }
        if(bestRound > 0)
            output += "\tBest Round: "+this.getBestRound()+"\n";
        if(this.isMVP)
            output += "\t Player is MVP!\n";
        return output;
    }

    public String toString() {
        return this.name;
    }
}