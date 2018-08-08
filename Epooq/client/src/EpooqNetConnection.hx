
import flash.events.AsyncErrorEvent;
import flash.net.NetConnection;

class EpooqNetConnection extends NetConnection {

 	public function new() {
		super();

		this.addEventListener(AsyncErrorEvent.ASYNC_ERROR, asyncErrorHandler);
	}
    
	private function asyncErrorHandler(event:AsyncErrorEvent) {
		//trace(event.error);
	}
}