
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
    private var microphoneView:MicrophoneView;
    //private var main:Main;
    
    
    //var paramList:String = root.loaderInfo.parameters;
    var from : String = Lib.current.loaderInfo.parameters.from;

	public function new() {
	
		// JS Callbacks
		ExternalInterface.addCallback("openCamera", openCamera);
		ExternalInterface.addCallback("startRecording", startRecording);
		ExternalInterface.addCallback("stopRecording", stopRecording);
		ExternalInterface.addCallback("startPlayback", startPlayback);
		
		// Chintan
		ExternalInterface.addCallback("openMicrophone", openMicrophone);
		ExternalInterface.addCallback("startAudioRecording", startAudioRecording);
		ExternalInterface.addCallback("stopAudioRecording", stopAudioRecording);
		
		cameraView = new CameraView(Lib.current, this);
		playbackView = new PlaybackView(Lib.current, this);
		microphoneView = new MicrophoneView(Lib.current, this);
		//main = new Main();
		
		initConnection();
    }
    
    private function initConnection():Void {
    
    	trace("Connecting...");

		nc = new EpooqNetConnection();
		nc.client = this;
		nc.addEventListener(NetStatusEvent.NET_STATUS, onNetConnectionStatus);
		nc.connect("rtmp://183.182.91.146:9192/epooq");
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
	
	// Chintan
	public function showMicrophone() {
	trace("Show Microphone");
		try {
		microphoneView.show();
		}
    	catch(error : String){
    		ExternalInterface.call("console.log", error);
    		trace("1: " + error);
    	}
		
	}
	
	public function openMicrophone() {
	trace("Open Microphone 1");
	
		try {
    	showMicrophone();
    	}
    	catch(error : String){
    		ExternalInterface.call("console.log", error);
    		trace("2: " + error);
    	}
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
    
    	trace('From: ' + from);
    	ExternalInterface.call('console.log', 'From: ' + from);
    	
    	cameraView.startSending(nc, filename);
    	
    }
    
    public function startAudioRecording(filename:String) {
    
    	microphoneView.startSending(nc, filename);    	
    }
    
	public function stopRecording() {
	
		cameraView.stopSending();
    	
    }
    
    public function stopAudioRecording() {
	
			microphoneView.stopSending();
			ExternalInterface.call('console.log', 'After stop');
    		//nc.close();
    		
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