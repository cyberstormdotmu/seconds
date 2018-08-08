package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PriceBandMovements implements Streamable<PriceBandMovement> {

    private final List<PriceBandMovement> movements;
    
    private PriceBandMovements(List<PriceBandMovement> movements) {
        this.movements = Collections.unmodifiableList(new ArrayList<>(movements));
    }
    
    public static Builder somePriceBandMovements() {
        return new Builder();
    }
    
    @Override
    public Iterator<PriceBandMovement> iterator() {
        return this.movements.iterator();
    }
    
    public int size() {
        return this.movements.size();
    }
    
    public static class Builder {
        
        private List<PriceBandMovement> movements;
        
        private Builder() {
            this.movements = new ArrayList<>();
        }
        
        public Builder with(PriceBandMovement.Builder movement) {
            this.movements.add(movement.build());
            return this;
        }
        
        public PriceBandMovements build() {
            return new PriceBandMovements(this.movements);
        }
    }
}