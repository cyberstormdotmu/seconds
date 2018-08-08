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

package com.cubeia.firebase.bot.mina.styx;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class StyxProtocolFactory implements ProtocolCodecFactory {
	
	private final StyxDecoder decoder;
	private final StyxEncoder encoder = new StyxEncoder();
    //private final boolean processLobby;
	
	public StyxProtocolFactory(boolean processLobby) {
        //this.processLobby = processLobby;
        decoder = new StyxDecoder(processLobby);
    }

    public ProtocolDecoder getDecoder() throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder() throws Exception {
		return encoder;
	}

}
