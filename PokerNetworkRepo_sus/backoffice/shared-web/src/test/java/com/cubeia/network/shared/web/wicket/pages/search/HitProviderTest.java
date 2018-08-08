/**
 * Copyright (C) 2010 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cubeia.network.shared.web.wicket.pages.search;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.cubeia.network.shared.web.wicket.pages.search.HitProvider;
import org.junit.Test;

import com.cubeia.network.shared.web.wicket.pages.search.HitProvider.Sort;

public class HitProviderTest {

    @Test
    public void testParseAndInitSort() {
        HitProvider hp = new HitProvider(null, null, null, 0,null);
        
        Sort s = hp.parseSort(" _sort:timestamp ");
        assertThat(s.getField(), is("timestamp"));
        assertThat(s.isAscending(), is(true));
        
        s = hp.parseSort("_sort:timestamp");
        assertThat(s.getField(), is("timestamp"));
        assertThat(s.isAscending(), is(true));
        
        s = hp.parseSort("_sort:timestamp,asc");
        assertThat(s.getField(), is("timestamp"));
        assertThat(s.isAscending(), is(true));
        
        s = hp.parseSort("_sort:timestamp,desc");
        assertThat(s.getField(), is("timestamp"));
        assertThat(s.isAscending(), is(false));
        
        s = hp.parseSort("_type:blabl admin _sort:timestamp,desc");
        assertThat(s.getField(), is("timestamp"));
        assertThat(s.isAscending(), is(false));
        
        s = hp.parseSort("_type:blabl admin _sort:timestamp,desc more stuff AND more");
        assertThat(s.getField(), is("timestamp"));
        assertThat(s.isAscending(), is(false));
        
        s = hp.parseSort("_type:blabl admin more stuff AND more");
        assertThat(s.getField(), nullValue());
        assertThat(s.isAscending(), is(true));
    }

}
