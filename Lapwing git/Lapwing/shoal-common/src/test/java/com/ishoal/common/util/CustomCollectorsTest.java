package com.ishoal.common.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class CustomCollectorsTest {

    @Test
    public void shouldCombineLists() {
        List<String> mergedList = listOfLists().stream().collect(CustomCollectors.toMergedList());
        assertThat(mergedList, contains("A", "B", "C", "D", "E"));
    }

    private List<List<String>> listOfLists() {
        return Arrays.asList(Arrays.asList("A", "B", "C"),
                Arrays.asList("D", "E"));
    }
}