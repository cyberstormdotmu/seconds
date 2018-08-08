package com.ishoal.core.repository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ishoal.core.buyer.BuyerProfileAdapter;
import com.ishoal.core.domain.BuyerWithdrawCredit;
import com.ishoal.core.domain.BuyerWithdrawCredits;
import com.ishoal.core.domain.User;
import com.ishoal.core.persistence.adapter.BuyerWithdrawCreditAdapter;
import com.ishoal.core.persistence.adapter.BuyerWithdrawCreditsEntityAdapter;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;
import com.ishoal.core.persistence.entity.BuyerWithdrawCreditEntity;
import com.ishoal.core.persistence.repository.BuyerWithdrawCreditEntityRepository;

public class BuyerWithdrawCreditRepository {

    BuyerWithdrawCreditEntityRepository buyerWithdrawCreditEntityRepository;
    BuyerWithdrawCreditAdapter withdrawCreditAdapter = new BuyerWithdrawCreditAdapter();
    BuyerWithdrawCreditsEntityAdapter buyerWithdrawCreditsEntityAdapter = new BuyerWithdrawCreditsEntityAdapter();
    BuyerProfileAdapter buyerProfileAdapter = new BuyerProfileAdapter();
    BuyerProfileRepository buyerProfileRepository;
    private static final Logger logger = LoggerFactory.getLogger(BuyerWithdrawCreditRepository.class);
    public BuyerWithdrawCreditRepository(BuyerWithdrawCreditEntityRepository buyerWithdrawCreditEntityRepository,BuyerProfileRepository buyerProfileRepository){
        this.buyerWithdrawCreditEntityRepository = buyerWithdrawCreditEntityRepository; 
        this.buyerProfileRepository = buyerProfileRepository;
    }
    
    public BuyerWithdrawCredit acceptLapwingCreditWithdraw(long id, String receivedDateStr,String paymentType, 
            String reference) {
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date receivedDate= null;
        try {
            receivedDate = formatter.parse(receivedDateStr);
            BuyerWithdrawCreditEntity buyerWithdrawCreditEntity = buyerWithdrawCreditEntityRepository.findByid(id);
            buyerWithdrawCreditEntity.setPaymentType(paymentType);
            buyerWithdrawCreditEntity.setReceivedDate(receivedDate);
            buyerWithdrawCreditEntity.setWithdrawReference(reference);
            buyerWithdrawCreditEntity.setWithdrawStatus("ACCEPT");
            buyerWithdrawCreditEntity.setConfirmedDate(new DateTime());
            BuyerWithdrawCreditEntity savedEntity = buyerWithdrawCreditEntityRepository.save(buyerWithdrawCreditEntity);

            return withdrawCreditAdapter.adapt(savedEntity);
        
        } catch (ParseException e) {
            logger.info("context", e);
            return null;
        }
    }
    
    public BuyerWithdrawCredit requestCreditWithdraw(BigDecimal withdrawAmount, User user) {
        
        BuyerWithdrawCreditEntity buyerWithdrawCreditEntity = new BuyerWithdrawCreditEntity();
        
        BuyerProfileEntity buyerProfileEntity = buyerProfileAdapter.adapt(buyerProfileRepository.fetchBuyerProfile(user));
        buyerWithdrawCreditEntity.setBuyer(buyerProfileEntity);
        buyerWithdrawCreditEntity.setRequestedAmount(withdrawAmount);
        buyerWithdrawCreditEntity.setRequestedDate(new DateTime());
        buyerWithdrawCreditEntity.setPaymentType(null);
        buyerWithdrawCreditEntity.setReceivedDate(null);
        buyerWithdrawCreditEntity.setWithdrawReference(null);
        buyerWithdrawCreditEntity.setWithdrawStatus("PENDING");
        buyerWithdrawCreditEntity.setConfirmedDate(null);
        BuyerWithdrawCreditEntity savedEntity = buyerWithdrawCreditEntityRepository.save(buyerWithdrawCreditEntity);

        return withdrawCreditAdapter.adapt(savedEntity);
    }

    public BigDecimal calculateTotalWithdrawCredits(User user) {
        BigDecimal total = new BigDecimal(0);
        List<BuyerWithdrawCreditEntity> buyerWithdrawCreditEntities = buyerWithdrawCreditEntityRepository
                .findBuyerWithdrawLapwingCredits(user.getId().asLong());

        for (BuyerWithdrawCreditEntity buyerWithdrawCreditEntity : buyerWithdrawCreditEntities) {
            if (buyerWithdrawCreditEntity.getWithdrawStatus() != null && 
                    buyerWithdrawCreditEntity.getWithdrawStatus().compareToIgnoreCase("ACCEPT") == 0 ||
                    buyerWithdrawCreditEntity.getWithdrawStatus().compareToIgnoreCase("PENDING") == 0) {
                    total = total.add(buyerWithdrawCreditEntity.getRequestedAmount());              
            }
        }
        return total;
    }

    public BuyerWithdrawCredits findByWithdrawStatus(String withdrawStatus){
        
        List<BuyerWithdrawCreditEntity> entityCredits;
        BuyerWithdrawCredits credits;
        
        if(buyerWithdrawCreditEntityRepository.findBywithdrawStatus(withdrawStatus) != null){
            entityCredits = buyerWithdrawCreditEntityRepository.findBywithdrawStatus(withdrawStatus);
        }else{
            credits = null;
            return credits;
        }
        
        if(entityCredits != null){
            credits = buyerWithdrawCreditsEntityAdapter.adapt(entityCredits);
        }else{
            credits = null;
        }
        return credits;
    }

    public BuyerWithdrawCredit rejectLapwingCreditWithdraw(long id) {
        
        try{
            BuyerWithdrawCreditEntity buyerWithdrawCreditEntity = buyerWithdrawCreditEntityRepository.findByid(id);
            buyerWithdrawCreditEntity.setWithdrawStatus("REJECT");
            buyerWithdrawCreditEntity.setConfirmedDate(new DateTime());
            BuyerWithdrawCreditEntity savedEntity = buyerWithdrawCreditEntityRepository.save(buyerWithdrawCreditEntity);

            return withdrawCreditAdapter.adapt(savedEntity);
        }catch (Exception e) {
            logger.info("context", e);
            return null;
        }
    }
    
    
}
