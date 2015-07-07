package guiatv.persistence.domain;

import guiatv.catalog.serializers.ScheduleChannelSerializer;
import guiatv.catalog.serializers.ScheduleProgrammeSerializer;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/*
 * El business-Id sobre el que se implementan los m�todos de equals() y hashCode() 
 * debe ser �nico(@UniqueConstraint) y est� compuesto de campos que deben ser inmutables:
 * business-Id: (channel, programme, start, end)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"channel_fk", "programme_fk", "start", "end"})})
@Entity(name = "schedule")
public class Schedule extends ResourceSupport implements Serializable {
	
	
	private static final long serialVersionUID = -8835185421528324020L;
	
	
	@Id
    @Column(name = "idSched", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSched;
    
	
	@JsonSerialize(using=ScheduleChannelSerializer.class)
	@ManyToOne(targetEntity=Channel.class, cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="channel_fk", referencedColumnName="idChPersistence")
	private Channel channel;
    
	@JsonSerialize(using=ScheduleProgrammeSerializer.class)
	@ManyToOne(targetEntity=Programme.class, cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="programme_fk", referencedColumnName="idProgPersistence")
	private Programme programme;
	
	@JsonProperty
	@Column(name = "start", nullable = false)
	private Timestamp start;
	
	@JsonProperty
	@Column(name = "end", nullable = false)
	private Timestamp end;

    
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
	public Schedule() {
	}

	public Schedule(Channel channel, Programme programme, Timestamp start, Timestamp end) {
    	this.channel = channel;
    	this.programme = programme;
    	this.start = start;
    	this.end = end;
    }
	
	
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

	public Timestamp getStart() {
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
				"channel="+channel.getIdChBusiness()+", "+
				"programme="+programme.getNameProg()+", "+
				"start="+start+", "+
				"end="+end+", "+
				"}";
	}
	
}
