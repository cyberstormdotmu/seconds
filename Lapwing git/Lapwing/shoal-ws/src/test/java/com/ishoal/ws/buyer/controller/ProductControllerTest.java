package com.ishoal.ws.buyer.controller;

import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.ProductTestData;
import com.ishoal.core.domain.Products;
import com.ishoal.core.products.CategoryService;
import com.ishoal.core.products.ProductService;
import com.ishoal.ws.buyer.dto.MoneyDto;
import com.ishoal.ws.buyer.dto.PriceBandDto;
import com.ishoal.ws.buyer.dto.ProductDto;
import com.ishoal.ws.buyer.dto.ProductImageDto;
import com.ishoal.ws.buyer.dto.ProductSpecDto;
import com.ishoal.ws.buyer.dto.ProductSummaryDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ishoal.core.domain.ProductCategory.aProductCategory;
import static com.ishoal.core.domain.ProductTestData.productBuilder;
import static com.ishoal.core.domain.ProductTestData.productBuilderWithNoOffers;
import static com.ishoal.ws.buyer.dto.PriceBandDto.aPriceBand;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    public static final String PRODUCT_CODE = "HPELITE840";
    public static final Product PRODUCT = product(PRODUCT_CODE);
    public static final String OTHER_PRODUCT_CODE = "HPOFFICEJET4620";
    public static final Product OTHER_PRODUCT = product(OTHER_PRODUCT_CODE);
    public static final Product INVALID_PRODUCT = productBuilderWithNoOffers("INVALID_PRODUCT").build();
    private static final String CATEGORY = "Laptops";
    public static final ProductImageDto FIRST_IMAGE = new ProductImageDto(0, "http://ishoal.com/main.image", "Main Image");
    public static final ProductImageDto SECOND_IMAGE = new ProductImageDto(1, "http://ishoal.com/secondary.image", "Secondary Image");

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    private ProductController subject;

    @Before
    public void before() {
        this.subject = new ProductController(productService, categoryService);

        when(this.productService.getProduct(ProductCode.from(PRODUCT_CODE))).thenReturn(PRODUCT);
        when(categoryService.fetchAllCategories()).thenReturn(Arrays.asList(
            aProductCategory().name("Products").build(),
            aProductCategory().name("Laptops").build(),
            aProductCategory().name("Power User").build()
        ));
    }

    private List<ProductSpecDto> expectedProductSpecs() {
        List<ProductSpecDto> specs = new ArrayList<>();
        specs.add(new ProductSpecDto("Processor", "Intel Core i5 processor"));
        specs.add(new ProductSpecDto("Memory", "4GB DDR3L-1600 SDRAM (1 x 4GB)"));
        specs.add(new ProductSpecDto("Drives", "256GB SATA TLC SSD"));
        return specs;
    }

    @Test
    public void shouldReturnProductCode() {
        assertThat(readProduct().getCode(), is(PRODUCT_CODE));
    }

    @Test
    public void shouldReturnProductName() {
        assertThat(readProduct().getName(), is(ProductTestData.NAME));
    }

    @Test
    public void shouldReturnProductDescription() {
        assertThat(readProduct().getDescription(), is(ProductTestData.DESCRIPTION));
    }
    
    @Test
    public void shouldReturnProductStock() {
        assertThat(readProduct().getStock(), is(40000L));
    }
    
    @Test
    public void shouldReturnProductTermsandConditions() {
        assertThat(readProduct().getTermsAndConditions(), is(ProductTestData.TERMS_AND_CONDITIONS));
    }

    @Test
    public void shouldReturnProductSpecs() {
        assertThat(readProduct().getSpecifications(), is(equalTo(expectedProductSpecs())));
    }

    @Test
    public void shouldReturnVendorName() {
        assertThat(readProduct().getVendorName(), is(ProductTestData.VENDOR.getName()));
    }

    @Test
    public void shouldReturnOfferEndDate() {
        assertThat(readProduct().getOfferEndDate(), is(notNullValue()));
    }

    @Test
    public void shouldReturnCurrentVolume() {
        assertThat(readProduct().getCurrentVolume(), is(notNullValue()));
    }

    @Test
    public void shouldReturnPriceBands() {
        assertThat(readProduct().getPriceBands(), is(equalTo(expectedPriceBands())));
    }

    @Test
    public void shouldReturnImages() {
        assertThat(readProduct().getImages(), is(equalTo(expectedProductImages())));
    }

    @Test
    public void shouldReturnCategories() {
        assertThat(readProduct().getCategories(), is(equalTo(expectedCategories())));
    }

    @Test
    public void shouldInvokeTheProductService() {
        subject.readProduct(PRODUCT_CODE);
        verify(productService).getProduct(ProductCode.from(PRODUCT_CODE));
    }

    @Test
    public void findProductsShouldInvokeTheProductService() {
        theServiceWillReturnAListWithTwoProducts();
        subject.findProducts(CATEGORY);
        verify(productService).findByCategory(CATEGORY);
    }

    @Test
    public void findProductsShouldReturnAListOfProductSummaries() {
        theServiceWillReturnAListWithTwoProducts();
        List<ProductSummaryDto> products = subject.findProducts(CATEGORY);
        assertThat(products, hasSize(2));
    }

    @Test
    public void findProductsShouldReturnProductWithCorrectCode() {
        theServiceWillReturnAListWithTwoProducts();
        assertThat(findProductsReturningFirst().getCode(), is(PRODUCT.getCode().toString()));
    }

    @Test
    public void findProductsShouldReturnProductWithCorrectName() {
        theServiceWillReturnAListWithTwoProducts();
        assertThat(findProductsReturningFirst().getName(), is(PRODUCT.getName()));
    }

    @Test
    public void findProductsShouldReturnProductWithCorrectOfferEndDate() {
        theServiceWillReturnAListWithTwoProducts();
        assertThat(findProductsReturningFirst().getOfferEndDate(), is(PRODUCT.currentOffer().getEndDateTime()));
    }

    @Test
    public void findProductsShouldReturnProductWithCorrectInitialPrice() {
        theServiceWillReturnAListWithTwoProducts();
        assertThat(findProductsReturningFirst().getInitialPrice(), is(initialPrice(PRODUCT)));
    }

    @Test
    public void findProductsShouldReturnProductWithCorrectCurrentPrice() {
        theServiceWillReturnAListWithTwoProducts();
        assertThat(findProductsReturningFirst().getCurrentPrice(), is(currentPrice(PRODUCT)));
    }

    @Test
    public void findProductsShouldReturnProductWithCorrectTargetPrice() {
        theServiceWillReturnAListWithTwoProducts();
        assertThat(findProductsReturningFirst().getTargetPrice(), is(targetPrice(PRODUCT)));
    }

    @Test
    public void findProductsShouldReturnProductWithCorrectImage() {
        theServiceWillReturnAListWithTwoProducts();
        assertThat(findProductsReturningFirst().getImage(), is(FIRST_IMAGE));
    }

    @Test
    public void shouldReturnAllValidProducts() {
        theServiceWillReturnAListWithTwoValidProductsAndOneInvalidProduct();
        assertThat(subject.findProducts(CATEGORY).size(), is(2));
    }

    @Test
    public void shouldFetchCategoriesFromService() {

        subject.getAllCategories();

        verify(categoryService).fetchAllCategories();
    }

    @Test
    public void shouldReturnAllProductCategories() {

        List<String> allCategories = subject.getAllCategories();

        assertThat(allCategories.size(), is(3));
    }

    private BigDecimal initialPrice(Product product) {
        return product.currentOffer().getInitialPriceBand().getBuyerPrice();
    }

    private BigDecimal currentPrice(Product product) {
        return product.currentOffer().getCurrentPriceBand().getBuyerPrice();
    }

    private BigDecimal targetPrice(Product product) {
        return product.currentOffer().getTargetPriceBand().getBuyerPrice();
    }

    private ProductSummaryDto findProductsReturningFirst() {
        return subject.findProducts(CATEGORY).get(0);
    }

    private void theServiceWillReturnAListWithTwoProducts() {
        when(productService.findByCategory(CATEGORY)).thenReturn(twoProducts());
    }

    private void theServiceWillReturnAListWithTwoValidProductsAndOneInvalidProduct() {
        when(productService.findByCategory(CATEGORY)).thenReturn(Products.over(PRODUCT,
            OTHER_PRODUCT, INVALID_PRODUCT));
    }

    private Products twoProducts() {
        return Products.over(PRODUCT, OTHER_PRODUCT);
    }

    private static Product product(String code) {
        return productBuilder(ProductCode.from(code)).build();
    }

    private ProductDto readProduct() {
        return subject.readProduct(PRODUCT_CODE);
    }

    private List<PriceBandDto> expectedPriceBands() {
        List<PriceBandDto> dtos = new ArrayList<>();
        dtos.add(aPriceBand().minVolume(0L).maxVolume(999L).buyerPrice(new MoneyDto("1000.00")).build());
        dtos.add(aPriceBand().minVolume(1000L).buyerPrice(new MoneyDto("900.00")).build());
        return dtos;
    }

    private List<ProductImageDto> expectedProductImages() {
        List<ProductImageDto> dtos = new ArrayList<>();
        dtos.add(FIRST_IMAGE);
        dtos.add(SECOND_IMAGE);
        return dtos;
    }

    private List<String> expectedCategories() {
        return Arrays.asList("Laptops", "Power User");
    }

}