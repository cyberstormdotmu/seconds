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
package com.cubeia.backoffice.operator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cubeia.backoffice.operator.api.OperatorConfigParamDTO;
import com.cubeia.backoffice.operator.api.OperatorCurrencyDTO;
import com.cubeia.backoffice.operator.api.OperatorDTO;
import com.cubeia.backoffice.operator.service.OperatorService;
import com.sun.jersey.api.core.ResourceContext;

@Component
@Scope("request")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class OperatorResource {

	Logger log = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private OperatorService operatorService;
    
    @Context
    private ResourceContext ctx;
    
	private Long operatorId;
	

	/**
	 * Wrapper needed for XML marshalling of Map
	 * @author w
	 */
    @XmlRootElement
    public static class ConfigWrapper {
        
        @XmlElementWrapper
        public HashMap<OperatorConfigParamDTO,String> config;
        
        public ConfigWrapper() {}
        
        public ConfigWrapper(Map<OperatorConfigParamDTO,String> cfg) {
            config = new HashMap<OperatorConfigParamDTO, String>(cfg);
        }
    }

    @GET
    public OperatorDTO getOperator() {
    	OperatorDTO operator = operatorService.getOperator(operatorId);
        return operator;
    }

    @Path("config")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Map<OperatorConfigParamDTO,String> getConfig() {
        return operatorService.getOperatorConfigList(operatorId);
    }
    
    /*
     * NOTE: this is a workaround for buggy jaxb marshalling of top level maps.
     */
    @Path("config")
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public ConfigWrapper getConfigWorkaroundForXml() {
        return new ConfigWrapper(operatorService.getOperatorConfigList(operatorId));
    }
    
    @Path("currencies")
    @GET
    public List<OperatorCurrencyDTO> getCurrencies() {
        return operatorService.getOperatorCurrencies(operatorId);
    }
    
    @Path("clientconfig")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Map<OperatorConfigParamDTO,String> getConfigForClients() {
        return operatorService.getOperatorConfigListForClients(operatorId);
    }
    
    @Path("clientconfig")
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public ConfigWrapper getConfigForClientsWorkaroundForXml() {
        return new ConfigWrapper(operatorService.getOperatorConfigListForClients(operatorId));
    }

    @Path("config")
    @PUT
    public Response updateConfig(Map<OperatorConfigParamDTO,String> config) {
    	log.info("Update operator["+operatorId+"] config: "+config);
        Set<OperatorConfigParamDTO> keys = config.keySet();
        for(OperatorConfigParamDTO param : keys) {
            operatorService.saveConfig(operatorId,param,config.get(param));
        }
        return Response.status(Response.Status.OK).build();
    }
    
    @Path("update")
    @PUT
    public Response updateOperator(OperatorDTO operator) {
        operatorService.updateOperator(operator);
        return Response.status(Response.Status.OK).build();
    }


    @Path("config/{param}")
    @GET
    public String getConfig(@PathParam("operatorId") Long operatorId,
                            @PathParam("param") OperatorConfigParamDTO param) {

        return operatorService.getOperatorConfig(operatorId,param);
    }
    
    
    @Path("config/{param}/{value}")
    @PUT
    @Consumes({MediaType.TEXT_PLAIN})
    public Response setConfigParamValue(@PathParam("param") OperatorConfigParamDTO param,
    								  @PathParam("value") String value) {
    	log.info("Update operator["+operatorId+"] param: "+param+" -> "+value);
    	operatorService.saveConfig(operatorId, param, value);
        return Response.ok().build();
    }
    
    @GET
    @Path("enabled")
    public Boolean isEnabled(@PathParam("operatorId") Long operatorId) {
        return operatorService.isEnabled(operatorId);
    }

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	
	@Path("wallet")
    public OperatorWalletResource getOperatorWallet() {
		OperatorWalletResource wallet = ctx.getResource(OperatorWalletResource.class);
		wallet.setOperatorId(operatorId);
        return wallet;
    }

}
