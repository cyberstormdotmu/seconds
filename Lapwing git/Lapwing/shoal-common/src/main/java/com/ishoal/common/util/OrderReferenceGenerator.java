package com.ishoal.common.util;

import com.ishoal.common.domain.OrderReference;

public class OrderReferenceGenerator {
    private static OrderReferenceGenerationStrategy generationStrategy = new TimeBasedOrderReferenceGenerationStrategy(1);

    private OrderReferenceGenerator() {
        super();
    }

    public static void setStrategy(OrderReferenceGenerationStrategy strategy) {
        generationStrategy = strategy;
    }

    public static OrderReference generate() {
        return generationStrategy.generate();
    }
}
