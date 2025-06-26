package lab2hw.dto;

public class GameWon {
    private int score;
    private String letters;

    public GameWon() {}
    public GameWon(int score, String letters) {
        this.score = score;
        this.letters = letters;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }
}
