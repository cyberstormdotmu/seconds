package com.ishoal.core.vendor;

import static com.ishoal.core.domain.BuyerVendorCredit.aBuyerVendorCredit;

import java.math.BigDecimal;
import java.util.List;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.BuyerVendorCredit;
import com.ishoal.core.domain.BuyerVendorCredits;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.repository.BuyerVendorCreditRepository;
import com.ishoal.core.repository.VendorRepository;

public class BuyerVendorCreditService {
		private final BuyerVendorCreditRepository vendorCreditRepository;
		private final VendorRepository vendorRepository;

	    public BuyerVendorCreditService(BuyerVendorCreditRepository vendorCreditRepository,VendorRepository vendorRepository) {
	        this.vendorCreditRepository = vendorCreditRepository;
	        this.vendorRepository = vendorRepository;
	    }
	    
		public BuyerVendorCredit addCreditInfoDetail(BuyerVendorCredit creditDetails) {
			return vendorCreditRepository.addCreditInfoDetail(creditDetails);
		}
		
		public List<BuyerVendorCredit> fetchBuyerVendorCredits(User user)
		{
			return vendorCreditRepository.fetchBuyerVendorCredits(user);
			
		}
		
		public List<BuyerVendorCredit> fetchBuyerVendorCreditsOfSuppiler(User user, User userSuppiler)
		{
			return vendorCreditRepository.fetchBuyerVendorCreditsOfSuppiler(user, userSuppiler);
			
		}
		
		public BuyerVendorCredit updateBuyerVendorCredit(BuyerVendorCredit buyerVendorCredit) {
			return vendorCreditRepository.updateBuyerVendorCredit(buyerVendorCredit);
		}

        public BuyerVendorCredits findAll() {
            return vendorCreditRepository.findAll();
        }
        
        public BuyerVendorCredits findUserMoneyOwedDetail(User user) {
            return vendorCreditRepository.findUserMoneyOwedDetail(user);
        }

        public void addVendorCreditDetails(PayloadResult<BuyerProfile> buyerprofile) {
            List<Vendor> vendorList = vendorRepository.findVendorList();
            
            for (Vendor vendor : vendorList) {
                BuyerVendorCredit buyerVendorCredit = aBuyerVendorCredit().buyer(buyerprofile.getPayload())
                                                                       .availableCredit(BigDecimal.valueOf(0.0))
                                                                       .totalCredit(BigDecimal.valueOf(0.0))
                                                                       .paymentDueDays(30)
                                                                       .vendor(vendor).build();
                    vendorCreditRepository.addCreditInfoDetail(buyerVendorCredit);
            }        
        }
        /*public PayloadResult<BuyerVendorCredit> addVendorCreditDetails(BuyerCreditInfo payload) {
            PayloadResult<BuyerVendorCredit> result;
            List<Vendor> vendorList = vendorRepository.findVendorList();
            
            //while(vendorList.iterator().hasNext()){
            for (Vendor vendor : vendorList) {
                BuyerVendorCredit buyerVendorCredit = aBuyerVendorCredit().buyer(payload.getBuyer())
                                                                       .availableCredit(BigDecimal.valueOf(0.0))
                                                                       .totalCredit(BigDecimal.valueOf(0.0))
                                                                       .paymentDueDays(30)
                                                                       .vendor(vendor).build();
            BuyerVendorCredit vendorCredit = vendorCreditRepository.addCreditInfoDetail(buyerVendorCredit);
            }
            if (vendorList != null) {
                result = PayloadResult.success(null);
            } else {
                result = PayloadResult.error("Buyer vendor for " + vendorList + " does not exist");
            }
            return result;
        }*/    
	}