package com.ishoal.core.buyer;
import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.BuyerCreditInfo;
import com.ishoal.core.repository.BuyerCreditInfoRepository;

public class ManageBuyerCreditInfoService {
    private  BuyerCreditInfoRepository creditInfoRepository;
	public ManageBuyerCreditInfoService(BuyerCreditInfoRepository creditInfoRepository) {
		this.creditInfoRepository=creditInfoRepository;
	}
	
	@Transactional
	public PayloadResult<BuyerCreditInfo> saveCreditInfoDetail(BuyerCreditInfo buyerCreditRequest) {

		PayloadResult<BuyerCreditInfo> payload=null;
		BuyerCreditInfo buyerCreditInfo = null;	
		buyerCreditInfo = creditInfoRepository.saveCreditInfo(buyerCreditRequest);
		if (buyerCreditInfo != null) {
			payload = PayloadResult.success(buyerCreditInfo);
		} else {
			payload = PayloadResult.error("ERROR DURING SAVING DATA");
		}
		return payload;
	}
	
	@Transactional
	public PayloadResult<BuyerCreditInfo> fetchBuyerCreditInfoDetails(Long buyerId) {
		
	    PayloadResult<BuyerCreditInfo> result;
	    BuyerCreditInfo buyerCreditInfo = null;
	    buyerCreditInfo=creditInfoRepository.fetchBuyerCreditInfo(buyerId);	    
	        if (buyerCreditInfo != null) {
	            result = PayloadResult.success(buyerCreditInfo);
	        } else {
	            result = PayloadResult.error("Buyer CreditInfo for " + buyerId + " does not exist");
	        }
	        return result;
	    }
}