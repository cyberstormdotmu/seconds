/*package com.ishoal.core.repository;

import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCategory;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.Products;
import com.ishoal.core.persistence.entity.CategoryEntity;
import com.ishoal.core.persistence.entity.ProductEntity;
import com.ishoal.core.persistence.entity.ProductVatRateEntity;
import com.ishoal.core.persistence.entity.VatRateEntity;
import com.ishoal.core.persistence.entity.VendorEntity;
import com.ishoal.core.persistence.repository.CategoryEntityRepository;
import com.ishoal.core.persistence.repository.ProductEntityRepository;
import com.ishoal.core.persistence.repository.VatRateEntityRepository;
import com.ishoal.core.persistence.repository.VendorEntityRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ishoal.core.domain.ProductTestData.productBuilder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductRepositoryTest {

    private static final DateTime START_DATE_TIME = DateTime.now();
    private static final DateTime END_DATE_TIME = DateTime.now().plusYears(1);
    private static final String PRODUCT_CODE = "HPELITE840";
    private static final BigDecimal VAT_RATE = new BigDecimal("20.00");
    private static final String CATEGORY = "Power User";
    private static final String VENDOR = "HP";
    private static final String STANDARD = "STANDARD";

    @Mock
    private ProductEntityRepository productEntityRepository;

    @Mock
    private CategoryEntityRepository categoryEntityRepository;

    @Mock
    private VendorEntityRepository vendorEntityRepository;

    @Mock
    private VatRateEntityRepository vatRateEntityRepository;


    private ProductRepository repository;

    private ArgumentCaptor<ProductEntity> persistedProductEntity = ArgumentCaptor.forClass(ProductEntity.class);


    @Before
    public void before() {
        repository = new ProductRepository(productEntityRepository, categoryEntityRepository, vendorEntityRepository,
            vatRateEntityRepository);
        whenFindByCodeReturnAProductEntity();
        whenFindCategoryByNameReturnOne();
        whenFindByCategoryReturnAProductEntity();
        whenFindByVendorReturnAVendorEntity();
        whenEntityPersistedReturnEntity();
    }

    private void whenEntityPersistedReturnEntity() {
        when(productEntityRepository.save(persistedProductEntity.capture()))
            .thenAnswer(p -> persistedProductEntity.getValue());

    }

    @Test
    public void shouldHaveVatRate() {
        assertThat(findProduct().currentVatRate().getRate(), is(VAT_RATE));
    }

    @Test
    public void findByCategoryShouldReturnTheMatchingProducts() {
        Products products = repository.findByCategory(CATEGORY);
        assertThat(products.size(), is(1));
    }

    @Test
    public void findByCategoryShouldReturnEmptyProductsForAnInvalidCategory() {
        when(categoryEntityRepository.findByName(CATEGORY)).thenReturn(null);

        Products products = repository.findByCategory(CATEGORY);
        assertThat(products.size(), is(0));
    }

    @Test
    public void shouldLookupRelatedEntities_whenSavingProduct() {

        repository.save(productBuilder(PRODUCT_CODE).build());

        verify(categoryEntityRepository).findByName(CATEGORY);
        verify(vendorEntityRepository).findByName(VENDOR);
        verify(vatRateEntityRepository).findByVatCode(STANDARD);
    }

    @Test
    public void shouldPersistEntity_whenSavingProduct() {
        repository.save(productBuilder(PRODUCT_CODE).build());

        verify(productEntityRepository).save(any(ProductEntity.class));
    }

    @Test
    public void shouldSaveProductInformation_whenSavingProduct() {
        repository.save(productBuilder(PRODUCT_CODE).category(ProductCategory.aProductCategory().name(CATEGORY).build()).build());

        verify(productEntityRepository).save(persistedProductEntity.capture());

        assertThat(persistedProductEntity.getValue().getCategory().getName(), is(CATEGORY));
        assertThat(persistedProductEntity.getValue().getVendor().getName(), is(VENDOR));
    }

    @Test
    public void shouldReturnTheSavedProduct_afterProductPersisted() {

        Product product = repository.save(productBuilder(PRODUCT_CODE).build());

        assertThat(product.getCode().toString(), is(PRODUCT_CODE));
    }


    private Product findProduct() {
        return repository.findByCode(ProductCode.from(PRODUCT_CODE));
    }

    private void whenFindByCodeReturnAProductEntity() {
        when(productEntityRepository.findByCode(PRODUCT_CODE)).thenReturn(productEntity());
    }

    private void whenFindByCategoryReturnAProductEntity() {
        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(productEntity());
        when(productEntityRepository.findByCategory(any(CategoryEntity.class))).thenReturn(productEntities);
    }

    private void whenFindByVendorReturnAVendorEntity() {
        VendorEntity vendorEntity = new VendorEntity();
        vendorEntity.setName(VENDOR);
        when(vendorEntityRepository.findByName(VENDOR)).thenReturn(vendorEntity);
    }

    private ProductEntity productEntity() {
        ProductEntity entity = new ProductEntity();
        entity.setId(12L);
        entity.setCode(PRODUCT_CODE);
        entity.setStock(40000L);
        entity.setTermsAndConditions("OFFER valid for 1 week only");
        entity.setDescription("Thin and light laptop");
        entity.setCategory(new CategoryEntity());
        entity.setImages(new ArrayList<>());
        entity.setName("HP Elite 840");
        entity.setOffers(new ArrayList<>());
        entity.setProductSpecs(new ArrayList<>());
        entity.setVendor(new VendorEntity());
        entity.setVatRates(vatRates(entity));
        return entity;
    }

    private List<ProductVatRateEntity> vatRates(ProductEntity productEntity) {
        ArrayList<ProductVatRateEntity> vatRates = new ArrayList<>();
        ProductVatRateEntity vatRateEntity = productVatRateEntity(productEntity);
        vatRates.add(vatRateEntity);
        return vatRates;
    }

    private ProductVatRateEntity productVatRateEntity(ProductEntity product) {
        ProductVatRateEntity entity = new ProductVatRateEntity();
        entity.setProduct(product);
        entity.setId(33L);
        entity.setStartDateTime(START_DATE_TIME);
        entity.setEndDateTime(END_DATE_TIME);
        entity.setVatRate(vatRate());
        return entity;
    }

    private VatRateEntity vatRate() {
        VatRateEntity entity = new VatRateEntity();
        entity.setId(45L);
        entity.setVatCode(STANDARD);
        entity.setVatRate(VAT_RATE);
        return entity;
    }

    private void whenFindCategoryByNameReturnOne() {
        when(categoryEntityRepository.findByName(CATEGORY)).thenReturn(category());
    }

    private CategoryEntity category() {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(22L);
        entity.setName(CATEGORY);
        entity.setChildren(childCategories());
        return entity;
    }

    private List<CategoryEntity> childCategories() {
        return Collections.emptyList();
    }
}*/