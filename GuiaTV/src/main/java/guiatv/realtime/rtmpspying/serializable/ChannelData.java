package guiatv.realtime.rtmpspying.serializable;

import guiatv.persistence.domain.MLChannel;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.hash.Hashing;


@XmlRootElement(name="channelData")
public class ChannelData {
	
	private String idChBusiness;
	private String hashIdChBusiness; // ESTE CAMPO NO EXISTE EN EL XML
	private String nameProg;
	private boolean trained;
	private String url;
	private int[] topLeft;
	private int[] botRight;
	private int cols;
	private int rows;
	private boolean active;
	private String batchDataUri;
	private int numSamplesToSwitchState;
	private boolean adminVisible;
	private boolean busy; // ESTE CAMPO NO EXISTE EN EL XML
	private MLChannel mlCh; // ESTE CAMPO NO EXISTE EN EL XML
	
	public ChannelData() {
		busy = false;
	}

	@XmlElement(name="chIdBusiness")
	public String getIdChBusiness() {
		return idChBusiness;
	}
	public void setIdChBusiness(String chIdBusiness) {
		this.idChBusiness = chIdBusiness;
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
	
	@XmlElement(name="nameProg")
	public String getNameProg() {
		return nameProg;
	}

	public void setNameProg(String nameProg) {
		this.nameProg = nameProg;
	}
	
	@XmlElement(name="numSamplesToSwitchState")
	public int getNumSamplesToSwitchState() {
		return numSamplesToSwitchState;
	}

	public void setNumSamplesToSwitchState(int numSamplesToSwitchState) {
		this.numSamplesToSwitchState = numSamplesToSwitchState;
	}
	
	@XmlElement(name="adminVisible")
	public boolean isAdminVisible() {
		return adminVisible;
	}

	public void setAdminVisible(boolean adminVisible) {
		this.adminVisible = adminVisible;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
//	public void computeHashIdChBusiness() {
//		hashIdChBusiness = Hashing.murmur3_32().hashString(idChBusiness, StandardCharsets.UTF_8).toString();
//	} 
	
	public void setHashIdChBusiness(String hashIdChBusiness) {
		this.hashIdChBusiness = hashIdChBusiness;
	}
	
	public String getHashIdChBusiness() {
		return hashIdChBusiness;
	}
	
	@XmlElement(name="trained")
	public boolean isTrained() {
		return trained;
	}

	public void setTrained(boolean trained) {
		this.trained = trained;
	}

	public MLChannel getMlChannel() {
		return mlCh;
	}

	public void setMlChannel(MLChannel mlCh) {
		this.mlCh = mlCh;
	}
	
	
	
}
