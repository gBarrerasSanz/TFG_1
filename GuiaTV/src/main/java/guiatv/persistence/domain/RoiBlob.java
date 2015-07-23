package guiatv.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity(name = "roiblob")
public class RoiBlob {

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
	
	public RoiBlob() {
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


	
	
}
