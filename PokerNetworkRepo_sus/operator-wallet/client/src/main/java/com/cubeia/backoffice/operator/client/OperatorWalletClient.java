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


import java.security.MessageDigest;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.cubeia.backoffice.operator.api.BalanceRequest;
import com.cubeia.backoffice.operator.api.BalanceResponse;
import com.cubeia.backoffice.operator.api.DepositRequest;
import com.cubeia.backoffice.operator.api.DepositResponse;
import com.cubeia.backoffice.operator.api.WithdrawRequest;
import com.cubeia.backoffice.operator.api.WithdrawResponse;
import com.cubeia.backoffice.operator.wallet.exceptions.AuthenticationFailedException;
import com.cubeia.backoffice.operator.wallet.exceptions.InsufficientFundsException;
import com.cubeia.backoffice.operator.wallet.exceptions.NotFoundException;
import com.cubeia.backoffice.operator.wallet.exceptions.RemoteFailureException;
import com.cubeia.backoffice.operator.wallet.exceptions.RemotePartnerCommunicationException;
import com.cubeia.backoffice.operator.wallet.exceptions.ServiceUnavailableException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class OperatorWalletClient {

    private final Logger log = Logger.getLogger(OperatorWalletClient.class);

    private static final String BALANCE = "/balance";
    private static final String WITHDRAW = "/withdraw";
    private static final String DEPOSIT = "/deposit";
    
    private String url;
    
    private final Client client;

    private final String apiKey;

    public OperatorWalletClient(String apiKey, String baseUrl) {
        client = createClient();
        this.apiKey = apiKey;
    	this.url = baseUrl;
    }

    public  OperatorWalletClient(String apiKey) {
        this(apiKey, "http://localhost:9093/operator-wallet/rest/wallet");
    }
    
    private Client createClient() {
		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(JacksonJsonProvider.class);
		Client client = Client.create(cc);
		client.setConnectTimeout(4000); // Milliseconds
		return client;
	}
    
    
    public BalanceResponse getBalance(Long userId, String currency) {
        BalanceRequest request = new BalanceRequest();
        request.setUserId(userId);
        request.setCurrency(currency);
		return getBalance(request);
	}
    
    /**
     * API Key header and request id will be injected.
     * 
     * @param request
     * @return
     */
    public BalanceResponse getBalance(BalanceRequest request) {
        try {
            Builder resource = client.resource(url).path(BALANCE).type(MediaType.APPLICATION_JSON);
            
            String requestId = requestIdAndApiKey(resource, request.getRequestId());
            request.setRequestId(requestId);
            
            return resource.post(BalanceResponse.class, request);
        } catch (UniformInterfaceException e) {
            handleException(e);
            return null;
        }
    }
    
    /**
     * API Key header and request id will be injected.
     * 
     * @param request
     * @return
     */
    public WithdrawResponse withdraw(WithdrawRequest request) {
        try {
            Builder resource = client.resource(url).path(WITHDRAW).type(MediaType.APPLICATION_JSON);
            
            String requestId = requestIdAndApiKey(resource, request.getRequestId());
            request.setRequestId(requestId);
            
            return resource.post(WithdrawResponse.class, request);
        } catch (UniformInterfaceException e) {
            handleException(e);
            return null;
        }
    }

    
    /**
     * API Key header and request id will be injected.
     * 
     * @param request
     * @return
     */
    public DepositResponse deposit(DepositRequest request) {
        try {
            Builder resource = client.resource(url).path(DEPOSIT).type(MediaType.APPLICATION_JSON);
            
            String requestId = requestIdAndApiKey(resource, request.getRequestId());
            request.setRequestId(requestId);
            
            return resource.post(DepositResponse.class, request);
        } catch (UniformInterfaceException e) {
            handleException(e);
            return null;
        }
    }



    private String requestIdAndApiKey(Builder resource, String requestId) {
        try {
            if (requestId == null) {
                log.debug("Adding missing request id to operator wallet request");
                requestId = UUID.randomUUID().toString();
            }
            
            String field = apiKey + requestId;
            byte[] bytes = field.getBytes();
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] mdbytes = md.digest(bytes);
            
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
              sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            resource.header("x-api-key", sb.toString());
        return requestId;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create request id and MD5 hash of api key", e);
        }
    }
    
    /**
	 * Map error response codes to internal exceptions. 
	 * 
	 * Try to use the body of the response as message if we can
	 * otherwise just use the response exceptions message.
	 * 
	 * @param e
	 */
	private void handleException(UniformInterfaceException e) {
		String message = e.getMessage();
		try {
			message = e.getResponse().getEntity(String.class);
		} catch (Exception ex) {}

		switch (e.getResponse().getStatus()) {
		case 401:
			throw new AuthenticationFailedException(message);
			
		case 402:
            throw new InsufficientFundsException(message);
			
		case 404:
			throw new NotFoundException(message);

		case 502:
			throw new RemotePartnerCommunicationException(message);

		case 503:
			throw new ServiceUnavailableException(message);
			
		case 204:
			log.warn("Server returned 204 : Empty response");
			break;

		default:
			throw new RemoteFailureException(message);
		}
	}

}