package fi.korri.epooq;

import org.apache.log4j.Logger;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IScope;

public class EpooqAdapter extends ApplicationAdapter {

	private static final Logger log = Logger.getLogger(EpooqAdapter.class);
	
	@Override
	public boolean appStart(IScope app) {
		log.info("Epooq app start");
		return true;
	}
}
