package lab2hw.dto;

import java.time.LocalDateTime;

public class ClassamentDTO {
    private String nume_player;
    private int scor;
    private LocalDateTime when;
    public ClassamentDTO() {}
    public ClassamentDTO(String nume_player, int scor, LocalDateTime when) {
        this.nume_player = nume_player;
        this.scor = scor;
        this.when = when;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }

    public int getScor() {
        return scor;
    }

    public void setScor(int scor) {
        this.scor = scor;
    }

    public String getNume_player() {
        return nume_player;
    }

    public void setNume_player(String nume_player) {
        this.nume_player = nume_player;
    }
}
