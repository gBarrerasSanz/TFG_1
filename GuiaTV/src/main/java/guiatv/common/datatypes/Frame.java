package guiatv.common.datatypes;

import java.util.Date;

public class Frame {
	
	private byte[] frameImg;
	
	private Long idCh;
	
	private String rtmpUrl;
	
	private Date takenDate;
	
	public Frame() {
	}

	public byte[] getFrameImg() {
		return frameImg;
	}

	public void setFrameImg(byte[] frameImg) {
		this.frameImg = frameImg;
	}

	public Long getIdCh() {
		return idCh;
	}

	public void setIdCh(Long idCh) {
		this.idCh = idCh;
	}

	public String getRtmpUrl() {
		return rtmpUrl;
	}

	public void setRtmpUrl(String rtmpUrl) {
		this.rtmpUrl = rtmpUrl;
	}

	public Date getTakenDate() {
		return takenDate;
	}

	public void setTakenDate(Date takenDate) {
		this.takenDate = takenDate;
	}
	
	
}
