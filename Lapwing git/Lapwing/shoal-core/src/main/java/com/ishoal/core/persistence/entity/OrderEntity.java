package com.ishoal.core.persistence.entity;

import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.PaymentStatus;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.List;

@Entity(name = "Order")
@Table(name = "ORDERS")
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUYER_ID", referencedColumnName = "ID")
	private UserEntity buyer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUYER_ORGANISATION_ID", referencedColumnName = "ID")
	private OrganisationEntity buyerOrganisation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "ID")
	private VendorEntity vendor;

	@Column(name = "REFERENCE")
	private String reference;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Column(name = "PAYMENT_STATUS")
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	@Version
	@Column(name = "VERSION")
	private long version;

	@Column(name = "INVOICE_DATE")
	private DateTime invoiceDate;

	@CreatedDate
	@Column(name = "CREATED_DATETIME")
	private DateTime created;

	@LastModifiedDate
	@Column(name = "MODIFIED_DATETIME")
	private DateTime modified;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
	private List<OrderLineEntity> lines;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
	private List<OrderPaymentEntity> payments;

	@ElementCollection
	@CollectionTable(name = "ORDER_ADDRESSES", joinColumns = @JoinColumn(name = "ORDER_ID") )
	private List<OrderAddressEmbeddable> addresses;

	@Column(name = "PAYMENT_DUE_DATE")
	private DateTime dueDate;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public DateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(DateTime dueDate) {
		this.dueDate = dueDate;
	}

	public void setBuyer(UserEntity buyer) {
		this.buyer = buyer;
	}

	public UserEntity getBuyer() {
		return buyer;
	}

	public OrganisationEntity getBuyerOrganisation() {
		return buyerOrganisation;
	}

	public void setBuyerOrganisation(OrganisationEntity buyerOrganisation) {
		this.buyerOrganisation = buyerOrganisation;
	}

	public VendorEntity getVendor() {
		return vendor;
	}

	public void setVendor(VendorEntity vendor) {
		this.vendor = vendor;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getReference() {
		return reference;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public void setLines(List<OrderLineEntity> lines) {
		this.lines = lines;
	}

	public List<OrderLineEntity> getLines() {
		return this.lines;
	}

	public List<OrderPaymentEntity> getPayments() {
		return payments;
	}

	public void setPayments(List<OrderPaymentEntity> payments) {
		this.payments = payments;
	}

	public List<OrderAddressEmbeddable> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<OrderAddressEmbeddable> addresses) {
		this.addresses = addresses;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public DateTime getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(DateTime invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public DateTime getModified() {
		return modified;
	}

	public void setModified(DateTime modified) {
		this.modified = modified;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof OrderEntity)) {
			return false;
		}

		OrderEntity that = (OrderEntity) o;

		return new EqualsBuilder().append(reference, that.reference).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(reference).toHashCode();
	}
}