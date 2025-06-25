package lab2hw;

import java.util.List;

public interface GameRepo extends Repository<Long,Game> {
      Game modifyFinal(Game game,Boolean t);

      List<Game> findGamesByPlayer(Player p );
}
