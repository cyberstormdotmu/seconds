package com.ishoal.core.util;

import com.ishoal.common.util.TimeBasedOrderReferenceGenerationStrategy;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TimeBasedOrderReferenceGenerationStrategyTest {
    private TimeBasedOrderReferenceGenerationStrategy generator = new TimeBasedOrderReferenceGenerationStrategy(1);

    @Test
    public void shouldGenerateAnOrderReference() {
        assertThat(generator.generate(), is(not(nullValue())));
    }

    @Test
    public void shouldGenerateAnOrderReferenceWithCorrectPattern() {
        assertTrue(generator.generate().asString().matches("[A-Z0-9]{4}-[A-Z0-9]{6}-[A-Z0-9]{4}"));
    }
}