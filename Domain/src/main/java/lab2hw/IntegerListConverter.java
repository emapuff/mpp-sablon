package lab2hw;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = false)
public class IntegerListConverter implements AttributeConverter<List<String>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String>attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return List.of();
        }
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .map(String::trim)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }
}
