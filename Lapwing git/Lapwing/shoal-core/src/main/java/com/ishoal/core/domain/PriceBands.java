package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static com.ishoal.core.domain.PriceBandMovement.aPriceBandMovement;
import static com.ishoal.core.domain.PriceBandMovements.somePriceBandMovements;
import static java.util.Arrays.asList;

public class PriceBands implements Streamable<PriceBand> {

    private static final PriceBandAscendingMinimumVolumeComparator COMPARATOR_MINIMUM_VOLUME_ASCENDING = new PriceBandAscendingMinimumVolumeComparator();

    private final List<PriceBand> priceBands;

    private PriceBands(Builder builder) {
        this(builder.priceBands);
    }

    private PriceBands(List<PriceBand> priceBands) {
        this.priceBands = Collections.unmodifiableList(priceBands);
    }

    public static PriceBands over(List<PriceBand> priceBands) {
        return new PriceBands(priceBands);
    }

    public static PriceBands over(PriceBand... priceBands) {
        return new PriceBands(asList(priceBands));
    }

    public static Builder somePriceBands() {
        return new Builder();
    }

    public PriceBand getPriceBandForBuyerPrice(BigDecimal buyerPrice) {
        for (PriceBand priceBand : this.priceBands) {
            if (priceBand.getBuyerPrice().compareTo(buyerPrice) == 0) {
                return priceBand;
            }
        }
        return null;
    }

    public PriceBand getPriceBandForVolume(long volume) {
        for (PriceBand priceBand : this.priceBands) {
            if (priceBand.isVolumeWithinBand(volume)) {
                return priceBand;
            }
        }
        return null;
    }

    public PriceBand first() {
        return priceBands.get(0);
    }

    public PriceBand last() {
        return priceBands.get(priceBands.size()-1);
    }

    public PriceBands range(PriceBand startBand, PriceBand endBand) {
        List<PriceBand> filteredPriceBands = new ArrayList<>();
        boolean foundStart = false;
        for (PriceBand priceBand : this.priceBands) {
            if (priceBand.equals(startBand)) {
                foundStart = true;
            }
            if (foundStart) {
                filteredPriceBands.add(priceBand);
            }
            if (priceBand.equals(endBand)) {
                break;
            }
        }
        return new PriceBands(filteredPriceBands);
    }

    public PriceBandMovements movements() {
        PriceBandMovements.Builder movements = somePriceBandMovements();
        for (int i = 0, max = size() - 1; i < max; i++) {
            movements.with(aPriceBandMovement().startPriceBand(get(i)).endPriceBand(get(i + 1)));
        }
        return movements.build();
    }

    @Override
    public Iterator<PriceBand> iterator() {
        return priceBands.iterator();
    }

    public PriceBand get(int index) {
        return priceBands.get(index);
    }

    public int size() {
        return priceBands.size();
    }

    public static PriceBands emptyPriceBands() {

        return somePriceBands().build();
    }

    public boolean allValidForSave() {

        if (priceBands.isEmpty()) {
            return true;
        }

        // validate price band invariants
        boolean isValid = true;
        Long lastBandHigh = -1L;
        for (int i = 0; i < priceBands.size(); i += 1) {
            PriceBand priceBand = priceBands.get(i);
            Long bandLow = priceBand.getMinVolume();
            Long bandHigh = priceBand.getMaxVolume();

            isValid &= isValidBandForSave(lastBandHigh, i, bandLow, bandHigh);
            lastBandHigh = bandHigh;
        }
        return isValid;
    }

    private boolean isValidBandForSave(Long lastBandHigh, int bandIndex, Long bandLow, Long bandHigh) {

        return (isLastMaxBandUnbounded(bandIndex, bandHigh) ||
            (isUnbounded(bandHigh)
                && !isLastBand(bandIndex)
                && isValidRange(bandLow, bandHigh)
                && isValidRange(lastBandHigh, bandLow))
        )
            && noGaps(lastBandHigh, bandLow);
    }

    private boolean noGaps(Long lastBandHigh, Long bandLow) {

        return bandLow - lastBandHigh == 1;
    }

    private boolean isLastBand(int i) {

        return i == priceBands.size() - 1;
    }

    private boolean isUnbounded(Long bandHigh) {

        return bandHigh != null;
    }

    private boolean isValidRange(Long bandLow, Long bandHigh) {

        return bandHigh > bandLow;
    }

    private boolean isLastMaxBandUnbounded(int index, Long bandHigh) {

        return bandHigh == null && isLastBand(index);
    }

    public boolean isEmpty() {

        return priceBands.isEmpty();
    }

    public static class Builder {
        private List<PriceBand> priceBands = new ArrayList<>();

        private Builder() {
            super();
        }

        public Builder priceBand(PriceBand priceBands) {
            this.priceBands.add(priceBands);
            return this;
        }

        public PriceBands build() {
            Collections.sort(priceBands, COMPARATOR_MINIMUM_VOLUME_ASCENDING);
            return new PriceBands(this);
        }
    }

    private static class PriceBandAscendingMinimumVolumeComparator implements Comparator<PriceBand> {
        @Override
        public int compare(PriceBand o1, PriceBand o2) {
            return Long.compare(o1.getMinVolume(), o2.getMinVolume());
        }
    }
}