package com.ishoal.core.buyer;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.User;
import com.ishoal.core.repository.BuyerProfileRepository;
import com.ishoal.core.repository.UserRepository;

public class BuyerRegistrationService {

    final BuyerProfileRepository buyerProfileRepository;
    final UserRepository userRepository;
    
    final BuyerProfileAdapter buyerProfileAdapter;
    final BuyerProfileDatabaseExceptionHandler buyerProfileDatabaseExceptionHandler;
    public BuyerRegistrationService(BuyerProfileRepository buyerProfileRepository, UserRepository userRepository) {

        this.buyerProfileRepository = buyerProfileRepository;
        this.userRepository = userRepository;
        this.buyerProfileAdapter = new BuyerProfileAdapter();
        this.buyerProfileDatabaseExceptionHandler = new BuyerProfileDatabaseExceptionHandler();
    }

    @Transactional
    public PayloadResult<BuyerProfile> registerBuyer(RegisterBuyerRequest request) {
        PayloadResult<BuyerProfile> result;
        try {
            result = tryRegister(request);
        } catch (DataIntegrityViolationException e) {
            result = handleDatabaseException(request, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result != null ? result : PayloadResult.error("Unexpected error");
    }

    private PayloadResult<BuyerProfile> tryRegister(RegisterBuyerRequest request) {

        BuyerProfile buyerProfile = registerNewBuyer(request);
        return PayloadResult.success(buyerProfile);
    }

    private PayloadResult<BuyerProfile> handleDatabaseException(RegisterBuyerRequest request,
        DataIntegrityViolationException dbException) {
        return buyerProfileDatabaseExceptionHandler.handleDatabaseException(request, dbException);
    }

    /*private User createUser(RegisterBuyerRequest request) {
        return userRepository.save(request.getUser());
    }*/

    private BuyerProfile registerNewBuyer(RegisterBuyerRequest request) {

        BuyerProfile buyerProfile = buyerProfileAdapter.adaptNew(request);
        buyerProfile = buyerProfileRepository.createNewBuyerProfile(buyerProfile);
        return buyerProfile;
    }
    public User findUserByRegistrationToken(String registrationToken){
    	return userRepository.findByRegistrationTokenIgnoreCase(registrationToken);
    }
}