package lab2hw;

import lab2hw.utils.HibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import lab2hw.repoimpl.PlayerRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PlayerRepository playerRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        System.out.println("am intrat in controller");
        String nickname = body.get("nickname");
        System.out.println("Nickname primit: " + nickname);
        Optional<Player> player = playerRepo.findPlayerByNickname(nickname);
        if (player.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not found");
        }
        return ResponseEntity.ok().build();
    }
}
