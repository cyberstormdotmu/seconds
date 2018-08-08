package com.ishoal.ws.buyer.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.credit.BuyerWithdrawService;
import com.ishoal.core.domain.BuyerWithdrawCredit;
import com.ishoal.core.domain.User;
import com.ishoal.ws.exceptionhandler.ErrorInfo;

@RestController
@RequestMapping("/ws/creditwithdrawform")
public class CreditWithdrawController {

	private final BuyerWithdrawService creditWithdrawService;
	
	public CreditWithdrawController(BuyerWithdrawService creditWithdrawService){
		this.creditWithdrawService = creditWithdrawService;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{withdrawAmount}")
	public ResponseEntity<?>  buyerLapwingCreditWithdraw(@PathVariable String withdrawAmount, User user){
		ResponseEntity<?> val ;
		PayloadResult<BuyerWithdrawCredit> result =creditWithdrawService.withdrawLapwingCredit(new BigDecimal(withdrawAmount),user);
		if(result.isSuccess()){
			val= ResponseEntity.ok(result.getPayload());
		}else{
			val=ResponseEntity.badRequest().body(ErrorInfo.badRequest("Unexpected Error"));
		}
		return val;
	}
	
}
