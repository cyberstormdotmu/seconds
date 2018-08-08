package com.ishoal.core.orders;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.BuyerAppliedCredit;
import com.ishoal.core.domain.CreditMovementType;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderPayment;
import com.ishoal.core.domain.PaymentRecordType;
import com.ishoal.core.domain.PaymentReference;
import com.ishoal.core.repository.BuyerVendorCreditRepository;
import com.ishoal.core.repository.OrderRepository;

public class OrderPaymentService {
	private final OrderRepository orderRepository;
	private final BuyerVendorCreditRepository buyerVendorCreditRepository;

	public OrderPaymentService(OrderRepository orderRepository,
			BuyerVendorCreditRepository buyerVendorCreditRepository) {
		this.orderRepository = orderRepository;
		this.buyerVendorCreditRepository = buyerVendorCreditRepository;
	}

	@Transactional
	public PayloadResult<Order> recordPayment(OrderReference orderReference, long version, OrderPayment payment) {
		Result result;

		PayloadResult<Order> findResult = orderRepository.findOrderValidatingVersion(orderReference, version);
		result = findResult;

		if (result.isSuccess()) {
			Order order = findResult.getPayload();
			result = order.addPayment(payment);
			this.orderRepository.save(order);

			BigDecimal appliedVendorCredit = updateVendorCredits(order);
			if (appliedVendorCredit.compareTo(payment.getAmount()) > 0) {
				appliedVendorCredit = payment.getAmount();
			}
			buyerVendorCreditRepository.addAvailableCredits(appliedVendorCredit, order.getVendor(), order.getBuyer());
			return PayloadResult.success(order);
		}
		return PayloadResult.error("bad Request");
	}

	private BigDecimal updateVendorCredits(Order order) {
		BigDecimal appliedVendorCredit = BigDecimal.ZERO;
		for (BuyerAppliedCredit buyerAppliedCredit : order.getAppliedCredits()) {
			if (buyerAppliedCredit.getSpendType().compareTo(CreditMovementType.V_SPEND) == 0) {
				appliedVendorCredit = appliedVendorCredit.add(buyerAppliedCredit.getAmount().gross());
			}
		}
		return appliedVendorCredit;
	}

	@Transactional
	public Result deletePayment(OrderReference orderReference, long version, PaymentReference paymentReference) {
		Result result;

		PayloadResult<Order> findResult = orderRepository.findOrderValidatingVersion(orderReference, version);
		result = findResult;

		if (result.isSuccess()) {
			Order order = findResult.getPayload();
			OrderPayment payment = order.searchForPayment(paymentReference);

			BigDecimal paidAmount = BigDecimal.ZERO;
			if (payment != null && payment.getPaymentRecordType() != null
					&& payment.getPaymentRecordType().compareTo(PaymentRecordType.SUPPLIER_CREDIT_PAYMENT) == 0) {
				paidAmount = paidAmount.add(payment.getAmount());
			}

			result = order.deletePayment(paymentReference);

			if (result.isSuccess()) {
				if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
					buyerVendorCreditRepository.deductAvailableCredits(paidAmount, order.getBuyer(), order.getVendor());
				}
				this.orderRepository.save(order);
			}
		}
		return result;
	}
}
