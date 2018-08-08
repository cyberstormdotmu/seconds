package com.ishoal.ws.buyer.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.core.credit.BuyerCreditService;
import com.ishoal.core.credit.BuyerWithdrawService;
import com.ishoal.core.domain.BuyerCredits;
import com.ishoal.core.domain.PaymentGatewayCharge;
import com.ishoal.core.domain.User;
import com.ishoal.core.payment.PaymentGatewayService;
import com.ishoal.core.vendor.BuyerVendorCreditService;
import com.ishoal.ws.buyer.dto.BuyerAllCreditsDto;
import com.ishoal.ws.buyer.dto.BuyerLapwingCreditsDto;
import com.ishoal.ws.buyer.dto.BuyerVendorCreditsDto;
import com.ishoal.ws.buyer.dto.PaymentChargesDto;
import com.ishoal.ws.buyer.dto.adapter.BuyerVendorCreditsDtoAdapter;

@RestController
@RequestMapping("/ws/credits")
public class CreditController {
	private static final Logger logger = LoggerFactory.getLogger(CreditController.class);

	private final BuyerCreditService buyerCreditService;
	private final BuyerWithdrawService buyerWithdrawService;
	private final BuyerVendorCreditService buyerVendorCreditService;
	private final BuyerVendorCreditsDtoAdapter buyerVendorCreditsDtoAdapter = new BuyerVendorCreditsDtoAdapter();
	private final PaymentGatewayService paymentGatewayService;

	public CreditController(BuyerCreditService buyerCreditService, BuyerWithdrawService buyerWithdrawService,
			BuyerVendorCreditService buyerVendorCreditService, PaymentGatewayService paymentGatewayService) {
		this.buyerCreditService = buyerCreditService;
		this.buyerWithdrawService = buyerWithdrawService;
		this.buyerVendorCreditService = buyerVendorCreditService;
		this.paymentGatewayService = paymentGatewayService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public BuyerAllCreditsDto getCreditBalances(User user) {
		logger.info("Credit balances requested for user=[{}]", user);

		BuyerCredits buyerCredits = buyerCreditService.fetchBuyerCredits(user);
		BigDecimal withdrawCredits = buyerWithdrawService.calculateTotalWithdrawCredits(user);
		PaymentGatewayCharge paymentGatewayCharges = paymentGatewayService.fetchPaymentCharges();
		
		
		BuyerLapwingCreditsDto buyerLapwingCreditsDto = BuyerLapwingCreditsDto.someCreditBalances()
				.pendingCreditBalance(buyerCredits.getPendingCreditBalance().gross())
				.availableCreditBalance(buyerCredits.getAvailableCreditBalance().gross().subtract(withdrawCredits))
				.redeemableCreditBalance(buyerCredits.getRedeemableCreditBalance().gross().subtract(withdrawCredits))
				.build();
		List<BuyerVendorCreditsDto> buyerVendorCreditsDto = buyerVendorCreditsDtoAdapter
				.adapt(buyerVendorCreditService.fetchBuyerVendorCredits(user));
		PaymentChargesDto paymentChargesDto=PaymentChargesDto.aPaymentChargesDto().name(paymentGatewayCharges.getName())
		.paymentChargesPercentage(paymentGatewayCharges.getPaymentChargesPercentage())
		.paymentExtraCharge(paymentGatewayCharges.getPaymentExtraCharge()).build();
		
		
		return BuyerAllCreditsDto.aBuyerAllCreditsDto().lapwingCredits(buyerLapwingCreditsDto).vendorCredits(buyerVendorCreditsDto).paymentCharges(paymentChargesDto).build();		 
	}
}
