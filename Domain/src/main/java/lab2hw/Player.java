package lab2hw;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "players")
@Component
public class Player extends lab2hw.Entity<Long> {

    @Column(name="nickname")
    private String nickname;




    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Player player = (Player) o;
        return  Objects.equals(nickname, player.nickname) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),  nickname);
    }

    @Override
    public String toString() {
        return "Player{" +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
