
import flash.external.ExternalInterface;

import flash.display.MovieClip;
import flash.display.Sprite;
import flash.Lib;

import flash.events.NetStatusEvent;
import flash.events.AsyncErrorEvent;
import flash.events.Event;

import flash.media.Camera;
import flash.media.Video;
import flash.media.Microphone;

import haxe.Timer;

class EpooqClient implements ControlInterface {
	
	private static var client : EpooqClient;
	
	private var nc:EpooqNetConnection;

	private var cam:Camera;
    private var mic:Microphone;
    
    private var cameraView:CameraView;
    private var playbackView:PlaybackView;

	public function new() {
	
		// JS Callbacks
		ExternalInterface.addCallback("openCamera", openCamera);
		ExternalInterface.addCallback("startRecording", startRecording);
		ExternalInterface.addCallback("stopRecording", stopRecording);
		ExternalInterface.addCallback("startPlayback", startPlayback);
		
		cameraView = new CameraView(Lib.current, this);
		playbackView = new PlaybackView(Lib.current, this);
		
		initConnection();
    }
    
    private function initConnection():Void {
    
    	trace("Connecting...");

		nc = new EpooqNetConnection();
		nc.client = this;
		nc.addEventListener(NetStatusEvent.NET_STATUS, onNetConnectionStatus);
		nc.connect("rtmp://127.0.0.1/epooq");
    }
    
	private function onNetConnectionStatus(event:NetStatusEvent) {

		trace(event.info.code);

		switch(event.info.code) {
			case 'NetConnection.Connect.Success':
				ExternalInterface.call("onConnectSuccess");
			case 'NetConnection.Connect.Failed':
				ExternalInterface.call("onConnectFailed");
			case 'NetConnection.Connect.Closed':
				ExternalInterface.call("onConnectClosed");
		}
	}
	
	/*
	 * ControlInterface
	 */
	 
	public function showCamera() {
		playbackView.hide();	
		cameraView.show();
	}
	
	public function showPlayback() {
		cameraView.hide();
		playbackView.show();
	}
    
    /*
     * JS Interface
     */
    
    public function openCamera() {
    	showCamera();
    }
    
    public function startRecording(filename:String) {
    	cameraView.startSending(nc, filename);
    }
    
	public function stopRecording() {
		cameraView.stopSending();
    }
    
    public function startPlayback(filename:String) {
    	playbackView.stopPlayback();
    	showPlayback();
		playbackView.startPlayback(nc, filename);
    }

	/*
	 * Initialization
	 */

    public static function onEnterFrame(event:Event):Void {
    	trace("Enter frame");
    	
    	Lib.current.stage.removeEventListener(Event.ENTER_FRAME, onEnterFrame); 
    	
    	// Start client
    	client = new EpooqClient();
    }
    
    static function main() {
		Lib.current.stage.addEventListener(Event.ENTER_FRAME, onEnterFrame);
    }
}