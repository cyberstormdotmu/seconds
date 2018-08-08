package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class VendorIdTest {

    @Test
    public void shouldConstructEmptyIdFromNull() {
        assertThat(VendorId.from(null), is(VendorId.emptyVendorId));
    }
    
    @Test
    public void shouldBeEqualIfSameInstance() {
        VendorId id = VendorId.from(345L);
        assertThat(id, is(equalTo(id)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(VendorId.from(456L), is(equalTo(VendorId.from(456L))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(VendorId.from(457L), is(not(equalTo(VendorId.from(783L)))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(VendorId.from(456L), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(VendorId.from(456L), is(not(equalTo("hello"))));
    }    
}