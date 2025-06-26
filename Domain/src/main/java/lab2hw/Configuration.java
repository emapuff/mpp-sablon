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
     * în coloana `values_str`, convertite în List<String>
     */
    @ElementCollection
    @Column(
            name = "values_str",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @Convert(converter = IntegerListConverter.class)
    private List<String> values;

    public Configuration() {}

    public Configuration(List<String> values) {
        this.values = values;
    }

    public List<String>  getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void makePairs(){
        values.addAll(values);
    }

}
