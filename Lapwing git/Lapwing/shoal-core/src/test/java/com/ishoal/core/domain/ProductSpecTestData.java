package com.ishoal.core.domain;

import static com.ishoal.core.domain.ProductSpec.aProductSpec;

public class ProductSpecTestData {

    public static ProductSpec driveSpec() {
        return aProductSpec().type("Drives").value("256GB SATA TLC SSD").build();
    }

    public static ProductSpec memorySpec() {
        return aProductSpec().type("Memory").value("4GB DDR3L-1600 SDRAM (1 x 4GB)").build();
    }

    public static ProductSpec processorSpec() {
        return aProductSpec().type("Processor").value("Intel Core i5 processor").build();
    }
}
