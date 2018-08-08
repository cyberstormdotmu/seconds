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

class PlaybackView {
    
    private var ns:EpooqNetStream;
    private var video:Video;
    
    private var parent:MovieClip;
    
    private var control : ControlInterface;
    
	private var visible : Bool = false;
    
    public function new( parent : MovieClip, control : ControlInterface) {
        
        this.parent = parent;
        this.control = control;
        
		video = new Video(parent.stage.stageWidth, parent.stage.stageHeight);
    }
    
    public function startPlayback( nc: NetConnection, filename:String ) {
    	trace("Start playback");
    	
    	ns = new EpooqNetStream(nc);
    	ns.bufferTime = 0;
    	ns.addEventListener(NetStatusEvent.NET_STATUS, onNetStreamStatus);
    	
        video.attachNetStream(ns);
        
        ns.play(filename);
    }
    
	private function onNetStreamStatus(event:NetStatusEvent) {
		trace(event.info.code);
		
		switch(event.info.code) {
			case 'NetStream.Play.Start':
				ExternalInterface.call("onPlayStart");
			case 'NetStream.Play.Stop':
				stopPlayback();
		}
	}
	
    public function stopPlayback() {
    	trace("Stop playback");
    	if(ns != null) {
			ns.close();
			ExternalInterface.call("onPlayStop");
		}
    }
    
    /*
     * Visibility
     */
    
    public function show() {
    	if(!visible) {
    		parent.addChild(video);
    		visible = true;
    	}
    }
    
    public function hide() {
    	if(visible) {
	    	parent.removeChild(video);
	    	visible = false;
    	}
    }
}