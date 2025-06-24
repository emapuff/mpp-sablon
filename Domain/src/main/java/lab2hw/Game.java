package lab2hw;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
//aceasta e clasa cu game session
@jakarta.persistence.Entity
@Table(name = "games")
@Component
public class Game extends lab2hw.Entity<Long> {

    @ManyToOne
    private Player player;

    private LocalDateTime startTime;

    private boolean finished;

    private int finalScore;

    public Game() {}

    public Game(Player player,
                LocalDateTime startTime,
                boolean finished,
                int finalScore) {
        this.player = player;
        this.startTime = startTime;
        this.finished = finished;
        this.finalScore = finalScore;
    }
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }
}
