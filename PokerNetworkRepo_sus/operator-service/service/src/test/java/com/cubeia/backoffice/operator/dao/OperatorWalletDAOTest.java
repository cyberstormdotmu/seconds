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
package com.cubeia.backoffice.operator.dao;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.cubeia.backoffice.operator.api.OperatorWalletRequestOrder;
import com.cubeia.backoffice.operator.service.BaseTest;
import com.cubeia.backoffice.operator.service.dao.OperatorWalletDAO;
import com.cubeia.backoffice.operator.service.entity.OperatorWalletRequestLog;
import com.cubeia.backoffice.operator.service.entity.OperatorWalletRequestLogEvent;
import com.cubeia.backoffice.operator.service.entity.RequestEventStatus;
import com.cubeia.backoffice.operator.service.entity.RequestStatus;

public class OperatorWalletDAOTest extends BaseTest {

    @Resource OperatorWalletDAO walletDAO;

    @Test
    public void testCreateRequest() {
        String uuid = UUID.randomUUID().toString();
        
        OperatorWalletRequestLog request = createRequest();
        request.setRequestId(uuid);
        walletDAO.save(request);
        Assert.assertNotNull(request.getId());

        OperatorWalletRequestLog lookup = walletDAO.getWalletRequestByRequestId(uuid);
        assertThat(lookup.getOperatorId(), is(2L));
        assertThat(lookup.getStatus(), is(RequestStatus.CREATED));
    }

    
    @Test
    public void testFindLogEntriesPaginated() {
        String uuid = UUID.randomUUID().toString();
        OperatorWalletRequestLog request = createRequest();
        request.setRequestId(uuid);
        
        OperatorWalletRequestLogEvent event1_1 = new OperatorWalletRequestLogEvent(request, RequestEventStatus.CREATED);
        OperatorWalletRequestLogEvent event1_2 = new OperatorWalletRequestLogEvent(request, RequestEventStatus.INTERNAL_TRANSFER_COMPLETED);
        request.getEvents().add(event1_1);
        request.getEvents().add(event1_2);
        
        walletDAO.save(request);
        Assert.assertNotNull(request.getId());
        
        uuid = UUID.randomUUID().toString();
        OperatorWalletRequestLog request2 = createRequest();
        request2.setRequestId(uuid);
        request2.setStatus(RequestStatus.FAILED);
        walletDAO.save(request2);
        Assert.assertNotNull(request2.getId());
        
        Collection<RequestStatus> statuses = new ArrayList<>();
        statuses.add(RequestStatus.CREATED);
        List<OperatorWalletRequestLog> entries = walletDAO.findLogEntries(2L, statuses, 0, 100, OperatorWalletRequestOrder.ID, true);
        assertThat(entries.size(), CoreMatchers.is(1));
        
        statuses.add(RequestStatus.FAILED);
        List<OperatorWalletRequestLog> entries2 = walletDAO.findLogEntries(2L, statuses, 0, 100, OperatorWalletRequestOrder.STATUS, true);
        assertThat(entries2.size(), CoreMatchers.is(2));
        
        // Null for statuses gives all, null for order is allowed
        List<OperatorWalletRequestLog> entries3 = walletDAO.findLogEntries(2L, null, 0, 100, null, true);
        assertThat(entries3.size(), CoreMatchers.is(2));
        
        int count = walletDAO.countLogEntries(2L, statuses);
        assertThat(count, CoreMatchers.is(2));
        count = walletDAO.countLogEntries(2L, null);
        assertThat(count, CoreMatchers.is(2));
        
    }
    
    @Test
    public void testEventsAreAdded() {
        String uuid = UUID.randomUUID().toString();
        OperatorWalletRequestLog request = createRequest();
        request.setRequestId(uuid);
        
        OperatorWalletRequestLogEvent event = new OperatorWalletRequestLogEvent(request, RequestEventStatus.FAILED);
        request.getEvents().add(event);
        
        walletDAO.save(request);
        
        OperatorWalletRequestLog found = walletDAO.findById(OperatorWalletRequestLog.class, request.getId());
        assertNotNull(found);
        assertThat(found.getEvents().size(), CoreMatchers.is(1));
    }
    
    private OperatorWalletRequestLog createRequest() {
        OperatorWalletRequestLog request = new OperatorWalletRequestLog();
        request.setOperatorId(2L);
        request.setPayload("ABC");
        request.setStatus(RequestStatus.CREATED);
        return request;
    }
    
}
