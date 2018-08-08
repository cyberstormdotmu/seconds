package com.ishoal.ws.buyer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.buyer.FetchBuyerProfileRequest;
import com.ishoal.core.buyer.ManageBuyerProfileService;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.User;
import com.ishoal.ws.buyer.adapter.UpdateBuyerProfileRequestAdapter;
import com.ishoal.ws.buyer.dto.BuyerProfileDto;
import com.ishoal.ws.buyer.dto.adapter.BuyerProfileDtoAdapter;
import com.ishoal.ws.exceptionhandler.ErrorInfo;
import com.ishoal.ws.session.BuyerSession;

@RestController
@RequestMapping("/ws/profile")
public class BuyerProfileController {

	private static final Logger logger = LoggerFactory.getLogger(BuyerProfileController.class);
	private final ManageBuyerProfileService manageBuyerProfileService;
	private final UpdateBuyerProfileRequestAdapter updateBuyerProfileRequestAdapter = new UpdateBuyerProfileRequestAdapter();
	private final BuyerProfileDtoAdapter buyerProfileDtoAdapter = new BuyerProfileDtoAdapter();

	public BuyerProfileController(ManageBuyerProfileService manageBuyerProfileService) {

		this.manageBuyerProfileService = manageBuyerProfileService;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> updateProfile(User user, @RequestBody BuyerProfileDto profile) {

		logger.info("buyer {} is updating their profile", user.getUsername());
		PayloadResult<BuyerProfile> result = updateBuyerProfile(user, profile);
		if (result.isSuccess()) {
			return ResponseEntity.ok(buyerProfileDtoAdapter.adapt(result.getPayload()));
		} else {

			logger.warn("Failed to update the buyer profile form. Reason: " + result.getError());
			return ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
		}
	}

	private PayloadResult<BuyerProfile> updateBuyerProfile(User user, BuyerProfileDto profile) {

		return manageBuyerProfileService
				.updateProfile(updateBuyerProfileRequestAdapter.buildUpdateRequest(user, profile));
	}

	/*public boolean updateBuyerInSession(BuyerSession buyerSession){
	    
	    boolean result = false;
	    
	    try{
	        this.buyerSession = buyerSession;
	        result = true;
	    }catch (Exception e) {
	        logger.warn("Error while saving buyer in session.");
	        result = false;
	    }
	    return result;
	}*/
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> fetchProfile(BuyerSession buyerSession, User user,@RequestParam(value = "useSessionUser", required=false) Boolean useSessionUser) {

		User sessionUser;
		if(useSessionUser == null){
		    useSessionUser = true;
		}
		if(buyerSession != null && useSessionUser){
		    sessionUser = buyerSession.getUser();
		}else{
		    sessionUser = null;
		    logger.info("Fetch buyer profile form for user {}", user.getEmailAddress());
		}
		
		PayloadResult<BuyerProfile> result;
		if(sessionUser != null && useSessionUser){
		    result = manageBuyerProfileService.fetchProfile(buildFetchRequest(sessionUser));
		}else{
		    result = manageBuyerProfileService.fetchProfile(buildFetchRequest(user));
		}
		
		if (result.isSuccess()) {
			return ResponseEntity.ok(buyerProfileDtoAdapter.adapt(result.getPayload()));
		} else {
			logger.warn("Failed to fetch the buyer profile form. Reason: " + result.getError());
			return ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
		}
	}

	private FetchBuyerProfileRequest buildFetchRequest(User user) {
		return FetchBuyerProfileRequest.aFetchBuyerProfileRequest().user(user).build();
	}
}