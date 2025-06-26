package lab2hw;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lab2hw.dto.Attempt;
import lab2hw.dto.ClassamentDTO;
import lab2hw.dto.GameWon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000",

        allowCredentials = "true" )
@RestController
@RequestMapping("/game")
public class Controller {

    private List<String> letters_left;
    private Game game;
    private Configuration configuration;
    private final BigController server;
    private final SimpMessagingTemplate messagingTemplate;


    @Autowired
    private SessionData sessionData;

    @Autowired
    public Controller(BigController server,
                      SimpMessagingTemplate messagingTemplate) {
        this.server=server;
        this.messagingTemplate=messagingTemplate;
    }

//    @PostConstruct
//    public void init() {
//        List<Configuration> configurationList = server.getConfigurations();
//        Collections.shuffle(configurationList);
//        configuration = configurationList.get(0);
//        letters_left =configuration.getKeys();
//        game = new Game();
//        game.setTryes(0);
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
            letters_left   = new ArrayList<>(configuration.getKeys());

            game = new Game();
            game.setTryes(0);
            game.setStartTime(LocalDateTime.now());
            game.setPlayer(player);  // acum e activă sesiunea
            game.setIsWon(false);
        }
        return configuration;
    }

    @GetMapping("/classament")
    public List<ClassamentDTO> getClassament() {
        System.out.println("s a apelat get clsamen");
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
    public lab2hw.dto.Attempt saveAttempt(@RequestBody Integer position) {
         String leter_chosen = letters_left.get(0);
         letters_left.remove(0);
         game.setTryes(game.getTryes() + 1);

         String letter_user = configuration.getKeys().get(position);

         if(letter_user.equals(leter_chosen)) { //caz cand sunt egale
             game.setIsWon(true);

         }else if(configuration.getValues().get(letter_user) > configuration.getValues().get(leter_chosen)) {
             game.setFinalScore(game.getFinalScore()+
                     configuration.getValues().get(letter_user)
                     + configuration.getValues().get(leter_chosen));
         } else {
             game.setFinalScore(game.getFinalScore() - configuration.getValues().get(letter_user));
         }

         if(game.getTryes()==4){
             server.saveGame(game);
             broadcastClassament();
         }

         return new Attempt(game.getFinalScore(),leter_chosen);
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
            gameWons.add(new GameWon(game.getFinalScore(),game.getLetters()));
        }

        return gameWons;
    }

}
