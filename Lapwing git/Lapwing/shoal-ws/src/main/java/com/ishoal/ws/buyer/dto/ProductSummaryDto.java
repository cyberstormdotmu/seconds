package com.ishoal.ws.buyer.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class ProductSummaryDto {
	private Long productId;
	private String code;
	private String name;
	private String category;
	private DateTime offerEndDate;
	private BigDecimal initialPrice;
	private BigDecimal currentPrice;
	private BigDecimal targetPrice;
	private ProductImageDto image;
	private List<PriceBandDto> priceBands;
	private Long stock;
	private Long currentVolume;

	private ProductSummaryDto(Builder builder) {
		productId = builder.productId;
		code = builder.code;
		name = builder.name;
		category = builder.category;
		offerEndDate = builder.offerEndDate;
		initialPrice = builder.initialPrice;
		currentPrice = builder.currentPrice;
		targetPrice = builder.targetPrice;
		image = builder.image;
		priceBands=builder.priceBands;
		stock=builder.stock;
		currentVolume=builder.currentVolume;
	} 
	public static Builder aProductSummary() {
		return new Builder();
	}

	public Long getProductId() {
		return productId;
	}
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public DateTime getOfferEndDate() {
		return offerEndDate;
	}

	public BigDecimal getInitialPrice() {
		return initialPrice;
	}

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public BigDecimal getTargetPrice() {
		return targetPrice;
	}

	public ProductImageDto getImage() {
		return image;
	}

	public List<PriceBandDto> getPriceBands() {
		return priceBands;
	}

	public Long getStock() {
		return stock;
	}

	public Long getCurrentVolume() {
		return currentVolume;
	}

	public static final class Builder {
		private Long productId;
		private String code;
		private String name;
		private String category;
		private DateTime offerStartDate;
		private DateTime offerEndDate;
		private BigDecimal initialPrice;
		private BigDecimal currentPrice;
		private BigDecimal targetPrice;
		private Long currentVolume;
		private ProductVatRateDto vatRate;
		private ProductImageDto image;
		private List<PriceBandDto> priceBands = new ArrayList<>();
		private Long stock;

		private Builder() {
		}

		public Builder productId(Long val) {

			productId = val;
			return this;
		}

		public Builder code(String val) {
			code = val;
			return this;
		}

		public Builder name(String val) {
			name = val;
			return this;
		}

		public Builder category(String val) {
			category = val;
			return this;
		}

		public Builder offerEndDate(DateTime val) {
			offerEndDate = val;
			return this;
		}

		public Builder initialPrice(BigDecimal val) {
			initialPrice = val;
			return this;
		}

		public Builder currentPrice(BigDecimal val) {
			currentPrice = val;
			return this;
		}

		public Builder targetPrice(BigDecimal val) {
			targetPrice = val;
			return this;
		}

		public Builder image(ProductImageDto val) {
			image = val;
			return this;
		}


		public Builder offerStartDate(DateTime val) {

			offerStartDate = val;
			return this;
		}
		public Builder currentVolume(Long val) {

			currentVolume = val;
			return this;
		}

		public Builder vatRate(ProductVatRateDto val) {

			vatRate = val;
			return this;
		}

		public Builder priceBands(List<PriceBandDto> val) {

			priceBands = val;
			return this;
		}

		public Builder stock(Long val) {

			stock = val;
			return this;
		}

		public ProductSummaryDto build() {
			return new ProductSummaryDto(this);
		}


	}
}
