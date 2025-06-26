package lab2hw.dto;

public class GameWon {
    private int score;
    private int wins;

    public GameWon() {}

    public GameWon(int score, int wins) {
        this.score = score;
        this.wins = wins;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}
