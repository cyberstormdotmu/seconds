package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ProductImages implements Streamable<ProductImage> {

	private final List<ProductImage> productImages;

	private ProductImages(Builder builder) {
		this.productImages = new ArrayList<>(builder.productImages);
	}

	private ProductImages(List<ProductImage> productImages) {
		this.productImages = Collections.unmodifiableList(productImages);
	}

	public static Builder someProductImages() {
		return new Builder();
	}

	public static ProductImages emptyProductImages() {
		return new Builder().build();
	}

	public int size() {
		return productImages.size();
	}

	public static ProductImages over(List<ProductImage> productImages) {
		return new ProductImages(productImages);
	}

	@Override
	public Iterator<ProductImage> iterator() {
		return productImages.iterator();
	}

	public ProductImage primary() {
		return productImages.get(0);
	}

	public boolean isEmpty() {

		return productImages.isEmpty();
	}

	public static class Builder {
		private List<ProductImage> productImages = new ArrayList<>();

		private Builder() {
		}

		public Builder productImage(ProductImage productImage) {
			this.productImages.add(productImage);
			return this;
		}

		public ProductImages build() {
			productImages.sort((ProductImage o1, ProductImage o2) -> Integer.compare(o1.getOrder(), o2.getOrder()));
			return new ProductImages(this);
		}
	}

}
