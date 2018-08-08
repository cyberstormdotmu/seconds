package com.ishoal.core.credit;

import com.ishoal.core.domain.BuyerAppliedCreditStatus;
import com.ishoal.core.domain.BuyerCredits;
import com.ishoal.core.domain.TaxableAmount;
import com.ishoal.core.domain.PaymentStatus;
import com.ishoal.core.domain.CreditMovementType;
import com.ishoal.core.domain.User;
import com.ishoal.core.persistence.entity.BuyerAppliedCreditEntity;
import com.ishoal.core.persistence.entity.BuyerCreditEntity;
import com.ishoal.core.persistence.entity.OfferEntity;
import com.ishoal.core.persistence.entity.OrderEntity;
import com.ishoal.core.persistence.entity.OrderLineEntity;
import com.ishoal.core.persistence.repository.BuyerCreditEntityRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static org.hamcrest.Matchers.is;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuyerCreditServiceTest {
    private static final BigDecimal UNPAID_ORDER_NET = new BigDecimal("100.00");
    private static final BigDecimal UNPAID_ORDER_VAT = new BigDecimal("10.00");
    private static final BigDecimal PAID_OPEN_ORDER_NET = new BigDecimal("200.00");
    private static final BigDecimal USED_UP_CREDIT_NET = new BigDecimal("180.00");
    private static final BigDecimal USED_UP_CREDIT_GROSS = new BigDecimal("240.00");
    private static final BigDecimal USED_UP_CREDIT_VAT = new BigDecimal("60.00");
    private static final BigDecimal PAID_OPEN_ORDER_VAT = new BigDecimal("40.00");
    private static final BigDecimal PAID_CLOSED_ORDER_NET = new BigDecimal("400.00");
    private static final BigDecimal PAID_CLOSED_ORDER_VAT = new BigDecimal("160.00");
    private static final User USER = User.aUser().build();

    @Mock
    private BuyerCreditEntityRepository repository;

    private BuyerCreditService service;

    @Before
    public void before() {
        service = new BuyerCreditService(repository);
        theRepositoryWillReturnTheBuyerCredits();
    }

    @Test
    public void shouldReturnTheBuyerCredits() {
        BuyerCredits credits = service.fetchBuyerCredits(USER);
        assertThat(credits.size(), is(3));
    }

    @Test
    public void shouldReturnThePendingCreditBalance() {
        BuyerCredits credits = service.fetchBuyerCredits(USER);
        assertThat(credits.getPendingCreditBalance(), is(expectedPendingCreditBalance()));
    }

    @Test
    public void shouldReturnTheAvailableCreditBalance() {
        BuyerCredits credits = service.fetchBuyerCredits(USER);
        assertThat(credits.getAvailableCreditBalance(), is(expectedAvailableCreditBalance()));
    }

    @Test
    public void shouldReturnTheAvailableCreditBalanceAfterAppliedCredits() {
        theRepositoryWillReturnTheBuyerCreditsWithSomeAvailableAppliedCredit();

        BuyerCredits credits = service.fetchBuyerCredits(USER);
        assertThat(credits.getAvailableCreditBalance(), is(expectedAvailableCreditBalanceAfterAppliedCredits()));
    }

    @Test
    public void shouldReturnTheRedeemableCreditBalance() {
        BuyerCredits credits = service.fetchBuyerCredits(USER);
        assertThat(credits.getRedeemableCreditBalance(), is(expectedRedeemableCreditBalance()));
    }

    @Test
    public void shouldReturnTheRedeemableCreditBalanceAfterAppliedCredits() {
        theRepositoryWillReturnTheBuyerCreditsWithSomeRedeemableAppliedCredit();

        BuyerCredits credits = service.fetchBuyerCredits(USER);
        assertThat(credits.getRedeemableCreditBalance(), is(expectedRedeemableCreditBalanceAfterCreditAppliedOrRedeemed()));
    }

    @Test
    public void shouldReturnTheRedeemableCreditBalanceAfterRedeemedCredits() {
        theRepositoryWillReturnTheBuyerCreditsWithSomeRedeemedCredit();

        BuyerCredits credits = service.fetchBuyerCredits(USER);
        assertThat(credits.getRedeemableCreditBalance(), is(expectedRedeemableCreditBalanceAfterCreditAppliedOrRedeemed()));
    }

    private void theRepositoryWillReturnTheBuyerCredits() {
        theRepositoryWillReturnTheBuyerCredits(creditOnUnpaidOrder(), creditOnPaidOrderInOpenOffer(), creditOnPaidOrderInClosedOffer());
    }

    private void theRepositoryWillReturnTheBuyerCreditsWithSomeAvailableAppliedCredit() {
        theRepositoryWillReturnTheBuyerCredits(creditOnUnpaidOrder(), creditOnPaidOrderInOpenOfferWithCreditApplied(),
            creditOnPaidOrderInClosedOffer());
    }

    private BuyerCreditEntity creditOnPaidOrderInOpenOfferWithCreditApplied() {
        BuyerCreditEntity credit = creditOnPaidOrderInOpenOffer();
        applyCredit(credit);
        return credit;
    }

    private void theRepositoryWillReturnTheBuyerCreditsWithSomeRedeemableAppliedCredit() {

        theRepositoryWillReturnTheBuyerCredits(creditOnUnpaidOrder(), creditOnPaidOrderInOpenOffer(),
            creditOnPaidOrderInClosedOfferWhereSomeOfItHasBeenAppliedToAnotherOrder());
    }

    private BuyerCreditEntity creditOnPaidOrderInClosedOfferWhereSomeOfItHasBeenAppliedToAnotherOrder() {
        BuyerCreditEntity credit = creditOnPaidOrderInClosedOffer();
        applyCredit(credit);
        return credit;
    }

    private void theRepositoryWillReturnTheBuyerCreditsWithSomeRedeemedCredit() {

        theRepositoryWillReturnTheBuyerCredits(creditOnUnpaidOrder(), creditOnPaidOrderInOpenOffer(),
            creditOnPaidOrderInClosedOfferWhereSomeOfHasBeenRedeemedAsCash());
    }

    private BuyerCreditEntity creditOnPaidOrderInClosedOfferWhereSomeOfHasBeenRedeemedAsCash() {
        BuyerCreditEntity credit = creditOnPaidOrderInClosedOffer();
        redeemCredit(credit);
        return credit;
    }

    private TaxableAmount expectedPendingCreditBalance() {
        return fromNetAndVat(UNPAID_ORDER_NET, UNPAID_ORDER_VAT);
    }

    private TaxableAmount expectedAvailableCreditBalance() {
        return fromNetAndVat(PAID_OPEN_ORDER_NET, PAID_OPEN_ORDER_VAT).add(fromNetAndVat(PAID_CLOSED_ORDER_NET, PAID_CLOSED_ORDER_VAT));
    }

    private TaxableAmount expectedRedeemableCreditBalance() {
        return fromNetAndVat(PAID_CLOSED_ORDER_NET, PAID_CLOSED_ORDER_VAT);
    }

    private TaxableAmount expectedAvailableCreditBalanceAfterAppliedCredits() {
        return expectedAvailableCreditBalance().subtract(fromNetAndVat(USED_UP_CREDIT_NET, USED_UP_CREDIT_VAT));
    }

    private TaxableAmount expectedRedeemableCreditBalanceAfterCreditAppliedOrRedeemed() {
        return expectedRedeemableCreditBalance().subtract(fromNetAndVat(USED_UP_CREDIT_NET, USED_UP_CREDIT_VAT));
    }

    private BuyerCreditEntity creditOnUnpaidOrder() {
        BuyerCreditEntity credit = new BuyerCreditEntity();
        credit.setOrderLine(orderLine(PaymentStatus.UNPAID, true));
        credit.setNetAmount(UNPAID_ORDER_NET);
        credit.setVatAmount(UNPAID_ORDER_VAT);
        return credit;
    }

    private BuyerCreditEntity creditOnPaidOrderInClosedOffer() {
        BuyerCreditEntity credit = new BuyerCreditEntity();
        credit.setOrderLine(orderLine(PaymentStatus.PAID, false));
        credit.setNetAmount(PAID_CLOSED_ORDER_NET);
        credit.setVatAmount(PAID_CLOSED_ORDER_VAT);
        return credit;
    }

    private BuyerCreditEntity creditOnPaidOrderInOpenOffer() {
        BuyerCreditEntity credit = new BuyerCreditEntity();
        credit.setOrderLine(orderLine(PaymentStatus.PAID, true));
        credit.setNetAmount(PAID_OPEN_ORDER_NET);
        credit.setVatAmount(PAID_OPEN_ORDER_VAT);
        return credit;
    }

    private void applyCredit(BuyerCreditEntity credit) {

        BuyerAppliedCreditEntity appliedCredit = new BuyerAppliedCreditEntity();
        appliedCredit.setBuyerCredit(credit);
        appliedCredit.setNetAmount(USED_UP_CREDIT_NET);
        appliedCredit.setVatAmount(USED_UP_CREDIT_VAT);
        appliedCredit.setStatus(BuyerAppliedCreditStatus.CONFIRMED);
        appliedCredit.setSpendType(CreditMovementType.SPEND);
        credit.setAppliedBuyerCredits(Arrays.asList(appliedCredit));
    }

    private void redeemCredit(BuyerCreditEntity credit) {
        BuyerAppliedCreditEntity appliedCredit = new BuyerAppliedCreditEntity();
        appliedCredit.setBuyerCredit(credit);
        appliedCredit.setNetAmount(USED_UP_CREDIT_NET);
        appliedCredit.setVatAmount(USED_UP_CREDIT_VAT);
        appliedCredit.setStatus(BuyerAppliedCreditStatus.CONFIRMED);
        appliedCredit.setSpendType(CreditMovementType.REDEEM);
        credit.setAppliedBuyerCredits(Arrays.asList(appliedCredit));
    }

    private void theRepositoryWillReturnTheBuyerCredits(BuyerCreditEntity... credits) {
        when(repository.findCreditsForBuyer(anyLong()))
            .thenReturn(Arrays.asList(credits));
    }

    private OrderLineEntity orderLine(PaymentStatus paymentStatus, boolean openOffer) {
        OrderLineEntity orderLine = new OrderLineEntity();
        orderLine.setOrder(order(paymentStatus));
        orderLine.setOffer(offer(openOffer));
        return orderLine;
    }

    private OfferEntity offer(boolean openOffer) {
        OfferEntity offer = new OfferEntity();
        if(openOffer) {
            offer.setEndDateTime(now().plusDays(1));
        } else {
            offer.setEndDateTime(now().minusMillis(1));
        }
        return offer;
    }

    private OrderEntity order(PaymentStatus paymentStatus) {
        OrderEntity order = new OrderEntity();
        order.setPaymentStatus(paymentStatus);
        return order;
    }
}