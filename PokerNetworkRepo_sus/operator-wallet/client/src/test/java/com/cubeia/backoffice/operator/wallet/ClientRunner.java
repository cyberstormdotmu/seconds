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
package com.cubeia.backoffice.operator.wallet;

import com.cubeia.backoffice.operator.api.BalanceResponse;
import com.cubeia.backoffice.operator.api.DepositRequest;
import com.cubeia.backoffice.operator.api.DepositResponse;
import com.cubeia.backoffice.operator.api.Money;
import com.cubeia.backoffice.operator.api.WithdrawRequest;
import com.cubeia.backoffice.operator.api.WithdrawResponse;
import com.cubeia.backoffice.operator.client.OperatorWalletClient;


public class ClientRunner {

    protected static String DEFAULT_API_KEY = "82813920-a9d2-11e3-a5e2-0800200c9a66";
    
	public static void main(String[] args) {

		Long playerId = 12L;
		
		OperatorWalletClient client = new OperatorWalletClient(DEFAULT_API_KEY);
		
		BalanceResponse balance = client.getBalance(playerId, "EUR");
		System.out.println("Balance 1: "+balance.getBalance());
		
		Money funds = new Money("EUR", "10");
		
		WithdrawRequest withdraw = new WithdrawRequest();
		withdraw.setUserId(playerId);
        withdraw.setFunds(funds);
        WithdrawResponse withdrawResponse = client.withdraw(withdraw);
        System.out.println("Balance 2 (after withdraw 10): "+withdrawResponse.getBalance());
		
        funds = new Money("EUR", "20");
		DepositRequest deposit = new DepositRequest();
		deposit.setFunds(funds);
		deposit.setUserId(playerId);
		DepositResponse depositResponse = client.deposit(deposit);
		System.out.println("Balance 3 (after deposit 20): "+depositResponse.getBalance());
        
	}
}
