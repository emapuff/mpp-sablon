package lab2hw;

import lab2hw.Player;

import java.util.Optional;

public interface PlayerInterface extends Repository<Long,Player>{
    Optional<Player> findPlayerByNickname(String nickname);

    Optional<Player> findPlayerById(Long id );
}
