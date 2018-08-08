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

package com.cubeia.network.web.operator.pages.wallet;

import static com.cubeia.network.shared.web.wicket.util.ParamBuilder.params;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.form.Button;
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

import com.cubeia.backoffice.operator.api.OperatorWalletRequestLogDTO;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestOrder;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestQueryResult;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestStatus;
import com.cubeia.backoffice.operator.client.OperatorServiceClient;
import com.cubeia.network.shared.web.wicket.BasePage;
import com.cubeia.network.shared.web.wicket.util.LabelLinkPanel;

@AuthorizeInstantiation({"ROLE_ADMIN", "ROLE_USER"})
public class WalletRequestList extends BasePage {
    private static final long serialVersionUID = 1L;
    
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(WalletRequestList.class);

    @SpringBean(name="operatorClient")
    private OperatorServiceClient operatorServiceClient;
    
    private Long operatorId;
    
    private Collection<OperatorWalletRequestStatus> includeStatuses = new ArrayList<OperatorWalletRequestStatus>(asList(
            OperatorWalletRequestStatus.FAILED
            ));
    
    
	private final class AuditLogProvider extends SortableDataProvider<OperatorWalletRequestLogDTO,String> {
        private static final long serialVersionUID = 1L;
        
        public AuditLogProvider() {
            setSort(OperatorWalletRequestOrder.ID.name(), SortOrder.ASCENDING);
        }

        @Override
        public Iterator<OperatorWalletRequestLogDTO> iterator(long first, long count) {
            SortParam<String> sort = getSort();
            return getAuditLog(getOperatorId(), includeStatuses, (int)first, (int)count, sort.getProperty(), sort.isAscending()).getEntries().iterator();
        }

        @Override
        public IModel<OperatorWalletRequestLogDTO> model(OperatorWalletRequestLogDTO r) {
            return Model.of(r);
        }

        @Override
        public long size() {
            OperatorWalletRequestQueryResult result = getAuditLog(getOperatorId(), includeStatuses, 0, 0, null, true);
            int size = result.getTotalQueryResultSize();
            return size;
        }
    }

    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
    public WalletRequestList(final PageParameters parameters) {
        super(parameters);
        final Form<?> searchForm = new Form<WalletRequestList>("searchForm", new CompoundPropertyModel<WalletRequestList>(this));
        final TextField<String> idField = new TextField<String>("operatorId");
        searchForm.add(idField);
        
        final CheckBoxMultipleChoice<OperatorWalletRequestStatus> statusesChoice = new CheckBoxMultipleChoice<OperatorWalletRequestStatus>("includeStatuses", Arrays.asList(OperatorWalletRequestStatus.values()));
        statusesChoice.setSuffix(" ");
        statusesChoice.add(new IValidator<Collection<OperatorWalletRequestStatus>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void validate(IValidatable<Collection<OperatorWalletRequestStatus>> validatable) {
                if(statusesChoice.getInputAsArray() == null) {
                    ValidationError error = new ValidationError().setMessage("Select one or more status");                
                    validatable.error(error);                 
                }
            }
        });
        
        searchForm.add(statusesChoice);
        
        searchForm.add(new Button("clearForm"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				super.onSubmit();
				idField.clearInput();
			}        	
        });
        add(searchForm);
        add(new FeedbackPanel("feedback"));
        
        final AuditLogProvider dataProvider = new AuditLogProvider();
    	List<IColumn<OperatorWalletRequestLogDTO,String>> columns = new ArrayList<IColumn<OperatorWalletRequestLogDTO,String>>();
        
    	
    	columns.add(new AbstractColumn<OperatorWalletRequestLogDTO,String>(new Model<String>("Id")) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<OperatorWalletRequestLogDTO>> item, String componentId, IModel<OperatorWalletRequestLogDTO> model) {
                OperatorWalletRequestLogDTO requestLog = model.getObject();
                Long requestId = requestLog.getId();
                LabelLinkPanel panel = new LabelLinkPanel(
                    componentId, 
                    "" + requestId, 
                    "details for request id " + requestId,
                    WalletRequestDetails.class, 
                    params(WalletRequestDetails.PARAM_WALLET_REQUEST_ID, requestId));
                item.add(panel);
            }
            
            @Override
            public boolean isSortable() {
                return true;
            }
            
            @Override
            public String getSortProperty() {
                return OperatorWalletRequestOrder.ID.name();
            }
        });
    	
        // columns.add(new PropertyColumn<OperatorWalletRequestLogDTO,String>(Model.of("ID"), "id"));
        columns.add(new PropertyColumn<OperatorWalletRequestLogDTO,String>(Model.of("Status"), OperatorWalletRequestOrder.STATUS.name(), "requestStatus"));
        columns.add(new PropertyColumn<OperatorWalletRequestLogDTO,String>(Model.of("Operator"), "operatorId"));
        columns.add(new PropertyColumn<OperatorWalletRequestLogDTO,String>(Model.of("Timestamp"), "timestamp"));
        columns.add(new PropertyColumn<OperatorWalletRequestLogDTO,String>(Model.of("Type"), "type"));
        columns.add(new PropertyColumn<OperatorWalletRequestLogDTO,String>(Model.of("Amount"), "amount"));
        columns.add(new PropertyColumn<OperatorWalletRequestLogDTO,String>(Model.of("Currency"), "currency"));
        
    	AjaxFallbackDefaultDataTable<OperatorWalletRequestLogDTO,String> auditLogTable = new AjaxFallbackDefaultDataTable<OperatorWalletRequestLogDTO,String>("userTable", columns, dataProvider , 20);
    	add(auditLogTable);
    }

    
    private OperatorWalletRequestQueryResult getAuditLog(Long operatorId, Collection<OperatorWalletRequestStatus> statuses, int offset, int limit, String sortProperty, boolean ascending) {
        OperatorWalletRequestOrder sortOrder = convertPropertyToSortOrder(sortProperty);
        OperatorWalletRequestQueryResult result = operatorServiceClient.findOperatorWalletRequestLogEntries(operatorId, statuses, offset, limit, sortOrder, ascending);
        return result;
    }
    
    private OperatorWalletRequestOrder convertPropertyToSortOrder(String sortProperty) {
        return sortProperty == null ? null : OperatorWalletRequestOrder.valueOf(sortProperty);
    }
    
    
    public Long getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }
    
    @Override
    public String getPageTitle() {
        return "Operator Wallet Requests";
    }
    
    public Collection<OperatorWalletRequestStatus> getIncludeStatuses() {
        return includeStatuses;
    }
    
    public void setIncludeStatuses(Collection<OperatorWalletRequestStatus> includeStatuses) {
        this.includeStatuses = includeStatuses;
    }
}
