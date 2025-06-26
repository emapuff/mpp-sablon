package lab2hw;

import lab2hw.repoimpl.ConfigGameImp;
import lab2hw.repoimpl.GameRepoImp;
import lab2hw.repoimpl.PlayerRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class BigController {
    private final GameRepoImp gameRepoImp;
    private final PlayerRepository playerRepo;
    private final ConfigGameImp configuration;

    public BigController(GameRepoImp gameRepoImp,
                         PlayerRepository playerRepo,
                         ConfigGameImp configuration) {
        this.gameRepoImp = gameRepoImp;
        this.playerRepo = playerRepo;
        this.configuration = configuration;
    }

    public List<Game> getGames(){
        return gameRepoImp.findAll();
    }

    public Game saveGame(Game game){
        return gameRepoImp.save(game);
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

    public List<Configuration> getConfigurations(){
        return this.configuration.findAll();
    }

    public Configuration saveConfiguration(Configuration configuration){
        return this.configuration.save(configuration);
    }

    public Optional<Configuration> findConfigurationById(Long id){
        return configuration.getConfigurationById(id);
    }

    public Configuration modifyConfiguration(Configuration configuratio ){
        /*
        aici primeste configuratia NOUA - modificata
         */
        return this.configuration.updateConfiguration(configuratio);
    }

    public List<Game> findGamesByPlayer(Player player){
        return gameRepoImp.findGamesByPlayer(player);
    }
}
