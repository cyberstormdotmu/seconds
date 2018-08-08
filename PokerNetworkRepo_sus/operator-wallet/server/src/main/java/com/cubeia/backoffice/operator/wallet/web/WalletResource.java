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
package com.cubeia.backoffice.operator.wallet.web;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cubeia.backoffice.operator.api.AbstractWalletResponse;
import com.cubeia.backoffice.operator.api.BalanceRequest;
import com.cubeia.backoffice.operator.api.BalanceResponse;
import com.cubeia.backoffice.operator.api.DepositRequest;
import com.cubeia.backoffice.operator.api.DepositResponse;
import com.cubeia.backoffice.operator.api.Money;
import com.cubeia.backoffice.operator.api.WalletDTO;
import com.cubeia.backoffice.operator.api.WithdrawRequest;
import com.cubeia.backoffice.operator.api.WithdrawResponse;
import com.cubeia.backoffice.operator.wallet.config.OperatorWalletRuntimeConfig;
import com.cubeia.backoffice.operator.wallet.service.ApiKeyCalculator;
import com.cubeia.backoffice.operator.wallet.service.WalletService;

@Path("/wallet")
@Component
@Scope("request")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class WalletResource {

	Logger log = LoggerFactory.getLogger(getClass());
	
    @Autowired WalletService walletService;
    
    @Autowired OperatorWalletRuntimeConfig config;

    @Autowired ApiKeyCalculator apiKeyCalculator;
    
//    @Context
//    private ResourceContext ctx;
    
    @GET
    public String getOperators() {
        return "This is "+this.getClass().getSimpleName()+". Time is now: "+new Date();
    }
    
    @POST
    @Path("balance")
    public BalanceResponse getBalance(@HeaderParam("x-api-key") String hashedKey, BalanceRequest request) {
        if (config.isVerifyApiKey()) {
            apiKeyCalculator.verifyApiKey(hashedKey, request.getRequestId());
        }
        checkThrowError();
        
        log.debug("Get Balance Request: "+request);
        
    	BalanceResponse response = new BalanceResponse();
        setCommonFields(request, response, request.getCurrency());
        return response ;
    }
    

    /**
     * The game wants to transfer funds from the game to the operator account.
     * 
     * This should never be denied since that will mean we will have money stuck
     * in the game implementation.
     * 
     * @param hashedKey
     * @param request
     * @return
     */
    @POST
    @Path("deposit")
    public DepositResponse deposit(@HeaderParam("x-api-key") String hashedKey, DepositRequest request) {
        if (config.isVerifyApiKey()) {
            apiKeyCalculator.verifyApiKey(hashedKey, request.getRequestId());
        }
        checkThrowError();
        
        log.debug("Deposit Request: "+request);
        
        Long userId = Long.parseLong(request.getExternalUserId());
        walletService.deposit(userId, request.getFunds().getCurrency(), request.getFunds().getAmount());
       
        DepositResponse response = new DepositResponse();
        setCommonFields(request, response, request.getFunds().getCurrency());
        return response;
    }
    
    /**
     * The game wants to transfer funds from the operator wallet to the game. 
     * 
     * This can be denied for various reasons. If the player is lacking funds etc then
     * a 402 should be returned.
     * 
     * @param hashedKey
     * @param request
     * @return
     */
    @POST
    @Path("withdraw")
    public WithdrawResponse withdraw(@HeaderParam("x-api-key") String hashedKey, WithdrawRequest request) {
        if (config.isVerifyApiKey()) {
            apiKeyCalculator.verifyApiKey(hashedKey, request.getRequestId());
        }
        checkThrowError();
        
        log.debug("Withdraw Request: "+request);
        
        Long userId = Long.parseLong(request.getExternalUserId());
        walletService.withdraw(userId, request.getFunds().getCurrency(), request.getFunds().getAmount());
       
        WithdrawResponse response = new WithdrawResponse();
        setCommonFields(request, response, request.getFunds().getCurrency());
        return response;
    }

    private Money getPlayerBalance(Long playerId, String currency) {
        return walletService.getBalance(playerId, currency);
    }
    
    /**
     * Populate:
     *  - Balance
     *  - Player ID
     *  - Request ID
     *  
     * @param request
     * @param response
     * @param currency
     */
    private void setCommonFields(WalletDTO request, AbstractWalletResponse response, String currency) {
        Long userId = Long.parseLong(request.getExternalUserId());
        Money balance = getPlayerBalance(userId, currency);
        response.setBalance(balance);
        response.setRequestId(request.getRequestId());
        response.setUserId(request.getUserId());
    }
    
    private void checkThrowError() {
        if (config.isThrowError()) {
            throw new RuntimeException("Error configured by runtime JMX configuration");
        }
    }
    
}
