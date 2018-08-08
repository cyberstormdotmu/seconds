package com.ishoal.ws.buyer.controller;

import com.ishoal.ws.buyer.dto.ProductVatRateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.ishoal.ws.buyer.dto.ProductVatRateDto.aVatRateDto;

@RestController
@RequestMapping("/ws/vatrates")
public class VatRateController {
    private static final Logger logger = LoggerFactory.getLogger(VatRateController.class);

    @RequestMapping(method = RequestMethod.GET)
    public List<ProductVatRateDto> getAllVatRates() {
        logger.info("Fetch All Vat Rates");
        // REW eventually this should retrieve from database
        return Arrays.asList(
            aVatRateDto().code("STANDARD")
                .rate(new BigDecimal("20.00"))
                .build(),
            aVatRateDto().code("REDUCED")
                .rate(new BigDecimal("5.00"))
                .build(),
            aVatRateDto().code("ZERO")
                .rate(new BigDecimal("0.00"))
                .build(),
            aVatRateDto().code("EXEMPT")
                .build());
    }

}
