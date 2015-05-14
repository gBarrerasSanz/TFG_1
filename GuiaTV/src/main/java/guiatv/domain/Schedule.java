package guiatv.domain;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idSched;
    
//	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OneToOne
	@JoinColumn(table="CHANNEL", name="IDCH")
    private String idCh;
    
//	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OneToOne
	@JoinColumn(table="PROGRAMME", name="IDPROG")
    private Long idProg;
	
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

	public String getIdCh() {
		return idCh;
	}

	public void setIdCh(String idCh) {
		this.idCh = idCh;
	}

	public Long getIdProg() {
		return idProg;
	}

	public void setIdProg(Long idProg) {
		this.idProg = idProg;
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
	
//	@Override
//	public String toString() {
//		return "Event "+id+" {"+
//				"channel="+channel+", "+
//				"title="+title+", "+
//				"start="+start+", "+
//				"end="+end+", "+
//				"}";
//	}
	
}
