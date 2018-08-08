package;

import flash.external.ExternalInterface;
import flash.display.MovieClip;
import flash.display.Bitmap;
import flash.media.Camera;
import flash.media.Video;
import flash.media.Microphone;
import flash.display.BitmapData;
import flash.events.NetStatusEvent;
import flash.net.NetConnection;

import flash.Lib;
import haxe.Timer;

class CameraView {
    
    private var ns:EpooqNetStream;
    
   	private var cam:Camera;
    private var mic:Microphone;
    
    private var video:Video;
    
    private var parent : MovieClip;
    
    private var control : ControlInterface;
    
    private var visible : Bool = false;
    
    public function new( parent : MovieClip, control : ControlInterface) {
        
        this.parent = parent;
        this.control = control;
    }
    
    private function initVideo() {
        this.cam = flash.media.Camera.getCamera();
        
        if (this.cam != null) {
	    	this.mic = Microphone.getMicrophone();
	    	this.mic.rate = 22;
	    	
	    	var cameraWidth = 320;
	    	var cameraHeight = 240;
	    	
			this.cam.setMode(cameraWidth, cameraHeight, 24);
			cam.setQuality(0, 95);

			this.video = new Video(parent.stage.stageWidth, parent.stage.stageHeight);
			this.video.attachCamera(cam);
		} 
		else {
			trace("No Camera") ;
		}
    }
    
    
    public function startSending( nc: NetConnection, filename:String ) {
    	if(visible) {
	    	trace("Start recording");
	    	
	    	ns = new EpooqNetStream(nc);
	    	ns.bufferTime = 0;
	    	ns.addEventListener(NetStatusEvent.NET_STATUS, onNetStreamStatus);
			ns.publish(filename, "record");
	        ns.attachAudio(mic);
	        ns.attachCamera(cam);
        }
    }
    
	private function onNetStreamStatus(evt:NetStatusEvent) {

		trace(evt.info.code);
		
		switch(evt.info.code) {
			case 'NetStream.Record.Start':
				ExternalInterface.call("onRecordStart");
			case 'NetStream.Record.Stop':
			case 'NetStream.Unpublish.Success':
				ExternalInterface.call("onRecordStop");
		}
	}
	
    public function stopSending() {
    	if(visible) {
	    	trace("Stop recording");
	    	ns.attachCamera(null);
			ns.attachAudio(null);
			ns.close();
		}
    }
    
    /*
     * Visibility
     */

    public function show() {
    	if(!visible) {
	    	initVideo();
	    	parent.addChild(video);
	    	visible = true;
	    	ExternalInterface.call("onRecordReady");
    	}
    }
    
    public function hide() {
    	if(visible) {
	    	parent.removeChild(video);
	    	visible = false;
	    	video = null;
    	}
    }
}