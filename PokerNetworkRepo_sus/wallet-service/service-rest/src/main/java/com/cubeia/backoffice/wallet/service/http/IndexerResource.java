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
package com.cubeia.backoffice.wallet.service.http;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cubeia.backoffice.wallet.IndexerService;
import com.cubeia.backoffice.wallet.WalletService;

@Path("/indexer")
@Component
@Scope("request")
@Consumes({MediaType.TEXT_PLAIN})
@Produces({MediaType.TEXT_PLAIN})
public class IndexerResource {

	@Resource(name = "wallet.service.adaptedWalletService")
	private WalletService walletService;
	
    @Resource(name = "wallet.service.indexingService")
    private IndexerService indexerService;
	
	@Path("reindex/account/{accountId}")
	@PUT
	public String reindexAccount(@PathParam("accountId") Long accountId) {
		return indexerService.reindexAccount(accountId);
	}
	
    @Path("reindex/accounts")
    @PUT
    public String reindexAllAccounts() {
        return indexerService.reindexAllAccounts();
    }

    @Path("reindex/accounts")
    @GET
    public String reindexAllAccountsStatus() {
        return indexerService.reindexAllAccountsStatus();
    }

    @Path("reindex/accounts")
    @DELETE
    public String stopReindexAllAccounts() {
        return indexerService.stopReindexAllAccounts();
    }

    
    
    @Path("reindex/transaction/{txId}")
    @PUT
    public String reindexTransaction(@PathParam("txId") Long txId) {
        return indexerService.reindexTransaction(txId);
    }
    
    @Path("reindex/transactions")
    @PUT
    public String reindexTransactions() {
        return indexerService.reindexAllTransactions();
    }
    
    @Path("reindex/transactions")
    @GET
    public String reindexTransactionsStatus() {
        return indexerService.reindexAllTransactionsStatus();
    }

    @Path("reindex/transactions")
    @DELETE
    public String stopReindexTransactions() {
        return indexerService.stopReindexAllTransactions();
    }
}
