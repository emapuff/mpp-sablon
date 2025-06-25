package lab2hw;

import lab2hw.repoimpl.AttemptRepoImp;
import lab2hw.repoimpl.GameRepoImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/game")
public class Controller {
    //TODO controller game

    private final BigController server;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public Controller(
                      BigController server
                     ) {
        this.server=server;
    }

    @GetMapping(params = "!nickname")
    public List<Game> getClassament() {
        return server
                .getGames()
                .stream()
                .sorted(Comparator.comparing(Game::getFinalScore).reversed())
                .collect(Collectors.toList());
    }

    @PostMapping("/attempt")
    public AttemptDTO saveAttempt(
            @RequestParam String nickname,
            @RequestParam String position,
            @RequestParam Long index) {

        Player player = server.findPlayerByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("Player not found: " + nickname));

        Configuration conf = server.findConfigurationById(index)
                .orElseThrow(() -> new RuntimeException("Configuration not found: " + index));

        Game game = server.getOrCreateGameForPlayer(player,conf);

        int pos;
        try {
            pos = Integer.parseInt(position);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }


        boolean isTrap = conf.getValues().contains(pos);


        List<Attempt> previous = server.findAttemptsByGame(game);
        int attemptCount = previous.size() + 1;

        if (isTrap) {
            game.setFinished(true);
            server.modifyGame(game, /* endWithTrap= */ true);
        } else {
            int scoreDelta = attemptCount * 2;
            game.setFinalScore(game.getFinalScore() + scoreDelta);
            if (attemptCount >= 5) {
                game.setFinished(true);
                server.modifyGame(game, /* endWithTrap= */ true);
            } else {
                server.modifyGame(game, /* endWithTrap= */ false);
            }
        }

        // 7. Salvăm încercarea
        Attempt attempt = new Attempt(player, game, game.getFinalScore());
        server.saveAttempt(attempt);

        AttemptDTO dto = new AttemptDTO(
                game.getId(),
                player.getNickname(),
                LocalDateTime.now(),
                game.getFinalScore(),
                isTrap,
                game.isFinished()
        );

        List<Game> updatedClassament = getClassament();
        messagingTemplate.convertAndSend(
                "/topic/classament"+player.getNickname(),
                updatedClassament
        );

        // 8. Returnăm DTO-ul
        return dto;
    }

    @PutMapping
    public Game updateGame(@RequestParam String nickname, @RequestParam Long index) {
        Player player = server.findPlayerByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("Player not found: " + nickname));

        Configuration conf = server.findConfigurationById(index)
                .orElseThrow(() -> new RuntimeException("Configuration not found: " + index));

        Game game = server.getOrCreateGameForPlayer(player,conf);
        game.setFinished(true);
        return server.modifyGame(game,true);
    }

    @GetMapping(params = "nickname")
    public List<Game> getGames(@RequestParam String nickname) {
        Player player = server.findPlayerByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("Player not found: " + nickname));

        List<Game> result = server.findGamesByPlayer(player);
        return result;
    }


}
