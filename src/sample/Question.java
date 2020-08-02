package sample;

public class Question {
    private String question;
    private String[] answers;
    private int[] points;
    private int id;

    public Question(String question, String[] answers)
    {
        this.question = question;
        this.answers = answers;
    }

    public String[] getAnswers() {
        return answers;
    }

    public String getAnswer(int index) {
        return answers[index];
    }

    public String getQuestion() {
        return question;
    }

//    public void setQuestion(String q) {
//        this.question = q;
//    }
}
