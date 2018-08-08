package com.ishoal.core.repository;

import com.ishoal.core.domain.BuyerCreditInfo;
import com.ishoal.core.persistence.adapter.BuyerCreditInfoAdapter;
import com.ishoal.core.persistence.entity.AddressEntity;
import com.ishoal.core.persistence.entity.BuyerCreditInfoEntity;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;
import com.ishoal.core.persistence.repository.AddressEntityRepository;
import com.ishoal.core.persistence.repository.BuyerCreditInfoEntityRepository;
import com.ishoal.core.persistence.repository.BuyerProfileEntityRepository;

public class BuyerCreditInfoRepository {
	private final AddressEntityRepository addressEntityRepository;
	private final BuyerProfileEntityRepository buyerProfileEntityRepository;
	private final BuyerCreditInfoEntityRepository buyerCreditInfoEntityRepository;
    private BuyerCreditInfoAdapter buyerCreditEntityAdapter = new BuyerCreditInfoAdapter();

	public BuyerCreditInfoRepository(AddressEntityRepository addressEntityRepository,
			  						 BuyerProfileEntityRepository buyerProfileEntityRepository,
			  						 BuyerCreditInfoEntityRepository buyerCreditInfoEntityRepository) {
		this.addressEntityRepository = addressEntityRepository;
		this.buyerProfileEntityRepository = buyerProfileEntityRepository;
		this.buyerCreditInfoEntityRepository = buyerCreditInfoEntityRepository;
	}
	public BuyerCreditInfo saveCreditInfo(BuyerCreditInfo creditDetails) {
		BuyerCreditInfoEntity buyerCreditEntity = buyerCreditEntityAdapter.adapt(creditDetails);
		
		BuyerProfileEntity buyerProfile = buyerProfileEntityRepository
				.findByUsername(creditDetails.getBuyer().getUser().getUsername());
		
		BuyerCreditInfoEntity alreadyExisttEntity = buyerCreditInfoEntityRepository
				.findByBuyerId(buyerProfile.getId());
		
		if(alreadyExisttEntity!=null){
			buyerCreditEntityAdapter.adapt(alreadyExisttEntity);
		}
		
		AddressEntity invoiceAddress = addressEntityRepository
				.findByDepartmentName(creditDetails.getInvoiceAddress().getDepartmentName(), buyerProfile.getId());
		AddressEntity deliveryAddress = addressEntityRepository
				.findByDepartmentName(creditDetails.getDeliveryAddress().getDepartmentName(), buyerProfile.getId());
		AddressEntity registerAddress = addressEntityRepository
				.findByDepartmentName(creditDetails.getRegisteredAddress().getDepartmentName(), buyerProfile.getId());
		
		buyerCreditEntity.setBuyer(buyerProfile);
		buyerCreditEntity.setInvoiceAddress(invoiceAddress);
		buyerCreditEntity.setDeliveryAddress(deliveryAddress);
		buyerCreditEntity.setRegisteredAddress(registerAddress);	

		BuyerCreditInfoEntity buyerCreditEntityResonse = buyerCreditInfoEntityRepository.save(buyerCreditEntity);
		return buyerCreditEntityAdapter.adapt(buyerCreditEntityResonse);

	}

	public BuyerCreditInfo fetchBuyerCreditInfo(Long buyerId) {
		BuyerCreditInfoEntity buyerCreditEntityResonse = buyerCreditInfoEntityRepository.findByBuyerId(buyerId);
		return buyerCreditEntityAdapter.adapt(buyerCreditEntityResonse);
	}
}
