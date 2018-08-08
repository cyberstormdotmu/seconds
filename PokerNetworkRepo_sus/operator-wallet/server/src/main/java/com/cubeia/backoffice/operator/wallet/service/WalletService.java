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
package com.cubeia.backoffice.operator.wallet.service;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cubeia.backoffice.operator.api.Money;
import com.cubeia.backoffice.operator.wallet.web.ApiException;

@Service
public class WalletService {
	
    private Logger log = LoggerFactory.getLogger(getClass());
    
	ConcurrentMap<String, ConcurrentMap<Long, BigDecimal>> currencies = new ConcurrentHashMap<>();
	
	/**
	 * Create a new balance for the user if not present.
	 * 
	 * @param userId
	 * @return Balance, never null.
	 */
	public Money getBalance(Long userId, String currency) {
		if (userId == null) {
			throw new ApiException(Status.BAD_REQUEST, "User id cannot be empty. Provided userId: "+userId);
		}
		
		ConcurrentMap<Long, BigDecimal> balances = getBalanceMap(userId, currency);
		BigDecimal balance = balances.get(userId);
		
		Money money = new Money();
		money.setAmount(balance);
		money.setCurrency(currency);
		
		return money;
	}

	private ConcurrentMap<Long, BigDecimal> getBalanceMap(Long userId, String currency) {
	    checkCurrencyMap(currency);
		ConcurrentMap<Long, BigDecimal> balances = currencies.get(currency);
		balances.putIfAbsent(userId, new BigDecimal("1000.00"));
		return balances;
	}
	
	private void checkCurrencyMap(String currency) {
		currencies.putIfAbsent(currency, new ConcurrentHashMap<Long, BigDecimal>());
	}


    public void withdraw(Long playerId, String currency, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new ApiException(Status.BAD_REQUEST, "Amount cannot be smaller than zero");
        }
        
        ConcurrentMap<Long, BigDecimal> balances = getBalanceMap(playerId, currency);
        BigDecimal balance = balances.get(playerId);
        
        log.debug("Balance["+balance+"] Withdraw["+amount+"]");
        if (amount.compareTo(balance) == 1) {
            throw new ApiException(402, "Insufficient funds");
        }
        
        balances.put(playerId, balance.subtract(amount));
    }
    
    public void deposit(Long userId, String currency, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new ApiException(Status.BAD_REQUEST, "Amount cannot be smaller than zero");
        }
        
        ConcurrentMap<Long, BigDecimal> balances = getBalanceMap(userId, currency);
        BigDecimal balance = balances.get(userId);
        balances.put(userId, balance.add(amount));
    }
    
	
}
