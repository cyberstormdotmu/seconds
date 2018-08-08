package com.ishoal.ws.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.core.domain.Products;
import com.ishoal.core.domain.User;
import com.ishoal.core.products.ProductService;
import com.ishoal.ws.admin.dto.OfferListingDto;
import com.ishoal.ws.admin.dto.adapter.OfferListingDtoAdapter;

@RestController
@RequestMapping("/ws/admin/offers")
public class AdminOfferListingController {
    private static final Logger logger = LoggerFactory.getLogger(AdminOfferListingController.class);

    private final ProductService productService;
    private final OfferListingDtoAdapter adapter = new OfferListingDtoAdapter();

    public AdminOfferListingController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<OfferListingDto> findAllOffers() {
        logger.info("Admin request to find all offers");
        Products products = productService.findAll();
        return adapter.adapt(products);
    }
    @RequestMapping(method = RequestMethod.GET, value = "supplier")
    public List<OfferListingDto> findAllOffers(User user) {
        logger.info("Admin request to find all offers");
        Products products = productService.findAllSupplierOffers(user);
        return adapter.adapt(products);
    }
}
