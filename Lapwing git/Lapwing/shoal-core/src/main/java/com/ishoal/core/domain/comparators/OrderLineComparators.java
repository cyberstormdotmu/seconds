package com.ishoal.core.domain.comparators;

import com.ishoal.core.domain.OrderLine;

import java.util.Comparator;

public class OrderLineComparators {

    private OrderLineComparators() {

    }

    public static Comparator<OrderLine> byPartConsumedCredit() {
        return (line1, line2) -> {
            boolean line1HasPartConsumedCredit = line1.getCredits().hasPartConsumedCredit();
            boolean line2HasPartConsumedCredit = line2.getCredits().hasPartConsumedCredit();
            if(line1HasPartConsumedCredit) {
                if(!line2HasPartConsumedCredit) {
                    return -1;
                }
                return 0;
            }
            if(line2HasPartConsumedCredit) {
                return 1;
            }
            return 0;
        };
    }

    public static Comparator<OrderLine> byOfferEndDate() {
        return (line1, line2) -> line1.getOffer().getEndDateTime().compareTo(line2.getOffer().getEndDateTime());
    }
}
