package guiatv.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Entity(name = "streamsource")
public class StreamSource {
	
	@Id
    @Column(name = "idStreamSourcePersistence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStreamSourcePersistence;
	
	@OneToOne(targetEntity=MLChannel.class, fetch=FetchType.LAZY)
	@JoinColumn(name="mlchannel_fk", referencedColumnName="idMlChPersistence")
	private MLChannel mlChannel;
	
	@Column(name="url", nullable=false)
	private String url;
	
	public StreamSource(){
	}
	
	public StreamSource(String url) {
		this.url = url;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getIdStreamSourcePersistence() {
		return idStreamSourcePersistence;
	}
	
	
}
