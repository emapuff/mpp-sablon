package lab2hw;

public interface GameRepo extends Repository<Long,Game> {
      Game modifyFinal(Game game);
}
