package com.ishoal.core.domain;

import com.ishoal.common.domain.OrderReference;
import org.joda.time.DateTime;

public class BuyerAppliedCredit {
    private final BuyerAppliedCreditId id;
    private BuyerAppliedCreditStatus status;
    private final CreditMovementType spendType;
    private final TaxableAmount amount;
    private final OrderReference originalOrderReference;
    private final ProductCode originalOrderLineProductCode;
    private final OrderReference orderSpentOnReference;
    private final DateTime created;
    private final DateTime modified;

    private BuyerAppliedCredit(Builder builder) {

        id = builder.id;
        status = builder.status;
        amount = builder.amount;
        spendType = builder.spendType;
        originalOrderReference = builder.originalOrderReference;
        originalOrderLineProductCode = builder.originalOrderLineProductCode;
        orderSpentOnReference = builder.orderSpentOnReference;
        created = builder.created;
        modified = builder.modified;
    }

    public BuyerAppliedCreditId getId() {
        return id;
    }

    public BuyerAppliedCreditStatus getStatus() {
        return status;
    }

    public void setStatus(BuyerAppliedCreditStatus status) {
        this.status = status;
    }

    public CreditMovementType getSpendType() {
        return spendType;
    }

    public TaxableAmount getAmount() {
        return amount;
    }

    public OrderReference getOriginalOrderReference() {
        return originalOrderReference;
    }

    public ProductCode getOriginalOrderLineProductCode() {
        return originalOrderLineProductCode;
    }

    public OrderReference getOrderSpentOnReference() {
        return orderSpentOnReference;
    }

    public DateTime getCreated() {
        return created;
    }

    public DateTime getModified() {
        return modified;
    }

    public static Builder aBuyerAppliedCredit() {
        return new Builder();
    }

    public static final class Builder {
        private BuyerAppliedCreditId id = BuyerAppliedCreditId.EMPTY_BUYER_APPLIED_CREDIT_ID;
        private BuyerAppliedCreditStatus status = BuyerAppliedCreditStatus.RESERVED;
        private CreditMovementType spendType = CreditMovementType.SPEND;
        private TaxableAmount amount;
        private OrderReference originalOrderReference = OrderReference.EMPTY_ORDER_REFERENCE;
        private ProductCode originalOrderLineProductCode = ProductCode.EMPTY_PRODUCT_CODE;
        private OrderReference orderSpentOnReference = OrderReference.EMPTY_ORDER_REFERENCE;
        private DateTime created;
        private DateTime modified;

        private Builder() {
        }

        public Builder id(BuyerAppliedCreditId val) {
            id = val;
            return this;
        }

        public Builder status(BuyerAppliedCreditStatus val) {
            status = val;
            return this;
        }

        public Builder spendType(CreditMovementType val) {
            spendType = val;
            return this;
        }

        public Builder amount(TaxableAmount val) {
            amount = val;
            return this;
        }

        public Builder originalOrderReference(OrderReference val) {
            this.originalOrderReference = val;
            return this;
        }

        public Builder originalOrderLineProductCode(ProductCode val) {
            this.originalOrderLineProductCode = val;
            return this;
        }

        public Builder orderSpentOnReference(OrderReference val) {
            this.orderSpentOnReference = val;
            return this;
        }

        public Builder created(DateTime created) {
            this.created = created;
            return this;
        }

        public Builder modified(DateTime modified) {
            this.modified = modified;
            return this;
        }

        public BuyerAppliedCredit build() {
            return new BuyerAppliedCredit(this);
        }
    }
}
