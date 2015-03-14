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
@Table(name="EVENTO")
public class Evento {	
	
	/**
	 * Representa un evento, que puede ser retransmisión de serie, película, etc, espacio publicitario.
	 * Para distinguir tipos: o incluir un campo que distinga el tipo o crear clases que hereden de esta
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="CHANNEL")
	private String channel;
	
	@Column(name="TITLE")
	private String title;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TIME_START")
	private Date start;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TIME_END")
	private Date end;

	public Evento() {
		super();
	}

	public Evento(String channel, String title, Date start, Date end) {
		super();
		this.channel = channel;
		this.title = title;
		// TODO: Tratar las fechas
		this.start = new Date();
		this.end = new Date();
	}
	
	public boolean checkInitValues() {
		return channel != null && title != null &&
				start != null && end != null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
		Evento other = (Evento) obj;
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
		else if (start.compareTo(other.start) != 0) {
			return false;
		}
		
		// End
		if (end == null) {
			if (other.end != null) {
				return false;
			}
		} 
		else if (end.compareTo(other.end) != 0) {
			return false;
		}
		return true;
	}

}