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
package com.cubeia.backoffice.wallet.service;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.cubeia.backoffice.accounting.api.AccountDTO;
import com.cubeia.backoffice.accounting.api.TransactionDTO;
import com.cubeia.backoffice.accounting.core.integrations.AccountingManagerPluginAdapter;
import com.cubeia.backoffice.accounting.core.integrations.PluginManager;
import com.cubeia.backoffice.wallet.api.dto.Account.AccountType;

public class IndexingPlugin extends AccountingManagerPluginAdapter {
	
	private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource(name = "accounting.pluginManager")
    private PluginManager pluginManager;
	
    @Value("${elastic.cluster.name}")
    private String clusterName;
    
    @Value("${elastic.client.url}")
    private String clientUrl;
    
    private final ExecutorService exec = new ThreadPoolExecutor(1, 20, 5, SECONDS, new ArrayBlockingQueue<Runnable>(100), new ThreadPoolExecutor.CallerRunsPolicy());
	
	private final ObjectMapper mapper = new ObjectMapper();

    private URL url;
    
    private String indexName;
    
    private Client client;
	
	/*
	 * Init is called by the spring context.
	 */
	public void init() {
        if (clientUrl != null && clientUrl.length() > 0) {
            try {
                url = new URL(clientUrl);
                indexName = url.getPath().replace("/", "");
            } catch (MalformedURLException e) {
                log.error("error parsing elasticsearch url, indexing plugin disabled", e);
            }
            
            log.info("Registering indexing plugin with base URL: {}, cluster name = {}, index name = {}", 
                new Object[] { clientUrl, clusterName, indexName });
            
            pluginManager.getPlugins().add(this);
            
        } else {
            log.info("Indexing plugin disabled");
        }
	    
	    
	}
	
	@Override
	public void afterCreate(AccountDTO account) {
	    addTask(account);
	}
	
	@Override
	public void afterUpdate(AccountDTO account) {
        addTask(account);
	}

	@Override
	public void afterCreate(TransactionDTO tx) {
        addTask(tx);
	}
	
	@Override
	public void afterUpdate(TransactionDTO tx) {
        addTask(tx);
	}
	
	private void addTask(TransactionDTO dto) {
	    addIndexingTask(dto, dto.getId());
	}
	
    private void addTask(AccountDTO dto) {
        if (AccountType.SESSION_ACCOUNT.name().equals(dto.getType())) {
            // session account, don't index this
        } else {
            addIndexingTask(dto, dto.getId());
        }
    }
	
	protected String generateTypeName(final Object entity) {
	    return entity.getClass().getSimpleName().replaceFirst("DTO$", "").toLowerCase();
	}
	
    private Client getClient() {
        if (client == null) {
            Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build();
            client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(url.getHost(), url.getPort()));
        }
        return client;
    }

    private void addIndexingTask(final Object entity, final Long id) {
        exec.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    doIndex(entity, id);
                } catch (Exception e) {
                    //log.error("error indexing user", e);
                }
            }
        });
    }

    private void doIndex(final Object entity, final Long id) throws IOException, JsonGenerationException, JsonMappingException {
        Client client = getClient();
        String json;
        json = mapper.writeValueAsString(entity);
        
        String type = generateTypeName(entity);
        IndexResponse resp = client.prepareIndex(indexName, type, "" + id)
            .setSource(json)
            .execute()
            .actionGet();        
        
        log.debug("{} indexed, created = {}, version = {}", 
            new Object[] { type, resp.isCreated(), resp.getVersion() });
    }	
	
	
}
