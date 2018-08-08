package com.ishoal.core.domain;

import static com.ishoal.common.util.IterableUtils.collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import com.ishoal.common.util.Streamable;

public class BuyerProfiles implements Streamable<BuyerProfile> {

	private final List<BuyerProfile> buyerProfiles;

    private BuyerProfiles(List<BuyerProfile> buyerProfiles) {
        this.buyerProfiles = Collections.unmodifiableList(new ArrayList<>(buyerProfiles));
    }
    
    public static BuyerProfiles over(List<BuyerProfile> buyerProfiles) {
        return new BuyerProfiles(buyerProfiles);
    }
    
    public static BuyerProfiles over(BuyerProfile... buyerProfiles) {
        return new BuyerProfiles(Arrays.asList(buyerProfiles));
    }

    public BuyerProfiles add(BuyerProfile buyerProfile) {
        List<BuyerProfile> extendedBuyerProfiles = new ArrayList<>(this.buyerProfiles);
        extendedBuyerProfiles.add(buyerProfile);
        return over(extendedBuyerProfiles);
    }
    
    @Override
    public Iterator<BuyerProfile> iterator() {
        return this.buyerProfiles.iterator();
    }

    public int size() {
        return this.buyerProfiles.size();
    }

    public BuyerProfiles filter(Predicate<BuyerProfile> predicate) {
        return stream()
            .filter(predicate)
            .collect(collector(BuyerProfiles::over));
    }
}
