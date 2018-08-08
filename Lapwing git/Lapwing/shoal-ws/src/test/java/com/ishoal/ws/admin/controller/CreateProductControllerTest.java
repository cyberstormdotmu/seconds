package com.ishoal.ws.admin.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ishoal.core.domain.Product;
import com.ishoal.core.products.CreateProductService;
import com.ishoal.ws.admin.dto.AdminProductDto;

@RunWith(MockitoJUnitRunner.class)
public class CreateProductControllerTest {

    @Mock
    private CreateProductService createProductService;

    private CreateProductController createProductController;
    public static final String PRODUCT_CODE = "aProduct";

    /*@Before
    public void before() {

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        when(createProductService.createProduct(productCaptor.capture()))
            .thenAnswer(invocation -> productCaptor.getValue());

        createProductController = new CreateProductController(createProductService);
    }*/

    @Test
    public void shouldCallProductService_whenProductPOSTed() throws URISyntaxException {

        createProductController.createProduct(aNewProductDto());

        verify(createProductService).createProduct(any(Product.class));
    }

    @Test
    public void shouldReturnCreatedStatus_whenProductPOSTed() throws URISyntaxException {

        ResponseEntity<?> responseEntity = createProductController.createProduct(aNewProductDto());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CREATED));
    }

    @Test
    public void shouldReturnTheSavedProduct_whenProductPOSTed() throws URISyntaxException {
        ResponseEntity<?> responseEntity = createProductController.createProduct(aNewProductDto());
        assertThat(responseEntity.getBody(), is(notNullValue()));
    }

    @Test
    public void shouldReturnTheSavedProductLocation_WhenProductPOSTed() throws URISyntaxException {
        ResponseEntity<?> responseEntity = createProductController.createProduct(aNewProductDto());
        assertThat(responseEntity.getHeaders()
            .get(HttpHeaders.LOCATION).iterator().next(), containsString("/ws/products/" + PRODUCT_CODE));
    }

    private AdminProductDto aNewProductDto() {

        return AdminProductDto.anAdminProduct().code(PRODUCT_CODE).build();
    }
}