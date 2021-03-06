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
package com.cubeia.backoffice.accounting.core.integrations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cubeia.backoffice.accounting.api.AccountDTO;
import com.cubeia.backoffice.accounting.api.TransactionDTO;

@Component("accounting.pluginManager")
public class PluginManager implements AccountingManagerPlugin {

	private final List<AccountingManagerPlugin> plugins = new ArrayList<AccountingManagerPlugin>(2);
	
	public List<AccountingManagerPlugin> getPlugins() {
		return plugins;
	}
	
	@Override
	public void beforeCreate(AccountDTO account) {
		for (AccountingManagerPlugin pl : plugins) {
			pl.beforeCreate(account);
		}
	}

	@Override
	public void afterCreate(AccountDTO account) {
		for (AccountingManagerPlugin pl : plugins) {
			pl.afterCreate(account);
		}
	}
	
    @Override
    public void afterUpdate(AccountDTO account) {
        for (AccountingManagerPlugin pl : plugins) {
            pl.afterUpdate(account);
        }
    }

	@Override
	public void afterCreate(TransactionDTO tx) {
		for (AccountingManagerPlugin pl : plugins) {
			pl.afterCreate(tx);
		}
	}
	
    @Override
    public void afterUpdate(TransactionDTO tx) {
        for (AccountingManagerPlugin pl : plugins) {
            pl.afterUpdate(tx);
        }
    }
}
