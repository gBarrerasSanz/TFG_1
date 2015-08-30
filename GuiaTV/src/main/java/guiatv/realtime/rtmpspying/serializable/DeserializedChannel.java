package guiatv.realtime.rtmpspying.serializable;


import java.nio.charset.StandardCharsets;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.hash.Hashing;


@XmlRootElement(name="deserializedChannel")
public class DeserializedChannel {
	
	private boolean enabled;
	private String idChBusiness;
	private String nameChGiven;
	private String url;
	private int cols;
	private int rows;
	private int[] topLeft;
	private int[] botRight;
	private boolean active;
	private boolean doSpying;
	private String batchGoodSamplesUri;
	private String batchBadSamplesUri;
	private int samplesToFalse;
	private int samplesToTrue;
	
	
	public DeserializedChannel() {
	}

	@XmlElement(name="enabled")
	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@XmlElement(name="idChBusiness")
	public String getIdChBusiness() {
		return idChBusiness;
	}


	public void setIdChBusiness(String idChBusiness) {
		this.idChBusiness = idChBusiness;
	}

	@XmlElement(name="nameChGiven")
	public String getNameChGiven() {
		return nameChGiven;
	}


	public void setNameChGiven(String nameChGiven) {
		this.nameChGiven = nameChGiven;
	}

	@XmlElement(name="url")
	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
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

	@XmlElements({
		 @XmlElement(name="cols"),
		 @XmlElement(name="rows")
	})
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
	public int[] getBotRight() {
		return botRight;
	}


	public void setBotRight(int[] botRight) {
		this.botRight = botRight;
	}

	@XmlElement(name="active")
	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}

	@XmlElement(name="doSpying")
	public boolean isDoSpying() {
		return doSpying;
	}


	public void setDoSpying(boolean doSpying) {
		this.doSpying = doSpying;
	}

	@XmlElement(name="batchGoodSamplesUri")
	public String getBatchGoodSamplesUri() {
		return batchGoodSamplesUri;
	}


	public void setBatchGoodSamplesUri(String batchGoodSamplesUri) {
		this.batchGoodSamplesUri = batchGoodSamplesUri;
	}

	@XmlElement(name="batchBadSamplesUri")
	public String getBatchBadSamplesUri() {
		return batchBadSamplesUri;
	}


	public void setBatchBadSamplesUri(String batchBadSamplesUri) {
		this.batchBadSamplesUri = batchBadSamplesUri;
	}

	@XmlElement(name="samplesToFalse")
	public int getSamplesToFalse() {
		return samplesToFalse;
	}


	public void setSamplesToFalse(int samplesToFalse) {
		this.samplesToFalse = samplesToFalse;
	}

	@XmlElement(name="samplesToTrue")
	public int getSamplesToTrue() {
		return samplesToTrue;
	}


	public void setSamplesToTrue(int samplesToTrue) {
		this.samplesToTrue = samplesToTrue;
	}

	
	
	
	
}
