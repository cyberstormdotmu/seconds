package com.ishoal.core.buyer;

import org.springframework.transaction.annotation.Transactional;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.BuyerProfiles;
import com.ishoal.core.domain.User;
import com.ishoal.core.repository.BuyerProfileRepository;

public class ManageBuyerProfileService {

    private final BuyerProfileRepository buyerProfileRepository;
    private final BuyerProfileAdapter buyerProfileAdapter;

    public ManageBuyerProfileService(BuyerProfileRepository buyerProfileRepository) {

        this.buyerProfileRepository = buyerProfileRepository;
        this.buyerProfileAdapter = new BuyerProfileAdapter();
    }

    @Transactional
    public PayloadResult<BuyerProfile> updateProfile(UpdateBuyerProfileRequest request) {

        BuyerProfile buyerProfile = buyerProfileAdapter.adapt(request);
        buyerProfile.setCompleted();
        return update(buyerProfile, request.getUser());
    }

    @Transactional(readOnly = true)
    public PayloadResult<BuyerProfile> fetchProfile(FetchBuyerProfileRequest request) {

        PayloadResult<BuyerProfile> result;

        User user = request.getUser();
        BuyerProfile profile = buyerProfileRepository.fetchBuyerProfile(user);

        if (profile != null) {
            result = PayloadResult.success(profile);
        } else {
            result = noBuyerProfileResult(user);
        }
        return result;
    }

    private PayloadResult<BuyerProfile> update(BuyerProfile buyerProfile, User user) {

        PayloadResult<BuyerProfile> payload;

        BuyerProfile updatedProfile = buyerProfileRepository.updateBuyerProfile(user, buyerProfile);
        if (updatedProfile != null) {

            payload = PayloadResult.success(updatedProfile);

        } else {
            payload = noBuyerProfileResult(user);
        }
        return payload;
    }

    public BuyerProfiles findAllBuyers() {
        return this.buyerProfileRepository.findAllBuyers();
    }
    
    public BuyerProfile fetchProfile(String id) {
        return this.buyerProfileRepository.fetchProfileById(id);
    }
    
    private PayloadResult<BuyerProfile> noBuyerProfileResult(User user) {

        return PayloadResult.error("Buyer profile for " + user.getUsername() + " does not exist");
    }
}
