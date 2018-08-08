package com.ishoal.core.repository;

import java.math.BigDecimal;
import java.util.List;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.BuyerVendorCredit;
import com.ishoal.core.domain.BuyerVendorCredits;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.persistence.adapter.BuyerVendorCreditEntityAdapter;
import com.ishoal.core.persistence.adapter.VendorEntityAdapter;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;
import com.ishoal.core.persistence.entity.BuyerVendorCreditEntity;
import com.ishoal.core.persistence.entity.VendorEntity;
import com.ishoal.core.persistence.repository.BuyerProfileEntityRepository;
import com.ishoal.core.persistence.repository.BuyerVendorCreditEntityRepository;

public class BuyerVendorCreditRepository {
	private final BuyerVendorCreditEntityRepository buyerVendorCreditEntityRepository;
	private final BuyerProfileEntityRepository buyerProfileEntityRepository; 
	private final BuyerVendorCreditEntityAdapter buyerVendorCreditEntityAdapter = new BuyerVendorCreditEntityAdapter();
	private BuyerVendorCreditEntityAdapter vendorCreditEntityAdapter = new BuyerVendorCreditEntityAdapter();
	private VendorEntityAdapter vendorEntityAdapter = new VendorEntityAdapter();
	public BuyerVendorCreditRepository(BuyerVendorCreditEntityRepository buyerVendorCreditEntityRepository, BuyerProfileEntityRepository buyerProfileEntityRepository)
	{
		this.buyerVendorCreditEntityRepository=buyerVendorCreditEntityRepository;
		this.buyerProfileEntityRepository=buyerProfileEntityRepository;
	}
	
	public List<BuyerVendorCredit> fetchBuyerVendorCredits(User user)
	{
		List<BuyerVendorCreditEntity> list=	buyerVendorCreditEntityRepository.findByUser(user.getUsername());
		return buyerVendorCreditEntityAdapter.adapt(list);
		
	}
	
	public List<BuyerVendorCredit> fetchBuyerVendorCreditsOfSuppiler(User user, User userSuppiler)
	{
		VendorEntity vendorentity = buyerProfileEntityRepository.findByUsername(userSuppiler.getUsername()).getUser().getVendor();
		BuyerProfileEntity buyerProfileEntity = buyerProfileEntityRepository.findByUsername(user.getUsername());
		List<BuyerVendorCreditEntity> list=	buyerVendorCreditEntityRepository.findByBuyerAndVendor(buyerProfileEntity, vendorentity);
		
		return buyerVendorCreditEntityAdapter.adapt(list);
		
	}

	public BuyerVendorCredit addCreditInfoDetail(BuyerVendorCredit creditDetails) {
		BuyerVendorCreditEntity buyerCreditEntity = vendorCreditEntityAdapter.adapt(creditDetails);
		BuyerVendorCreditEntity buyerCreditEntity1= buyerVendorCreditEntityRepository.save(buyerCreditEntity);
		return vendorCreditEntityAdapter.adapt(buyerCreditEntity1);
	}

	public BuyerVendorCredit deductAvailableCredits(BigDecimal creditsUsed , User user, Vendor vendor)
	{
		BuyerVendorCreditEntity entity=buyerVendorCreditEntityRepository.findByVendorAndBuyer(vendorEntityAdapter.adapt(vendor), buyerProfileEntityRepository.findByUsername(user.getUsername()));
		entity.setAvailableCredit(entity.getAvailableCredit().subtract(creditsUsed));
		BuyerVendorCreditEntity updatedEntity=buyerVendorCreditEntityRepository.save(entity);
		return buyerVendorCreditEntityAdapter.adapt(updatedEntity);
	}
	
	public BuyerVendorCredit addAvailableCredits(BigDecimal appliedVendorCredit, Vendor vendor, User user)
	{
		BuyerVendorCreditEntity entity=buyerVendorCreditEntityRepository.findByVendorAndBuyer(vendorEntityAdapter.adapt(vendor), buyerProfileEntityRepository.findByUsername(user.getUsername()));
		entity.setAvailableCredit(entity.getAvailableCredit().add(appliedVendorCredit));
		BuyerVendorCreditEntity updatedEntity=buyerVendorCreditEntityRepository.save(entity);
		return buyerVendorCreditEntityAdapter.adapt(updatedEntity);
	}

	public BuyerVendorCredit updateBuyerVendorCredit(BuyerVendorCredit buyerVendorCredit) {
		BuyerVendorCreditEntity entity= buyerVendorCreditEntityRepository.findByVendorAndBuyer(vendorEntityAdapter.adapt(buyerVendorCredit.getVendor()), buyerProfileEntityRepository.findOne(buyerVendorCredit.getBuyer().getId()));
		entity.setAvailableCredit(buyerVendorCredit.getAvailableCredit());
		entity.setTotalCredit(buyerVendorCredit.getTotalCredit());
		BuyerVendorCreditEntity updatedEntity = buyerVendorCreditEntityRepository.save(entity);
		return buyerVendorCreditEntityAdapter.adapt(updatedEntity);
	}

    public BuyerVendorCredits findAll() {
       return IterableUtils.mapToCollection(buyerVendorCreditEntityRepository.findAll(), buyerVendorCreditEntityAdapter::adapt, BuyerVendorCredits::over);
    }
    
    public BuyerVendorCredits findUserMoneyOwedDetail(User user) {
    	VendorEntity entity = buyerProfileEntityRepository.findByUsername(user.getUsername()).getUser().getVendor();
        return IterableUtils.mapToCollection(buyerVendorCreditEntityRepository.findByVendor(entity), buyerVendorCreditEntityAdapter::adapt, BuyerVendorCredits::over);
     }
	
}
