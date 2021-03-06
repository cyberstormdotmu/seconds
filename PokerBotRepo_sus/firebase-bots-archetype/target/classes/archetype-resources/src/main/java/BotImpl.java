package ${package};

import com.cubeia.firebase.bot.Bot;
import com.cubeia.firebase.bot.ai.BasicAI;
import com.cubeia.firebase.io.protocol.GameTransportPacket;
import com.cubeia.firebase.io.protocol.ProbePacket;

public class BotImpl extends BasicAI {
	
	/*
	 * This is an example parameter, it will be set
	 * via the bot control file...
	 */
	private boolean fixedRate;
	
	public BotImpl(Bot bot) {
		super(bot);
	}
	
	// --- BOT PARAMETERS --- //
	
	public boolean getFixedRate() {
		return fixedRate;
	}

	public void setFixedRate(boolean fixedRate) {
		this.fixedRate = fixedRate;
	}
	
	
	// --- BOT METHODS --- //

	public void handleGamePacket(GameTransportPacket packet) { 
		/*
		 * Handle the game data packet here...
		 */
	}

	public void handleProbePacket(ProbePacket packet) { 
		/*
		 * This method can safely be ignored
		 */
	}

	public void stop() { 
		/*
		 * Stop any threads the bot is using.
		 */
	}

	@Override
	protected void handleSeated() {
		super.handleSeated();
		/*
		 * This is where you add logic the bot should do
		 * when it is first seated, like scheduling the first
		 * action to do...
		 */
	}
}
