/*package com.ishoal.ws.buyer.controller;

import com.ishoal.core.credit.BuyerCreditService;
import com.ishoal.core.credit.BuyerWithdrawService;
import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.domain.BuyerCredits;
import com.ishoal.core.domain.User;
import com.ishoal.ws.buyer.dto.CreditBalancesDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.ishoal.core.domain.BuyerCredit.aBuyerCredit;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreditControllerTest {

    private static final User USER = User.aUser().build();

    @Mock
    private BuyerCreditService service;
    @Mock
    private BuyerWithdrawService wservice;

    private CreditController controller;

    @Before
    public void before() {
        controller = new CreditController(service,wservice);
        theServiceWillReturnTheBuyerCredits();
    }

    @Test
    public void shouldReturnCreditBalancesWithCorrectPendingCreditBalance() {
        CreditBalancesDto dto = controller.getCreditBalances(USER);
        assertThat(dto.getPendingCreditBalance(), is(new BigDecimal("120.00")));
    }

    @Test
    public void shouldReturnCreditBalancesWithCorrectAvailableCreditBalance() {
        CreditBalancesDto dto = controller.getCreditBalances(USER);
        assertThat(dto.getAvailableCreditBalance(), is(new BigDecimal("720.00")));
    }

    @Test
    public void shouldReturnCreditBalancesWithCorrectRedeemableCreditBalance() {
        CreditBalancesDto dto = controller.getCreditBalances(USER);
        assertThat(dto.getRedeemableCreditBalance(), is(new BigDecimal("480.00")));
    }

    private void theServiceWillReturnTheBuyerCredits() {
        when(service.fetchBuyerCredits(any())).thenReturn(buyerCredits());
    }

    private BuyerCredits buyerCredits() {
        return BuyerCredits.over(Arrays.asList(pending(), available(), redeemable()));
    }

    private BuyerCredit pending() {
        return aBuyerCredit()
                .amount(fromNetAndVat(new BigDecimal("100.00"), new BigDecimal("20.00")))
                .build();
    }

    private BuyerCredit available() {
        return aBuyerCredit().spendable(true)
                .amount(fromNetAndVat(new BigDecimal("200.00"), new BigDecimal("40.00")))
                .build();
    }

    private BuyerCredit redeemable() {
        return aBuyerCredit().spendable(true).redeemable(true)
                .amount(fromNetAndVat(new BigDecimal("400.00"), new BigDecimal("80.00")))
                .build();
    }
}*/