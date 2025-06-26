package lab2hw.dto;

public class Attempt {
    private int score;
    private boolean ok;
    private boolean finished;

    public Attempt(int score, boolean ok) {
        this.score = score;
        this.ok = ok;
        this.finished = false;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
