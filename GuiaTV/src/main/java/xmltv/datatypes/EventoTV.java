package xmltv.datatypes;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="EVENTOTV")
public class EventoTV {	
	
	/**
	 * Representa un evento, que puede ser retransmisión de serie, película, etc, espacio publicitario.
	 * Para distinguir tipos: o incluir un campo que distinga el tipo o crear clases que hereden de esta
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name="CHANNEL")
	private String channel;
	
	@Column(name="TITLE")
	private String title;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START")
	private Date start;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END")
	private Date end;

	public EventoTV() {
		super();
		throw new IllegalArgumentException("No se puede construir un evento sin informacion");
	}

	public EventoTV(String channel, String title, Date start, Date end) {
		super();
		this.channel = channel;
		this.title = title;
		// TODO: Tratar las fechas
		this.start = new Date();
		this.start = new Date();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		if (channel != null) {
			result += channel.hashCode();
		}
		if (title != null) {
			result += title.hashCode();
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EventoTV other = (EventoTV) obj;
		// Channel
		if (channel == null) {
			if (other.channel != null) {
				return false;
			}
		} 
		else if (!channel.equals(other.channel)) {
			return false;
		}
		// Title
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} 
		else if (!title.equals(other.title)) {
			return false;
		}
		
		// Start
		if (start == null) {
			if (other.start != null) {
				return false;
			}
		} 
		else if (!start.equals(other.start)) {
			return false;
		}
		
		// End
		if (end == null) {
			if (other.end != null) {
				return false;
			}
		} 
		else if (!end.equals(other.end)) {
			return false;
		}
		return true;
	}

}