package com.ishoal.ws.buyer.dto;

import static com.ishoal.ws.buyer.dto.PlaceOrderRequestDto.aPlaceOrderRequest;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlaceOrderRequestDtoTest {

    private ObjectMapper objectMapper;

    @Before
    public void before() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void canSerialiseToJsonAndBack() throws IOException {
        PlaceOrderRequestDto originalDto = aPlaceOrderRequest().build();
        PlaceOrderRequestDto roundTrippedDto = toDto(toJson(originalDto));
        assertThat(EqualsBuilder.reflectionEquals(originalDto, roundTrippedDto), is(true));
    }

    private String toJson(PlaceOrderRequestDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private PlaceOrderRequestDto toDto(String json) {
        try {
            return objectMapper.readValue(json, PlaceOrderRequestDto.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
