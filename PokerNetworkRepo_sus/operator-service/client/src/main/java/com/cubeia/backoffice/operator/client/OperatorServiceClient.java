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
package com.cubeia.backoffice.operator.client;


import com.cubeia.backoffice.operator.api.OperatorConfigParamDTO;
import com.cubeia.backoffice.operator.api.OperatorCurrencyDTO;
import com.cubeia.backoffice.operator.api.OperatorDTO;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestLogDTO;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestOrder;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestQueryResult;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface OperatorServiceClient {


    List<OperatorDTO> getOperators();

    void setBaseUrl(String url);

    void createOperator(OperatorDTO operator);

    void updateOperator(OperatorDTO operator);

    Map<OperatorConfigParamDTO,String> getConfig(Long operatorId);

    OperatorDTO getOperator(Long operatorId);
    
    OperatorDTO getOperatorByApiKey(String apiKey);

    void updateConfig(Long operatorId, Map<OperatorConfigParamDTO, String> config);

    /**
     * Retrieves a configuration value for a specific operator,
     * This method is cached
     * @param operatorId
     * @param param
     * @return
     */
    String getConfig(Long operatorId, OperatorConfigParamDTO param);

    /**
     * Checks whether a specific operator is enabled or not.
     * This method is cached
     * @param operatorId
     * @return
     */
    boolean isEnabled(Long operatorId);

	void updateConfig(Long operatorId, OperatorConfigParamDTO authenticationUrl, String string);

	/**
	 * Get Operator configuration parameters that should be sent to the client. This should
	 * exclude configuration that is not public.
	 * 
	 * @param operatorId
	 * @return
	 */
	Map<OperatorConfigParamDTO, String> getClientConfig(Long operatorId);
	
	/**
	 * Returns the list of currencies defined for the given operator.
	 * @param operatorId operator id
	 * @return list of currencies, never null
	 */
	List<OperatorCurrencyDTO> getOperatorCurrencies(Long operatorId);

	/**
	 * Get a paginated list of operator wallet request entries (audit log).
	 * 
	 * @param operatorId
	 * @param statuses, null acceptable for all statuses
	 * @param offset
	 * @param limit
	 * @param sortOrder, null acceptable for default sorting (ID)
	 * @param ascending
	 * 
	 * @return
	 */
    OperatorWalletRequestQueryResult findOperatorWalletRequestLogEntries(Long operatorId, Collection<OperatorWalletRequestStatus> statuses, int offset,
            int limit, OperatorWalletRequestOrder sortOrder, boolean ascending);

    OperatorWalletRequestLogDTO findOperatorWalletRequestById(Long requestId);
	
}

