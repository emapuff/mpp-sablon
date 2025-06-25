package lab2hw;

import lab2hw.utils.HibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import lab2hw.repoimpl.PlayerRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private BigController controller;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        System.out.println("am intrat in controller");
        String nickname = body.get("nickname");
        System.out.println("Nickname primit: " + nickname);
        Optional<Player> player = controller.findPlayerByNickname(nickname);
        if (player.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not found");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Configuration>> getAll() {
        List<Configuration> configs = controller.getConfigurations();
        return ResponseEntity.ok(configs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Configuration> update(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        Configuration conf = controller.findConfigurationById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Config not found: " + id)
                );

        String csv = body.get("values");
        if (csv == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing values");
        }
        List<Integer> list = Arrays.stream(csv.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        conf.setValues(list);
        Configuration updated = controller.modifyConfiguration(conf);
        return ResponseEntity.ok(updated);
    }
}
