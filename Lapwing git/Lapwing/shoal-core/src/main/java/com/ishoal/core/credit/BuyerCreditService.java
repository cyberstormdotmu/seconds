package com.ishoal.core.credit;

import java.util.List;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.BuyerCredits;
import com.ishoal.core.domain.User;
import com.ishoal.core.persistence.adapter.BuyerCreditEntityAdapter;
import com.ishoal.core.persistence.entity.BuyerCreditEntity;
import com.ishoal.core.persistence.repository.BuyerCreditEntityRepository;

public class BuyerCreditService {
	private final BuyerCreditEntityRepository creditEntityRepository;

	private BuyerCreditEntityAdapter buyerCreditEntityAdapter = new BuyerCreditEntityAdapter();

	public BuyerCreditService(BuyerCreditEntityRepository creditEntityRepository) {
		this.creditEntityRepository = creditEntityRepository;
	}

	public BuyerCredits fetchBuyerCredits(User buyer) {
		List<BuyerCreditEntity> creditEntities = creditEntityRepository.findCreditsForBuyer(buyer.getId().asLong());

		return IterableUtils.mapToCollection(creditEntities, buyerCreditEntityAdapter::adapt, BuyerCredits::over);
	}
}
