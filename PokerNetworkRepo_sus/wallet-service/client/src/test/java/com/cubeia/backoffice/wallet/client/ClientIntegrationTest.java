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
package com.cubeia.backoffice.wallet.client;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cubeia.backoffice.wallet.api.dto.TransactionQueryResult;
import com.cubeia.backoffice.wallet.api.dto.request.ListTransactionsByAttributeRequest;

public class ClientIntegrationTest {

    @Test
    public void test() {
        
        WalletServiceClientHTTP client = new WalletServiceClientHTTP("https://localhost:9091/wallet-service-rest/rest");
        
        ListTransactionsByAttributeRequest req = new ListTransactionsByAttributeRequest(null, 1007L, "SESSION_ID", "175924784", 0, 0, null, true);
        TransactionQueryResult res = client.listTransactionsByAttribute(req);
        
        System.err.println("size: " + res.getTransactions().size());
        System.err.println("total size: " + res.getTotalQueryResultSize());
        
        System.err.println("got: " + res);
        
        
    }

}
