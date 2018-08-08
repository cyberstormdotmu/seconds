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

package com.cubeia.network.web.wallet;

import com.cubeia.network.shared.web.wicket.BasePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@SuppressWarnings("serial")
@AuthorizeInstantiation({"ROLE_ADMIN", "ROLE_USER"})
public class InvalidTransaction extends BasePage {

    private long transactionId;

    public InvalidTransaction(PageParameters params) {
        super(params);
        transactionId = params.get("transactionId").toLong();
        add(new Label("transactionId", "" + transactionId));
    }
    
    @Override
    public String getPageTitle() {
        return "Invalid transaction (" + transactionId + ")";
    }

}
