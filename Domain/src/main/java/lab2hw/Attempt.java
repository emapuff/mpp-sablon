package lab2hw;

import jakarta.persistence.*;
import java.time.LocalDateTime;
// clasa cu incercarile
@jakarta.persistence.Entity
@Table(name = "attempts")
public class Attempt extends lab2hw.Entity<Long> {

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

//    @Embedded
//    private Position position;
    //todo aici mai trebuie adaugat un camp

    private int score;

    private LocalDateTime timestamp;

    public Attempt() {}

    public Attempt(Player player, Game game, int score) {
        this.player = player;
        this.game = game;
        //this.position = position;
        this.score = score;
        this.timestamp = LocalDateTime.now();
    }

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

//    public Position getPosition() {
//        return position;
//    }
//
//    public void setPosition(Position position) {
//        this.position = position;
//    }

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
