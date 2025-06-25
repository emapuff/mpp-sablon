package lab2hw;

import jakarta.persistence.*;
import java.util.List;

@jakarta.persistence.Entity
@Table(name = "configurations")
@Access(AccessType.FIELD)
@AttributeOverride(
        name = "id",
        column = @Column(name = "configuration_id")
)
public class Configuration extends lab2hw.Entity<Long>  {

    /**
     * Stocăm pozițiile capcanelor ca TEXT CSV
     * în coloana `values_str`, convertite în List<Integer>
     */
    @Column(
            name = "values_str",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> values;

    public Configuration() {}

    public Configuration(List<Integer> values) {
        this.values = values;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }
}
