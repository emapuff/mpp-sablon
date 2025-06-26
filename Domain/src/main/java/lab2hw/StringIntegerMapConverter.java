package lab2hw;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Converter(autoApply = false)
public class StringIntegerMapConverter implements AttributeConverter<Map<String, Integer>, String> {

    private static final String PAIR_SEPARATOR = ",";
    private static final String KEY_VALUE_SEPARATOR = "-";

    @Override
    public String convertToDatabaseColumn(Map<String, Integer> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        // e -> "cheie-valoare"
        return attribute.entrySet().stream()
                .map(e -> e.getKey() + KEY_VALUE_SEPARATOR + e.getValue())
                .collect(Collectors.joining(PAIR_SEPARATOR));
    }

    @Override
    public Map<String, Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptyMap();
        }
        return Arrays.stream(dbData.split(PAIR_SEPARATOR))
                .map(String::trim)
                .map(pair -> {
                    String[] parts = pair.split(KEY_VALUE_SEPARATOR, 2);
                    String key = parts[0].trim();
                    Integer value = Integer.valueOf(parts[1].trim());
                    return Map.entry(key, value);
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }
}
