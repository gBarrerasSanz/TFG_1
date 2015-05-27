package guiatv.realtime.rtmpspying;

import java.util.UUID;

import org.apache.log4j.Logger;

public class RtmpSpierWorker extends RtmpSpier {
	
	private static Logger logger = Logger.getLogger("debugLog");
	private UUID id;
	
	public RtmpSpierWorker(String rtmpSource) {
		super(rtmpSource);
		this.id = UUID.randomUUID();
	}
	
	@Override
	public void doRtmpSpying() {
		logger.info(this.toString()+"-> doRtmpSpying()");
		super.doRtmpSpying();
	}
	
	@Override
	public String toString() {
		return "RtmpSpier: id="+id.toString()+"; rtmpSource=\""+rtmpSource+"\"";
	}
}
