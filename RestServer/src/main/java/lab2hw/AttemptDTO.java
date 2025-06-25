package lab2hw;


import java.time.LocalDateTime;

public class AttemptDTO {
    private Long gameId;
    private String playerNickname;
    private LocalDateTime timestamp;
    private int score;
    private boolean wasTrap;
    private boolean gameFinished;

    public AttemptDTO(Long gameId, String playerNickname, LocalDateTime timestamp,
                      int score, boolean wasTrap, boolean gameFinished) {
        this.gameId = gameId;
        this.playerNickname = playerNickname;
        this.timestamp = timestamp;
        this.score = score;
        this.wasTrap = wasTrap;
        this.gameFinished = gameFinished;
    }

    // Getters și Setters
    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public String getPlayerNickname() { return playerNickname; }
    public void setPlayerNickname(String playerNickname) { this.playerNickname = playerNickname; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public boolean isWasTrap() { return wasTrap; }
    public void setWasTrap(boolean wasTrap) { this.wasTrap = wasTrap; }

    public boolean isGameFinished() { return gameFinished; }
    public void setGameFinished(boolean gameFinished) { this.gameFinished = gameFinished; }
}
