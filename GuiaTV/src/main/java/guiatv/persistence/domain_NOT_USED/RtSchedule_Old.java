//package guiatv.persistence.domain_NOT_USED;
//
//import guiatv.persistence.domain.Channel;
//
//import java.io.Serializable;
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.persistence.UniqueConstraint;
//
//import org.hibernate.annotations.Cascade;
//import org.hibernate.annotations.CascadeType;
//
//// TODO: Hacer el Entidad Relacion y hacer los @OneToMany y @ManyToOne y luego ya las restricciones
///*
// * El business-Id sobre el que se implementan los métodos de equals() y hashCode() 
// * debe ser único(@UniqueConstraint) y está compuesto de campos que deben ser inmutables:
// * business-Id: (channel, start)
// */
////@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"channel", "start", "end"})})
//
//public class RtSchedule_Old implements Serializable {	
//    /**
//	 * 
//	 */
//	private static final long serialVersionUID = 2487267960612253453L;
//
//	public static enum EventType {
//    	BREAK_START, BREAK_END, UNKNOWN 
//    }
//	
//	@Id
//    @Column(name = "idEv", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long idEv;
//	
//	
//	@OneToOne(fetch=FetchType.EAGER)
//	@JoinColumn(name="idCh")
//    @Cascade(value=CascadeType.ALL)
//	private Channel channel;
//    
////	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "start", nullable = false)
//	private Timestamp start;
//	
//	@Column(name = "type", nullable = false)
//	private EventType type;
//	
//	@Column(name = "expectedLength", nullable = true)
//	private Integer expectedLegth; // Duración esperada en segundos
//	
////	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "end", nullable = true)
//	private Timestamp end;
//
//	public Channel getChannel() {
//		return channel;
//	}
//
//	public void setChannel(Channel channel) {
//		this.channel = channel;
//	}
//
//	public EventType getType() {
//		return type;
//	}
//
//	public void setType(EventType type) {
//		this.type = type;
//	}
//
//	public Timestamp getStart() {
//		return start;
//	}
//
//	public void setStart(Timestamp start) {
//		this.start = start;
//	}
//
//	public Timestamp getEnd() {
//		return end;
//	}
//
//	public void setEnd(Timestamp end) {
//		this.end = end;
//	}
//
//	public Integer getExpectedLegth() {
//		return expectedLegth;
//	}
//
//	public void setExpectedLegth(Integer expectedLegth) {
//		this.expectedLegth = expectedLegth;
//	}
//
//
//	
//}