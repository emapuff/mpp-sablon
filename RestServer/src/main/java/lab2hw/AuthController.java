package lab2hw;

import jakarta.servlet.http.HttpSession;
import lab2hw.utils.HibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import lab2hw.repoimpl.PlayerRepository;

@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private SessionData sessionData;

    @Autowired
    private PlayerRepository playerRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String,String> body,
            HttpSession session) {

        String nickname = body.get("nickname");
        Optional<Player> opt = playerRepo.findPlayerByNickname(nickname);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        session.setAttribute("player", opt.get());
        return ResponseEntity.ok().build();
    }
}
