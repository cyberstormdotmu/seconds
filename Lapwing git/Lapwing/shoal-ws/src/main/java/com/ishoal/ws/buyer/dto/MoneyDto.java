package com.ishoal.ws.buyer.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonSerialize(using = MoneyDto.MoneySerializer.class)
@JsonDeserialize(using = MoneyDto.MoneyDeserializer.class)
public class MoneyDto {
    private final BigDecimal value;

    public MoneyDto(BigDecimal value) {

        this.value = value.setScale(2, RoundingMode.UNNECESSARY);
    }

    public MoneyDto(String stringValue) {
        this.value = new BigDecimal(stringValue).setScale(2);
    }

    public String getStringRepresentation() {
        return value.toPlainString();
    }

    public BigDecimal toBigDecimal() {

        return value;
    }

    public static class MoneySerializer extends JsonSerializer<MoneyDto> {
        @Override
        public void serialize(MoneyDto value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

            jgen.writeNumber(value.getStringRepresentation());
        }
    }

    public static class MoneyDeserializer extends JsonDeserializer<MoneyDto> {

        @Override
        public MoneyDto deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {

            return new MoneyDto(jsonParser.getValueAsString());
        }
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MoneyDto moneyDto = (MoneyDto) o;

        return new EqualsBuilder()
            .append(value, moneyDto.value)
            .isEquals();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 37)
            .append(value)
            .toHashCode();
    }
}
