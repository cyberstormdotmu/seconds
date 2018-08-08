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

package com.cubeia.firebase.bot;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.cubeia.firebase.api.defined.Parameter;
import com.cubeia.firebase.bot.action.Action;
import com.cubeia.firebase.bot.ai.AI;
import com.cubeia.firebase.bot.ai.LoginCredentials;
import com.cubeia.firebase.bot.ai.impl.lobby.LobbyAI;
import com.cubeia.firebase.bot.io.BinaryUtil;
import com.cubeia.firebase.bot.io.ConnectionListener;
import com.cubeia.firebase.bot.io.IOClient;
import com.cubeia.firebase.bot.io.connectors.CometConnectorClient;
import com.cubeia.firebase.bot.io.connectors.SocketConnectorClient;
import com.cubeia.firebase.bot.io.connectors.WebSocketConnectorClient;
import com.cubeia.firebase.bot.io.connectors.mina.MinaClient;
import com.cubeia.firebase.bot.mina.crypto.AESCryptoProvider;
import com.cubeia.firebase.bot.model.Table;
import com.cubeia.firebase.bot.model.TargetTable;
import com.cubeia.firebase.bot.packet.PacketHandler;
import com.cubeia.firebase.bot.packet.PacketModelHandler;
import com.cubeia.firebase.bot.util.ChatRepo;
import com.cubeia.firebase.bot.util.PropertyCache;
import com.cubeia.firebase.io.ProtocolObject;
import com.cubeia.firebase.io.protocol.EncryptedTransportPacket;
import com.cubeia.firebase.io.protocol.FilteredJoinTableRequestPacket;
import com.cubeia.firebase.io.protocol.JoinRequestPacket;
import com.cubeia.firebase.io.protocol.LeaveRequestPacket;
import com.cubeia.firebase.io.protocol.LobbyQueryPacket;
import com.cubeia.firebase.io.protocol.LoginRequestPacket;
import com.cubeia.firebase.io.protocol.LogoutPacket;
import com.cubeia.firebase.io.protocol.Param;
import com.cubeia.firebase.io.protocol.ParamFilter;
import com.cubeia.firebase.io.protocol.TableChatPacket;
import com.cubeia.firebase.io.protocol.UnwatchRequestPacket;
import com.cubeia.firebase.poker.pokerbots.jmx.ResourceMonitor;
import com.cubeia.firebase.poker.pokerbots.thread.JmxScheduledExecutor;

/**
 * Basic Bot.
 * 
 * A Bot's responses are handled through action (see Action class). The Bot's AI
 * implementation should return proper actions that will be executed by the bot.
 * 
 * 
 * Created on 2006-sep-26
 * 
 * @author Fredrik Johansson
 * 
 * $RCSFile: $ $Revision: $ $Author: $ $Date: $
 */
public class Bot implements ConnectionListener {

	public static final String CRYPTO_PROVIDER = "crypto";

	private static Map<String, PropertyCache> propertyCache = new ConcurrentHashMap<String, PropertyCache>();

	private PropertyCache properties;

	public static final int MAX_CONNECT_TRIES = 50;

	private Logger log = Logger.getLogger(this.getClass());

	/** Executor for actions */
	// static ScheduledExecutorService executor =
	// Executors.newScheduledThreadPool(4, new
	// NamedThreadFactory("BotExecutor"));
	public static JmxScheduledExecutor executor = new JmxScheduledExecutor(4, "BotExecutor");

	/** Group belonging */
	private BotGroup group;

	/** The packet handler for the bot */
	private PacketHandler packetHandler;

	/** The packet handler for the bot Ai model */
	private PacketModelHandler packetModelHandler;

	/** The configuration for this bot */
	private BotConfig config;

	/** The underlying connection handler */
	protected IOClient client; 

	/** Number of reconnection tries made */
	private int connectionRetries = 0;

	/** Local Bot ID. This is not the same as Player ID in the server! */
	private int id;

	private String screenname;

	/** Current state */
	private BotState state = BotState.IDLE;

	private AI botAI;

	private long joinRequestTimestamp;

	/** Set by the system. */
	private int pid;

	private int requestedTableId;

	/** The number of seats that should be left empty. */
	private int leaveEmptySeats = 0;

	private final boolean useCrypto;

	public static final int ENCRYPTED_DATA = 0;
	public static final int SESSION_KEY_REQUEST = 1;
	public static final int SESSION_KEY_RESPONSE = 2;

	public static final String USE_CRYPTO = "usecrypto";

	private RSAPrivateKey privateKey;

	@SuppressWarnings("unused")
	private RSAPublicKey publicKey;

	private boolean loggedOut = false;
	
	public Bot(BotGroup group, int id, BotConfig config) {
		this.group = group;
		this.config = config;
		this.id = id;

		createConnectorType();

		Map<String, Boolean> flags = config.getBatchFlags();
		Boolean test = (flags == null ? null : flags.get("encryption"));
		this.useCrypto = (test == null ? false : test.booleanValue());

		initAI();

		packetHandler = new PacketHandler(this);
		client.addPacketHandler(packetHandler);
		client.setConnectionListener(this);

		if (config.getBatchProperties() != null && config.getBatchProperties().containsKey("leaveEmptySeats")) {
			this.leaveEmptySeats = config.getBatchProperties().get("leaveEmptySeats");
		}
	}

	private void createConnectorType() {
		if (config.getConnectorType() == null) {
			log.warn("No connector type specified, will default to NIO Socket connector provided by MINA.");
			client = new MinaClient();

		} else if (config.getConnectorType().equalsIgnoreCase("mina")) {
			log.info("Using MINA connector");
			client = new MinaClient();

		} else if (config.getConnectorType().equalsIgnoreCase("socket")) {
			log.info("Using socket connector");
			client = new SocketConnectorClient();

		} else if (config.getConnectorType().equalsIgnoreCase("websocket")) {
			log.info("Using WebSocket connector");
			client = new WebSocketConnectorClient();

		} else if (config.getConnectorType().equalsIgnoreCase("comet")) {
			log.info("Using Comet connector");
			client = new CometConnectorClient();

		} else {
			log.warn("Unknown connector type ["+config.getConnectorType()+"], will default to NIO Socket connector provided by MINA.");
			client = new MinaClient();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initAI() {
		// Setup AI

		boolean found = false;
		if (config.getAiclass() != null || !config.getAiclass().equals("")) {
			try {

				// Find bot AI class and create an instance
				Class ai = Class.forName(config.getAiclass());
				Constructor con = ai.getConstructor(new Class[] { this.getClass() });
				botAI = (AI) con.newInstance(new Object[] { this });

				// Get the property cache or create one if it doesn't exist
				checkPropertyCache();

				// set the properties for this AI
				setPropertyValues();

				found = true;
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("Specified ai-class not valid: " + config.getAiclass()+ " e: " + e);
			}

		}

		if (!found) {
			botAI = new LobbyAI(this);
			log.warn("Bot AI not specified. Using default: "+ botAI.getClass());
		}

		if (botAI.trackTableState()) {
			// Ordering is important, we want the model handler prior to
			// the bot packet handler so we have the latest state
			packetModelHandler = new PacketModelHandler(this);
			client.addPacketHandler(packetModelHandler);
		}
	}

	/**
	 * Check if we have created a property cache,
	 * if not - create it!
	 *
	 */
	private void checkPropertyCache() {
		properties = propertyCache.get(config.getAiclass());
		if (properties == null) {
			synchronized (propertyCache) {
				// double check, we might have a race condition and lost
				if (propertyCache.get(config.getAiclass()) == null) {
					// Create a new property cache and populate it 
					// with the setter properties in the AI object
					properties = new PropertyCache(botAI);
					propertyCache.put(config.getAiclass(), properties);
				}
			}
		}
	}

	/**
	 * Add a PacketHandler to the client connection.
	 * 
	 */
	public void addPacketHandler(PacketHandler handler) {
		client.addPacketHandler(handler);
	}

	@Override
	public String toString() {
		return screenname + "[" + pid + "]";
	}

	/**
	 * Send Game Data (as a GameTransportPacket) to the server.
	 * 
	 * @param packet
	 */
	public void sendGameData(int tableid, int pid, ProtocolObject packet) {
		ResourceMonitor.gamePacketCounter.register();
		client.sendDataPacket(tableid, pid, packet);
	}

	public void wrapAndSendByteArray(int tableId, int pid, byte[] bytes) {
		client.wrapAndSendByteBuffer(tableId, pid, ByteBuffer.wrap(bytes));
	}

	public void sendPacket(ProtocolObject packet) {
		client.sendPacket(packet);
	}

	public void sendPacketAsync(final ProtocolObject packet) {
		Action action = new Action(this) {
			public void run() {
				client.sendPacket(packet);
			}
		};
		executor.submit(action);
	}

	/**
	 * Connect to the address from config
	 * 
	 */
	public void connect() {
		client.connect(config.getHost(), botAI.processLobbyPackets());
	}

	/**
	 * Login to the game server
	 * 
	 */
	public void login() {
		login(null);
	}

	/**
	 * Sends a login request, with credentials.
	 * 
	 * @param credentials
	 */
	public void login(byte[] credentials) {
		LoginRequestPacket packet = new LoginRequestPacket();

		LoginCredentials loginCredentials = botAI.getCredentials();
		if (loginCredentials != null) {
			logDebug("Logging in with game specific credentials: " + loginCredentials);
			packet.user = loginCredentials.getUsername();
			packet.password = loginCredentials.getPassword();
			packet.operatorid = loginCredentials.getOperatorId();
		} else {
			packet.user = screenname;
			packet.password = id + "";
			packet.operatorid = 0;
		}

		if (credentials != null) {
			logDebug("Logging in with credentials " + new String(credentials));
			packet.credentials = credentials;
		}

		sendPacket((ProtocolObject) packet);
	}

	/**
	 * Login using the supplied credentials
	 *
	 */
	public void login(String user, String password) {
		LoginRequestPacket packet = new LoginRequestPacket();
		packet.user = user;
		packet.password = password;
		packet.operatorid = 0;

		sendPacket((ProtocolObject) packet);
	}

	/**
	 * Logout from the game server
	 *
	 */
	public void logout() {
		logDebug("Logging out");
		botAI.stop();
		LogoutPacket packet = new LogoutPacket();
		packet.leaveTables = true;
		sendPacket((ProtocolObject) packet);
		loggedOut  = true;
		//		executor.schedule(new Runnable() {
		//			public void run() {
		client.disconnect();
		//			}
		//		}, 12, TimeUnit.MILLISECONDS);
	}

	/**
	 * Try and take a seat at a table.
	 * 
	 */
//	@SuppressWarnings("cast")
//	public void watchRandomTable() {
//		WatchRequestPacket packet = new WatchRequestPacket();
//		packet.tableid = group.getLobby().getRandomTableId();
//
//		sendPacket((ProtocolObject) packet);
//	}


	public void joinWaitingList() {
		FilteredJoinTableRequestPacket packet = new FilteredJoinTableRequestPacket();
		packet.gameid = getGroup().getConfig().getGameId();
		packet.address = "/";

		Param identifier = new Param();
		identifier.key = "_SEATED";
		identifier.type = (byte)Parameter.Type.INT.ordinal();
		identifier.value = BinaryUtil.intToBytes(0);

		ParamFilter filter = new ParamFilter();
		filter.op = (byte)Parameter.Operator.EQUALS_OR_GREATER_THAN.ordinal();
		filter.param = identifier;

		List<ParamFilter> params = new LinkedList<ParamFilter>();
		params.add(filter);
		packet.params = params;
		logDebug("Join waitinglist: "+packet);
		sendPacket(packet);
	}

	/**
	 * Try and take a seat at a table.
	 * 
	 * @return true if successful
	 */
	@SuppressWarnings({ "cast", "unchecked" })
	public boolean joinRandomTable() {
		JoinRequestPacket packet = new JoinRequestPacket();
		TargetTable target = group.getRandomTargetTable(pid, leaveEmptySeats);
		
		if (target == null) {
			logWarn("Did not get a table to join (" + group.getLobby().getTableCount() + " known tables)");
		} else {
			logDebug("Join target table: "+target.getTable().getId());
		}

		if (target == null || target.getTable() == null) {
			// logInfo("I could not get a target for seating. I will reschedule
			// myself. Target returned: "+target);
			return false;
		}

		Table table = target.getTable();
		requestedTableId = table.getId();
		joinRequestTimestamp = System.currentTimeMillis();
		packet.tableid = table.getId();
		packet.seat = (byte) target.getSeat();
		packet.params = Collections.EMPTY_LIST;
		sendPacketAsync((ProtocolObject) packet);
		return true;
	}

	/**
	 * Send a request to join the given table and seat.
	 * 
	 * @param tableId
	 * @param seat
	 */
	@SuppressWarnings("unchecked")
	public void joinTable(int tableId, int seat) {
		JoinRequestPacket packet = new JoinRequestPacket();
		requestedTableId = tableId;
		joinRequestTimestamp = System.currentTimeMillis();
		packet.tableid = tableId;
		packet.seat = (byte) seat;
		packet.params = Collections.EMPTY_LIST;
		sendPacket((ProtocolObject) packet);
	}



	@SuppressWarnings("cast")
	public void leaveTable() {
		LeaveRequestPacket packet = new LeaveRequestPacket();
		packet.tableid = this.getBotAI().getTable().getId();
		logDebug("Leaving table: " + packet.tableid);
		sendPacket((ProtocolObject) packet);
	}

	@SuppressWarnings("cast")
	public void unwatchTable() {
		unwatchTable(this.getBotAI().getTable().getId());
	}

	public void unwatchTable(int tableid) {
		UnwatchRequestPacket packet = new UnwatchRequestPacket();
		packet.tableid = tableid;
		sendPacket((ProtocolObject) packet);
	}

	/**
	 * Get the default lobby list
	 * 
	 */
	public void getLobby() {
		LobbyQueryPacket packet = new LobbyQueryPacket();
		packet.address = "";
		sendPacket(packet);
	}

	/**
	 * Update state. Will propagate state handling to the AI.
	 */
	public void setState(BotState newState) {
		this.state = newState;
		botAI.handleStateChange(state);
	}

	@SuppressWarnings("unused")
	private void logSeating() {
		StringBuilder nfo = new StringBuilder(this.toString()).append(" was seated in ");
		if (joinRequestTimestamp > 0)
			nfo.append(System.currentTimeMillis() - joinRequestTimestamp).append("ms");
		log.info(nfo.toString());
	}

	public void logDebug(String msg) {
		log.debug(this.toString() + " - " + msg);
	}

	public void logInfo(String msg) {
		log.info(this.toString() + " - " + msg);
	}

	public void logWarn(String msg) {
		log.warn(this.toString() + " - " + msg);
	}

	// -----------------------
	// Connection Listener methods
	//

	/**
	 * I was connected to a game server
	 */
	public void gotConnected() {
		connectionRetries = 0;
		group.getStats().connected.incrementAndGet();

		if (useCrypto) {
			// System.out.println(id + " is requesting session key.");
			// Request crypto key.
			requestSessionKey();
		} else {
			setState(BotState.CONNECTED);
		}
	}

	private void requestSessionKey() {
		EncryptedTransportPacket packet = new EncryptedTransportPacket();
		packet.func = SESSION_KEY_REQUEST;
		KeyPair keyPair;
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(new RSAKeyGenParameterSpec(512, new BigInteger("10001", 16)));
			keyPair = generator.generateKeyPair();
			privateKey = (RSAPrivateKey) keyPair.getPrivate();
			publicKey = (RSAPublicKey) keyPair.getPublic();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		AESCryptoProvider provider = new AESCryptoProvider();
		client.getSession().setAttribute(CRYPTO_PROVIDER, provider);		

		packet.payload = ((RSAPublicKey) keyPair.getPublic()).getModulus().toString(16).getBytes();
		sendPacket(packet);		
	}

	/**
	 * I got disconnected from a game server
	 */
	public void gotDisconnected() {
		group.getStats().connected.decrementAndGet();
		if (!(state.equals(BotState.IDLE) || state.equals(BotState.CONNECTED))) {
			group.getStats().loggedin.decrementAndGet();
		}
		setState(BotState.IDLE);
		botAI.handleDisconnected();
		gotConnectionError();
	}

	/**
	 * Connection was refused. Try and reconnect if applicable
	 */
	public void gotConnectionError() {
		new RuntimeException().printStackTrace();
		boolean reconnect = !loggedOut && config.isReconnect();
		logInfo("Got Connection Error. Will Reconnect: "+reconnect);
		if (reconnect) {
			if (connectionRetries < MAX_CONNECT_TRIES) {
				connectionRetries++;
				logInfo("Connection lost. Will reconnect for the "+ connectionRetries + " time");
				Runnable connect = new Runnable() {
					public void run() {
						try {
							connect();
						} catch (Throwable th) {
							log.error("Error when connecting to remote server: "+th, th);
						}
					}
				};

				executor.schedule(connect, 1000, TimeUnit.MILLISECONDS);

			} else {
				logInfo("Connection refused " + MAX_CONNECT_TRIES
						+ " times. I'm giving up!");
			}
		} else {
			logInfo("Connection closed");
		}
	}

	// -----------------------
	// Getters & Setters
	//

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getScreenname() {
		return screenname;
	}

	public void setScreenname(String screenname) {
		this.screenname = screenname;
	}

	public BotState getState() {
		return state;
	}

	public BotGroup getGroup() {
		return group;
	}

	public AI getBotAI() {
		return botAI;
	}

	public int getRequestedTableId() {
		return requestedTableId;
	}
    
    public int getLeaveEmptySeats() {
        return leaveEmptySeats;
    }

	/**
	 * Set the property values for the ai
	 *
	 */
	private void setPropertyValues() {
		// Set integer properties
		setPropeties(config.getBatchProperties());

		// Set boolean properties
		setPropeties(config.getBatchFlags());

		// Set string properties
		setPropeties(config.getStringProperties());		
	}

	private void setPropeties(Map<String, ?> props) {
		if (props != null) {
			for (String s : props.keySet()) {
				log.info("SET PROP: "+s+" -> "+props.get(s));
				// create method name
				String methodName = "set" + s.substring(0,1).toUpperCase() + s.substring(1);
				// find method in property cache 
				Method method = properties.getMethod(methodName);
				if (method != null) {
					try {
						method.invoke(botAI, props.get(s));
					} catch (Exception e) {
						// Gulp.
					}
				}
			}
		}
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getPid() {
		return pid;
	}

	public void chat() {
		TableChatPacket packet = new TableChatPacket();
		packet.pid = getPid();
		packet.message = ChatRepo.getInstance().getRandom();
		packet.tableid = botAI.getTable().getId();
		sendPacket(packet);
	}

	public IOClient getClient() {
		return client;
	}

	public Key getPrivateKey() {
		return privateKey;
	}

}
