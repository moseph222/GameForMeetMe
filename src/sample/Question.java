package sample;

import java.util.LinkedList;

public class Question {

    private String question;
    private LinkedList<String> answers;
    private LinkedList<Integer> points;
    private int id;

    public Question(String question,LinkedList<String> answers, LinkedList<Integer> points, int id){
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
}


