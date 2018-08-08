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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cubeia.backoffice.operator.api.OperatorWalletRequestLogDTO;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestLogEventDTO;
import com.cubeia.backoffice.operator.client.OperatorServiceClient;
import com.cubeia.network.shared.web.wicket.BasePage;

@AuthorizeInstantiation({"ROLE_ADMIN", "ROLE_USER"})
public class WalletRequestDetails extends BasePage {
    private static final long serialVersionUID = 1L;
    
    public static final String PARAM_WALLET_REQUEST_ID = "walletRequestId";
    
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(WalletRequestDetails.class);

    @SpringBean(name="operatorClient")
    private OperatorServiceClient operatorServiceClient;
    
    private OperatorWalletRequestLogDTO walletRequest;
    
    
    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param params, Page parameters
	 */
    public WalletRequestDetails(PageParameters params) {
        super(params);
        Long requestId = params.get(PARAM_WALLET_REQUEST_ID).toLongObject();
        walletRequest = operatorServiceClient.findOperatorWalletRequestById(requestId);
        
        CompoundPropertyModel<?> cpm = new CompoundPropertyModel<OperatorWalletRequestLogDTO>(walletRequest);
        
        add(new Label("id", cpm.bind("id")));
        add(new Label("operatorId", cpm.bind("operatorId")));
        add(new Label("status", cpm.bind("requestStatus")));
        add(new Label("timestamp", cpm.bind("timestamp")));
        add(new Label("timestampMillis", cpm.bind("timestampMillis")));
        add(new Label("type", cpm.bind("type")));
        add(new Label("payload", cpm.bind("payload")));
        add(new Label("response", cpm.bind("response")));
        add(new Label("requestId", cpm.bind("requestId")));
        add(new Label("message", cpm.bind("message")));
        add(new Label("amount", cpm.bind("amount")));
        add(new Label("currency", cpm.bind("currency")));

        // Event table listings
        List<IColumn<OperatorWalletRequestLogEventDTO,String>> columns = new ArrayList<IColumn<OperatorWalletRequestLogEventDTO,String>>();
        columns.add(new PropertyColumn<OperatorWalletRequestLogEventDTO,String>(Model.of("Id"), "id"));
        columns.add(new PropertyColumn<OperatorWalletRequestLogEventDTO,String>(Model.of("NewStatus"), "newStatus"));
        columns.add(new PropertyColumn<OperatorWalletRequestLogEventDTO,String>(Model.of("Timestamp"), "timestamp"));
        columns.add(new PropertyColumn<OperatorWalletRequestLogEventDTO,String>(Model.of("Timestamp Millis"), "timestampMillis"));
        columns.add(new PropertyColumn<OperatorWalletRequestLogEventDTO,String>(Model.of("Message"), "message"));
        
        AjaxFallbackDefaultDataTable<OperatorWalletRequestLogEventDTO,String> entryTable = new AjaxFallbackDefaultDataTable<OperatorWalletRequestLogEventDTO,String>("eventTable", columns, new EventDataProvider() , 20);
        add(entryTable);
    }

    public OperatorWalletRequestLogDTO getWalletRequest() {
        return walletRequest;
    }
    
    @Override
    public String getPageTitle() {
        return "Wallet Request Details";
    }
    
    
    private final class EventDataProvider extends SortableDataProvider<OperatorWalletRequestLogEventDTO,String> {

        private static final long serialVersionUID = 1L;

        @Override
        public Iterator<? extends OperatorWalletRequestLogEventDTO> iterator(long first, long count) {
            return walletRequest.getEvents().iterator();
        }

        @Override
        public IModel<OperatorWalletRequestLogEventDTO> model(OperatorWalletRequestLogEventDTO e) {
            return Model.of(e);
        }

        @Override
        public long size() {
            return walletRequest.getEvents().size();
        }
        
    }

}
