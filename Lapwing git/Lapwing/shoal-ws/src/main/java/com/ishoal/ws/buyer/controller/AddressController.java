package com.ishoal.ws.buyer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.Result;
import com.ishoal.common.util.ErrorType;
import com.ishoal.core.buyer.ManageAddressService;
import com.ishoal.core.domain.Address;
import com.ishoal.core.domain.User;
import com.ishoal.ws.buyer.adapter.AddressRequestAdapter;
import com.ishoal.ws.common.dto.AddressDto;
import com.ishoal.ws.common.dto.adapter.AddressDtoAdapter;
import com.ishoal.ws.exceptionhandler.ErrorInfo;

@RestController
@RequestMapping("/ws/addresses")
public class AddressController {
	private static final Logger logger = LoggerFactory.getLogger(AddressController.class);
	AddressRequestAdapter addressRequestAdapter = new AddressRequestAdapter();
	ManageAddressService manageAddressService;
	AddressDtoAdapter addressDtoAdapter = new AddressDtoAdapter ();

	public AddressController(ManageAddressService manageAddressService) {
		this.manageAddressService = manageAddressService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewAddress(User user, @RequestBody AddressDto newAddress) {
		ResponseEntity<?> val ;
		PayloadResult<Address> result = manageAddressService.saveAddressofBuyer(user,addressRequestAdapter.buildRequest(newAddress));
		if(result.isSuccess())
		{
			logger.info("Successfully Saveded Address");
			val= ResponseEntity.ok(addressDtoAdapter.adapt(result.getPayload()));
		}
		else
		{
			logger.warn("Failed to update the buyer profile form. Reason: " + result.getError());
			val=generateErrorResponse(result);
		}
		return val;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> updateAddress(User user, @RequestBody AddressDto newAddress) {
		ResponseEntity<?> val ;
		PayloadResult<Address> result = manageAddressService.saveAddressofBuyer(user,addressRequestAdapter.buildRequest(newAddress));
		if (result.isSuccess()) {
			logger.info("Successfully Edited Address");
			val= ResponseEntity.ok(addressDtoAdapter.adapt(result.getPayload()));
		} else {

			logger.warn("Failed to update the buyer profile form. Reason: " + result.getError());
			val=generateErrorResponse(result);
		}
		return val;
	}

	private ResponseEntity<?> generateErrorResponse(PayloadResult<Address> result) {
		if (result.getErrorType().equals(ErrorType.CONFLICT)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorInfo.badRequest(result.getError()));
		} else {
			return ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
		}
	}
	
	private ResponseEntity<?> generateErrorResponseD(Result result) {
		if (result.getError().equals(ErrorType.CONFLICT)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorInfo.badRequest(result.getError()));
		} else {
			return ResponseEntity.badRequest().body(ErrorInfo.badRequest(result.getError()));
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAddress(@RequestParam(value = "id", required = false) Long id) {
	    logger.info("Address id for delete",id);
        PayloadResult<Address> result = this.manageAddressService.deleteAddressofBuyer(id);
        ResponseEntity<?> response;
        
        if (result.isSuccess()) {
			logger.info("Successfully Edited Address");
			response= ResponseEntity.ok(addressDtoAdapter.adapt(result.getPayload()));
		} else {
			logger.warn("Failed to update the buyer profile form. Reason: " + result.getError());
			response=generateErrorResponse(result);
		}
		return response;
    }
}