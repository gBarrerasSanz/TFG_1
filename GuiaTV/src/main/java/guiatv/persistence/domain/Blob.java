package guiatv.persistence.domain;

import guiatv.computervision.CvUtils;
import guiatv.realtime.rtmpspying.serializable.ChannelData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

@Entity(name = "roiblob")
public class Blob {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Id
    @Column(name = "idRoiBlobPersistence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRoiBlobPersistence;
	
	@ManyToOne(targetEntity=MLChannel.class, fetch=FetchType.LAZY)
	@JoinColumn(name="mlchannel_fk", referencedColumnName="idMlChPersistence")
	private MLChannel mlChannel;
	
	@Lob
	@Column(name = "blob", nullable = false)
	private byte[] blob;
	
	@Column(name = "blobCols", nullable = false)
	private int blobCols;

	@Column(name = "blobRows", nullable = false)
	private int blobRows;
	
	public Blob() {
	}
	
	public Blob(byte[] img, MLChannel mlChannel) throws Exception {
		// Extraer ROI
		Mat imgMat = CvUtils.getGrayMatFromByteArray(img, mlChannel.getImgCols(), mlChannel.getImgRows());
		Mat roiMat = CvUtils.getRoiFromMat(imgMat, mlChannel.getTopLeft(), mlChannel.getBotRight());
		Highgui.imwrite("aragon.jpeg", roiMat);
		this.blobCols = roiMat.cols();
		this.blobRows = roiMat.rows();
		this.blob = CvUtils.getByteArrayFromMat(roiMat);
		this.mlChannel = mlChannel;
	}
	
	public Blob(Mat grayImgMat, MLChannel mlChannel) {
		// Extraer ROI
		Mat roiMat = CvUtils.getRoiFromMat(grayImgMat, mlChannel.getTopLeft(), mlChannel.getBotRight());
		this.blobCols = roiMat.cols();
		this.blobRows = roiMat.rows();
		this.blob = CvUtils.getByteArrayFromMat(roiMat);
		this.mlChannel = mlChannel;
	}
	
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
	
	public MLChannel getMlChannel() {
		return mlChannel;
	}

	public void setMlChannel(MLChannel mlChannel) {
		this.mlChannel = mlChannel;
	}

	public byte[] getBlob() {
		return blob;
	}

	public void setBlob(byte[] blob) {
		this.blob = blob;
	}

	public Long getIdRoiBlobPersistence() {
		return idRoiBlobPersistence;
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
	
	

	
	
}
