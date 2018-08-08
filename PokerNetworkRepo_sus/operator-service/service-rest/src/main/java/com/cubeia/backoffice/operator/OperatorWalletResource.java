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
package com.cubeia.backoffice.operator;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cubeia.backoffice.accounting.api.NegativeBalanceException;
import com.cubeia.backoffice.operator.api.BalanceRequest;
import com.cubeia.backoffice.operator.api.BalanceResponse;
import com.cubeia.backoffice.operator.api.DepositRequest;
import com.cubeia.backoffice.operator.api.DepositResponse;
import com.cubeia.backoffice.operator.api.WithdrawRequest;
import com.cubeia.backoffice.operator.api.WithdrawResponse;
import com.cubeia.backoffice.operator.exception.ApiException;
import com.cubeia.backoffice.operator.service.OperatorWalletService;

/**
 * Single Wallet Resource
 *  
 * @author Fredrik
 */
@Component
@Scope("request")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class OperatorWalletResource {

	Logger log = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private OperatorWalletService operatorWalletService;
    
	private Long operatorId;
	
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	@Path("test")
    @GET
    public String test() {
        return "I am "+this.getClass().getSimpleName()+". Operator ID is: "+operatorId+". Time is: "+new Date();
    }
	
	@Path("balance")
    @POST
    public BalanceResponse getPlayerBalance(BalanceRequest request) {
	    return operatorWalletService.getBalance(operatorId, request);
    }
	
	@Path("withdraw")
    @POST
    public WithdrawResponse withdraw(WithdrawRequest request) {
        return operatorWalletService.withdraw(operatorId, request);
    }
	
	@Path("deposit")
    @POST
    public DepositResponse deposit(DepositRequest request) {
	    try {
	        return operatorWalletService.deposit(operatorId, request);
	    } catch (NegativeBalanceException e) {
	        throw new ApiException(402, "Player funds exceeded");
	    }
    }
	
}
