package com.ishoal.core.orders;

import static com.ishoal.common.util.IterableUtils.stream;
import static com.ishoal.core.buyer.FetchBuyerProfileRequest.aFetchBuyerProfileRequest;
import static com.ishoal.core.domain.Order.anOrder;
import static com.ishoal.core.domain.OrderLine.anOrderLine;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static com.ishoal.core.domain.comparators.OrderLineComparators.byOfferEndDate;
import static com.ishoal.core.domain.comparators.OrderLineComparators.byPartConsumedCredit;
import static java.util.Comparator.comparing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.SimpleResult;
import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.buyer.ManageBuyerProfileService;
import com.ishoal.core.domain.BuyerAppliedCredit;
import com.ishoal.core.domain.BuyerAppliedCreditStatus;
import com.ishoal.core.domain.BuyerAppliedCredits;
import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.CreditMovementType;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.OrderPayment;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.PaymentMethod;
import com.ishoal.core.domain.PaymentRecordType;
import com.ishoal.core.domain.PaymentStatus;
import com.ishoal.core.domain.PaymentType;
import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.TaxableAmount;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.VatRate;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.payment.PaymentGatewayService;
import com.ishoal.core.products.ProductService;
import com.ishoal.core.repository.BuyerAppliedCreditRepository;
import com.ishoal.core.repository.BuyerVendorCreditRepository;
import com.ishoal.core.repository.OrderRepository;
import com.ishoal.payment.buyer.BuyerChargeResponse;
import com.ishoal.payment.buyer.BuyerPaymentService;

public class OrderCreationService {
	private static final Logger logger = LoggerFactory.getLogger(OrderCreationService.class);

	private final OrderRepository orderRepository;
	private final VatCalculator vatCalculator;
	private final ProductService productService;
	private final ManageBuyerProfileService manageBuyerProfileService;
	private final BuyerPaymentService buyerPaymentService;
	private final BuyerAppliedCreditRepository buyerAppliedCreditRepository;
	private final BuyerVendorCreditRepository buyerVendorCreditRepository;
	private final PaymentGatewayService paymentGatewayService;

	public OrderCreationService(ProductService productService, OrderRepository orderRepository,
			VatCalculator vatCalculator, ManageBuyerProfileService manageBuyerProfileService,
			BuyerPaymentService buyerPaymentService, BuyerAppliedCreditRepository buyerAppliedCreditRepository,
			BuyerVendorCreditRepository buyerVendorCreditRepository, PaymentGatewayService paymentGatewayService) {

		this.productService = productService;
		this.orderRepository = orderRepository;
		this.vatCalculator = vatCalculator;
		this.manageBuyerProfileService = manageBuyerProfileService;
		this.buyerPaymentService = buyerPaymentService;
		this.buyerAppliedCreditRepository = buyerAppliedCreditRepository;
		this.buyerVendorCreditRepository = buyerVendorCreditRepository;
		this.paymentGatewayService = paymentGatewayService;
	}

	@Transactional
	public PayloadResult<Order> create(NewOrderRequest newOrderRequest, User user) {

		BigDecimal totalVendorCredits = BigDecimal.ZERO;
		for (AppliedVendorCredit vendorCredit : newOrderRequest.getAppliedVendorCredits()) {
			totalVendorCredits = totalVendorCredits.add(vendorCredit.getCreditsApplied());
		}
		PayloadResult<Order> orderResult = toFullOrder(newOrderRequest, totalVendorCredits);
		Result result = orderResult;
		Order order = null;

		if (result.isSuccess()) {
			order = orderResult.getPayload();
			Vendor vendor = resolveVendor(order.getLines().list()).getPayload();
			result = applyCreditSpend(order, newOrderRequest.getCreditToBeApplied());
			List<BuyerAppliedCredit> VendorCreditsResult = applyVendorCredits(newOrderRequest.getCreditToBeApplied(),
					totalVendorCredits, order);
			buyerAppliedCreditRepository.saveBuyerVendorCredit(BuyerAppliedCredits.over(VendorCreditsResult), user,
					vendor);
			if (totalVendorCredits.compareTo(BigDecimal.ZERO) > 0) {
				buyerVendorCreditRepository.deductAvailableCredits(totalVendorCredits, user, vendor);
			}
		}

		if (result.isSuccess()) {
			result = handleOrderPayment(order, newOrderRequest);
		}

		if (result.isSuccess()) {
			this.orderRepository.create(order);

			return PayloadResult.success(order);
		}
		return PayloadResult.error(result.getError());
	}

	private Result handleOrderPayment(Order order, NewOrderRequest newOrderRequest) {

		if (newOrderRequest.getPaymentMethod() == PaymentMethod.CARD_PAYMENT) {
			BigDecimal paymentAmount = order.getUnpaidAmount().subtract(newOrderRequest.getCreditToBeApplied())
					.subtract(newOrderRequest.calculateAppliedVendorCredits());

			BigDecimal chargePercent = paymentGatewayService.fetchPaymentCharges().getPaymentChargesPercentage();
			BigDecimal extraCharge = paymentGatewayService.fetchPaymentCharges().getPaymentExtraCharge();
			BigDecimal paymentGatewayCharge = paymentAmount.multiply(chargePercent).divide(new BigDecimal(100));
			BigDecimal totalPaymentCharge = paymentGatewayCharge.add(extraCharge);

			BigDecimal finalPaymentAmount = paymentAmount.add(totalPaymentCharge);

			PayloadResult<BuyerChargeResponse> chargeResult = buyerPaymentService
					.createCharge(newOrderRequest.getPaymentCardToken(), order.getReference(), finalPaymentAmount);

			if (chargeResult.isSuccess()) {
				BuyerChargeResponse chargeResponse = chargeResult.getPayload();
				OrderPayment payment = OrderPayment.anOrderPayment().paymentType(PaymentType.CARD_PAYMENT)
						.userReference(chargeResponse.getChargeReference().asString()).amount(paymentAmount)
						.dateReceived(chargeResponse.getCreated()).paymentGatewayCharges(totalPaymentCharge)
						.paymentRecordType(PaymentRecordType.ORDER_CHECKOUT_PAYMENT).build();

				order.addPayment(payment);
			}
			return chargeResult;
		}

		return SimpleResult.success();
	}

	private Result applyCreditSpend(Order order, BigDecimal creditToBeApplied) {

		Result result = validateCreditAmountToApply(order, creditToBeApplied);
		if (result.isSuccess() && creditToBeApplied.signum() > 0) {
			result = applyCredits(order, creditToBeApplied);
		}
		return result;
	}

	private Result validateCreditAmountToApply(Order order, BigDecimal creditToBeApplied) {

		if (creditToBeApplied.compareTo(order.getTotal().gross()) > 0) {
			return SimpleResult.error("Cannot apply credit spend for a greater amount than the gross order total");
		}
		return SimpleResult.success();
	}

	private PayloadResult<Order> toFullOrder(NewOrderRequest newOrderRequest, BigDecimal totalVendorCredits) {

		List<OrderLine> lines = adapt(newOrderRequest.getLines());

		PayloadResult<Vendor> vendorResult = resolveVendor(lines);
		if (vendorResult.isSuccess()) {
			PayloadResult<BuyerProfile> profileResult = manageBuyerProfileService
					.fetchProfile(aFetchBuyerProfileRequest().user(newOrderRequest.getBuyer()).build());
			if (profileResult.isError()) {
				return PayloadResult.error(profileResult.getError());
			}
			Organisation organisation = profileResult.getPayload().getOrganisation();

			PaymentStatus paymentstatus;
			if (totalVendorCredits.compareTo(BigDecimal.ZERO) > 0
					&& (newOrderRequest.getCreditToBeApplied().compareTo(BigDecimal.ZERO) > 0
							|| newOrderRequest.getPaymentMethod().compareTo(PaymentMethod.CARD_PAYMENT) == 0)) {
				paymentstatus = PaymentStatus.PART_PAID;
			} else if (totalVendorCredits.compareTo(BigDecimal.ZERO) <= 0) {
				paymentstatus = PaymentStatus.PAID;
			} else {
				paymentstatus = PaymentStatus.UNPAID;
			}

			OrderStatus orderStatus;
			orderStatus = OrderStatus.PROCESSING;

			Order order = anOrder().buyer(newOrderRequest.getBuyer()).buyerOrganisation(organisation)
					.status(orderStatus).vendor(vendorResult.getPayload())
					.invoiceAddress(newOrderRequest.getInvoiceAddress())
					.deliveryAddress(newOrderRequest.getDeliveryAddress()).lines(lines).paymentStatus(paymentstatus)
					.build();

			return PayloadResult.success(order);
		}
		return PayloadResult.error(vendorResult.getError());
	}

	private PayloadResult<Vendor> resolveVendor(List<OrderLine> lines) {

		Set<Vendor> vendors = new HashSet<>();

		lines.forEach(line -> vendors.add(line.getProduct().getVendor()));

		if (vendors.size() != 1) {
			return PayloadResult.error("Must be exactly one vendor per order");
		}
		return PayloadResult.success(vendors.iterator().next());
	}

	private List<OrderLine> adapt(NewOrderRequestOrderLines orderLines) {

		return IterableUtils.mapToList(orderLines, this::adapt);
	}

	private OrderLine adapt(NewOrderRequestOrderLine newOrderLine) {

		Product product = product(newOrderLine);
		Offer offer = product.currentOffer();
		long quantity = newOrderLine.getQuantity();
		BigDecimal unitPrice = newOrderLine.getUnitPrice();

		PriceBand priceBand = offer.getPriceBandForBuyerPrice(unitPrice);
		BigDecimal netAmount = calculateNetAmount(priceBand, quantity);
		VatRate vatRate = product.currentVatRate();
		BigDecimal vatAmount = calculateVatAmount(netAmount, vatRate);

		return anOrderLine().product(product).offer(offer).initialPriceBand(priceBand).currentPriceBand(priceBand)
				.quantity(quantity).amount(fromNetAndVat(netAmount, vatAmount)).vatRate(vatRate.getRate()).build();
	}

	private BigDecimal calculateVatAmount(BigDecimal netAmount, VatRate vatRate) {

		return vatCalculator.calculateVatAmount(netAmount, vatRate);
	}

	private BigDecimal calculateNetAmount(PriceBand priceBand, long quantity) {

		return priceBand.getBuyerPrice().multiply(BigDecimal.valueOf(quantity));
	}

	private Product product(NewOrderRequestOrderLine unconfirmedLine) {

		return this.productService.getProduct(unconfirmedLine.getProductCode());
	}

	private Result applyCredits(Order order, BigDecimal creditToBeApplied) {

		List<Pair<OrderLine, Order>> linesWithCredit = findOrderLinesWithAvailableCredit(order.getBuyer());

		Set<Order> ordersAffected = new HashSet<>();

		BigDecimal creditRemainingToBeApplied = creditToBeApplied;

		Iterator<Pair<OrderLine, Order>> lineIterator = linesWithCredit.iterator();
		while (creditRemainingToBeApplied.signum() > 0 && lineIterator.hasNext()) {
			Pair<OrderLine, Order> line = lineIterator.next();

			creditRemainingToBeApplied = applyCredit(creditRemainingToBeApplied, line, order);
			ordersAffected.add(line.getRight());
		}

		Result result;
		if (creditRemainingToBeApplied.signum() == 0) {
			logger.info("Successfully applied credit spend of [{}] to order with reference [{}]", creditToBeApplied,
					order.getReference());

			orderRepository.save(Orders.over(ordersAffected.toArray(new Order[ordersAffected.size()])));
			result = SimpleResult.success();
		} else {
			logger.warn("Did not find enough available credit to apply spend of [{}] to order with reference [{}]",
					creditToBeApplied, order.getReference());

			result = SimpleResult.error("Did not find enough available credit to match the amount to be applied");
		}
		return result;
	}

	private BigDecimal applyCredit(BigDecimal creditToBeApplied, Pair<OrderLine, Order> originalLineAndOrder,
			Order order) {

		BigDecimal creditRemainingToBeApplied = creditToBeApplied;

		OrderLine orderLineWithCredits = originalLineAndOrder.getLeft();
		Order originalOrder = originalLineAndOrder.getRight();

		Iterator<BuyerCredit> creditIterator = orderLineWithCredits.getCredits().iterator();
		while (creditRemainingToBeApplied.signum() > 0 && creditIterator.hasNext()) {
			BuyerCredit credit = creditIterator.next();
			BigDecimal creditGrossRemainingBalance = credit.getRemainingBalance().gross();
			if (creditGrossRemainingBalance.signum() > 0) {
				BigDecimal newAppliedCreditGross;
				if (creditGrossRemainingBalance.compareTo(creditRemainingToBeApplied) > 0) {
					newAppliedCreditGross = creditRemainingToBeApplied;
				} else {
					newAppliedCreditGross = creditGrossRemainingBalance;
				}

				BigDecimal alreadyAppliedGross = credit.getAppliedCredits().getTotal().gross();
				BigDecimal alreadyAppliedNet = credit.getAppliedCredits().getTotal().net();
				BigDecimal cumulativeRemainder = vatCalculator.calculateRemainderOnNetFromGross(alreadyAppliedGross,
						alreadyAppliedNet, orderLineWithCredits.getVatRate());

				BigDecimal newAppliedCreditNet = vatCalculator.calculateNetFromGross(newAppliedCreditGross,
						orderLineWithCredits.getVatRate(), cumulativeRemainder);
				BigDecimal newAppliedCreditVat = newAppliedCreditGross.subtract(newAppliedCreditNet);

				BuyerAppliedCredit newAppliedCredit = BuyerAppliedCredit.aBuyerAppliedCredit()
						.amount(fromNetAndVat(newAppliedCreditNet, newAppliedCreditVat))
						.originalOrderReference(originalOrder.getReference())
						.originalOrderLineProductCode(orderLineWithCredits.getProduct().getCode())
						.orderSpentOnReference(order.getReference()).spendType(CreditMovementType.SPEND)
						.status(BuyerAppliedCreditStatus.RESERVED).build();

				credit.consumeCredit(newAppliedCredit);

				creditRemainingToBeApplied = creditRemainingToBeApplied.subtract(newAppliedCreditGross);

				logger.info(
						"Applied credit spend of net [{}], VAT [{}], gross [{}] on order with reference [{}]. Remaining "
								+ "gross credit to be applied [{}]",
						newAppliedCreditNet, newAppliedCreditVat, newAppliedCreditGross, order.getReference(),
						creditRemainingToBeApplied);
			}
		}
		return creditRemainingToBeApplied;
	}

	private List<BuyerAppliedCredit> applyVendorCredits(BigDecimal creditToBeApplied,
			BigDecimal vendorCreditToBeApplied, Order order) {
		List<BuyerAppliedCredit> buyerVendorCreditsToBeAdded = new ArrayList<>();

		BigDecimal remainedLapwingCredits = creditToBeApplied;
		BigDecimal remainedVendorCredits = vendorCreditToBeApplied;

		List<OrderLine> lines = new ArrayList<>();
		for (OrderLine line : order.getLines().list()) {
			lines.add(line);
		}
		for (OrderLine line : order.getLines()) {
			BigDecimal productNet = line.getCurrentPriceBand().getBuyerPrice()
					.multiply(new BigDecimal(line.getQuantity()));
			BigDecimal productGross = TaxableAmount
					.fromNetAndVat(productNet, line.getVatRate().multiply(productNet).divide(new BigDecimal(100)))
					.gross();

			if (remainedLapwingCredits.compareTo(BigDecimal.ZERO) > 0
					&& productGross.compareTo(remainedLapwingCredits) < 0) {
				remainedLapwingCredits = remainedLapwingCredits.subtract(productGross);
				lines.remove(line);
			} else if (remainedLapwingCredits.compareTo(BigDecimal.ZERO) > 0) {

				BigDecimal gross = BigDecimal.ZERO;
				if (productGross.compareTo(remainedVendorCredits) > 0) {
					gross = remainedVendorCredits;
				} else {
					gross = gross.add(productGross.subtract(remainedLapwingCredits));
				}
				BigDecimal net = BigDecimal.ZERO;
				net = net.add(vatCalculator.calculateUnroundedNetFromGross(gross, line.getVatRate()));
				BigDecimal vat = BigDecimal.ZERO;
				vat = vat.add(gross.subtract(net));
				TaxableAmount amount = TaxableAmount.fromNetAndVat(net, vat);

				BuyerAppliedCredit newAppliedCredit = BuyerAppliedCredit.aBuyerAppliedCredit().amount(amount)
						.orderSpentOnReference(order.getReference()).spendType(CreditMovementType.V_SPEND)
						.status(BuyerAppliedCreditStatus.V_RESERVED).build();

				lines.remove(line);
				remainedLapwingCredits = BigDecimal.ZERO;

				if (BigDecimal.ZERO.compareTo(newAppliedCredit.getAmount().gross()) != 0) {
					buyerVendorCreditsToBeAdded.add(newAppliedCredit);
				}
			} else if (remainedVendorCredits.compareTo(BigDecimal.ZERO) > 0) {

				BigDecimal gross = BigDecimal.ZERO;
				if (productGross.compareTo(remainedVendorCredits) > 0) {
					gross = remainedVendorCredits;
				} else {
					gross = gross.add(productGross);
				}
				BigDecimal net = BigDecimal.ZERO;
				net = net.add(vatCalculator.calculateUnroundedNetFromGross(gross, line.getVatRate()));
				BigDecimal vat = BigDecimal.ZERO;
				vat = vat.add(gross.subtract(net));
				TaxableAmount amount = TaxableAmount.fromNetAndVat(net, vat);

				BuyerAppliedCredit newAppliedCredit = BuyerAppliedCredit.aBuyerAppliedCredit().amount(amount)
						.originalOrderReference(order.getReference()).orderSpentOnReference(order.getReference())
						.spendType(CreditMovementType.V_SPEND).status(BuyerAppliedCreditStatus.V_RESERVED).build();

				lines.remove(line);
				remainedVendorCredits = remainedVendorCredits.subtract(productGross);

				if (BigDecimal.ZERO.compareTo(newAppliedCredit.getAmount().gross()) != 0) {
					buyerVendorCreditsToBeAdded.add(newAppliedCredit);
				}
			}
		}
		return buyerVendorCreditsToBeAdded;
	}

	private List<Pair<OrderLine, Order>> findOrderLinesWithAvailableCredit(User buyer) {

		List<Pair<OrderLine, Order>> orderLinesWithAvailableCredit = new ArrayList<>();

		Orders orders = orderRepository.findBuyerPaidOrdersWithCredits(buyer);

		stream(orders).forEach(order -> stream(order.getLines()).forEach(line -> {
			if (line.getAvailableCreditBalance().gross().signum() > 0) {
				orderLinesWithAvailableCredit.add(Pair.of(line, order));
			}
		}));

		orderLinesWithAvailableCredit.sort(comparing((Pair<OrderLine, Order> p) -> p.getKey(), byPartConsumedCredit())
				.thenComparing((Pair<OrderLine, Order> p) -> p.getKey(), byOfferEndDate()));

		return orderLinesWithAvailableCredit;
	}
}
