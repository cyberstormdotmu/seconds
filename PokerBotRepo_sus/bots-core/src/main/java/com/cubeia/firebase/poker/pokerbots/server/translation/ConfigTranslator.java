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

package com.cubeia.firebase.poker.pokerbots.server.translation;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import com.cubeia.firebase.bot.BotConfig;
import com.cubeia.firebase.poker.pokerbots.server.Batch;

/**
 * Translates batch DTOs to BotConfig(s).
 * 
 * The keys used to get configuration properties needs to be matched with the
 * ones defined in the html forms.
 * 
 * @author Fredrik
 */
public class ConfigTranslator {
    
	@SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(ConfigTranslator.class);
    
	public static String TOURNAMENT_PROPERTY_FLAG = "tournament_bot";
	
    /**
     * Translate a batch job to a regular GolemConfig.
     * 
     * @param batch
     * @return
     */
    public static BotConfig translate(Batch batch) {
        BotConfig config = new BotConfig();
        
        /* ----- REQUIRED PARAMETERS ----- */
        InetSocketAddress host = new InetSocketAddress(batch.getUrl(), batch.getPort());
        
        config.setHost(host);
        config.setStartid(batch.getStartingid());
        config.setGroupid(batch.getId());
        config.setGameId(batch.getGameId());
        config.setReconnect(!batch.getFlags().containsKey("noReconnect"));
        config.setTournamentBot(batch.getFlags().containsKey("tournament_bot"));
        config.setConnectorType(batch.getConnectorType());
        config.setCurrency(batch.getCurrency());
        
        /* ----- AI Implementation ----- */
        
        if (batch.getStringProperties().containsKey("aiclass")) {
            config.setAiclass(batch.getStringProperties().get("aiclass"));
            batch.getStringProperties().remove("aiclass");
        }
        
        if (batch.getStringProperties().containsKey("groupconfigclass")) {
            config.setGroupConfigClass(batch.getStringProperties().get("groupconfigclass"));
            batch.getStringProperties().remove("groupconfigclass");
        }
        
        /* ---------- BATCH PROPERTIES ----------- */
        config.setBatchProperties(batch.getProperties());
        config.setStringProperties(batch.getStringProperties());
        config.setLobbyAttributes(batch.getLobbyAttributes());
        
        /* ---------- BATCH FLAGS ----------- */
        config.setBatchFlags(batch.getFlags());
        
        return config;
    }

}
