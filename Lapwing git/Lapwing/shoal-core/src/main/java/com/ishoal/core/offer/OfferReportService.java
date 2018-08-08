package com.ishoal.core.offer;

import static com.ishoal.core.domain.OfferReport.anOfferReport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import com.ishoal.core.domain.AdminPaymentDetailsOfOrder;
import com.ishoal.core.domain.AdminRoutePayment;
import com.ishoal.core.domain.BuyerAppliedCredit;
import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.domain.CreditMovementType;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.OfferPayment;
import com.ishoal.core.domain.OfferReference;
import com.ishoal.core.domain.OfferReport;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.OrderPayment;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.OutstandingBalanceOfADay;
import com.ishoal.core.domain.PaymentRecordType;
import com.ishoal.core.domain.PaymentStatus;
import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.PriceBands;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.RoutePaymentDetails;
import com.ishoal.core.domain.SupplierPaymentForAnOrder;
import com.ishoal.core.domain.TaxableAmount;
import com.ishoal.core.domain.VatRate;
import com.ishoal.core.orders.VatCalculator;
import com.ishoal.core.payment.SupplierPaymentService;
import com.ishoal.core.repository.OrderRepository;
import com.ishoal.core.repository.ProductRepository;

public class OfferReportService {
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final VatCalculator vatCalculator = new VatCalculator();
	private final SupplierPaymentService supplierPaymentService;

	public OfferReportService(ProductRepository productRepository, OrderRepository orderRepository,
			SupplierPaymentService supplierPaymentService) {
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.supplierPaymentService = supplierPaymentService;
	}

	@Transactional(readOnly = true)
	public OfferReport createOfferReport(OfferReference offerReference) {
		OfferReport.Builder reportBuilder = anOfferReport();

		Product product = productRepository.findProductForOffer(offerReference);
		reportBuilder.product(product);

		Offer offer = product.findOffer(offerReference);
		reportBuilder.offer(offer);

		Orders orders = orderRepository.findOrdersForOffer(offerReference);

		List<OrderLine> confirmedOrderLinesForOffer = confirmedOrderLinesForOffer(orders, offerReference);
		
		Orders confirmedOrders  = orderRepository.findConfirmedOrdersFor(offer.getId());
		
		List<OrderLine> paidOrderLinesForOffer = paidOrderLinesForOffer(confirmedOrders, offerReference);
		List<OrderLine> unpaidOrderLinesForOffer = unpaidOrderLinesForOffer(confirmedOrders, offerReference);
		List<OrderLine> partiallyPaidOrderLinesForOffer = partiallyPaidOrderLinesForOffer(confirmedOrders, offerReference);

		long confirmedVolume = volume(confirmedOrderLinesForOffer);
		reportBuilder.confirmedVolume(confirmedVolume);

		long paidVolume = volume(paidOrderLinesForOffer);
		reportBuilder.paidVolume(paidVolume);

		long unpaidVolume = volume(unpaidOrderLinesForOffer);
		reportBuilder.unpaidVolume(unpaidVolume);

		long partiallyPaidVolume = volume(partiallyPaidOrderLinesForOffer);
		reportBuilder.partiallyPaidVolume(partiallyPaidVolume);

		PriceBand currentPriceband = offer.getPriceBands().getPriceBandForVolume(confirmedVolume);
		reportBuilder.currentPriceBand(currentPriceband);

		BigDecimal targetPrice = offer.getPriceBands().first().getBuyerPrice();
		for (PriceBand priceband : offer.getPriceBands()) {
			if (priceband.getBuyerPrice().compareTo(targetPrice) < 0) {
				targetPrice = priceband.getBuyerPrice();
			}
		}

		addBuyerCreditInformation(reportBuilder, paidOrderLinesForOffer);
		addBuyerPaymentInformation(reportBuilder, paidOrderLinesForOffer);

		List<AdminPaymentDetailsOfOrder> allRouteDetails = new ArrayList<>();
		for (Order order : orders) {
			if (order.getStatus().compareTo(OrderStatus.CONFIRMED) == 0
					&& order.getPaymentStatus().compareTo(PaymentStatus.PAID) == 0) {
				int quantites = 0;
				BigDecimal pricePaid = BigDecimal.ZERO;
				BigDecimal minimumPrice = BigDecimal.ZERO;
				BigDecimal lapwingCreditsUsed = BigDecimal.ZERO;
				BigDecimal cardPaymentUsed = BigDecimal.ZERO;
				BigDecimal supplierCreditsUsed = BigDecimal.ZERO;
				BigDecimal totalDiscountMonies = BigDecimal.ZERO;
				BigDecimal lapwingMargin = BigDecimal.ZERO;
				BigDecimal balanceToBeTransferredToSupplier= BigDecimal.ZERO;
				BigDecimal balanceToBePaidToLapwingexpected= BigDecimal.ZERO;
				BigDecimal balanceToBePaidToLapwingactual= BigDecimal.ZERO;
				BigDecimal silverwingMarginRetainedBySilverwing= BigDecimal.ZERO;
				BigDecimal silverwingMarginPayableToSilverwing= BigDecimal.ZERO;
				BigDecimal netMoneyExchangeSilverwingToSupplier= BigDecimal.ZERO;
				BigDecimal netMoneyExchangeSupplierToSilverwing= BigDecimal.ZERO;

				
				for (OrderLine line : order.getLines()) {
					if (line.getOffer().getId().equals(offer.getId())) {
						quantites += line.getQuantity();
					}
				}
				minimumPrice = offer.getCurrentPriceBand().getBuyerPrice().multiply(new BigDecimal(quantites));
				for (OrderPayment payment : order.getPayments()) {
					if (payment.getPaymentRecordType().compareTo(PaymentRecordType.ORDER_CHECKOUT_PAYMENT) == 0) {
						cardPaymentUsed = cardPaymentUsed.add(payment.getAmount());
					}
				}
				for(BuyerAppliedCredit appliedCredit : order.getAppliedCredits())
				{
					if(appliedCredit.getOrderSpentOnReference().equals(order.getReference())){
						if(appliedCredit.getSpendType().compareTo(CreditMovementType.V_SPEND)==0)
						{
							supplierCreditsUsed = supplierCreditsUsed.add(appliedCredit.getAmount().gross());
						}
						else if(appliedCredit.getSpendType().compareTo(CreditMovementType.SPEND)==0)
						{
							lapwingCreditsUsed = lapwingCreditsUsed.add(appliedCredit.getAmount().gross());
						}
					}
				}
				pricePaid = cardPaymentUsed.add(supplierCreditsUsed).add(lapwingCreditsUsed);
				totalDiscountMonies = pricePaid.subtract(minimumPrice);
				lapwingMargin = offer.getCurrentPriceBand().getShoalMargin().multiply(new BigDecimal(quantites));

				BigDecimal upfront = lapwingCreditsUsed.add(cardPaymentUsed);
				if(upfront.compareTo(totalDiscountMonies) > 0)
				{
					balanceToBeTransferredToSupplier = upfront.subtract(totalDiscountMonies);
				}
				else
				{
					balanceToBePaidToLapwingexpected = totalDiscountMonies.subtract(upfront);
				}
				if(upfront.compareTo(totalDiscountMonies) > 0)
				{
					if((upfront.subtract(totalDiscountMonies)).compareTo(lapwingMargin) > 0)
					{
						silverwingMarginRetainedBySilverwing = lapwingMargin;
					}
					else
					{
						silverwingMarginRetainedBySilverwing = upfront.subtract(totalDiscountMonies);
					}
				}
				silverwingMarginPayableToSilverwing = lapwingMargin.subtract(silverwingMarginRetainedBySilverwing);
				netMoneyExchangeSilverwingToSupplier = balanceToBeTransferredToSupplier.subtract(silverwingMarginRetainedBySilverwing);
				netMoneyExchangeSupplierToSilverwing = balanceToBePaidToLapwingactual.add(silverwingMarginPayableToSilverwing);
				
				AdminPaymentDetailsOfOrder adminRouteOneAndTwoPayment = AdminPaymentDetailsOfOrder
						.anAdminRouteOneAndTwoPayment().time(order.getCreated())
						.orderReference(order.getReference().asString())
						.totalPricePaid(pricePaid)
						.minimumPrice(minimumPrice)
						.lapwingCreditsUsed(lapwingCreditsUsed)
						.cardPaymentUsed(cardPaymentUsed)
						.supplierCreditsUsed(supplierCreditsUsed)
						.totalDiscountMonies(totalDiscountMonies)
						.silverwingMargin(lapwingMargin)
						.balanceToBeTransferredToSupplier(balanceToBeTransferredToSupplier)
						.balanceToBePaidToLapwingexpected(balanceToBePaidToLapwingexpected)
						.balanceToBePaidToLapwingactual(balanceToBePaidToLapwingactual)
						.silverwingMarginRetainedBySilverwing(silverwingMarginRetainedBySilverwing)
						.silverwingMarginPayableToSilverwing(silverwingMarginPayableToSilverwing)
						.netMoneyExchangeSilverwingToSupplier(netMoneyExchangeSilverwingToSupplier)
						.netMoneyExchangeSupplierToSilverwing(netMoneyExchangeSupplierToSilverwing)
						.build();
				allRouteDetails.add(adminRouteOneAndTwoPayment);
			}
		}

		reportBuilder.allRouteDetails(allRouteDetails);

		DateTime offerEndDate = offer.getEndDateTime();
		DateTime currentDate = offer.getStartDateTime();
		List<OutstandingBalanceOfADay> OutstandingBalanceBetweenLapwingAndSupplier = new ArrayList<>();

		while (currentDate.isBefore(offerEndDate)) {
			BigDecimal balanceToBeTransferredToSupplier = BigDecimal.ZERO;
			BigDecimal balanceToBePaidToLapwing = BigDecimal.ZERO;
			OutstandingBalanceOfADay.Builder outstandingBalanceOfADay = OutstandingBalanceOfADay
					.anOutstandingBalanceOfADay().date(currentDate);
			for (AdminPaymentDetailsOfOrder payment : allRouteDetails) {
				Date paymentDate = getZeroTimeDate(payment.getTime().toDate());
				Date currentUtilDate = getZeroTimeDate(currentDate.toDate());
				if (paymentDate.equals(currentUtilDate)) {
					balanceToBeTransferredToSupplier = balanceToBeTransferredToSupplier
							.add(payment.getNetMoneyExchangeSilverwingToSupplier());
					balanceToBePaidToLapwing = balanceToBePaidToLapwing.add(payment.getNetMoneyExchangeSupplierToSilverwing());
				}
			}
			outstandingBalanceOfADay.balanceToBePaidToLapwing(balanceToBePaidToLapwing)
					.balanceToBeTransferredToSupplier(balanceToBeTransferredToSupplier);

			OutstandingBalanceBetweenLapwingAndSupplier.add(outstandingBalanceOfADay.build());
			currentDate = currentDate.plusDays(1);
		}
		reportBuilder.outstandingBalanceOfADay(OutstandingBalanceBetweenLapwingAndSupplier);

		List<SupplierPaymentForAnOrder> supplierPayments = supplierPaymentService.fetchSupplierPayments(offerReference);
		reportBuilder.supplierPayments(supplierPayments);
		return reportBuilder.build();
	}

	private static Date getZeroTimeDate(Date fecha) {
		Date res = fecha;
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(fecha);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		res = calendar.getTime();
		return res;
	}

	private BigDecimal calculateOutstandingBalanceToSupplier(BigDecimal supplierBalance, BigDecimal lapwingBalance) {
		if (supplierBalance.compareTo(lapwingBalance) > 0) {
			return supplierBalance.subtract(lapwingBalance);
		}
		return BigDecimal.ZERO;
	}

	private BigDecimal calculateOutstandingBalanceToLapwing(BigDecimal supplierBalance, BigDecimal lapwingBalance) {
		if (lapwingBalance.compareTo(supplierBalance) > 0) {
			return lapwingBalance.subtract(supplierBalance);
		}
		return BigDecimal.ZERO;
	}

	private BigDecimal calculateSupplierKeeps(BigDecimal targetPrice, List<RoutePaymentDetails> paymentDetails,
			Product product, PriceBands pricebands) {
		BigDecimal targetPriceIncVat = (BigDecimal.valueOf(100).add(product.getVatRates().current().getRate()))
				.multiply(targetPrice).divide(BigDecimal.valueOf(100));
		BigDecimal totalPaidQuantities = BigDecimal.ZERO;
		for (RoutePaymentDetails paymentDetail : paymentDetails) {
			totalPaidQuantities = totalPaidQuantities.add(BigDecimal.valueOf(paymentDetail.getPaidQuantities()));
		}
		BigDecimal totalLapwingMargin = BigDecimal.ZERO;
		for (RoutePaymentDetails paymentDetail : paymentDetails) {
			int quantites = paymentDetail.getPaidQuantities();
			for (PriceBand priceband : pricebands) {
				if (priceband.getBuyerPrice().compareTo(paymentDetail.getUnitPrice()) == 0) {
					totalLapwingMargin = totalLapwingMargin
							.add(priceband.getShoalMargin().multiply(new BigDecimal(quantites)));
				}
			}
		}

		BigDecimal avgLapwingMargin = BigDecimal.ZERO;
		if(totalPaidQuantities.compareTo(BigDecimal.ZERO) != 0 && totalLapwingMargin.compareTo(BigDecimal.ZERO) != 0)
		{
			avgLapwingMargin = totalLapwingMargin.divide(totalPaidQuantities);
		}
		return (targetPriceIncVat.subtract(avgLapwingMargin)).multiply(totalPaidQuantities);
	}

	private BigDecimal calculateSupplierBalance(BigDecimal targetPrice, List<RoutePaymentDetails> paymentDetails,
			Product product, BigDecimal lapwingMargin) {

		BigDecimal targetPriceIncVat = (BigDecimal.valueOf(100).add(product.getVatRates().current().getRate()))
				.multiply(targetPrice).divide(BigDecimal.valueOf(100));
		BigDecimal totalPaidQuantities = BigDecimal.ZERO;
		for (RoutePaymentDetails paymentDetail : paymentDetails) {
			totalPaidQuantities = totalPaidQuantities.add(BigDecimal.valueOf(paymentDetail.getPaidQuantities()));
		}
		return (targetPriceIncVat.subtract(lapwingMargin)).multiply(totalPaidQuantities);
	}

	private BigDecimal calculateLapwingMargin(List<RoutePaymentDetails> paymentDetails, PriceBand currentPriceBand) {
		BigDecimal totalLapwingMargin = BigDecimal.ZERO;
		for (RoutePaymentDetails paymentDetail : paymentDetails) {
			totalLapwingMargin = totalLapwingMargin
					.add(currentPriceBand.getShoalMargin().multiply(new BigDecimal(paymentDetail.getPaidQuantities())));
		}
		return totalLapwingMargin;
	}

	private BigDecimal calculateDiscountMoniedIncVat(List<RoutePaymentDetails> paymentDetails) {
		BigDecimal totalDiscountMoniedIncVat = BigDecimal.ZERO;
		for (RoutePaymentDetails paymentDetail : paymentDetails) {
			totalDiscountMoniedIncVat = totalDiscountMoniedIncVat.add(paymentDetail.getDiscountMoniesPerOrder()
					.multiply(new BigDecimal(paymentDetail.getPaidQuantities())));
		}
		return totalDiscountMoniedIncVat;
	}

	private TaxableAmount calculateLapwingCreditsUsed(List<Order> orders, Product product) {
		BigDecimal grossLapwingCreditsUsed = BigDecimal.ZERO;
		for (Order order : orders) {
			if (order.getStatus().compareTo(OrderStatus.CONFIRMED) == 0) {
				for (BuyerAppliedCredit credit : order.getAppliedCredits()) {
					if (credit.getSpendType().compareTo(CreditMovementType.SPEND) == 0) {
						grossLapwingCreditsUsed = grossLapwingCreditsUsed.add(credit.getAmount().getGross());
					}
				}
			}
		}
		grossLapwingCreditsUsed = grossLapwingCreditsUsed.setScale(0, RoundingMode.HALF_UP);
		BigDecimal netLapwingCreditsUsed = vatCalculator
				.calculateUnroundedNetFromGross(grossLapwingCreditsUsed, product.getVatRates().current().getRate())
				.setScale(0, RoundingMode.HALF_UP);
		BigDecimal vatReceived = grossLapwingCreditsUsed.subtract(netLapwingCreditsUsed).setScale(0,
				RoundingMode.HALF_UP);
		return TaxableAmount.fromNetAndVat(netLapwingCreditsUsed, vatReceived);
	}

	private TaxableAmount calculateLapwingCreditsEarned(List<Order> orders, Product product) {
		BigDecimal grossLapwingCreditsEarned = BigDecimal.ZERO;
		for (Order order : orders) {
			if (order.getStatus().compareTo(OrderStatus.CONFIRMED) == 0) {
				for (OrderLine line : order.getLines()) {
					if (line.getProduct().getCode().equals(product.getCode())) {
						grossLapwingCreditsEarned = grossLapwingCreditsEarned.add(line.getCredits().getTotal().gross());
					}
				}

			}
		}
		grossLapwingCreditsEarned = grossLapwingCreditsEarned.setScale(0, RoundingMode.HALF_UP);
		BigDecimal netLapwingCreditsUsed = vatCalculator
				.calculateUnroundedNetFromGross(grossLapwingCreditsEarned, product.getVatRates().current().getRate())
				.setScale(0, RoundingMode.HALF_UP);
		BigDecimal vatReceived = grossLapwingCreditsEarned.subtract(netLapwingCreditsUsed).setScale(0,
				RoundingMode.HALF_UP);
		return TaxableAmount.fromNetAndVat(netLapwingCreditsUsed, vatReceived);
	}

	private TaxableAmount calculateAmountReceivedForARoute(List<Order> orders, Product product) {
		BigDecimal grossAmountReceived = BigDecimal.ZERO;
		for (Order order : orders) {
			if (order.getStatus().compareTo(OrderStatus.CONFIRMED) == 0) {
				grossAmountReceived = grossAmountReceived.add(order.getAmountPaid());
			}
		}
		grossAmountReceived = grossAmountReceived.setScale(0, RoundingMode.HALF_UP);
		BigDecimal netAmountReceived = vatCalculator
				.calculateUnroundedNetFromGross(grossAmountReceived, product.getVatRates().current().getRate())
				.setScale(0, RoundingMode.HALF_UP);
		BigDecimal vatReceived = grossAmountReceived.subtract(netAmountReceived).setScale(0, RoundingMode.HALF_UP);
		return TaxableAmount.fromNetAndVat(netAmountReceived, vatReceived);
	}

	private TaxableAmount calculateAmountPendingForARoute(List<Order> orders, Product product) {
		BigDecimal grossAmountReceived = BigDecimal.ZERO;
		for (Order order : orders) {
			if (order.getStatus().compareTo(OrderStatus.CONFIRMED) == 0) {
				grossAmountReceived = grossAmountReceived.add(order.getUnpaidAmount());
			}
		}
		grossAmountReceived = grossAmountReceived.setScale(0, RoundingMode.HALF_UP);
		BigDecimal netAmountReceived = vatCalculator
				.calculateUnroundedNetFromGross(grossAmountReceived, product.getVatRates().current().getRate())
				.setScale(0, RoundingMode.HALF_UP);
		BigDecimal vatReceived = grossAmountReceived.subtract(netAmountReceived).setScale(0, RoundingMode.HALF_UP);
		return TaxableAmount.fromNetAndVat(netAmountReceived, vatReceived);
	}

	private List<RoutePaymentDetails> calculatePaymentsForARoute(Offer offer, List<Order> orders, Product product) {
		List<RoutePaymentDetails> result = new ArrayList<>();
		PriceBands pricebands = offer.getPriceBands();

		for (PriceBand priceBand : pricebands) {
			long quantitiesConfirmed = 0;
			int numberOfPaidOrders = 0;
			int numberOfUnpaidOrders = 0;
			int numberOfPartpaidOrders = 0;
			int paidQuantities = 0;
			int unpaiQuantities = 0;
			int partpaidQuantities = 0;
			int orderPlaced = 0;

			for (Order order : orders) {
				for (OrderLine orderLine : order.getLines()) {
					if (orderLine.getInitialPriceBand().getId().equals(priceBand.getId())) {
						if (order.getStatus().compareTo(OrderStatus.CONFIRMED) == 0) {
							quantitiesConfirmed += orderLine.getQuantity();
							orderPlaced++;
						}
						if (order.getPaymentStatus().compareTo(PaymentStatus.PAID) == 0
								&& order.getStatus().compareTo(OrderStatus.CONFIRMED) == 0) {
							numberOfPaidOrders++;
							paidQuantities += orderLine.getQuantity();
						}
						if (order.getPaymentStatus().compareTo(PaymentStatus.UNPAID) == 0
								&& order.getStatus().compareTo(OrderStatus.CONFIRMED) == 0) {
							numberOfUnpaidOrders++;
							unpaiQuantities += orderLine.getQuantity();
						}
						if (order.getPaymentStatus().compareTo(PaymentStatus.PART_PAID) == 0
								&& order.getStatus().compareTo(OrderStatus.CONFIRMED) == 0) {
							numberOfPartpaidOrders++;
							partpaidQuantities += orderLine.getQuantity();
						}
					}
				}
			}

			BigDecimal discountMoniesPerOrder = BigDecimal.ZERO;
			BigDecimal difference = priceBand.getBuyerPrice().subtract(pricebands.last().getBuyerPrice());
			discountMoniesPerOrder = difference
					.add(difference.multiply(product.getVatRates().current().getRate()).divide(new BigDecimal(100)));

			RoutePaymentDetails offerPaymentForPriceBand = RoutePaymentDetails.aRoutePaymentDetails()
					.unitPrice(priceBand.getBuyerPrice()).quantitiesConfirmed(quantitiesConfirmed)
					.numberOfPaymentsReceived(numberOfPaidOrders).numberOfPaymentsPending(numberOfUnpaidOrders)
					.numberOfPartialPayments(numberOfPartpaidOrders).discountMoniesPerOrder(discountMoniesPerOrder)
					.paidQuantities(paidQuantities).unpaiQuantities(unpaiQuantities).numberOfOrderPlaced(orderPlaced)
					.partpaidQuantities(partpaidQuantities).build();
			result.add(offerPaymentForPriceBand);
		}
		return result;
	}

	private TaxableAmount taxableAmount(BigDecimal net, VatRate vatRate) {
		return TaxableAmount.fromNetAndVat(net, vatCalculator.calculateVatAmount(net, vatRate));
	}

	private void addBuyerCreditInformation(OfferReport.Builder reportBuilder, List<OrderLine> orderLines) {
		BigDecimal buyerCreditsEarned = buyerCreditsEarned(orderLines);
		BigDecimal buyerCreditsConsumed = buyerCreditsConsumed(orderLines);

		reportBuilder.buyerCreditsEarned(buyerCreditsEarned);
		reportBuilder.buyerCreditsSpent(buyerCreditsConsumed);
	}

	private void addBuyerPaymentInformation(OfferReport.Builder reportBuilder, List<OrderLine> orderLines) {
		BigDecimal fullPaymentsReceived = fullPaymentsReceived(orderLines);

		reportBuilder.buyerPaymentsReceived(fullPaymentsReceived);
	}

	private long volume(List<OrderLine> orderLines) {
		return orderLines.stream().mapToLong(line -> line.getQuantity()).sum();
	}

	private List<OrderLine> confirmedOrderLinesForOffer(Orders orders, OfferReference offerReference) {
		return orderLinesForOffer(confirmedOrders(orders), offerReference).collect(Collectors.toList());
	}

	private List<OrderLine> paidOrderLinesForOffer(Orders orders, OfferReference offerReference) {
		return orderLinesForOffer(paidOrders(orders), offerReference).collect(Collectors.toList());
	}

	private List<OrderLine> unpaidOrderLinesForOffer(Orders orders, OfferReference offerReference) {
		return orderLinesForOffer(unpaidOrders(orders), offerReference).collect(Collectors.toList());
	}

	private List<OrderLine> partiallyPaidOrderLinesForOffer(Orders orders, OfferReference offerReference) {
		return orderLinesForOffer(partiallyPaidOrders(orders), offerReference).collect(Collectors.toList());
	}

	private Stream<OrderLine> orderLinesForOffer(Stream<Order> orders, OfferReference offerReference) {
		return orders.flatMap(order -> order.getLines().stream())
				.filter(line -> line.getOffer().getOfferReference().equals(offerReference));
	}

	private Stream<Order> confirmedOrders(Orders orders) {
		return orders.stream().filter(order -> order.getStatus() == OrderStatus.CONFIRMED);
	}

	private Stream<Order> unpaidOrders(Orders orders) {
		return orders.stream().filter(order -> order.getPaymentStatus() == PaymentStatus.UNPAID);
	}

	private Stream<Order> partiallyPaidOrders(Orders orders) {
		return orders.stream().filter(order -> order.getPaymentStatus() == PaymentStatus.PART_PAID);
	}

	private Stream<Order> paidOrders(Orders orders) {
		return orders.stream().filter(order -> order.getPaymentStatus() == PaymentStatus.PAID);
	}

	private BigDecimal fullPaymentsReceived(List<OrderLine> orderLines) {
		BigDecimal paidAmount = BigDecimal.ZERO;

		for (OrderLine line : orderLines) {
			paidAmount = paidAmount.add(line.getAmount().gross());
		}

		return paidAmount;
	}

	private BigDecimal buyerCreditsEarned(List<OrderLine> orderLines) {
		TaxableAmount totalEarned = TaxableAmount.ZERO;

		for (OrderLine line : orderLines) {
			totalEarned = totalEarned.add(line.getCreditTotal());
		}

		return totalEarned.gross();
	}

	private BigDecimal buyerCreditsConsumed(List<OrderLine> orderLines) {
		TaxableAmount totalConsumed = TaxableAmount.ZERO;

		for (OrderLine line : orderLines) {
			totalConsumed = totalConsumed.add(line.getConsumedCreditTotal());
		}

		return totalConsumed.gross();
	}

	private BigDecimal multiply(BigDecimal value, long multiplicand) {
		return value.multiply(BigDecimal.valueOf(multiplicand));
	}
}
