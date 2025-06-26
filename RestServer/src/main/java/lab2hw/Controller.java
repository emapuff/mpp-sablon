package lab2hw;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lab2hw.dto.Attempt;
import lab2hw.dto.AttemptRequest;
import lab2hw.dto.ClassamentDTO;
import lab2hw.dto.GameWon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000",

        allowCredentials = "true" )
@RestController
@RequestMapping("/game")
public class Controller {

    private List<String> animals_left;
    private Game game;
    private Configuration configuration;
    private final BigController server;
    private final SimpMessagingTemplate messagingTemplate;


//    @Autowired
//    private SessionData sessionData;

    @Autowired
    public Controller(BigController server,
                      SimpMessagingTemplate messagingTemplate) {
        this.server=server;
        this.messagingTemplate=messagingTemplate;
    }

//    @PostConstruct NU AM NEVOIE DELETE WHEN RAFINATING
//    public void init() {
//        List<Configuration> configurationList = server.getConfigurations();
//        Collections.shuffle(configurationList);
//        configuration = configurationList.get(0);
//        animals_left = configuration.getValues(); //aici salvam ce animale mai avem negasite
//        game = new Game();
//        game.setTryes(0);
//        game.setWins(0);
//        game.setStartTime(LocalDateTime.now());
//        game.setPlayer(sessionData.getPlayer());
//        game.setIsWon(false);
//    }

    @GetMapping
    public Configuration getConfiguration(HttpSession session) {
        Player player = (Player) session.getAttribute("player");
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        if (game == null) {
            List<Configuration> configs = server.getConfigurations();
            Collections.shuffle(configs);
            configuration = configs.get(0);
            animals_left = configuration.getValues(); //nu are dubluri
            configuration.makePairs();
            game = new Game();
            game.setTryes(0);
            game.setWins(0);
            game.setFinalScore(0);
            game.setStartTime(LocalDateTime.now());
            game.setPlayer(player);  // acum e activă sesiunea
            game.setIsWon(false);
        }
        return configuration;
    }

    @GetMapping("/classament")
    public List<ClassamentDTO> getClassament() {
        System.out.println("s-a apelat get clsamen");
        return server.getGames().stream()
                .filter(g -> g.getPlayer() != null)
                .sorted(Comparator.comparing(Game::getFinalScore).reversed())
                .map(g -> new ClassamentDTO(
                        g.getPlayer().getNickname(),
                        g.getFinalScore(),
                        g.getStartTime()))
                .collect(Collectors.toList());
    }


    @PostMapping
    public Attempt saveAttempt(@RequestBody AttemptRequest request, HttpSession session) {
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not initialized");
        }
        game.setTryes(game.getTryes() + 1);
        boolean match = false;
        int idx1 = request.getPositionOne();
        int idx2 = request.getPositionTwo();

        List<String> values = configuration.getValues();
        if (idx1 < 0 || idx2 < 0 || idx1 >= values.size() || idx2 >= values.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid positions");
        }

        if (values.get(idx1).equals(values.get(idx2))) {
            match = true;
            game.setWins(game.getWins() + 1);
            game.setFinalScore(game.getFinalScore() - 1);
            // remove matched item once (both pairs)
            animals_left.remove(values.get(idx1));

            if (game.getWins()==3) {
                game.setIsWon(true);
                server.saveGame(game);
                broadcastClassament();
            } else {
                server.updateGame(game);
                broadcastClassament();
            }
        } else {
            game.setFinalScore(game.getFinalScore() + 2);
        }

        Attempt attempt = new Attempt(game.getFinalScore(), match);
        // finish if max tries or all pairs found
        if (game.getTryes() ==6 || animals_left.isEmpty()) {
            attempt.setFinished(true);
        }
        return attempt;
    }

    private void broadcastClassament() {
        List<ClassamentDTO> dto = getClassament();
        messagingTemplate
                .convertAndSend("/topic/classament", dto);
    }

    @GetMapping("/won")
    public List<GameWon> getGamesWhereTrue() {
        List<Game> games = server.getGames().stream()
                .filter(Game::isIsWon)
                .toList();

        List<GameWon> gameWons = new ArrayList<>();
        for (Game game : games) {
            if(game.getWins()>=2){
                gameWons.add(new GameWon(game.getFinalScore(),game.getWins()));
            }
        }
        if(gameWons.isEmpty()) return List.of();
        return gameWons;
    }

    @GetMapping("/{id}")
    public List<GameWon> getByID(@PathVariable String id) {
        List<Game> games = server.getGames().stream()
                .filter(Game::isIsWon)
                .toList();

        List<GameWon> gameWons = new ArrayList<>();
        for (Game game : games) {
            if(game.getWins()>=2){
                gameWons.add(new GameWon(game.getFinalScore(),game.getWins()));
            }
        }
        if(gameWons.isEmpty()) return List.of();
        return gameWons;
    }

}
