package com.ishoal.core.persistence.adapter;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.Address;
import com.ishoal.core.domain.BuyerAppliedCredits;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.OrderId;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.OrderLines;
import com.ishoal.core.domain.OrderPayment;
import com.ishoal.core.domain.OrderPayments;
import com.ishoal.common.domain.OrderReference;
import com.ishoal.core.domain.Organisation;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.Vendor;
import com.ishoal.core.persistence.entity.OrderAddressEmbeddable;
import com.ishoal.core.persistence.entity.OrderEntity;
import com.ishoal.core.persistence.entity.OrderLineEntity;
import com.ishoal.core.persistence.entity.OrderPaymentEntity;
import com.ishoal.core.persistence.entity.OrganisationEntity;
import com.ishoal.core.persistence.entity.UserEntity;
import com.ishoal.core.persistence.entity.VendorEntity;
import com.ishoal.core.persistence.repository.UserEntityRepository;

import java.util.ArrayList;
import java.util.List;

import static com.ishoal.core.domain.Address.anAddress;
import static com.ishoal.core.domain.Order.anOrder;

public class OrderEntityAdapter {

    private final UserEntityRepository userEntityRepository;
    private final UserEntityAdapter userEntityAdapter;
    private final OrganisationEntityAdapter buyerOrganisationEntityAdapter;
    private final VendorEntityAdapter vendorEntityAdapter;
    private final OrderLineEntityAdapter orderLineEntityAdapter;
    private final OrderPaymentEntityAdapter paymentEntityAdapter;

    public OrderEntityAdapter(UserEntityRepository userEntityRepository,
            OrderLineEntityAdapter orderLineEntityAdapter) {
        this.userEntityRepository = userEntityRepository;
        this.orderLineEntityAdapter = orderLineEntityAdapter;
        this.paymentEntityAdapter = new OrderPaymentEntityAdapter();
        this.userEntityAdapter = new UserEntityAdapter();
        this.buyerOrganisationEntityAdapter = new OrganisationEntityAdapter();
        this.vendorEntityAdapter = new VendorEntityAdapter();
    }

    public OrderEntity adapt(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(order.getId().asLong());
        orderEntity.setBuyer(adapt(order.getBuyer()));
        orderEntity.setBuyerOrganisation(adapt(order.getBuyerOrganisation()));
        orderEntity.setVendor(adapt(order.getVendor()));
        orderEntity.setReference(order.getReference().asString());
        orderEntity.setStatus(order.getStatus());
        orderEntity.setPaymentStatus(order.getPaymentStatus());
        orderEntity.setInvoiceDate(order.getInvoiceDate());
        orderEntity.setVersion(order.getVersion());
        orderEntity.setCreated(order.getCreated());
        orderEntity.setModified(order.getModified());
        orderEntity.setLines(adapt(order.getLines(), orderEntity));
        orderEntity.setPayments(adapt(order.getPayments(), orderEntity));
        orderEntity.setAddresses(adaptAddresses(order));
        orderEntity.setDueDate(order.getDueDate());
        return orderEntity;
    }

    public Order adapt(OrderEntity orderEntity, BuyerAppliedCredits appliedCredits) {
        return anOrder()
                .id(OrderId.from(orderEntity.getId()))
                .buyer(adaptBuyer(orderEntity))
                .buyerOrganisation(adaptBuyerOrganisation(orderEntity))
                .vendor(adaptVendor(orderEntity))
                .reference(OrderReference.from(orderEntity.getReference()))
                .status(orderEntity.getStatus())
                .paymentStatus(orderEntity.getPaymentStatus())
                .version(orderEntity.getVersion())
                .invoiceDate(orderEntity.getInvoiceDate())
                .invoiceAddress(adaptInvoiceAddress(orderEntity))
                .deliveryAddress(adaptDeliveryAddress(orderEntity))
                .created(orderEntity.getCreated())
                .modified(orderEntity.getModified())
                .lines(adaptLines(orderEntity))
                .payments(adaptPayments(orderEntity))
                .appliedCredits(appliedCredits)
                .dueDate(orderEntity.getDueDate())
                .build();
    }
    
    public Order adaptWithoutCredits(OrderEntity orderEntity) {
        return anOrder()
                .id(OrderId.from(orderEntity.getId()))
                .buyer(adaptBuyer(orderEntity))
                .buyerOrganisation(adaptBuyerOrganisation(orderEntity))
                .vendor(adaptVendor(orderEntity))
                .reference(OrderReference.from(orderEntity.getReference()))
                .status(orderEntity.getStatus())
                .paymentStatus(orderEntity.getPaymentStatus())
                .version(orderEntity.getVersion())
                .invoiceDate(orderEntity.getInvoiceDate())
                .invoiceAddress(adaptInvoiceAddress(orderEntity))
                .deliveryAddress(adaptDeliveryAddress(orderEntity))
                .created(orderEntity.getCreated())
                .modified(orderEntity.getModified())
                .lines(adaptLines(orderEntity))
                .payments(adaptPayments(orderEntity))
                .dueDate(orderEntity.getDueDate())
                .build();
    }

    
      private List<OrderAddressEmbeddable> adaptAddresses(Order order) {
        List<OrderAddressEmbeddable> addresses = new ArrayList<>();

        OrderAddressEmbeddable address = adaptAddress(order.getInvoiceAddress());
        address.setInvoiceAddress(true);
        addresses.add(address);

        if(order.getDeliveryAddress().equals(order.getInvoiceAddress())) {
            address.setDeliveryAddress(true);
        } else {
            OrderAddressEmbeddable deliveryAddress = adaptAddress(order.getDeliveryAddress());
            deliveryAddress.setDeliveryAddress(true);
            addresses.add(deliveryAddress);
        }

        return addresses;
    }

    private OrderAddressEmbeddable adaptAddress(Address address) {
        OrderAddressEmbeddable entity = new OrderAddressEmbeddable();
        entity.setOrganisationName(address.getOrganisationName());
        entity.setDepartmentName(address.getDepartmentName());
        entity.setBuildingName(address.getBuildingName());
        entity.setStreetAddress(address.getStreetAddress());
        entity.setLocality(address.getLocality());
        entity.setPostTown(address.getPostTown());
        entity.setPostcode(address.getPostcode());

        return entity;
    }

    private Address adaptInvoiceAddress(OrderEntity orderEntity) {
        for(OrderAddressEmbeddable address : orderEntity.getAddresses()) {
            if(address.isInvoiceAddress()) {
                return adaptAddress(address);
            }
        }
        return null;
    }

    private Address adaptDeliveryAddress(OrderEntity orderEntity) {
        for(OrderAddressEmbeddable address : orderEntity.getAddresses()) {
            if(address.isDeliveryAddress()) {
                return adaptAddress(address);
            }
        }
        return null;
    }

    private Address adaptAddress(OrderAddressEmbeddable entity) {
        return anAddress()
                .organisationName(entity.getOrganisationName())
                .departmentName(entity.getDepartmentName())
                .buildingName(entity.getBuildingName())
                .streetAddress(entity.getStreetAddress())
                .locality(entity.getLocality())
                .postTown(entity.getPostTown())
                .postcode(entity.getPostcode())
                .build();
    }

    private UserEntity adapt(User buyer) {
        return this.userEntityRepository.findByUsernameIgnoreCase(buyer.getUsername());
    }

    private OrganisationEntity adapt(Organisation buyerOrganisation) {
        return this.buyerOrganisationEntityAdapter.adapt(buyerOrganisation);
    }

    private VendorEntity adapt(Vendor vendor) {
        return this.vendorEntityAdapter.adapt(vendor);
    }

    private List<OrderLineEntity> adapt(OrderLines orderLines, OrderEntity orderEntity) {
        return IterableUtils.mapToList(orderLines, it -> orderLineEntityAdapter.adapt(it, orderEntity));
    }

    private List<OrderPaymentEntity> adapt(OrderPayments payments, OrderEntity orderEntity) {
        return IterableUtils.mapToList(payments, it -> paymentEntityAdapter.adapt(it, orderEntity));
    }

    private User adaptBuyer(OrderEntity orderEntity) {
        return this.userEntityAdapter.adaptWithPassword(orderEntity.getBuyer());
    }

    private Organisation adaptBuyerOrganisation(OrderEntity orderEntity) {
        return buyerOrganisationEntityAdapter.adapt(orderEntity.getBuyerOrganisation());
    }

    private Vendor adaptVendor(OrderEntity orderEntity) {
        return vendorEntityAdapter.adapt(orderEntity.getVendor());
    }

    private List<OrderLine> adaptLines(OrderEntity orderEntity) {
        return IterableUtils.mapToList(orderEntity.getLines(), orderLineEntityAdapter::adapt);
    }

    private List<OrderPayment> adaptPayments(OrderEntity orderEntity) {
        return IterableUtils.mapToList(orderEntity.getPayments(), paymentEntityAdapter::adapt);
    }
}