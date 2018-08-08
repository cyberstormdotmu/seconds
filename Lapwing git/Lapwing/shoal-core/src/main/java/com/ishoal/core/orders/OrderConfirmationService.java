package com.ishoal.core.orders;

import static com.ishoal.common.util.IterableUtils.stream;

import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.SimpleResult;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.BuyerAppliedCreditStatus;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.OfferId;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.Product;
import com.ishoal.core.products.ProductService;
import com.ishoal.core.repository.BuyerAppliedCreditRepository;
import com.ishoal.core.repository.OrderRepository;

public class OrderConfirmationService {

	private final ProductService productService;
	private final OrderRepository orderRepository;
	private final PriceMovementProcessor priceMovementProcessor;
	private final BuyerAppliedCreditRepository appliedCreditRepository;

	public OrderConfirmationService(ProductService productService, OrderRepository orderRepository,
			PriceMovementProcessor priceMovementProcessor, BuyerAppliedCreditRepository appliedCreditRepository) {
		this.productService = productService;
		this.orderRepository = orderRepository;
		this.priceMovementProcessor = priceMovementProcessor;
		this.appliedCreditRepository = appliedCreditRepository;
	}

	@Transactional
	public Result confirm(OrderReference orderReference, long version) {
		PayloadResult<Order> findResult = orderRepository.findOrderValidatingVersion(orderReference, version);
		if (findResult.isError()) {
			return findResult;
		}

		Order order = findResult.getPayload();

		int dueDays = orderRepository.getOrderDueDate(order);
		Result confirmResult = order.confirm(dueDays);
		if (confirmResult.isError()) {
			return confirmResult;
		}
		
		Orders affectedOrders = ordersAffectedByConfirmation(order);

		for (OrderLine line : order.getLines()) {
			Product product = line.getProduct();
			Offer offer = line.getOffer();
			long newVolume = this.productService.increaseCurrentVolume(offer, line.getQuantity());
			productService.updateStock(product,line.getQuantity());
			PriceBand priceBandForVolume = offer.getPriceBands().getPriceBandForVolume(newVolume);
			this.priceMovementProcessor.process(affectedOrders, product, priceBandForVolume);
		}
		this.orderRepository.save(affectedOrders);

		stream(order.getAppliedCredits()).forEach(it -> it.setStatus(BuyerAppliedCreditStatus.CONFIRMED));
		this.appliedCreditRepository.save(order.getAppliedCredits());

		return SimpleResult.success();
	}

	private Orders ordersAffectedByConfirmation(Order order) {

		Set<OfferId> offerIds = new HashSet<>();
		for (OrderLine line : order.getLines()) {
			Offer offer = line.getOffer();
			offerIds.add(offer.getId());
		}

		return this.orderRepository.findConfirmedOrdersFor(offerIds).add(order);
	}
	
	@Transactional
	public Order fetchOrder(OrderReference orderReference)
	{
		return orderRepository.findBy(orderReference);
	}
	public long getVersion(OrderReference reference)
	{
		return orderRepository.findBy(reference).getVersion();
	}
}
