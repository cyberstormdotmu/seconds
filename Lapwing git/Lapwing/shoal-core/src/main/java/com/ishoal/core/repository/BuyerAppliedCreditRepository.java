package com.ishoal.core.repository;

import static com.ishoal.common.util.IterableUtils.stream;

import java.util.List;

import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.BuyerAppliedCredits;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.persistence.adapter.BuyerAppliedCreditEntityAdapter;
import com.ishoal.core.persistence.adapter.VendorEntityAdapter;
import com.ishoal.core.persistence.entity.BuyerAppliedCreditEntity;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;
import com.ishoal.core.persistence.entity.BuyerVendorCreditEntity;
import com.ishoal.core.persistence.repository.BuyerAppliedCreditEntityRepository;
import com.ishoal.core.persistence.repository.BuyerProfileEntityRepository;
import com.ishoal.core.persistence.repository.BuyerVendorCreditEntityRepository;

public class BuyerAppliedCreditRepository {

	private final BuyerAppliedCreditEntityRepository appliedCreditEntityRepository;
	private final BuyerAppliedCreditEntityAdapter appliedCreditEntityAdapter;
	private final BuyerVendorCreditEntityRepository buyerVendorCreditEntityRepository;
	private final BuyerProfileEntityRepository buyerProfileEntityRepository;
	private final VendorEntityAdapter vendorEntityAdapter= new VendorEntityAdapter();

	public BuyerAppliedCreditRepository(BuyerAppliedCreditEntityRepository appliedCreditEntityRepository,
			BuyerVendorCreditEntityRepository buyerVendorCreditEntityRepository,
			BuyerProfileEntityRepository buyerProfileEntityRepository) {
		this.appliedCreditEntityRepository = appliedCreditEntityRepository;
		this.appliedCreditEntityAdapter = new BuyerAppliedCreditEntityAdapter();
		this.buyerVendorCreditEntityRepository = buyerVendorCreditEntityRepository;
		this.buyerProfileEntityRepository = buyerProfileEntityRepository;
	}

	public BuyerAppliedCredits findCreditsSpentOnOrder(OrderReference orderReference) {

		List<BuyerAppliedCreditEntity> entities = appliedCreditEntityRepository
				.findByOrderSpentOnReference(orderReference.asString());

		return appliedCreditEntityAdapter.adapt(entities);
	}

	public void save(BuyerAppliedCredits appliedCredits) {
		stream(appliedCredits).forEach(it -> {
			BuyerAppliedCreditEntity existingEntity = appliedCreditEntityRepository.findOne(it.getId().asLong());
			BuyerAppliedCreditEntity entity = appliedCreditEntityAdapter.adapt(it, existingEntity.getBuyerCredit());
			appliedCreditEntityRepository.save(entity);
		});
	}

	public void saveBuyerVendorCredit(BuyerAppliedCredits appliedCredits, User user, Vendor vendor) {

		BuyerProfileEntity buyerProfileEntity = buyerProfileEntityRepository.findByUsername(user.getUsername());
		BuyerVendorCreditEntity buyerVendorCreditEntity = buyerVendorCreditEntityRepository.findByVendorAndBuyer(vendorEntityAdapter.adapt(vendor), buyerProfileEntity);
				stream(appliedCredits).forEach(it -> {
			BuyerAppliedCreditEntity entity = appliedCreditEntityAdapter.adapt(it, buyerVendorCreditEntity);
			appliedCreditEntityRepository.save(entity);
		});
	}

}
