package guiatv.persistence.domain;

import guiatv.catalog.serializers.ScheduleChannelSerializer;
import guiatv.catalog.serializers.ScheduleProgrammeSerializer;
import guiatv.catalog.serializers.TimestampDateSerializer;
import guiatv.persistence.domain.Schedule.CustomSchedule;
import guiatv.schedule.publisher.SchedulePublisher;
import guiatv.schedule.publisher.SchedulePublisher.PublisherScheduleView;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

import net.minidev.json.JSONObject;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.ResourcesLinksVisible;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/*
 * El business-Id sobre el que se implementan los métodos de equals() y hashCode() 
 * debe ser único(@UniqueConstraint) y está compuesto de campos que deben ser inmutables:
 * business-Id: (channel, programme, start, end)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
//@Entity(name = "rtschedule")
public class RtSchedule implements Serializable {
	
	/**
	 * TODO: Por el momento está desactivada la persistencia de RtSchedule.
	 */
	private static final long serialVersionUID = 6261936189827031704L;

	public static enum EventType {
    	BREAK_START, BREAK_END, UNKNOWN 
    }
	
	public static enum InstantState {
		ON_PROGRAMME, ON_ADVERTS
	}
	
//	@JsonView({CustomSchedule.class, SchedulePublisher.PublisherScheduleView.class})
//	@Id
//    @Column(name = "idRtSched", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    protected Long idRtSched;
	
	@JsonView({SchedulePublisher.PublisherRtScheduleView.class})
//	@ManyToOne(targetEntity=Channel.class, fetch=FetchType.LAZY)
//	@JoinColumn(name="channel_fk", referencedColumnName="idChPersistence")
	private Channel channel;
	
//	@Column(name = "instant", nullable = false)
	@JsonView({SchedulePublisher.PublisherRtScheduleView.class})
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss a z")
	private Timestamp instant;
	
//	@Column(name = "type", nullable = false)
	@JsonView({SchedulePublisher.PublisherRtScheduleView.class})
	private EventType type;
	
	@JsonView({SchedulePublisher.PublisherRtScheduleView.class})
	private InstantState state;
	
    
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
	public RtSchedule() {
	}

	public RtSchedule(Channel channel, Timestamp instant) {
    	this.channel = channel;
    	this.instant = instant;
    	this.type = EventType.UNKNOWN;
    }

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Timestamp getInstant() {
		return instant;
	}

	public void setInstant(Timestamp instant) {
		this.instant = instant;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public InstantState getState() {
		return state;
	}

	public void setState(InstantState state) {
		this.state = state;
	}
	
}
