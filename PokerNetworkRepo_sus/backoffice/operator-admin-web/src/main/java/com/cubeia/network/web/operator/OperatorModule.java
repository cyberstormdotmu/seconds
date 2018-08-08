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
package com.cubeia.network.web.operator;

import com.cubeia.network.shared.web.wicket.module.AdminWebModule;
import com.cubeia.network.shared.web.wicket.navigation.PageNode;
import com.cubeia.network.shared.web.wicket.navigation.PageNodeUtils;
import com.cubeia.network.web.operator.pages.CreateOperator;
import com.cubeia.network.web.operator.pages.EditOperator;
import com.cubeia.network.web.operator.pages.OperatorList;
import com.cubeia.network.web.operator.pages.wallet.WalletRequestList;

import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class OperatorModule extends AdminWebModule {

    private static final List<PageNode> pages = new ArrayList<>();

    static {
        PageNodeUtils.add(pages, "Operators", OperatorList.class, "icon-list-alt",
                PageNodeUtils.node("Edit Operator", EditOperator.class, false),
                PageNodeUtils.node("Create Operator", CreateOperator.class, true),
                PageNodeUtils.node("Wallet Requests", WalletRequestList.class, true));
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
