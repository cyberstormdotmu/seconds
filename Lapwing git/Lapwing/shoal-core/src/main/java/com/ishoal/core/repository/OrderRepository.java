package com.ishoal.core.repository;

import static org.joda.time.DateTime.now;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.BuyerAppliedCredits;
import com.ishoal.core.domain.OfferId;
import com.ishoal.core.domain.OfferReference;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.Orders;
import com.ishoal.core.domain.PaymentStatus;
import com.ishoal.core.domain.User;
import com.ishoal.core.persistence.adapter.OrderEntityAdapter;
import com.ishoal.core.persistence.adapter.OrderLineEntityAdapter;
import com.ishoal.core.persistence.adapter.VendorEntityAdapter;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;
import com.ishoal.core.persistence.entity.BuyerVendorCreditEntity;
import com.ishoal.core.persistence.entity.OrderEntity;
import com.ishoal.core.persistence.entity.VendorEntity;
import com.ishoal.core.persistence.repository.BuyerProfileEntityRepository;
import com.ishoal.core.persistence.repository.BuyerVendorCreditEntityRepository;
import com.ishoal.core.persistence.repository.OrderEntityRepository;
import com.ishoal.core.persistence.repository.UserEntityRepository;

public class OrderRepository {

	private final OrderEntityRepository orderEntityRepository;
	private final BuyerAppliedCreditRepository appliedCreditRepository;
	private final OrderEntityAdapter orderEntityAdapter;
	private final OrderLineEntityAdapter orderLineEntityAdapter;
	private final BuyerProfileEntityRepository buyerProfileEntityRepository;
	private final BuyerVendorCreditEntityRepository buyerVendorCreditEntityRepository;
	private final VendorEntityAdapter vendorEntityAdapter;
	private final OrderReturnRepository orderReturnRepository;

	public OrderRepository(UserEntityRepository userEntityRepository, OrderEntityRepository orderEntityRepository,
			OrderLineEntityAdapter orderLineEntityAdapter, BuyerAppliedCreditRepository appliedCreditRepository,
			BuyerProfileEntityRepository buyerProfileEntityRepository,
			BuyerVendorCreditEntityRepository buyerVendorCreditEntityRepository,
			OrderReturnRepository orderReturnRepository) {
		this.orderEntityRepository = orderEntityRepository;
		this.orderLineEntityAdapter = orderLineEntityAdapter;
		this.appliedCreditRepository = appliedCreditRepository;
		this.orderEntityAdapter = new OrderEntityAdapter(userEntityRepository, this.orderLineEntityAdapter);
		this.buyerProfileEntityRepository = buyerProfileEntityRepository;
		this.buyerVendorCreditEntityRepository = buyerVendorCreditEntityRepository;
		this.vendorEntityAdapter = new VendorEntityAdapter();
		this.orderReturnRepository = orderReturnRepository;
	}

	public void create(Order order) {
		OrderEntity orderEntity = this.orderEntityAdapter.adapt(order);
		this.orderEntityRepository.save(orderEntity);
	}

	public PayloadResult<Order> findOrderValidatingVersion(OrderReference orderReference, long version) {
		Order order = findBy(orderReference);
		if (order == null) {
			return orderNotFoundResult(orderReference);
		}

		if (order.getVersion() != version) {
			return PayloadResult.error("Incorrect order version " + version + " (expected " + order.getVersion() + ")");
		}
		return PayloadResult.success(order);
	}

	public Order findBy(OrderReference reference) {
		Order order = null;
		OrderEntity orderEntity = this.orderEntityRepository.findByReference(reference.asString());
		if (orderEntity != null) {
			order = retrieveAppliedCreditsAndAdapt(orderEntity);
			order = orderReturnRepository.calculeteTotalReturnedQuantitiesForAnOrder(order);

			return order;
		}
		return null;
	}
	
	public Orders findConfirmedOrdersFor(OfferId offerId) {
		Set<OrderEntity> orderEntities = new HashSet<>();
		orderEntities.addAll(this.orderEntityRepository.findOrdersForOfferWithStatus(offerId.asLong(), OrderStatus.CONFIRMED));
		return adapt(orderEntities);
	}

	public Orders findConfirmedOrdersFor(Set<OfferId> offerIds) {
		Set<OrderEntity> orderEntities = new HashSet<>();

		for (OfferId offerId : offerIds) {
			orderEntities.addAll(
					this.orderEntityRepository.findOrdersForOfferWithStatus(offerId.asLong(), OrderStatus.CONFIRMED));
		}

		return adapt(orderEntities);
	}

	public Orders findOrdersForOffer(OfferReference offerReference) {
		List<OrderEntity> orderEntities = this.orderEntityRepository.findOrdersForOffer(offerReference.asString());
		return adapt(orderEntities);
	}

	public Orders findByStatus(Set<OrderStatus> orderStatuses) {
		int dueDays;
		List<OrderEntity> orderEntities = this.orderEntityRepository.findByStatus(orderStatuses);
		for (OrderEntity entity : orderEntities) {
			if (entity.getStatus() == OrderStatus.PROCESSING) {
				if (entity.getPaymentStatus() == PaymentStatus.UNPAID
						|| entity.getPaymentStatus() == PaymentStatus.PART_PAID) {
					dueDays = getOrderDueDate(orderEntityAdapter.adaptWithoutCredits(entity));
					entity.setDueDate(now().plusDays(dueDays).withTimeAtStartOfDay());
				}
			}
		}
		return adapt(orderEntities);
	}

	public Orders findByStatusAndSupplier(Set<OrderStatus> orderStatuses, User user) {
		int dueDays;
		VendorEntity vendorEntity = buyerProfileEntityRepository.findByUsername(user.getUsername()).getUser()
				.getVendor();
		List<OrderEntity> orderEntities = this.orderEntityRepository.findByStatusAndVendor(orderStatuses, vendorEntity);
		for (OrderEntity entity : orderEntities) {
			if (entity.getStatus() == OrderStatus.PROCESSING) {
				if (entity.getPaymentStatus() == PaymentStatus.UNPAID
						|| entity.getPaymentStatus() == PaymentStatus.PART_PAID) {
					dueDays = getOrderDueDate(orderEntityAdapter.adaptWithoutCredits(entity));
					entity.setDueDate(now().plusDays(dueDays).withTimeAtStartOfDay());
				}
			}
		}
		return adapt(orderEntities);
	}

	public Orders findByStatusAndBuyerID(Set<OrderStatus> orderStatuses, Long id) {
		int dueDays;

		BuyerProfileEntity buyerProfileEntity = buyerProfileEntityRepository.findOne(id);
		List<OrderEntity> orderEntities = this.orderEntityRepository.findByStatusAndBuyer(orderStatuses,
				buyerProfileEntity.getUser());
		for (OrderEntity entity : orderEntities) {
			if (entity.getStatus() == OrderStatus.PROCESSING) {
				if (entity.getPaymentStatus() == PaymentStatus.UNPAID
						|| entity.getPaymentStatus() == PaymentStatus.PART_PAID) {
					dueDays = getOrderDueDate(orderEntityAdapter.adaptWithoutCredits(entity));
					entity.setDueDate(now().plusDays(dueDays).withTimeAtStartOfDay());
				}
			}
		}
		return adapt(orderEntities);
	}

	public Orders findByStatusAndBuyerIDOfSupplier(Set<OrderStatus> orderStatuses, Long id, User user) {
		int dueDays;
		VendorEntity vendorEntity = buyerProfileEntityRepository.findByUsername(user.getUsername()).getUser()
				.getVendor();
		BuyerProfileEntity buyerProfileEntity = buyerProfileEntityRepository.findOne(id);
		List<OrderEntity> orderEntities = this.orderEntityRepository.findByStatusAndBuyerAndVendor(orderStatuses,
				buyerProfileEntity.getUser(), vendorEntity);
		for (OrderEntity entity : orderEntities) {
			if (entity.getStatus() == OrderStatus.PROCESSING) {
				if (entity.getPaymentStatus() == PaymentStatus.UNPAID
						|| entity.getPaymentStatus() == PaymentStatus.PART_PAID) {
					dueDays = getOrderDueDate(orderEntityAdapter.adaptWithoutCredits(entity));
					entity.setDueDate(now().plusDays(dueDays).withTimeAtStartOfDay());
				}
			}
		}
		return adapt(orderEntities);
	}

	private Orders adapt(Iterable<OrderEntity> orderEntities) {
		return IterableUtils.mapToCollection(orderEntities, this::retrieveAppliedCreditsAndAdapt, Orders::over);
	}

	private Order retrieveAppliedCreditsAndAdapt(OrderEntity orderEntity) {
		BuyerAppliedCredits appliedCredits = appliedCreditRepository
				.findCreditsSpentOnOrder(OrderReference.from(orderEntity.getReference()));
		return orderEntityAdapter.adapt(orderEntity, appliedCredits);
	}

	public void save(Orders affectedOrders) {
		affectedOrders.forEach(it -> save(it));
	}

	public void save(Order order) {
		OrderEntity updatedEntity = this.orderEntityAdapter.adapt(order);
		this.orderEntityRepository.save(updatedEntity);
	}

	public Orders findBuyerOrders(User buyer) {
		List<OrderEntity> orderEntities = this.orderEntityRepository.findOrdersForBuyer(buyer.getId().asLong());
		for (OrderEntity entity : orderEntities) {
			if (entity.getPaymentStatus().compareTo(PaymentStatus.UNPAID) == 0
					|| entity.getPaymentStatus().compareTo(PaymentStatus.PART_PAID) == 0) {
				if (entity.getStatus().compareTo(OrderStatus.PROCESSING) == 0) {
					int dueDays = getOrderDueDate(orderEntityAdapter.adapt(entity, appliedCreditRepository
							.findCreditsSpentOnOrder(OrderReference.from(entity.getReference()))));
					entity.setDueDate(now().plusDays(dueDays).withTimeAtStartOfDay());
				}
			}
		}
		return adapt(orderEntities);
	}

	public List<OrderEntity> findBuyerOrdersForLatePaymentBalance(User buyer) {
		List<OrderEntity> orderEntities = this.orderEntityRepository.findOrdersForBuyer(buyer.getId().asLong());
		List<OrderEntity> orderEntitiesForLatePayment = new ArrayList<OrderEntity>();
		for (OrderEntity entity : orderEntities) {
			if (entity.getStatus().toString().equals("CONFIRMED")
					&& (entity.getPaymentStatus().toString().equals("UNPAID")
							|| entity.getPaymentStatus().toString().equals("PART_PAID"))
					&& entity.getDueDate().toString() != null && entity.getDueDate().isBeforeNow()) {
				orderEntitiesForLatePayment.add(entity);
			}
		}
		return orderEntitiesForLatePayment;
	}

	public List<OrderEntity> findBuyerOrdersForAccountsPayableBalance(User buyer) {
		List<OrderEntity> orderEntities = this.orderEntityRepository.findOrdersForBuyer(buyer.getId().asLong());
		List<OrderEntity> orderEntitiesForAccountsPayable = new ArrayList<OrderEntity>();
		for (OrderEntity entity : orderEntities) {
			if (entity.getStatus().toString().equals("CONFIRMED")
					&& (entity.getPaymentStatus().toString().equals("UNPAID")
							|| entity.getPaymentStatus().toString().equals("PART_PAID"))
					&& entity.getDueDate().toString() != null) {
				orderEntitiesForAccountsPayable.add(entity);
			}
		}
		return orderEntitiesForAccountsPayable;
	}

	public Orders findBuyerPaidOrdersWithCredits(User buyer) {
		List<OrderEntity> orderEntities = this.orderEntityRepository
				.findPaidOrdersWithCreditsForBuyer(buyer.getId().asLong());
		return adapt(orderEntities);
	}

	private PayloadResult<Order> orderNotFoundResult(OrderReference orderReference) {
		return PayloadResult.error("Order with reference " + orderReference + " not found");
	}

	public int getOrderDueDate(Order order) {
		if (order.calculateAppliedVendorCreditsforOrder() > 0) {
			BuyerProfileEntity buyerProfileEntity = buyerProfileEntityRepository
					.findByUsername(order.getBuyer().getUsername());
			BuyerVendorCreditEntity buyerVendorCreditEntity = buyerVendorCreditEntityRepository
					.findByVendorAndBuyer(vendorEntityAdapter.adapt(order.getVendor()), buyerProfileEntity);
			return buyerVendorCreditEntity.getPaymentDueDays();
		}
		return 0;
	}
}