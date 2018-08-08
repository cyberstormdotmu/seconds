

import flash.external.ExternalInterface;

import flash.display.Bitmap;

import flash.display.MovieClip;
import flash.media.Microphone;
import flash.display.BitmapData;
import flash.events.NetStatusEvent;
import flash.net.NetConnection;

import flash.Lib;
import haxe.Timer;

import flash.media.Sound;
import flash.sensors.Accelerometer;
import flash.utils.ByteArray;
 

class MicrophoneView {
    
    private var ns:EpooqNetStream;
    
   	private var mic:Microphone;
    

    
	private var parent : MovieClip;
    
    private var control : ControlInterface;
    
    private var visible : Bool = false;
    
    public function new( parent : MovieClip, control : ControlInterface) {
        
        this.parent = parent;
        this.control = control;
    }
    
   
    private function initAudio() {
    trace("Init Audio");
    ExternalInterface.call('console.log', 'Init Audio');
    try {
        this.mic = Microphone.getMicrophone();
                
        if (this.mic != null) {
	    	ExternalInterface.call('console.log', 'Inside MIC <> NULL');
	    	//this.mic.rate = 22;
	    	// this.mic.rate = 44;
	    	mic.rate = 16;
	    	mic.setSilenceLevel(0);
    		// mic.gain = 100;
    		mic.gain = 40;
	    	mic.setUseEchoSuppression(true); 
			mic.setLoopBack(false);
			mic.setSilenceLevel(5, 1000);	    	
	    	
	    	
	    	ExternalInterface.call('console.log', 'MIC activityLevel: ' + mic.activityLevel);
	    	ExternalInterface.call('console.log', 'MIC framesPerPacket: ' + mic.framesPerPacket);
	    	// ExternalInterface.call('console.log', 'MIC isSupported: ' + Microphone.isSupported);
	    	ExternalInterface.call('console.log', 'MIC muted: ' + mic.muted);
	    	ExternalInterface.call('console.log', 'MIC Names: ' + Microphone.names);
	    	ExternalInterface.call('console.log', 'MIC Name: ' + mic.name);
	    	ExternalInterface.call('console.log', 'MIC: ' + mic);
	    	
		}		
		}
    	catch(error : String){
    		trace(error);
    		ExternalInterface.call('console.log', error);
    	}
    }
    
    
    public function startSending( nc: NetConnection, filename:String ) {
    	trace ('Startrecording');
    	if(visible) {
	    	
	    	ExternalInterface.call('console.log', 'NC Client: ' + nc.client);
	    	ExternalInterface.call('console.log', 'NC Connected: ' + nc.connected);
	    	ExternalInterface.call('console.log', 'NC Protocol: ' + nc.protocol);
	    	ExternalInterface.call('console.log', 'NC URI: ' + nc.uri);
	    	ns = new EpooqNetStream(nc);
	    	//ns.bufferTime = 0;
	    	ns.addEventListener(NetStatusEvent.NET_STATUS, onNetStreamStatus);
			ns.publish(filename, 'record');
	        ns.attachAudio(mic);	        
        }
    }
    
	private function onNetStreamStatus(evt:NetStatusEvent) {

		trace(evt.info.code);
		ExternalInterface.call('console.log', 'Net status ' + evt.info.code);
		switch(evt.info.code) {
			case 'NetStream.Record.Start':
				ExternalInterface.call('onRecordStart');
			case 'NetStream.Record.Stop':
			case 'NetStream.Unpublish.Success':
				ExternalInterface.call('onRecordStop');
		}
	}
	
    public function stopSending() {
    	if(visible) {
	    	trace('Stop recording');
	    	ExternalInterface.call('console.log', 'Stop recording');
	    	mic.setLoopBack(false);
			ns.attachAudio(null);
			ns.close();
			ns.dispose();
									
			ExternalInterface.call('console.log', 'Recording stopped, NS: ' + ns);
		}
    }
    
    /*
     * Visibility
     */

    public function show() {
    trace('MicrophoneView Show');
    try {
    	if(!visible) {
	    	initAudio();
	    	
	    	visible = true;
	    	ExternalInterface.call('onRecordReady');
    	}
    	}
    	catch(error : String){
    		ExternalInterface.call('console.log', error);
    		trace(error);
    	}
    }
    
    public function hide() {
    	if(visible) {
	    	
	    	visible = false;
	    	//audio = null;
    	}
    }
}