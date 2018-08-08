package com.ishoal.ws.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.core.credit.BuyerWithdrawService;
import com.ishoal.core.domain.BuyerWithdrawCredits;
import com.ishoal.core.domain.User;

@RestController
@RequestMapping("/ws/admin/fetchWithdrawCreditList")
public class LapwingCreditWithdrawController {
	
	private final BuyerWithdrawService creditWithdrawService;
	
	public LapwingCreditWithdrawController(BuyerWithdrawService creditWithdrawService) {
		this.creditWithdrawService = creditWithdrawService;
    }

	@RequestMapping(method = RequestMethod.GET)
	public BuyerWithdrawCredits adminLapwingCreditWithdraw(@RequestParam(value="status", required = false) String withdrawStatus, User user){
		
	    return creditWithdrawService.findByStatus(withdrawStatus);
		
	}
	
}
