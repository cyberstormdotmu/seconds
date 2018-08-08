package com.ishoal.core.products;

import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.Products;
import com.ishoal.core.domain.User;
import com.ishoal.core.repository.OfferRepository;
import com.ishoal.core.repository.ProductRepository;


public class ProductService {
	private ProductRepository productRepository;
    private OfferRepository offerRepository;

    public ProductService(ProductRepository productRepository, OfferRepository offerRepository) {
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
      }

    public Product getProduct(ProductCode code) {

        Product product = productRepository.findByCode(code);
        if (product == null || product.isActive() || product.isExpired()) {
            return product;
        }
        throw new InvalidProductException(code);
    }

    public PriceBand getPriceBandForOrder(ProductCode code, long quantity) {
        Product product = getProduct(code);
        Offer offer = product.currentOffer();
        long volumeIncludingThisOrder = offer.getCurrentVolume() + quantity;
        return offer.getPriceBands().getPriceBandForVolume(volumeIncludingThisOrder);
    }

    public long increaseCurrentVolume(Offer offer, long quantityDelta) {
        return this.offerRepository.increaseCurrentVolume(offer.getId(), quantityDelta);
    }

    public Products findByCategory(String category) {
        return this.productRepository.findByCategory(category);
    }
    
    public Products findAll() {
        return this.productRepository.findAll();
    }
    
    public Products findAllSupplierOffers(User user) {
        return this.productRepository.findAllSupplierOffers(user);
    }
    
	public void updateStock(Product product, long quantity) {
		  this.productRepository.updateStock(product.getId(), quantity);
		
	}
    public void deleteProduct(long id) {
        this.productRepository.deleteProduct(id);      
    }
}