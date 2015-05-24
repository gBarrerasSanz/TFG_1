package guiatv.persistence.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity(name = "learnedChannel")
public class LearnedChannel implements Serializable {

	private static final long serialVersionUID = -5158218062363768675L;
    
	@Id
    @Column(name = "idLearnedCh", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLearnedCh;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="idCh")
	private Channel channel;
    
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="idRtmpSource")
	private RtmpSource rtmpSource;
	
	@Column(name = "learned", nullable = false)
	private boolean learned;
	
    @Lob
    @Column(name = "templateImg", nullable = true)
    private byte[] templateImg;
    
    
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
    

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public RtmpSource getRtmpSource() {
		return rtmpSource;
	}

	public void setRtmpSource(RtmpSource rtmpSource) {
		this.rtmpSource = rtmpSource;
	}

	public boolean isLearned() {
		return learned;
	}

	public void setLearned(boolean learned) {
		this.learned = learned;
	}

	public byte[] getTemplateImg() {
		return templateImg;
	}

	public void setTemplateImg(byte[] templateImg) {
		this.templateImg = templateImg;
	}
    
	
}
