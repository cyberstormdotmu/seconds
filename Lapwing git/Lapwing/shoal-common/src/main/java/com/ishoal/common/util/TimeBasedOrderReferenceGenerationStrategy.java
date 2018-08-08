package com.ishoal.common.util;

import com.ishoal.common.domain.OrderReference;
import org.joda.time.DateTime;

import java.util.Random;

public class TimeBasedOrderReferenceGenerationStrategy implements  OrderReferenceGenerationStrategy {
    private static final String LEXICON = "BCDFGHJKLMNPQRSTVWXYZ1234567890";

    private final char serverIndicator;
    private final Random randomNumberGenerator = new Random();

    public TimeBasedOrderReferenceGenerationStrategy(int serverNumber) {
        if(serverNumber < 1 || serverNumber > 26) {
            throw new IllegalArgumentException("Server number must be between 1 and 26");
        }

        this.serverIndicator = encode(serverNumber-1);
    }

    @Override
    public OrderReference generate() {
        return OrderReference.from(generateAsString());
    }

    private String generateAsString() {
        char[] encoded = new char[14];

        long quotient = DateTime.now().getMillis();

        int i = 0;
        while(i < 9) {
            int remainder = (int)(quotient % LEXICON.length());
            quotient = quotient / LEXICON.length();
            encoded[i++] = encode(remainder);
        }

        encoded[i++] = serverIndicator;

        while(i < 14) {
            encoded[i++] = encode(randomNumberGenerator.nextInt(LEXICON.length()));
        }

        return String.format("%s-%s-%s",
                String.valueOf(encoded, 0, 4),
                String.valueOf(encoded, 4, 6),
                String.valueOf(encoded, 10, 4));
    }

    private char encode(int lexiconEntry) {
        return LEXICON.charAt(lexiconEntry);
    }

}
