package lab2hw;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@jakarta.persistence.Entity
@Table(name = "configurations")
@Access(AccessType.FIELD)
@AttributeOverride(
        name = "id",
        column = @Column(name = "configuration_id")
)
public class Configuration extends lab2hw.Entity<Long> {
    /**
     * Stocăm pozițiile capcanelor ca TEXT CSV
     * în coloana `values_str`, convertite în List<Integer>
     */
    @Column(
            name = "values_str",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @Convert(converter = StringIntegerMapConverter.class)
    private Map<String, Integer> values;

    public Configuration() {}

    public Configuration(Map<String, Integer> values) {
        this.values = values;
    }

    public Map<String, Integer>  getValues() {
        return values;
    }

    public void setValues(Map<String, Integer> values) {
        this.values = values;
    }

    public List<String> getKeys(){
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }
}
