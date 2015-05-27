package guiatv.common.datatypes;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.RtmpSource;

import java.util.Date;

public class Frame {
	
	private byte[] frameImg;
	
	private Channel channel;
	
	private RtmpSource rtmp;
	
	private Date takenDate;
	
	public Frame() {
	}
	
	public Frame(byte[] frameImg, Channel ch, RtmpSource rtmp, Date takenDate) {
		this.frameImg = frameImg;
		this.channel = ch;
		this.rtmp = rtmp;
		this.rtmp = rtmp;
		this.takenDate = takenDate;
	}

	public byte[] getFrameImg() {
		return frameImg;
	}

	public void setFrameImg(byte[] frameImg) {
		this.frameImg = frameImg;
	}


	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public RtmpSource getRtmp() {
		return rtmp;
	}

	public void setRtmp(RtmpSource rtmp) {
		this.rtmp = rtmp;
	}

	public Date getTakenDate() {
		return takenDate;
	}

	public void setTakenDate(Date takenDate) {
		this.takenDate = takenDate;
	}
	
	
}
