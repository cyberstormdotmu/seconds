package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.Stream;

public class TaxableAmount {
    public static final TaxableAmount ZERO = fromNetAndVat(BigDecimal.ZERO, BigDecimal.ZERO);

    private final BigDecimal net;
    private final BigDecimal vat;
    private final BigDecimal gross;
    
	public TaxableAmount(BigDecimal net, BigDecimal vat, BigDecimal gross) {
        this.net = net;
        this.vat = vat;
        this.gross = gross;
    }

    public static TaxableAmount fromNetAndVat(BigDecimal net, BigDecimal vat) {
        BigDecimal gross = net.add(vat);
        return new TaxableAmount(net, vat, gross);
    }

    public BigDecimal getNet() {
		return net;
	}

	public BigDecimal getVat() {
		return vat;
	}

	public BigDecimal getGross() {
		return gross;
	}

	public BigDecimal net() {
        return net;
    }

    public BigDecimal vat() {
        return vat;
    }

    public BigDecimal gross() {
        return gross;
    }

    public TaxableAmount add(TaxableAmount addend) {
        return fromNetAndVat(this.net.add(addend.net), this.vat.add(addend.vat));
    }

    public TaxableAmount subtract(TaxableAmount subtrahend) {
        return fromNetAndVat(this.net.subtract(subtrahend.net), this.vat.subtract(subtrahend.vat));
    }

    public TaxableAmount negate() {
        return fromNetAndVat(this.net.negate(), this.vat.negate());
    }

    public static <T> TaxableAmount sum(Stream<T> stream, Function<T, TaxableAmount> valueProvider) {
        return stream.map(valueProvider).reduce(ZERO, TaxableAmount::add);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TaxableAmount)) {
            return false;
        }

        TaxableAmount that = (TaxableAmount) o;

        return new CompareToBuilder()
                .append(this.net, that.net)
                .append(this.vat, that.vat)
                .append(this.gross, that.gross)
                .toComparison() == 0;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(net)
                .append(vat)
                .append(gross)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("net", net)
                .append("vat", vat)
                .append("gross", gross)
                .toString();
    }
}
