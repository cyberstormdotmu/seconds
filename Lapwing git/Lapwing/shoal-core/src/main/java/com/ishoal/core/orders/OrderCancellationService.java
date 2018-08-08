package com.ishoal.core.orders;

import static com.ishoal.common.util.IterableUtils.stream;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.SimpleResult;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.BuyerAppliedCredit;
import com.ishoal.core.domain.BuyerAppliedCreditStatus;
import com.ishoal.core.domain.BuyerVendorCredit;
import com.ishoal.core.domain.CreditMovementType;
import com.ishoal.core.domain.Order;
import com.ishoal.core.repository.BuyerAppliedCreditRepository;
import com.ishoal.core.repository.BuyerVendorCreditRepository;
import com.ishoal.core.repository.OrderRepository;

public class OrderCancellationService {
    private final OrderRepository orderRepository;
    private final BuyerAppliedCreditRepository appliedCreditRepository;
    private final BuyerVendorCreditRepository buyerVendorCreditRepository;
    public OrderCancellationService(OrderRepository orderRepository, BuyerAppliedCreditRepository appliedCreditRepository, BuyerVendorCreditRepository buyerVendorCreditRepository) {
        this.orderRepository = orderRepository;
        this.appliedCreditRepository = appliedCreditRepository;
        this.buyerVendorCreditRepository = buyerVendorCreditRepository;
    }

    @Transactional
    public Result cancel(OrderReference orderReference, long version) {
        PayloadResult<Order> findResult = orderRepository.findOrderValidatingVersion(orderReference, version);
        if(findResult.isError()) {
            return findResult;
        }
        Order order = findResult.getPayload();
        Result cancelResult = order.cancel();
        if(cancelResult.isError()) {
            return cancelResult;
        }
    	
        this.orderRepository.save(order);

        stream(order.getAppliedCredits()).forEach(it -> it.setStatus(BuyerAppliedCreditStatus.CANCELLED));
        this.appliedCreditRepository.save(order.getAppliedCredits());
        
        BigDecimal appliedSuppliercredits = BigDecimal.ZERO;
        for(BuyerAppliedCredit credit : order.getAppliedCredits())
        {
        	if(credit.getSpendType().compareTo(CreditMovementType.V_SPEND)==0)
        	{
        		appliedSuppliercredits = appliedSuppliercredits.add(credit.getAmount().gross());
        	}
        }
        
        BuyerVendorCredit updatedBuyerVendorCredit = buyerVendorCreditRepository.addAvailableCredits(appliedSuppliercredits, order.getVendor(), order.getBuyer());

        return SimpleResult.success();
    }
}
