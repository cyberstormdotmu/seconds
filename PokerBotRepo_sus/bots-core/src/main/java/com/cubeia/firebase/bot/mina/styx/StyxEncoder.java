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

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.cubeia.firebase.bot.Bot;
import com.cubeia.firebase.bot.mina.crypto.AESCryptoProvider;
import com.cubeia.firebase.io.ProtocolObject;
import com.cubeia.firebase.io.StyxSerializer;
import com.cubeia.firebase.io.protocol.EncryptedTransportPacket;

public class StyxEncoder implements ProtocolEncoder {

	private transient Logger log = Logger.getLogger(this.getClass());
	
	/** Making static to avoid creating new all the time */
	private static StyxSerializer styx = new StyxSerializer(null);
	
	public static final int ENCRYPTED_DATA = 0;
	public static final int SESSION_KEY_REQUEST = 1;
	public static final int SESSION_KEY_RESPONSE = 2;		
	
	/**
	 * Release all resources related with this encoder.
	 */
	public void dispose(IoSession session) throws Exception {
		
	}
	
	/**
	 * Encodes higher-level message objects into binary or protocol-specific data. 
	 * MINA invokes encode(IoSession, Object, ProtocolEncoderOutput)  
	 * method with message which is popped from the session write queue, and then 
	 * the encoder implementation puts encoded ByteBuffers into ProtocolEncoderOutput.
	 */
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if (message instanceof ProtocolObject) {
			ProtocolObject packet = (ProtocolObject)message;

			if (session.containsAttribute(Bot.USE_CRYPTO)) {
				// System.err.println("using crypto");
				packet = encrypt(session, packet);
			}

        	ByteBuffer packed = styx.pack(packet);
        	
        	// MINA uses it's own cute little ByteBuffer implementation
        	out.write(org.apache.mina.common.ByteBuffer.wrap(packed));
        	
		} else {
			log.fatal("Unknown message object encountered: "+message);
		}
	}

	private ProtocolObject encrypt(IoSession session, ProtocolObject packet) throws Exception {
		// get crypto provider
		AESCryptoProvider cryptoProvider = (AESCryptoProvider) session.getAttribute(Bot.CRYPTO_PROVIDER);
		// get data to be encrypted
		ByteBuffer buffer = styx.pack(packet);
		
		// create EncryptedTransportPacket
		EncryptedTransportPacket encryptedTransportPacket = new EncryptedTransportPacket();
		encryptedTransportPacket.func = ENCRYPTED_DATA;
		// encrypt data
		encryptedTransportPacket.payload = cryptoProvider.encrypt(buffer.array());
		return encryptedTransportPacket;
	}	
}
