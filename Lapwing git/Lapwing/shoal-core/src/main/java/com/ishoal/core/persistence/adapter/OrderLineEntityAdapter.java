package com.ishoal.core.persistence.adapter;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.BuyerCredit;
import com.ishoal.core.domain.BuyerCredits;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.OrderLineId;
import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.Product;
import com.ishoal.core.persistence.entity.BuyerCreditEntity;
import com.ishoal.core.persistence.entity.OfferEntity;
import com.ishoal.core.persistence.entity.OrderEntity;
import com.ishoal.core.persistence.entity.OrderLineEntity;
import com.ishoal.core.persistence.entity.PriceBandEntity;
import com.ishoal.core.persistence.entity.ProductEntity;
import com.ishoal.core.persistence.repository.OfferEntityRepository;
import com.ishoal.core.persistence.repository.PriceBandEntityRepository;
import com.ishoal.core.persistence.repository.ProductEntityRepository;

import java.util.List;

import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static com.ishoal.core.domain.OrderLine.anOrderLine;

public class OrderLineEntityAdapter {

    private final ProductEntityRepository productEntityRepository;
    private final OfferEntityRepository offerEntityRepository;
    private final PriceBandEntityRepository priceBandEntityRepository;
    private final BuyerCreditEntityAdapter buyerCreditEntityAdapter;
    private final ProductEntityAdapter productEntityAdapter = new ProductEntityAdapter();
    private final OfferEntityAdapter offerEntityAdapter = new OfferEntityAdapter();
    private final PriceBandEntityAdapter priceBandEntityAdapter = new PriceBandEntityAdapter();

    public OrderLineEntityAdapter(ProductEntityRepository productEntityRepository,
        OfferEntityRepository offerEntityRepository,
        PriceBandEntityRepository priceBandEntityRepository) {

        this.productEntityRepository = productEntityRepository;
        this.offerEntityRepository = offerEntityRepository;
        this.priceBandEntityRepository = priceBandEntityRepository;
        this.buyerCreditEntityAdapter = new BuyerCreditEntityAdapter();
    }

    public OrderLineEntity adapt(OrderLine orderLine, OrderEntity orderEntity) {

        OrderLineEntity orderLineEntity = new OrderLineEntity();
        orderLineEntity.setId(orderLine.getId().asLong());
        orderLineEntity.setCreatedDateTime(orderLine.getCreated());
        orderLineEntity.setModifiedDateTime(orderLine.getModified());
        orderLineEntity.setOrder(orderEntity);
        orderLineEntity.setProduct(adapt(orderLine.getProduct()));
        orderLineEntity.setOffer(adapt(orderLine.getOffer()));
        orderLineEntity.setInitialPriceBand(adapt(orderLine.getInitialPriceBand()));
        orderLineEntity.setCurrentPriceBand(adapt(orderLine.getCurrentPriceBand()));
        orderLineEntity.setQuantity(orderLine.getQuantity());
        orderLineEntity.setNetAmount(orderLine.getAmount().net());
        orderLineEntity.setVatAmount(orderLine.getAmount().vat());
        orderLineEntity.setVatRate(orderLine.getVatRate());
        orderLineEntity.setCredits(adapt(orderLine.getCredits(), orderLineEntity));
        return orderLineEntity;
    }

    private ProductEntity adapt(Product product) {

        return this.productEntityRepository.findByCode(product.getCode().toString());
    }

    private OfferEntity adapt(Offer offer) {

        return this.offerEntityRepository.findOne(offer.getId().asLong());
    }

    private PriceBandEntity adapt(PriceBand priceBand) {

        return this.priceBandEntityRepository.findOne(priceBand.getId().asLong());
    }

    public OrderLine adapt(OrderLineEntity orderLineEntity) {

        return anOrderLine()
            .id(OrderLineId.from(orderLineEntity.getId()))
            .created(orderLineEntity.getCreatedDateTime())
            .modified(orderLineEntity.getModifiedDateTime())
            .product(adapt(orderLineEntity.getProduct()))
            .offer(adapt(orderLineEntity.getOffer()))
            .initialPriceBand(adapt(orderLineEntity.getInitialPriceBand()))
            .currentPriceBand(adapt(orderLineEntity.getCurrentPriceBand()))
            .quantity(orderLineEntity.getQuantity())
            .amount(fromNetAndVat(orderLineEntity.getNetAmount(), orderLineEntity.getVatAmount()))
            .vatRate(orderLineEntity.getVatRate())
            .credits(adapt(orderLineEntity.getCredits()))
            .build();
    }

    private Product adapt(ProductEntity productEntity) {

        return this.productEntityAdapter.adapt(productEntity);
    }

    private Offer adapt(OfferEntity offerEntity) {

        return this.offerEntityAdapter.adapt(offerEntity);
    }

    private PriceBand adapt(PriceBandEntity priceBandEntity) {

        return this.priceBandEntityAdapter.adapt(priceBandEntity);
    }

    private List<BuyerCreditEntity> adapt(BuyerCredits credits, OrderLineEntity orderLineEntity) {

        return IterableUtils.mapToList(credits, it -> buyerCreditEntityAdapter.adapt(it, orderLineEntity));
    }

    private List<BuyerCredit> adapt(List<BuyerCreditEntity> orderLineCreditEntities) {

        return IterableUtils.mapToList(orderLineCreditEntities, it -> buyerCreditEntityAdapter.adapt(it));
    }
}