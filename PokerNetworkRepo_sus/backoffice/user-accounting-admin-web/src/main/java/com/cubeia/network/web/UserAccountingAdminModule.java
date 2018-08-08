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

import com.cubeia.network.shared.web.wicket.module.AdminWebModule;
import com.cubeia.network.shared.web.wicket.module.PanelCreator;
import com.cubeia.network.shared.web.wicket.navigation.PageNode;
import com.cubeia.network.shared.web.wicket.search.SearchResultPanel;
import com.cubeia.network.web.search.*;
import com.cubeia.network.web.user.CreateUser;
import com.cubeia.network.web.user.EditUser;
import com.cubeia.network.web.user.UserList;
import com.cubeia.network.web.user.UserSummary;
import com.cubeia.network.web.wallet.*;
import org.apache.wicket.model.Model;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.cubeia.network.shared.web.wicket.navigation.PageNodeUtils.add;
import static com.cubeia.network.shared.web.wicket.navigation.PageNodeUtils.node;

@Component
public class UserAccountingAdminModule extends AdminWebModule {

    private static final List<PageNode> pages = new ArrayList<>();

    static {
        add(pages, "Users", "user", UserList.class, "icon-list-alt",
                node("User Summary","details", UserSummary.class, false),
                node("Edit User", "edit",EditUser.class, false),
                node("Create User", "create", CreateUser.class, true));

        add(pages, "Accounts", "account", AccountList.class, "icon-list-alt",
                node("Account Details", "detail", AccountDetails.class, false),
                node("List Accounts", AccountList.class),
                node("Create Account", "create", CreateAccount.class),
                node("Edit Account", "edit",EditAccount.class, false));
        add(pages,"Transactions","transaction",TransactionList.class,"icon-list-alt",
                node("List Transactions", TransactionList.class),
                node("Transaction Info", "info",TransactionInfo.class, false),
                node("Create Transaction", "create",CreateTransaction.class));

        add(pages,"Currencies","currency",EditCurrencies.class,"icon-list-alt");
    }

    public UserAccountingAdminModule() {

        registerSearchType("user",User.class, new PanelCreator<User>() {
            @Override
            public SearchResultPanel createPanel(String wicketId, User entity) {
                return new UserPanel(wicketId, Model.of(entity));
            }
        });

        registerSearchType("transaction",Transaction.class, new PanelCreator<Transaction>() {
            @Override
            public SearchResultPanel createPanel(String wicketId, Transaction entity) {
                return new TransactionPanel(wicketId, Model.of(entity));
            }
        });
        registerSearchType("account",Account.class, new PanelCreator<Account>() {
            @Override
            public SearchResultPanel createPanel(String wicketId, Account entity) {
                return new AccountPanel(wicketId, Model.of(entity));
            }
        });



    }

    @Override
    public String getName() {
        return "operator";
    }

    @Override
    public List<PageNode> getPages() {
        return pages;
    }
}
