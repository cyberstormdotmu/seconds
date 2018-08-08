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

import static com.cubeia.network.shared.web.wicket.util.ParamBuilder.params;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cubeia.backoffice.wallet.api.dto.Account;
import com.cubeia.backoffice.wallet.api.dto.Account.AccountStatus;
import com.cubeia.backoffice.wallet.api.dto.Account.AccountType;
import com.cubeia.backoffice.wallet.api.dto.AccountQueryResult;
import com.cubeia.backoffice.wallet.api.dto.AccountsOrder;
import com.cubeia.backoffice.wallet.api.dto.request.ListAccountsRequest;
import com.cubeia.network.shared.web.wicket.BasePage;
import com.cubeia.network.shared.web.wicket.util.DatePropertyColumn;
import com.cubeia.network.shared.web.wicket.util.LabelLinkPanel;
import com.cubeia.network.web.user.UserSummary;

@AuthorizeInstantiation({"ROLE_ADMIN", "ROLE_USER"})
public class AccountList extends BasePage {
    private static final long serialVersionUID = 1L;
    
    private static Logger log = LoggerFactory.getLogger(AccountList.class);

    @SpringBean(name="walletCache")
    private WalletCache accountCache;
    
    private Long userId;
    private Long accountId;
    private Collection<AccountStatus> includeStatuses = new ArrayList<AccountStatus>(asList(AccountStatus.OPEN));
    private Collection<AccountType> includeTypes = new ArrayList<AccountType>(asList(
        AccountType.OPERATOR_ACCOUNT,
        AccountType.STATIC_ACCOUNT,
        AccountType.SYSTEM_ACCOUNT));
    
    private final class AccountDataProvider extends SortableDataProvider<Account,String> {
        private static final long serialVersionUID = 1L;
        
        private boolean showEmptyResult = true;

        
        public AccountDataProvider() {
            setSort(AccountsOrder.ID.name(), SortOrder.ASCENDING);
        }
        
        @Override
        public void detach() {
            super.detach();
        }
        
        @Override
        public Iterator<Account> iterator(long first, long count) {
            if (showEmptyResult) {
                return Collections.emptyIterator();
            }
            
            log.debug("getting account iterator...");
            
            SortParam<String> sort = getSort();
            AccountQueryResult result = getAccountListCached((int)first, (int)count, sort.getProperty(),
                getIncludeStatuses(), getIncludeTypes(), sort.isAscending());
            
            log.debug("account query results, got {} accounts", (result == null) ? "null" : result.getAccounts().size());
            return result == null || result.getAccounts() == null ? null : result.getAccounts().iterator();
        }

        @Override
        public IModel<Account> model(Account a) {
            return Model.of(a);
        }

        @Override
        public long size() {
            if (showEmptyResult) {
                return 0;
            }
            
            log.debug("getting account query results size...");
	    	AccountQueryResult result = getAccountListCached(0, 0, null, getIncludeStatuses(), getIncludeTypes(), true);
	    	int count = result == null ? 0 : result.getTotalQueryResultSize();
            log.debug("account query results total size: {}", count);
            return count; 
        }
        
        public void setShowEmptyResult(boolean showEmptyResult) {
            this.showEmptyResult = showEmptyResult;
        }
        
    }
    
    private AccountDataProvider dataProvider;

    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
    @SuppressWarnings("serial")
    public AccountList(final PageParameters parameters) {
        super(parameters);
        Form<AccountList> searchForm = new Form<AccountList>("searchForm", new CompoundPropertyModel<AccountList>(this)) {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                dataProvider.setShowEmptyResult(false);
            }
        };
        TextField<Long> idField = new TextField<Long>("userId");
        searchForm.add(idField);
        TextField<Long> userNameField = new TextField<Long>("accountId");
        searchForm.add(userNameField);
        
        final CheckBoxMultipleChoice<AccountStatus> statusesChoice = new CheckBoxMultipleChoice<AccountStatus>("includeStatuses", Arrays.asList(AccountStatus.values()));
        statusesChoice.setSuffix(" ");
        statusesChoice.add(new IValidator<Collection<AccountStatus>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(IValidatable<Collection<AccountStatus>> validatable) {
			    if(statusesChoice.getInputAsArray() == null) {
			        ValidationError error = new ValidationError().setMessage("Select one or more status");                
			        validatable.error(error);                 
			    }
			}
        });

        searchForm.add(statusesChoice);
        
        CheckBoxMultipleChoice<AccountType> typesChoice = new CheckBoxMultipleChoice<AccountType>("includeTypes", asList(AccountType.values()));
        typesChoice.setSuffix(" ");
        searchForm.add(typesChoice);
        
        add(searchForm);
        add(new FeedbackPanel("feedback"));
        
    	dataProvider = new AccountDataProvider();
    	ArrayList<IColumn<Account,String>> columns = new ArrayList<IColumn<Account,String>>();
        
    	columns.add(new AbstractColumn<Account,String>(new Model<String>("Account Id")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<Account>> item, String componentId, IModel<Account> model) {
                Account account = model.getObject();
                Long accountId = account.getId();
                LabelLinkPanel panel = new LabelLinkPanel(
                    componentId, 
                    "" + accountId, 
                    "details for account id " + accountId,
                    AccountDetails.class, 
                    params(AccountDetails.PARAM_ACCOUNT_ID, accountId));
                item.add(panel);
            }
            
            @Override
            public boolean isSortable() {
                return true;
            }
            
            @Override
            public String getSortProperty() {
                return AccountsOrder.ID.name();
            }
    	});

        columns.add(new AbstractColumn<Account,String>(new Model<String>("User id")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<Account>> item, String componentId, IModel<Account> model) {
                Account account = model.getObject();
                Long userId = account.getUserId();
                if (userId != null) {
                    Component panel = new LabelLinkPanel(
                        componentId, 
                        "" + userId, 
                        UserSummary.class,
                        params(UserSummary.PARAM_USER_ID, userId)).setVisible(userId != null);
                    item.add(panel);
                } else {
                    item.add(new Label(componentId, ""));
                }
            }
            
            @Override
            public boolean isSortable() {
                return true;
            }
            
            @Override
            public String getSortProperty() {
                return AccountsOrder.USER_ID.name();
            }
        });
    	
        columns.add(new PropertyColumn<Account,String>(new Model<String>("Account name"), AccountsOrder.ACCOUNT_NAME.name(), "information.name"));
        columns.add(new PropertyColumn<Account,String>(new Model<String>("Status"), AccountsOrder.STATUS.name(), "status"));
        columns.add(new PropertyColumn<Account,String>(new Model<String>("Type"), AccountsOrder.TYPE.name(), "type"));
        columns.add(new PropertyColumn<Account,String>(new Model<String>("Currency"), "currencyCode"));
        columns.add(new PropertyColumn<Account,String>(new Model<String>("Operator id"), AccountsOrder.OPERATOR_ID.name(), "operatorId"));
        columns.add(new DatePropertyColumn<Account,String>(new Model<String>("Creation date"), AccountsOrder.CREATION_DATE.name(), "created"));
        columns.add(new DatePropertyColumn<Account,String>(new Model<String>("Close date"), AccountsOrder.CLOSE_DATE.name(), "closed"));
        columns.add(new AbstractColumn<Account,String>(new Model<String>("Actions")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<Account>> item, String componentId, IModel<Account> model) {
                Account account = model.getObject();
                Long accountId = account.getId();
                LabelLinkPanel panel = new LabelLinkPanel(
                    componentId, 
                    "transactions", 
                    "transactions involving account id " + accountId,
                    TransactionList.class, 
                    params(TransactionList.PARAM_ACCOUNT_ID, accountId));
                item.add(panel);
            }
        });
        
        
    	AjaxFallbackDefaultDataTable<Account,String> userTable = new AjaxFallbackDefaultDataTable<Account,String>(
    	    "accountTable", columns, dataProvider , 20);
    	add(userTable);
    }

	private AccountQueryResult getAccountListCached(int offset, int limit, String sortProperty,
	    Collection<AccountStatus> statuses, Collection<AccountType> types, boolean ascending) {
	    
	    AccountsOrder sortOrder = convertPropertyToSortOrder(sortProperty);
	    
	    ListAccountsRequest request = new ListAccountsRequest();
	    request.setAccountId(getAccountId());
	    request.setUserId(getUserId());
        request.setStatuses(statuses);
        request.setTypes(types);
        request.setOffset(offset);
        request.setLimit(limit);
        request.setSortOrder(sortOrder);
        request.setAscending(ascending);
	    
        return accountCache.getAccountCache().getUnchecked(request);
	}
	
    private AccountsOrder convertPropertyToSortOrder(String sortProperty) {
        return sortProperty == null ? null : AccountsOrder.valueOf(sortProperty);
    }

    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    
    public void setIncludeTypes(Collection<AccountType> includedTypes) {
        this.includeTypes = includedTypes;
    }
    
    public Collection<AccountType> getIncludeTypes() {
        return includeTypes;
    }
    
    public void setIncludeStatuses(Collection<AccountStatus> includeStatuses) {
        this.includeStatuses = includeStatuses;
    }
    
    public Collection<AccountStatus> getIncludeStatuses() {
        return includeStatuses;
    }
    
    @Override
    public String getPageTitle() {
        return "Account list";
    }
}
