package com.ishoal.core.products;

import com.ishoal.core.domain.ProductCategory;
import com.ishoal.core.persistence.entity.CategoryEntity;
import com.ishoal.core.persistence.repository.CategoryEntityRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    private CategoryService categoryService;

    @Mock
    private CategoryEntityRepository categoryEntityRepository;

    @Before
    public void before() {

        CategoryEntity parent = new CategoryEntity();
        parent.setName("Products");

        CategoryEntity firstChild = new CategoryEntity();
        firstChild.setName("Laptops");

        CategoryEntity secondChild = new CategoryEntity();
        firstChild.setName("Power User");

        // set up relationship
        parent.setChildren(Collections.singletonList(firstChild));

        firstChild.setParent(parent);
        firstChild.setChildren(Collections.singletonList(secondChild));

        secondChild.setParent(firstChild);

        when(categoryEntityRepository.findAll()).thenReturn(Arrays.asList(parent, firstChild, secondChild));
        categoryService = new CategoryService(categoryEntityRepository);
    }

    @Test
    public void shouldCallRepository() {
        categoryService.fetchAllCategories();

        verify(categoryEntityRepository).findAll();
    }

    @Test
    public void shouldReturnAllCategories() {

        List<ProductCategory> categories = categoryService.fetchAllCategories();

        assertThat(categories.size(), is(3));
    }

}