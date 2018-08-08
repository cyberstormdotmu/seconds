
import flash.net.NetConnection;
import flash.net.NetStream;

class EpooqNetStream extends NetStream {

 	public function new(nc:NetConnection) {
		super(nc);
		
		this.client = this;
		this.bufferTime = 0;
	}
	
	public function onMetaData(infoObject:Dynamic) {
		trace("onMetaData");
	}
}