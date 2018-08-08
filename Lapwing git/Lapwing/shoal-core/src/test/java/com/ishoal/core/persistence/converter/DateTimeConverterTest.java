package com.ishoal.core.persistence.converter;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class DateTimeConverterTest {

    private DateTimeConverter converter = new DateTimeConverter();

    @Test
    public void convertToDatabaseColumnReturnsNullWhenGivenNull() {
        assertThat(converter.convertToDatabaseColumn(null), is(nullValue()));
    }

    @Test
    public void convertToEntityAttributeReturnsNullWhenGivenNull() {
        assertThat(converter.convertToEntityAttribute(null), is(nullValue()));
    }
}