package guiatv.persistence.domain;

import guiatv.computervision.CvUtils;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

@Entity(name = "blob")
public class Blob {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Id
    @Column(name = "idBlobPersistence", nullable = false)
    @TableGenerator(
    	    name="blobGen",
    	    table="blob_sequence_table",
    	    pkColumnValue="idBlobPersistence",
    	    allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator="blobGen")
    private Long idBlobPersistence;
	
	@ManyToOne(targetEntity=Channel.class, fetch=FetchType.LAZY)
	@JoinColumn(name="channel_fk", referencedColumnName="idChPersistence")
	private Channel channel;
	
	@Transient
	private MyCh myCh;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "blob", nullable = false)
	private byte[] blob;
	
	@Column(name = "blobCols", nullable = false)
	private int blobCols;

	@Column(name = "blobRows", nullable = false)
	private int blobRows;
	
	public Blob() {
	}
	
	public Blob(byte[] img, MyCh myCh) throws Exception {
		// Extraer ROI
		Mat imgMat = CvUtils.getGrayMatFromByteArray(img, 
				myCh.getStreamSrc().getCols(), myCh.getStreamSrc().getRows());
		Mat roiMat = CvUtils.getRoiFromMat(imgMat, myCh.getStreamSrc().getTopLeft(), 
				myCh.getStreamSrc().getBotRight());
		Highgui.imwrite("aragon.jpeg", roiMat);
		this.blobCols = roiMat.cols();
		this.blobRows = roiMat.rows();
		this.blob = CvUtils.getByteArrayFromMat(roiMat);
		this.channel = myCh.getChannel();
		this.myCh = myCh;
	}
	
	public Blob(Mat grayImgMat, MyCh myCh) {
		// Extraer ROI
		Mat roiMat = CvUtils.getRoiFromMat(grayImgMat, 
				myCh.getStreamSrc().getTopLeft(), myCh.getStreamSrc().getBotRight());
		this.blobCols = roiMat.cols();
		this.blobRows = roiMat.rows();
		this.blob = CvUtils.getByteArrayFromMat(roiMat);
		this.channel = myCh.getChannel();
		this.myCh = myCh;
	}
	
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/

	public byte[] getBlob() {
		return blob;
	}

	public void setBlob(byte[] blob) {
		this.blob = blob;
	}

	public Long getIdBlobPersistence() {
		return idBlobPersistence;
	}

	public int getBlobCols() {
		return blobCols;
	}

	public void setBlobCols(int blobCols) {
		this.blobCols = blobCols;
	}

	public int getBlobRows() {
		return blobRows;
	}

	public void setBlobRows(int blobRows) {
		this.blobRows = blobRows;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public MyCh getMyCh() throws IllegalAccessException {
		if (myCh != null) {
			return myCh;
		}
		else {
			throw new IllegalAccessException("Trying to access to a persisted-not-in-memory blob");
		}
	}

	public void setMyCh(MyCh myCh) {
		this.myCh = myCh;
	}
	
	
}
