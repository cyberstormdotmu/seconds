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

import java.util.LinkedList;
import java.util.List;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.cubeia.firebase.bot.Bot;
import com.cubeia.firebase.bot.mina.crypto.AESCryptoProvider;
import com.cubeia.firebase.io.ProtocolObject;
import com.cubeia.firebase.io.StyxSerializer;
import com.cubeia.firebase.io.protocol.EncryptedTransportPacket;
import com.cubeia.firebase.io.protocol.ProtocolObjectFactory;

/**
 * Protocol decoder for the firebase wireprotocol.
 * 
 * @author Fredrik
 *
 */
public class StyxDecoder extends CumulativeProtocolDecoder {
	
	// private static final Logger log = Logger.getLogger(StyxDecoder.class);
	
	private StyxSerializer styx = new StyxSerializer(new ProtocolObjectFactory());
	
    private final boolean processLobby;
	
	public StyxDecoder(boolean processLobby) {
        this.processLobby = processLobby;
    }


    /**
	 * Decodes binary or protocol-specific content into higher-level message objects. 
	 * 
	 * MINA invokes decode(IoSession, ByteBuffer, ProtocolDecoderOutput)  
	 * method with read data, and then the decoder implementation puts decoded messages 
	 * into ProtocolDecoderOutput.
	 * 
	 */
	public boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
		List<ProtocolObject> packets = new LinkedList<ProtocolObject>();
		boolean enough = true;
		int payload = -1;
        short packetId = -1;
		
		// Find out if we have enough data for a full packet
        // We also extract the packet id for early filtering
		if (in.remaining() >= 5) {
			in.mark();
			payload = in.getInt();
			packetId = in.getUnsigned();
			in.reset();
			if (in.remaining() < payload) {
				enough = false;
			}
		} else {
			enough = false;
		}
		
		if (enough) {
            if (processLobby || !isLobbyPacket(packetId)) {
    			ProtocolObject packet = styx.unpack(in.buf());

    			if (packet != null) {
    				packets.add(decrypt(session, packet));
    	        }
            } else {
                // flush packet (dummy read it)
                in.get(new byte[payload]);
            }
		}
		
		out.write(packets);
		
		// If we return true this method will be called again
		// If we have more data remaining and last data was a packet then rerun
		boolean cont = in.remaining() > 0 && enough;
		return cont;
	}
	
	/**
	 * Decrypts the given packet, if it is encrypted and returns a unencrypted packet
	 * 
	 * Note that null may be returned in case the packet should be dropped.
	 * 
	 * @param session
	 * @param packet
	 * @return
	 * @throws Exception 
	 */
	private ProtocolObject decrypt(IoSession session, ProtocolObject packet) throws Exception {
		if (packet instanceof EncryptedTransportPacket) {			
			EncryptedTransportPacket encrypted = (EncryptedTransportPacket) packet;
			switch (encrypted.func) {
				case StyxEncoder.ENCRYPTED_DATA:
					// decrypt data and pass it on through the filter chain
					byte[] decrypted = handleEncryptedData(encrypted.payload, session);
					packet = styx.unpack(java.nio.ByteBuffer.wrap(decrypted));
					break;
			}
					
		}
		
		return packet;
	}
	
	/**
	 * Decrypt incoming data
	 * 
	 * @param data - incoming data
	 * @param session - Apache MINA session
	 * 
	 * @return clear text data
	 */
	public byte[] handleEncryptedData(byte[] data, IoSession session) throws Exception {
		session.setAttribute("usecrypto");

		AESCryptoProvider cryptoProvider = (AESCryptoProvider) session.getAttribute(Bot.CRYPTO_PROVIDER);
		return cryptoProvider.decrypt(data);
	}
	
	
	private boolean isLobbyPacket(short packetId) {
	    // Table snapshot + update
	    if (packetId == 143 || packetId == 144 || packetId == 147) {
	        return true;
	    }
	    
	    // Tournament snapshot + update
        if (packetId == 148 || packetId == 149 || packetId == 150) {
            return true;
        }
        
	    return false;
    }


    /**
	 * Release all resources related with this decoder.
	 */
	public void dispose(IoSession session) throws Exception {}

	
	/**
	 * Invoked when the specified session is closed. This method is useful 
	 * when you deal with the protocol which doesn't specify the length of a 
	 * message such as HTTP response without content-length header. 
	 * 
	 * Implement this method to process the remaining data that 
	 * decode(IoSession, ByteBuffer, ProtocolDecoderOutput)  
	 * method didn't process completely.
	 * 
	 */
	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {}

}
