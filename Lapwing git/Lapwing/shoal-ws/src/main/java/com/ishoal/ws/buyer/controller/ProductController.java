package com.ishoal.ws.buyer.controller;

import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCategory;
import com.ishoal.core.products.CategoryService;
import com.ishoal.core.products.ProductService;
import com.ishoal.ws.buyer.dto.ProductDto;
import com.ishoal.ws.buyer.dto.ProductSummaryDto;
import com.ishoal.ws.buyer.dto.adapter.ProductDtoAdapter;
import com.ishoal.ws.buyer.dto.adapter.ProductSummaryDtoAdapter;
import com.ishoal.ws.common.dto.CategoryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ishoal.common.util.IterableUtils.mapToList;
import static com.ishoal.core.domain.ProductCode.from;

@RestController
@RequestMapping("/ws/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductDtoAdapter productAdapter = new ProductDtoAdapter();
    private final ProductSummaryDtoAdapter productSummaryDtoAdapter = new ProductSummaryDtoAdapter();

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService =  productService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "{code}", method = RequestMethod.GET)
    public ProductDto readProduct(@PathVariable("code") String code) {
        logger.info("Fetch Product for product with code [{}]", code);
        return productAdapter.adapt(productService.getProduct(from(code)));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ProductSummaryDto> findProducts(@RequestParam(value = "category", required = false) String category) {
        logger.info("Find Products for products in category [{}]", category);

        return productSummaryDtoAdapter.adapt(productService.findByCategory(category).filter(Product::isActive));
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<String> getAllCategories() {
        logger.info("Fetch all product categories");
        return mapToList(categoryService.fetchAllCategories(), ProductCategory::getName);
    }

    @RequestMapping(value = "/categories/{name}", method = RequestMethod.GET)
    public CategoryDto findCategory(@PathVariable("name") String categoryName) {
        CategoryDto.Builder builder = CategoryDto.aCategory().name(categoryName);

        for(ProductCategory category : categoryService.fetchAllCategories()) {
            if(category.getName().equals(categoryName)) {
                ProductCategory parent = category.getParent();
                while(parent != null) {
                    builder.parent(parent.getName());
                    parent = parent.getParent();
                }
            }
            if(category.getParent() != null && category.getParent().getName().equals(categoryName)) {
                builder.child(category.getName());
            }
        }

        return builder.build();
    }
}
