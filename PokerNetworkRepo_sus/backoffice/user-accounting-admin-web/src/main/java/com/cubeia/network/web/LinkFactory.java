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
package com.cubeia.network.web;

import static com.cubeia.network.web.wallet.AccountDetails.PARAM_ACCOUNT_ID;
import static com.cubeia.network.web.wallet.TransactionInfo.PARAM_TX_ID;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.cubeia.network.web.user.UserSummary;
import com.cubeia.network.web.wallet.AccountDetails;
import com.cubeia.network.web.wallet.TransactionInfo;

public class LinkFactory {

    public static BookmarkablePageLink<Void> accountDetailsLink(String wicketId, Long accountId) {
        return new BookmarkablePageLink<>(wicketId, AccountDetails.class, 
            new PageParameters().add(PARAM_ACCOUNT_ID, accountId));
    }
    
    public static BookmarkablePageLink<Void> userDetailsLink(String wicketId, Long userId) {
        return new BookmarkablePageLink<>(wicketId, UserSummary.class, 
            new PageParameters().add(UserSummary.PARAM_USER_ID, userId));
    }
 
    public static BookmarkablePageLink<Void> transactionDetailsLink(String wicketId, Long txId) {
        return new BookmarkablePageLink<>(wicketId, TransactionInfo.class, 
            new PageParameters().add(PARAM_TX_ID, txId));
    }
    
}
