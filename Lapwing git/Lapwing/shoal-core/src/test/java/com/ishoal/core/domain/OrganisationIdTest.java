package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class OrganisationIdTest {

    @Test
    public void shouldConstructEmptyOrganisationIdFromNull() {
        assertThat(OrganisationId.from(null), is(OrganisationId.emptyOrganisationId));
    }
    
    @Test
    public void shouldBeEqualIfSameInstance() {
        OrganisationId id = OrganisationId.from(345L);
        assertThat(id, is(equalTo(id)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(OrganisationId.from(456L), is(equalTo(OrganisationId.from(456L))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(OrganisationId.from(457L), is(not(equalTo(OrganisationId.from(783L)))));
    }
    
    @Test
    public void shouldNotBeEqualIfToNull() {
        assertThat(OrganisationId.from(456L), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(OrganisationId.from(456L), is(not(equalTo("hello"))));
    }    
}