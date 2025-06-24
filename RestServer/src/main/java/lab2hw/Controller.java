package lab2hw;

import lab2hw.repoimpl.AttemptRepoImp;
import lab2hw.repoimpl.GameRepoImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/game")
public class Controller {
    //TODO controller game

    private final BigController server;

    @Autowired
    public Controller(
                      BigController server
                     ) {
        this.server=server;
    }

    @GetMapping
    public List<Game> getClassament() {
        return server
                .getGames()
                .stream()
                .sorted(Comparator.comparing(Game::getFinalScore).reversed())
                .collect(Collectors.toList());
    }

    @PostMapping
    public Attempt saveAttempt(@RequestParam String nickname, @RequestBody String position) {
        Player player = server.findPlayerByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("Player not found: " + nickname));

        Game game = server.getOrCreateGameForPlayer(player);

        Attempt attempt = new Attempt();
        attempt.setGame(game);
        attempt.setPlayer(player);
        attempt.setTimestamp(LocalDateTime.now());
        //TODO aici adaugam logica

        return server.saveAttempt(attempt);
    }

    @PutMapping
    public Game updateGame(@RequestParam String nickname) {
        Player player = server.findPlayerByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("Player not found: " + nickname));

        Game game = server.getOrCreateGameForPlayer(player);
        game.setFinished(true);
        return server.modifyGame(game);
    }


}
