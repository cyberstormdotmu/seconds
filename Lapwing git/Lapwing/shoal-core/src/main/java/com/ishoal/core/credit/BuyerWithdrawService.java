package com.ishoal.core.credit;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.BuyerWithdrawCredit;
import com.ishoal.core.domain.BuyerWithdrawCredits;
import com.ishoal.core.domain.User;
import com.ishoal.core.repository.BuyerWithdrawCreditRepository;

public class BuyerWithdrawService {
	
	BuyerWithdrawCreditRepository buyerWithdrawCreditRepository;

	public BuyerWithdrawService(BuyerWithdrawCreditRepository buyerWithdrawCreditRepository) {
		this.buyerWithdrawCreditRepository = buyerWithdrawCreditRepository;
	}

	@Transactional
	public PayloadResult<BuyerWithdrawCredit> withdrawLapwingCredit(BigDecimal withdrawAmount, User user) {

		PayloadResult<BuyerWithdrawCredit> payload = null;
		BuyerWithdrawCredit withdrawCredit = buyerWithdrawCreditRepository.requestCreditWithdraw(withdrawAmount, user);
		

		if (withdrawCredit != null) {
			payload = PayloadResult.success(withdrawCredit);
		}

		return payload != null ? payload : PayloadResult.error("Unexpected error");

	}

	public BuyerWithdrawCredit acceptWithdrawLapwingCredit(long id, String receivedDate,String paymentType, 
            String reference) {

	    return buyerWithdrawCreditRepository.acceptLapwingCreditWithdraw(id,receivedDate,paymentType,reference);

	}
	
	public BuyerWithdrawCredit rejectWithdrawLapwingCredit(long id) {

	    return  buyerWithdrawCreditRepository.rejectLapwingCreditWithdraw(id);

    }

	public BuyerWithdrawCredits findByStatus(String withdrawStatus) {

		return buyerWithdrawCreditRepository.findByWithdrawStatus(withdrawStatus);
		
	}

	public BigDecimal calculateTotalWithdrawCredits(User user) {
		
		return buyerWithdrawCreditRepository.calculateTotalWithdrawCredits(user);
	}
}
