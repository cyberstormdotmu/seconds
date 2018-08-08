/*package com.ishoal.core.offer;

import com.ishoal.core.domain.BuyerAppliedCredit;
import com.ishoal.core.domain.BuyerAppliedCreditStatus;
import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.OfferReference;
import com.ishoal.core.domain.OfferReport;
import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.PaymentStatus;
import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.ProductVatRate;
import com.ishoal.core.domain.ProductVatRates;
import com.ishoal.core.domain.TaxableAmount;
import com.ishoal.core.domain.VatRate;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.repository.OrderRepository;
import com.ishoal.core.repository.ProductRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static com.ishoal.core.domain.PriceBands.somePriceBands;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfferReportServiceTest {

    private static Vendor HP = Vendor.aVendor().name("HP").build();
    private static DateTime OFFER_START_TIME = DateTime.now().minusDays(20);
    private static DateTime OFFER_END_TIME = DateTime.now().plusDays(20);
    private static OfferReference HP_ELITE840_OFFER = OfferReference.create();
    private static Product HPELITE840 = hpElite840();
    private static OfferReference HP_SPECTRE_OFFER = OfferReference.create();
    private static Product HPSPECTRE = hpSpectre();

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    private OfferReportService service;

    @Before
    public void before() {
        theProductRepositoryWillReturnTheProduct();
        theOrderRepositoryWillReturnSomeOrders();
        service = new OfferReportService(productRepository, orderRepository);
    }

    @Test
    public void theReportContainsTheProduct() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getProduct().getCode(), is(HPELITE840.getCode()));
    }

    @Test
    public void theReportContainsTheOffer() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getOffer(), is(notNullValue()));
    }

    @Test
    public void theReportHasTheConfirmedVolume() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getConfirmedVolume(), is(18L));
    }

    @Test
    public void theReportHasThePaidVolume() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getPaidVolume(), is(12L));
    }

    @Test
    public void theReportHasTheCurrentPriceBand() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getCurrentPriceBand().getMinVolume(), is(10L));
    }

    @Test
    public void shoalInitialPaymentPerUnitTakesTheMinimumFromAcrossThePriceCurve() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getShoalInitialPayment().getPaymentPerUnit(), is(TaxableAmount.fromNetAndVat(new BigDecimal("5.00"), new BigDecimal("1.00"))));
    }

    @Test
    public void shoalInitialPaymentPendingAmountIncludesUnpaidAndPartPaidOrders() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getShoalInitialPayment().getPendingAmount(), is(TaxableAmount.fromNetAndVat(new BigDecimal("30.00"), new BigDecimal("6.00"))));
    }

    @Test
    public void shoalInitialPaymentUnpaidAmountIncludesOnlyPaidOrders() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getShoalInitialPayment().getUnpaidAmount(), is(TaxableAmount.fromNetAndVat(new BigDecimal("60.00"), new BigDecimal("12.00"))));
    }

    @Test
    public void shoalInitialPaymentPaidAmountIsZeroWhenNoPaymentMade() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getShoalInitialPayment().getPaidAmount(), is(TaxableAmount.fromNetAndVat(new BigDecimal("0.00"), new BigDecimal("0.00"))));
    }

    @Test
    public void shoalBalancingPaymentPerUnitTakesTheMinimumFromAcrossThePriceCurve() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getShoalBalancingPayment().getPaymentPerUnit(), is(TaxableAmount.fromNetAndVat(new BigDecimal("5.00"), new BigDecimal("1.00"))));
    }

    @Test
    public void shoalBalancingPaymentPendingAmountIncludesUnpaidAndPartPaidOrders() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getShoalBalancingPayment().getPendingAmount(), is(TaxableAmount.fromNetAndVat(new BigDecimal("30.00"), new BigDecimal("6.00"))));
    }

    @Test
    public void shoalBalancingPaymentUnpaidAmountIncludesOnlyPaidOrders() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getShoalBalancingPayment().getUnpaidAmount(), is(TaxableAmount.fromNetAndVat(new BigDecimal("60.00"), new BigDecimal("12.00"))));
    }

    @Test
    public void shoalBalancingPaymentPaidAmountIsZeroWhenNoPaymentMade() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getShoalBalancingPayment().getPaidAmount(), is(TaxableAmount.fromNetAndVat(new BigDecimal("0.00"), new BigDecimal("0.00"))));
    }

    @Test
    public void vendorInitialPaymentPerUnitTakesTheMinimumFromAcrossThePriceCurve() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getVendorInitialPayment().getPaymentPerUnit(), is(TaxableAmount.fromNetAndVat(new BigDecimal("65.00"), new BigDecimal("13.00"))));
    }

    @Test
    public void vendorInitialPaymentPendingAmountIncludesUnpaidAndPartPaidOrders() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getVendorInitialPayment().getPendingAmount(), is(TaxableAmount.fromNetAndVat(new BigDecimal("390.00"), new BigDecimal("78.00"))));
    }

    @Test
    public void vendorInitialPaymentUnpaidAmountOnlyIncludesPaidOrders() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getVendorInitialPayment().getUnpaidAmount(), is(TaxableAmount.fromNetAndVat(new BigDecimal("780.00"), new BigDecimal("156.00"))));
    }

    @Test
    public void vendorInitialPaymentPaidAmountIsZeroWhenNoPaymentMade() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getVendorInitialPayment().getPaidAmount(), is(TaxableAmount.fromNetAndVat(new BigDecimal("0.00"), new BigDecimal("0.00"))));
    }

    @Test
    public void vendorBalancingPaymentPerUnitTakesTheMinimumFromAcrossThePriceCurve() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getVendorBalancingPayment().getPaymentPerUnit(), is(TaxableAmount.fromNetAndVat(new BigDecimal("15.00"), new BigDecimal("3.00"))));
    }

    @Test
    public void vendorBalancingPaymentPendingAmountIncludesUnpaidAndPartPaidOrders() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getVendorBalancingPayment().getPendingAmount(), is(TaxableAmount.fromNetAndVat(new BigDecimal("90.00"), new BigDecimal("18.00"))));
    }

    @Test
    public void vendorBalancingPaymentUnpaidAmountOnlyIncludesPaidOrders() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getVendorBalancingPayment().getUnpaidAmount(), is(TaxableAmount.fromNetAndVat(new BigDecimal("180.00"), new BigDecimal("36.00"))));
    }

    @Test
    public void vendorBalancingPaymentPaidAmountIsZeroWhenNoPaymentMade() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getVendorBalancingPayment().getPaidAmount(), is(TaxableAmount.fromNetAndVat(new BigDecimal("0.00"), new BigDecimal("0.00"))));
    }

    @Test
    public void buyerCreditsEarnedOnlyIncludesCreditsOnPaidOrders() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getBuyerCreditsEarned(), comparesEqualTo(new BigDecimal("20.00")));
    }

    @Test
    public void theReportHasTheBuyerCreditsSpent() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getBuyerCreditsSpent(), comparesEqualTo(new BigDecimal("5.00")));
    }

    @Test
    public void buyerPaymentsReceivedOnlyIncludesFullPayments() {
        OfferReport report = service.createOfferReport(HP_ELITE840_OFFER);
        assertThat(report.getBuyerPaymentsReceived(), comparesEqualTo(new BigDecimal("1100.00")));
    }

    private void theProductRepositoryWillReturnTheProduct() {
        when(productRepository.findProductForOffer(HP_ELITE840_OFFER)).thenReturn(HPELITE840);
    }

    private void theOrderRepositoryWillReturnSomeOrders() {
        when(orderRepository.findOrdersForOffer(any()))
                .thenReturn(Orders.over(anUnconfirmedOrder(),
                        aPaidOrderWithCredit(),
                        aPaidOrderWithPartSpentCredit(),
                        aPaidOrderWithTwoOrderLines(),
                        aPartPaidOrder(),
                        aPartPaidOrderWithCredit(),
                        anUnpaidOrder(),
                        anUnpaidOrderWithCredit(),
                        anUnconfirmedOrder()));
    }

    private static Product hpSpectre() {
        return Product.aProduct()
                .code(ProductCode.from("HPSPECTRE"))
                .name("HP Spectre")
                .stock(40000L)
                .termsAndConditions("OFFER valid for 1 week only")
                .vendor(HP)
                .offers(Offers.over(offerWithThreePriceBands(HP_SPECTRE_OFFER)))
                .vatRates(vatRates())
                .build();
    }

    private static ProductVatRates vatRates() {
        return ProductVatRates.over(ProductVatRate.aProductVatRate().vatRate(VatRate.aVatRate().rate(new BigDecimal("20.00")).build()).startDateTime(DateTime.now().minusDays(1)).build());
    }

    private static Product hpElite840() {
        return Product.aProduct()
                .code(ProductCode.from("HPELITE840"))
                .name("HP Elite 840")
                .vendor(HP)
                .stock(40000L)
                .termsAndConditions("OFFER valid for 1 week only")
                .offers(Offers.over(offerWithThreePriceBands(HP_ELITE840_OFFER)))
                .vatRates(vatRates())
                .build();
    }

    private static Offer offerWithThreePriceBands(OfferReference offerReference) {
        return Offer.anOffer()
                .offerReference(offerReference)
                .priceBands(somePriceBands()
                        .priceBand(priceBand(0, 9, "100.00", "95.00", "5.00"))
                        .priceBand(priceBand(10, 19, "90.00", "80.00", "10.00"))
                        .priceBand(priceBand(20, 29, "80.00", "65.00", "15.00"))
                        .build())
                .startDateTime(OFFER_START_TIME)
                .endDateTime(OFFER_END_TIME)
                .build();
    }

    private static PriceBand priceBand(long minVolume, long maxVolume, String buyerPrice, String vendorPrice, String shoalMargin) {
        return PriceBand.aPriceBand()
                .buyerPrice(new BigDecimal(buyerPrice))
                .vendorPrice(new BigDecimal(vendorPrice))
                .shoalMargin(new BigDecimal(shoalMargin))
                .minVolume(minVolume)
                .maxVolume(maxVolume)
                .build();
    }

    private static Order anUnconfirmedOrder() {
        return Order.anOrder()
                .status(OrderStatus.PROCESSING)
                .line(anOrderLineFor(10, HPELITE840))
                .build();
    }

    private static Order aPaidOrderWithCredit() {
        return Order.anOrder()
                .status(OrderStatus.CONFIRMED)
                .paymentStatus(PaymentStatus.PAID)
                .line(anOrderLineWithCredit(1, HPELITE840, "10.00"))
                .build();
    }

    private static Order aPaidOrderWithPartSpentCredit() {
        return Order.anOrder()
                .status(OrderStatus.CONFIRMED)
                .paymentStatus(PaymentStatus.PAID)
                .line(anOrderLineWithPartSpentCredit(1, HPELITE840, "10.00", "5.00"))
                .build();
    }

    private static Order aPaidOrderWithTwoOrderLines() {
        return Order.anOrder()
                .status(OrderStatus.CONFIRMED)
                .paymentStatus(PaymentStatus.PAID)
                .line(anOrderLineFor(10, HPELITE840))
                .line(anOrderLineFor(10, HPSPECTRE))
                .build();
    }

    private static Order anUnpaidOrder() {
        return Order.anOrder()
                .status(OrderStatus.CONFIRMED)
                .paymentStatus(PaymentStatus.UNPAID)
                .line(anOrderLineFor(2, HPELITE840))
                .build();
    }

    private static Order anUnpaidOrderWithCredit() {
        return Order.anOrder()
                .status(OrderStatus.CONFIRMED)
                .paymentStatus(PaymentStatus.UNPAID)
                .line(anOrderLineWithCredit(1, HPELITE840, "10.00"))
                .build();
    }

    private static Order aPartPaidOrder() {
        return Order.anOrder()
                .status(OrderStatus.CONFIRMED)
                .paymentStatus(PaymentStatus.PART_PAID)
                .line(anOrderLineFor(2, HPELITE840))
                .build();
    }

    private static Order aPartPaidOrderWithCredit() {
        return Order.anOrder()
                .status(OrderStatus.CONFIRMED)
                .paymentStatus(PaymentStatus.PART_PAID)
                .line(anOrderLineWithCredit(1, HPELITE840, "10.00"))
                .build();
    }

    private static OrderLine anOrderLineFor(long quantity, Product product) {
        return orderLine(quantity, product)
                .build();
    }

    private static OrderLine.Builder orderLine(long quantity, Product product) {
        BigDecimal buyerPrice = product.currentOffer().getPriceBandForRequestedQuantity(quantity).getBuyerPrice();

        return OrderLine.anOrderLine()
                .quantity(quantity)
                .product(product)
                .amount(taxableAmount(buyerPrice.multiply(new BigDecimal(quantity))))
                .offer(product.currentOffer());
    }

    private static OrderLine anOrderLineWithCredit(long quantity, Product product, String creditAmount) {
        BuyerCredit credit = BuyerCredit.aBuyerCredit()
                .amount(taxableAmount(new BigDecimal(creditAmount)))
                .build();

        return orderLine(quantity, product)
                .credit(credit)
                .build();
    }

    private static OrderLine anOrderLineWithPartSpentCredit(long quantity, Product product, String creditAmount, String creditSpentAmount) {
        BuyerAppliedCredit spentCredit = BuyerAppliedCredit.aBuyerAppliedCredit()
                .amount(taxableAmount(new BigDecimal(creditSpentAmount)))
                .status(BuyerAppliedCreditStatus.CONFIRMED)
                .build();

        BuyerCredit credit = BuyerCredit.aBuyerCredit()
                .amount(taxableAmount(new BigDecimal(creditAmount)))
                .spendable(true)
                .appliedCredit(spentCredit)
                .build();

        return orderLine(quantity, product)
                .credit(credit)
                .build();
    }

    private static TaxableAmount taxableAmount(BigDecimal amount) {
        return TaxableAmount.fromNetAndVat(amount, BigDecimal.ZERO);
    }

}*/