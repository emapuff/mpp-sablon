package lab2hw;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")

@RestController
@RequestMapping("/admin")
public class ModifyConfController {

    @Autowired
    private BigController controller;

    @GetMapping
    public List<Configuration> getAll() {
        System.out.println(controller.getConfigurations().size());
        return controller.getConfigurations();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Configuration> update(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> newValues) {
        System.out.println(newValues);
        System.out.println(id);
        Configuration conf = controller.findConfigurationById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Config not found: " + id
                        )
                );

        // aplici noul map direct
        conf.setValues(newValues);
        Configuration updated = controller.modifyConfiguration(conf);
        return ResponseEntity.ok(updated);
    }

}
