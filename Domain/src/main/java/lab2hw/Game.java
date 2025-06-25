package lab2hw;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "games")
public class Game extends lab2hw.Entity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "configuration_id",
            referencedColumnName = "configuration_id",
            nullable = false
    )
    private Configuration configuration;

    @Column(name = "finished", nullable = false)
    private boolean finished;

    @Column(name = "final_score", nullable = false)
    private int finalScore;

    public Game() {}

    public Game(Player player,
                LocalDateTime startTime,
                Configuration configuration,
                boolean finished,
                int finalScore) {
        this.player = player;
        this.startTime = startTime;
        this.configuration = configuration;
        this.finished = finished;
        this.finalScore = finalScore;
    }

    // --- getters & setters ---

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

    public Configuration getConfiguration() {
        return configuration;
    }
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
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
