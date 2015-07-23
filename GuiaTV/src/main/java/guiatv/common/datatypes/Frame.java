package guiatv.common.datatypes;

import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MLChannel;

import java.util.Date;

public class Frame {
	
	private byte[] frameImg;
	
	private MLChannel mlCh;
	
	private Date takenDate;
	
	public Frame() {
	}
	
	public Frame(byte[] frameImg, MLChannel mlCh, Date takenDate) {
		this.frameImg = frameImg;
		this.mlCh = mlCh;
		this.takenDate = takenDate;
	}

	public byte[] getFrameImg() {
		return frameImg;
	}

	public void setFrameImg(byte[] frameImg) {
		this.frameImg = frameImg;
	}

	public Date getTakenDate() {
		return takenDate;
	}

	public void setTakenDate(Date takenDate) {
		this.takenDate = takenDate;
	}
	
	
}
