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

@Entity(name = "SCHEDULE")
public class Schedule implements Serializable {

	private static final long serialVersionUID = -8835185421528324020L;
	
	@Id
    @Column(name = "IDSCHED", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSched;
    
//	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="CHID")
	private Channel channel;
    
//	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="PROGID")
	private Programme programme;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START", nullable = false)
	private Date start;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END", nullable = false)
	private Date end;

    
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
    
	public Long getIdSched() {
		return idSched;
	}

	public void setIdSched(Long idSched) {
		this.idSched = idSched;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Programme getProgramme() {
		return programme;
	}

	public void setProgramme(Programme programme) {
		this.programme = programme;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	@Override
	public String toString() {
		return "Schedule "+idSched+" {"+
				"channel="+channel.getNomIdCh()+", "+
				"programme="+programme.getNomProg()+", "+
				"start="+start+", "+
				"end="+end+", "+
				"}";
	}
	
}
