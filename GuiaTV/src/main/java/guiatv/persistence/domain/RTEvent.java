package guiatv.persistence.domain;

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
@Table(name="RTEVENT")
public class RTEvent {	
	
    public static enum EventType {
    	BREAK_START, BREAK_END, UNKNOWN 
    }
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="CHID")
	private Channel channel;
    
	@Column(name = "TYPE", nullable = false)
	private EventType type;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START", nullable = false)
	private Date start;
	
	@Column(name = "EXPECTED_LEGTH", nullable = true)
	private Integer expectedLegth; // Duración esperada en segundos
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END", nullable = true)
	private Date end;
	
	

}