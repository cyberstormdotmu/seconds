package com.ishoal.ws.buyer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.core.domain.BuyerVendorCredit;
import com.ishoal.core.products.InvalidProductException;
import com.ishoal.core.vendor.BuyerVendorCreditService;
import com.ishoal.ws.exceptionhandler.ErrorInfo;

@RestController
@RequestMapping("/ws/vendorCredit")
public class BuyerVendorCreditController {
	private static final Logger logger = LoggerFactory.getLogger(BuyerVendorCreditController.class);

	private final BuyerVendorCreditService buyerVendorCreditService;

	public BuyerVendorCreditController(BuyerVendorCreditService buyerVendorCreditService) {
		this.buyerVendorCreditService = buyerVendorCreditService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> vedorCreditDetails(@RequestBody BuyerVendorCredit creditDetails) {

		logger.info("Adding  a new vedorCreditDetails ",creditDetails);

		try {
			BuyerVendorCredit vendorcreditDetails = buyerVendorCreditService.addCreditInfoDetail(creditDetails);
			return ResponseEntity.ok(vendorcreditDetails);
		} catch (InvalidProductException e) {
			return ResponseEntity.badRequest().body(ErrorInfo.badRequest(e.getMessage()));
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public void fetchBuyerVendorCredits() {
		logger.info("Fetching BuyerVedorCreditDetails");
	}
}
