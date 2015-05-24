package guiatv.persistence.domain;

import guiatv.persistence.domain.RtEvent.EventType;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="RtEvent")
public class RtEvent implements Serializable {	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2487267960612253453L;

	public static enum EventType {
    	BREAK_START, BREAK_END, UNKNOWN 
    }
	
    @Id
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="idCh")
	private Channel channel;
    
    @Id
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start", nullable = false)
	private Date start;
	
	@Column(name = "type", nullable = false)
	private EventType type;
	
	@Column(name = "expectedLength", nullable = true)
	private Integer expectedLegth; // Duración esperada en segundos
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end", nullable = true)
	private Date end;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Integer getExpectedLegth() {
		return expectedLegth;
	}

	public void setExpectedLegth(Integer expectedLegth) {
		this.expectedLegth = expectedLegth;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
}