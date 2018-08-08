package fi.korri.epooq;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.scope.IScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EpooqAdapter extends ApplicationAdapter {

	private static final Logger log = LoggerFactory.getLogger(EpooqAdapter.class);
	
	@Override
	public boolean appStart(IScope app) {
		log.info("Epooq appStart");
		return true;
	}
}
