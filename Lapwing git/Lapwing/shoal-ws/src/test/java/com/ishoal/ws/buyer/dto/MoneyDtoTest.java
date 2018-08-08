package com.ishoal.ws.buyer.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MoneyDtoTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldSerializeInCorrectFormat() throws Exception {

        MoneyDto moneyDto = new MoneyDto("1000.00");

        String actual = mapper.writeValueAsString(moneyDto);
        System.out.println(actual);
        assertThat(actual, is("1000.00"));

        moneyDto = new MoneyDto("1000.0");

        actual = mapper.writeValueAsString(moneyDto);
        System.out.println(actual);
        assertThat(actual, is("1000.00"));

        moneyDto = new MoneyDto("1000");

        actual = mapper.writeValueAsString(moneyDto);
        System.out.println(actual);
        assertThat(actual, is("1000.00"));
    }

    @Test
    public void shouldErrorWhenAttemptToInitialiseWithTooManyDecimalPoints() {

        thrown.expect(ArithmeticException.class);
        new MoneyDto("1000.001");
    }

    @Test
    public void shouldDeserializeWithCorrectValue() throws IOException {

        MoneyDto moneyDto = mapper.readValue("1000.00", MoneyDto.class);

        assertThat(moneyDto.toBigDecimal(), is(new BigDecimal("1000.00")));

        moneyDto = mapper.readValue("1000.0", MoneyDto.class);

        assertThat(moneyDto.toBigDecimal(), is(new BigDecimal("1000.00")));

        moneyDto = mapper.readValue("1000", MoneyDto.class);

        assertThat(moneyDto.toBigDecimal(), is(new BigDecimal("1000.00")));
    }

    @Test
    public void shouldErrorWhenAttemptToDeserializeValueWithTooManyDecimalPoints() throws IOException {

        thrown.expect(ArithmeticException.class);
        mapper.readValue("1000.001", MoneyDto.class);
    }
}
