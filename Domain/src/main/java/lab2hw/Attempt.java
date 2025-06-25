package lab2hw;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "attempts")
public class Attempt extends lab2hw.Entity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "attempt_time", nullable = false)
    private LocalDateTime timestamp;

    public Attempt() {}

    public Attempt(Player player, Game game, int score) {
        this.player = player;
        this.game = game;
        this.score = score;
        // timestamp va fi setat automat în @PrePersist
    }

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

    // --- getters & setters ---

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Attempt{" +
                "player=" + player +
                ", game=" + game +
                ", score=" + score +
                ", timestamp=" + timestamp +
                '}';
    }
}
