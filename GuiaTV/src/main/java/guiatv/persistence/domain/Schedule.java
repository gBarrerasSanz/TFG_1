package guiatv.persistence.domain;

import java.io.Serializable;
import java.sql.Timestamp;
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

@Entity(name = "schedule")
public class Schedule implements Serializable {

	private static final long serialVersionUID = -8835185421528324020L;
	
	@Id
    @Column(name = "idSched", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSched;
    
//	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="idCh")
	private Channel channel;
    
//	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="idProg")
	private Programme programme;
	
//	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start", nullable = false)
	private Timestamp start;
	
//	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end", nullable = false)
	private Timestamp end;

    
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

	
	
	
	public Timestamp getEnd() {
		return end;
	}

	public void setEnd(Timestamp end) {
		this.end = end;
	}

	public void setStart(Timestamp start) {
		this.start = start;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((channel == null) ? 0 : channel.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result
				+ ((programme == null) ? 0 : programme.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Schedule other = (Schedule) obj;
		if (channel == null) {
			if (other.channel != null)
				return false;
		} else if (!channel.equals(other.channel))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (programme == null) {
			if (other.programme != null)
				return false;
		} else if (!programme.equals(other.programme))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Schedule {"+
				"channel="+channel.getNomIdCh()+", "+
				"programme="+programme.getNomProg()+", "+
				"start="+start+", "+
				"end="+end+", "+
				"}";
	}
	
}
