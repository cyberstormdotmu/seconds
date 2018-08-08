package com.ishoal.core.persistence.adapter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.ishoal.core.persistence.entity.CategoryEntity;

public class ProductCategoryEntityAdapterTest {

    private ProductCategoryEntityAdapter adapter = new ProductCategoryEntityAdapter();

    @Test
    public void shouldReturnNullWhenGivenNull() {
        assertThat(adapter.adapt((CategoryEntity) null), is(nullValue()));
    }

    @Test
    public void shouldReturnCategoryWithName() {
        assertThat(adapter.adapt(categoryWithNoParent()).getName(), is("Laptops"));
    }

    @Test
    public void shouldReturnCategoryWithNoParent() {
        assertThat(adapter.adapt(categoryWithNoParent()).getParent(), is(nullValue()));
    }

    @Test
    public void shouldReturnCategoryWithParent() {
        assertThat(adapter.adapt(categoryWithParent()).getParent(), is(not(nullValue())));
    }

    @Test
    public void shouldReturnCategoryWithCorrectParent() {
        assertThat(adapter.adapt(categoryWithParent()).getParent().getName(), is("Laptops"));
    }

    private CategoryEntity categoryWithNoParent() {
        CategoryEntity entity = new CategoryEntity();
        entity.setName("Laptops");
        return entity;
    }

    private CategoryEntity categoryWithParent() {
        CategoryEntity entity = new CategoryEntity();
        entity.setName("Power User");
        entity.setParent(categoryWithNoParent());
        return entity;
    }
}