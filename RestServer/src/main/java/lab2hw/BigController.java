package lab2hw;

import lab2hw.repoimpl.AttemptRepoImp;
import lab2hw.repoimpl.GameRepoImp;
import lab2hw.repoimpl.PlayerRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class BigController {
    private final GameRepoImp gameRepoImp;
    private final AttemptRepoImp attemptRepoImp;
    private final PlayerRepository playerRepo;

    public BigController(GameRepoImp gameRepoImp,
                         AttemptRepoImp attemptRepoImp,
                         PlayerRepository playerRepo) {
        this.gameRepoImp = gameRepoImp;
        this.attemptRepoImp = attemptRepoImp;
        this.playerRepo = playerRepo;
    }

    public List<Game> getGames(){
        return gameRepoImp.findAll();
    }

    public Game saveGame(Game game){
        return gameRepoImp.save(game);
    }

    public Game modifyGame(Game game){
        return gameRepoImp.save(game);
    }

    public List<Attempt> getAttempts(){
        return attemptRepoImp.findAll();
    }

    public Attempt saveAttempt(Attempt attempt){
        return attemptRepoImp.save(attempt);
    }

    public List<Attempt> findAttemptsByPlayer(Player player){
        return attemptRepoImp.findByPlayerId(player.id);
    }

    public List<Player> getPlayers(){
        return playerRepo.findAll();
    }

    public Player savePlayer(Player player){
        return playerRepo.save(player);
    }

    public Optional<Player> findPlayerByNickname(String nickname){
        return playerRepo.findPlayerByNickname(nickname);
    }

    public Optional<Player> findPlayerById(Long id){
        return playerRepo.findPlayerById(id);
    }

    public Game getOrCreateGameForPlayer(Player player) {
        return gameRepoImp.findAll().stream()
                .filter(g -> g.getPlayer().equals(player) && !g.isFinished())
                .findFirst()
                .orElseGet(() -> {
                    Game newGame = new Game(player, LocalDateTime.now(), false, 0);
                    return gameRepoImp.save(newGame);
                });
    }
}
