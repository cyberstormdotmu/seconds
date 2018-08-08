package com.ishoal.ws.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.core.credit.BuyerWithdrawService;
import com.ishoal.core.domain.BuyerWithdrawCredit;
import com.ishoal.ws.exceptionhandler.ErrorInfo;

@RestController
@RequestMapping("/ws/admin/withdrawrequest")
public class AdminLapwingWithdrawCreditController {

	private static final Logger logger = LoggerFactory.getLogger(AdminLapwingWithdrawCreditController.class);
	
	private final BuyerWithdrawService creditWithdrawService;
	
	public AdminLapwingWithdrawCreditController(BuyerWithdrawService creditWithdrawService) {
		this.creditWithdrawService = creditWithdrawService;
    }
	
	@RequestMapping(method = RequestMethod.POST, value="/accept")
	public ResponseEntity<?> acceptWithdrawCreditRequest(@RequestParam(value = "id") long id,
	        @RequestParam(value = "date") String receivedDate, @RequestParam(value = "type") String paymentType, 
	        @RequestParam(value = "reference") String reference){

		BuyerWithdrawCredit buyerWithdrawCredit = creditWithdrawService.acceptWithdrawLapwingCredit(id,receivedDate,paymentType,reference);
		
		boolean result = false;
        if(buyerWithdrawCredit != null){
            result = true;
        }
        
		ResponseEntity<?> response;
        if(result) {
            logger.info("Successfully cancelled order with orderReference=[{}]","");
            response = ResponseEntity.ok().build();
        } else {
            logger.warn("Order with orderReference=[{}] could not be cancelled. Error=[{}]", "", "Accept credit request denied.");
            response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("Cancel credit request denied."));
        }
		
		return response;
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/cancel")
    public ResponseEntity<?> rejectWithdrawCreditRequest(@RequestParam(value = "id") long id){
	    
        BuyerWithdrawCredit buyerWithdrawCredit = creditWithdrawService.rejectWithdrawLapwingCredit(id);
        
        boolean result = false;
        if(buyerWithdrawCredit != null){
            result = true;
        }
        
        ResponseEntity<?> response;
        if(result) {
            logger.info("Successfully cancelled order with orderReference=[{}]","");
            response = ResponseEntity.ok().build();
        } else {
            logger.warn("Order with orderReference=[{}] could not be cancelled. Error=[{}]", "", "Cancel credit request denied.");
            response = ResponseEntity.badRequest().body(ErrorInfo.badRequest("Cancel credit request denied."));
        }
        
        return response;
    }
	
}
