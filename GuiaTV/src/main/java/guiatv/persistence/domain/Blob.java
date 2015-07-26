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

import org.opencv.core.Mat;

@Entity(name = "roiblob")
public class Blob {

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
		Mat imgMat = CvUtils.getMatFromByteArray(img, mlChannel.getImgCols(), mlChannel.getImgRows());
		Mat roiMat = CvUtils.getRoiFromMat(imgMat, mlChannel.getTopLeft(), mlChannel.getBotRight());
		this.blob = CvUtils.getByteArrayFromMat(roiMat);
		this.blobCols = mlChannel.getBotRight()[0] - mlChannel.getTopLeft()[0];
		this.blobRows = mlChannel.getBotRight()[1] - mlChannel.getTopLeft()[1];
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
