package com.onlinemed.config.converters;

import com.onlinemed.model.enums.RoleType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class RoleTypeListConverter implements AttributeConverter<List<RoleType>, String> {

    private static final String SPLIT_CHAR = ";";
    private static final RoleType[] values = RoleType.values();

    @Override
    public String convertToDatabaseColumn(List<RoleType> attribute) {
        return attribute != null ? attribute.stream()
                .map(r -> (String.valueOf(r.ordinal()))).collect(Collectors.joining(SPLIT_CHAR)) : "";
    }

    @Override
    public List<RoleType> convertToEntityAttribute(String dbData) {
        return (dbData.length() == 0) ? null : Arrays.stream(dbData.split(SPLIT_CHAR))
                .map(r -> values[Integer.parseInt(r)]).collect(Collectors.toList());
    }

}
