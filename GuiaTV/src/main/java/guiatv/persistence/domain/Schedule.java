package guiatv.persistence.domain;

import guiatv.catalog.serializers.ScheduleChannelSerializer;
import guiatv.catalog.serializers.ScheduleProgrammeSerializer;
import guiatv.catalog.serializers.TimestampDateSerializer;
import guiatv.common.CommonUtility;
import guiatv.schedule.publisher.SchedulePublisher;
import guiatv.schedule.publisher.SchedulePublisher.PublisherScheduleView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
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
//@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"channel", "programme", "start", "end"})})
@Entity(name = "schedule")
public class Schedule extends ResourceSupport implements Serializable {
	
	public interface CustomSchedule extends ResourcesLinksVisible {}
	
	private static final long serialVersionUID = -8835185421528324020L;
	
	
	@JsonView({CustomSchedule.class, SchedulePublisher.PublisherScheduleView.class})
	@Id
    @TableGenerator(
	    name="scheduleGen",
	    table="schedule_sequence_table",
	    pkColumnValue="idSched",
	    allocationSize=1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="scheduleGen")
    @Column(name = "idSched", nullable = false)
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long idSched;
    
	
	@JsonView({CustomSchedule.class, SchedulePublisher.PublisherScheduleView.class})
	@JsonSerialize(using=ScheduleChannelSerializer.class)
//	@ManyToOne(targetEntity=Channel.class, cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@ManyToOne(targetEntity=Channel.class, fetch=FetchType.LAZY)
	@JoinColumn(name="channel", referencedColumnName="idChPersistence")
	protected Channel channel;
    
	@JsonView({CustomSchedule.class, SchedulePublisher.PublisherScheduleView.class})
	@JsonSerialize(using=ScheduleProgrammeSerializer.class)
//	@ManyToOne(targetEntity=Programme.class, cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@ManyToOne(targetEntity=Programme.class, fetch=FetchType.LAZY)
	@JoinColumn(name="programme", referencedColumnName="idProgPersistence")
	protected Programme programme;
	
	
//	@JsonSerialize(using=TimestampDateSerializer.class)
//	@JsonProperty // Creo que no hace falta con @JsonView
	@JsonView({CustomSchedule.class, SchedulePublisher.PublisherScheduleView.class})
	@Column(name = "start", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=CommonUtility.zonedDateFormat)
	protected Date start;
	
	
//	@JsonSerialize(using=TimestampDateSerializer.class)
	@JsonProperty // Creo que no hace falta con @JsonView
	@JsonView({CustomSchedule.class, SchedulePublisher.PublisherScheduleView.class})
	@Column(name = "end", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=CommonUtility.zonedDateFormat)
	protected Date end;
	
	@Column(name = "published", nullable = false)
	protected boolean published;

    
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
	public Schedule() {
	}

	public Schedule(Channel channel, Programme programme, Date start, Date end) {
    	this.channel = channel;
    	this.programme = programme;
    	this.start = start;
    	this.end = end;
    	this.published = false;
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

	public Date getStart() {
		
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setStart(Date start) {
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
	
	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
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
	
	
	public String toStringPublisher() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
//		String chString = mapper.writerWithView(PublisherView.class).writeValueAsString(channel);
//		String progString = mapper.writerWithView(PublisherView.class).writeValueAsString(programme);
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a CEST", SPAIN_LOCALE);
		
		JSONObject schedJson = new JSONObject();
		schedJson.put("idSched", idSched);
		// Channel
		JSONObject chJson = new JSONObject();
		chJson.put("idChBusiness", channel.getIdChBusiness());
		chJson.put("hashIdChBusiness", channel.getHashIdChBusiness());
		chJson.put("nameCh", channel.getNameCh());
		schedJson.put("channel", chJson);
		// Programme
		JSONObject progJson = new JSONObject();
		progJson.put("nameProg", programme.getNameProg());
		progJson.put("hashNameProg", programme.getHashNameProg());
		schedJson.put("programme", progJson);
		
		
		schedJson.put("start", CommonUtility.dateToPlainStr(start));
		schedJson.put("end", CommonUtility.dateToPlainStr(end));
		String schedString = schedJson.toJSONString();
		return schedString;
	}
	
	public boolean isValid(){
		return channel != null &&
				programme != null &&
				start != null &&
				end != null;
	}
	
	public void setIdSched(long idSched) {
		this.idSched = idSched;
	}
	
}
