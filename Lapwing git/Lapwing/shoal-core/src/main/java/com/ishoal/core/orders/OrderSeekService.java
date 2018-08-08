package com.ishoal.core.orders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.BuyerVendorCredit;
import com.ishoal.core.domain.BuyerVendorCredits;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.User;
import com.ishoal.core.persistence.adapter.BuyerVendorCreditsEntityAdapter;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;
import com.ishoal.core.persistence.entity.BuyerVendorCreditEntity;
import com.ishoal.core.persistence.entity.OrderEntity;
import com.ishoal.core.persistence.entity.OrderLineEntity;
import com.ishoal.core.persistence.entity.OrderPaymentEntity;
import com.ishoal.core.persistence.entity.ProductEntity;
import com.ishoal.core.persistence.repository.BuyerProfileEntityRepository;
import com.ishoal.core.persistence.repository.BuyerVendorCreditEntityRepository;
import com.ishoal.core.persistence.repository.OrderLineEntityRepository;
import com.ishoal.core.persistence.repository.OrderPaymentEntityRepository;
import com.ishoal.core.repository.OrderRepository;

public class OrderSeekService {

	private final OrderRepository orderRepository;
	private final OrderLineEntityRepository orderLineEntityRepository;
	private final OrderPaymentEntityRepository orderPaymentEntityRepository;
	private final BuyerProfileEntityRepository buyerProfileEntityRepository;
	private final BuyerVendorCreditEntityRepository buyerVendorCreditEntityRepository;
	private final BuyerVendorCreditsEntityAdapter buyerVendorCreditsAdapter = new BuyerVendorCreditsEntityAdapter();
				
	public OrderSeekService(OrderRepository orderRepository, BuyerVendorCreditEntityRepository buyerVendorCreditEntityRepository,
			BuyerProfileEntityRepository buyerProfileEntityRepository, OrderLineEntityRepository orderLineEntityRepository, 
			OrderPaymentEntityRepository orderPaymentEntityRepository) {
		this.buyerProfileEntityRepository = buyerProfileEntityRepository;
		this.orderLineEntityRepository = orderLineEntityRepository;
		this.buyerVendorCreditEntityRepository = buyerVendorCreditEntityRepository;
		this.orderRepository = orderRepository;
		this.orderPaymentEntityRepository = orderPaymentEntityRepository;
	}

	@Transactional(readOnly = true)
	public Orders findByStatus(Set<OrderStatus> orderStatuses) {
		return this.orderRepository.findByStatus(orderStatuses);
	}

	@Transactional(readOnly = true)
	public Orders findByStatusAndSupplier(Set<OrderStatus> orderStatuses, User user) {
		return this.orderRepository.findByStatusAndSupplier(orderStatuses, user);
	}
	
	@Transactional(readOnly = true)
	public Orders findByStatusBuyerOrders(Set<OrderStatus> orderStatuses, Long id) {
		return this.orderRepository.findByStatusAndBuyerID(orderStatuses, id);
	}
	
	@Transactional(readOnly = true)
	public Orders findByStatusBuyerOrdersOfSupplier(Set<OrderStatus> orderStatuses, Long id, User user) {
		return this.orderRepository.findByStatusAndBuyerIDOfSupplier(orderStatuses, id, user);
	}
	
	@Transactional(readOnly = true)
	public Orders findBuyerOrders(User buyer) {
		return this.orderRepository.findBuyerOrders(buyer);
	}

	@Transactional(readOnly = true)
	public PayloadResult<Order> findOrder(OrderReference orderReference) {
		Order order = orderRepository.findBy(orderReference);
		if(order != null) {
			return PayloadResult.success(order);
		}
		return orderNotFoundResult(orderReference);
	}

	@Transactional(readOnly = true)
	public PayloadResult<Order> findBuyerOrder(User user, OrderReference orderReference) {
		PayloadResult<Order> result = findOrder(orderReference);
		if(result.isSuccess()) {
			Order order = result.getPayload();
			if (!order.getBuyer().equals(user)) {
				result = PayloadResult.error("No order with reference " + orderReference + " found for buyer");
			}
		}
		return result;
	}

	private PayloadResult<Order> orderNotFoundResult(OrderReference orderReference) {
		return PayloadResult.error("Order with reference " + orderReference + " not found");
	}

	public BigDecimal findCreditMoneyOwnedBalance(User user) {

		BuyerProfileEntity buyerProfileEntity =  buyerProfileEntityRepository.findByUsername(user.getUsername());
		List<BuyerVendorCreditEntity> buyerVendorCreditEntity = buyerVendorCreditEntityRepository.findByBuyer(buyerProfileEntity);
		BuyerVendorCredits buyerVendorCredits = buyerVendorCreditsAdapter.adapt(buyerVendorCreditEntity);
		BigDecimal moneyOwnedBalance = new BigDecimal(0);
		for(BuyerVendorCredit entity: buyerVendorCredits){
			moneyOwnedBalance =  moneyOwnedBalance.add(entity.getTotalCredit().subtract(entity.getAvailableCredit()));
		}
		return moneyOwnedBalance;
	}

	public BigDecimal findCreditLatePaymentBalance(User user) {
		BigDecimal partPaidLatePaymentBalance = new BigDecimal(0);
		BigDecimal unpaidLatePaymentBalance = new BigDecimal(0);
		List<OrderEntity> orderEntity = orderRepository.findBuyerOrdersForLatePaymentBalance(user);
		for (OrderEntity entity : orderEntity) {
			if(entity.getPaymentStatus().toString().equals("PART_PAID")){
				List<OrderLineEntity> listOrderLineEntity = orderLineEntityRepository.findByOrder(entity);
				BigDecimal unpaidPaymentBalance = new BigDecimal(0);
				for (OrderLineEntity orderLineEntity : listOrderLineEntity){
					unpaidPaymentBalance =  unpaidPaymentBalance.add(orderLineEntity.getNetAmount().add(orderLineEntity.getVatAmount()));
				}
				List<OrderPaymentEntity> listOrderPaymentEntity = orderPaymentEntityRepository.findByOrder(entity);
				BigDecimal paidPaymentBalance = new BigDecimal(0);
				for (OrderPaymentEntity orderPaymentEntity : listOrderPaymentEntity) {
					paidPaymentBalance = paidPaymentBalance.add(orderPaymentEntity.getAmount());
				}
				partPaidLatePaymentBalance = unpaidPaymentBalance.subtract(paidPaymentBalance);
			}
			else {
				List<OrderLineEntity> listOrderLineEntity = orderLineEntityRepository.findByOrder(entity);
				for (OrderLineEntity orderLineEntity : listOrderLineEntity){
					unpaidLatePaymentBalance =  unpaidLatePaymentBalance.add(orderLineEntity.getNetAmount().add(orderLineEntity.getVatAmount()));
				}

			}
		}
		return partPaidLatePaymentBalance.add(unpaidLatePaymentBalance);
	}

	public BigDecimal findCreditAccountsPayableBalance(User user) {

		BigDecimal partPaidAccountsPayableBalance = new BigDecimal(0);
		BigDecimal unpaidAccountsPayableBalance = new BigDecimal(0);
		List<OrderEntity> orderEntity = orderRepository.findBuyerOrdersForAccountsPayableBalance(user);
		for (OrderEntity entity : orderEntity) {
			if(entity.getPaymentStatus().toString().equals("PART_PAID")){
				List<OrderLineEntity> listOrderLineEntity = orderLineEntityRepository.findByOrder(entity);
				BigDecimal unpaidPaymentBalance = new BigDecimal(0);
				for (OrderLineEntity orderLineEntity : listOrderLineEntity){
					unpaidPaymentBalance =  unpaidPaymentBalance.add(orderLineEntity.getNetAmount().add(orderLineEntity.getVatAmount()));
				}
				List<OrderPaymentEntity> listOrderPaymentEntity = orderPaymentEntityRepository.findByOrder(entity);
				BigDecimal paidPaymentBalance = new BigDecimal(0);

				for (OrderPaymentEntity orderPaymentEntity : listOrderPaymentEntity) {
					paidPaymentBalance = paidPaymentBalance.add(orderPaymentEntity.getAmount());
				}

				partPaidAccountsPayableBalance = unpaidPaymentBalance.subtract(paidPaymentBalance);
			}
			else {
				List<OrderLineEntity> listOrderLineEntity = orderLineEntityRepository.findByOrder(entity);
				for (OrderLineEntity orderLineEntity : listOrderLineEntity){
					unpaidAccountsPayableBalance =  unpaidAccountsPayableBalance.add(orderLineEntity.getNetAmount().add(orderLineEntity.getVatAmount()));
				}
			}
		}
		return partPaidAccountsPayableBalance.add(unpaidAccountsPayableBalance);
	}

    public List<OrderLineEntity> findByProduct(ProductEntity product) {
        return orderLineEntityRepository.findByProduct(product);
    }
}