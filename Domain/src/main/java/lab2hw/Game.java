package lab2hw;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
//aceasta e clasa cu game session
@jakarta.persistence.Entity
@Table(name = "games")
@Component
public class Game extends lab2hw.Entity<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="player_id", nullable=false)
    private Player player;

    private LocalDateTime startTime;

    private boolean isWon;

    private int finalScore;

    private int tryes;

    private int wins;

    public Game() {}

    public Game(Player player,
                LocalDateTime startTime,
                boolean finished,
                int finalScore,
                int tryes, int wins) {
        this.player = player;
        this.startTime = startTime;
        this.isWon = finished;
        this.finalScore = finalScore;
        this.tryes=tryes;
        this.wins=wins;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
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

    public boolean isIsWon() {
        return isWon;
    }

    public void setIsWon(boolean finished) {
        this.isWon = finished;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public boolean isWon() {
        return isWon;
    }

    public void setWon(boolean won) {
        isWon = won;
    }

    public int getTryes() {
        return tryes;
    }

    public void setTryes(int tryes) {
        this.tryes = tryes;
    }


}
