package lab2hw.dto;

public class Attempt {
    private int score;
    private String letter;

    public Attempt(int score, String letter) {
        this.score = score;
        this.letter = letter;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
