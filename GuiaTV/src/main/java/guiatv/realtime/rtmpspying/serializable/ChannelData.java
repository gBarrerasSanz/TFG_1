package guiatv.realtime.rtmpspying.serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="channelData")
public class ChannelData {
	
	private String chIdBusiness;
	private String url;
	private int[] topLeft;
	private int[] botRight;
	private int cols;
	private int rows;
	private boolean active;
	private String batchDataUri;
	
	public ChannelData() {
	}

	@XmlElement(name="chIdBusiness")
	public String getChIdBusiness() {
		return chIdBusiness;
	}
	public void setChIdBusiness(String chIdBusiness) {
		this.chIdBusiness = chIdBusiness;
	}
	@XmlElement(name="url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@XmlElements({
		 @XmlElement(name="cols"),
		 @XmlElement(name="rows")
	})
	@XmlElementWrapper
	public int[] getTopLeft() {
		return topLeft;
	}
	
	public void setTopLeft(int[] topLeft) {
		this.topLeft = topLeft;
	}
	
	@XmlElements({
		 @XmlElement(name="cols"),
		 @XmlElement(name="rows")
	})
	@XmlElementWrapper
	public int[] getBotRight() {
		return botRight;
	}

	public void setBotRight(int[] botRight) {
		this.botRight = botRight;
	}
	
	@XmlElement(name="cols")
	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}
	
	@XmlElement(name="rows")
	public int getRows() {
		return rows;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	@XmlElement(name="active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	@XmlElement(name="batchDataUri")
	public String getBatchDataUri() {
		return batchDataUri;
	}

	public void setBatchDataUri(String batchDataUri) {
		this.batchDataUri = batchDataUri;
	}
	
	
	
	
}
