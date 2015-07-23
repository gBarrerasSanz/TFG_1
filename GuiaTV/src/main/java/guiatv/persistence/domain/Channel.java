package guiatv.persistence.domain;


import guiatv.catalog.restcontroller.CatalogRestController;
import guiatv.catalog.serializers.ListProgFromSchedSerializer;
import guiatv.schedule.publisher.SchedulePublisher;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.ResourcesLinksVisible;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.hash.Hashing;

//@Entity(name = "channel")

/*
 * El business-Id sobre el que se implementan los métodos de equals() y hashCode() 
 * debe ser único(@UniqueConstraint) y está compuesto de campos que deben ser inmutables:
 * business-Id: nameIdCh
 */
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"idChBusiness"})})
@Entity(name = "channel")
public class Channel extends ResourceSupport implements Serializable {
	private static final long serialVersionUID = 6291049572278425446L;
	
	public interface MultipleChannels extends ResourcesLinksVisible {}
	public interface SingleChannel extends ResourcesLinksVisible {}
	
	@JsonCreator
	public Channel() {
		listSchedules = new ArrayList<Schedule>();
	}
	@Id
    @Column(name = "idChPersistence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChPersistence;
    
	@JsonView({SingleChannel.class, MultipleChannels.class, SchedulePublisher.PublisherView.class})
    @Column(name = "idChBusiness", nullable = false, length = 50)
    private String idChBusiness;
    
	@JsonView({SingleChannel.class, MultipleChannels.class, SchedulePublisher.PublisherView.class})
	@Column(name="hashIdChBusiness", nullable=false)
	private String hashIdChBusiness;
	
	@JsonView({SingleChannel.class, MultipleChannels.class, SchedulePublisher.PublisherView.class})
    @Column(name = "nameCh", nullable = true, length = 50)
    private String nameCh;
    
//	@JsonProperty(value="listProgrammes")
//	@JsonSerialize(using=ListProgFromSchedSerializer.class)
//	@JsonView(SingleChannel.class)
//	@OneToMany(targetEntity=Schedule.class, cascade=CascadeType.ALL, mappedBy="channel", fetch=FetchType.LAZY, orphanRemoval=true)
	@OneToMany(targetEntity=Schedule.class, mappedBy="channel", fetch=FetchType.LAZY, orphanRemoval=true)
	private List<Schedule> listSchedules;
    
	@Transient
	@JsonView(SingleChannel.class)
	private List<Programme> listProgrammes;
	
    @OneToMany(mappedBy="channel", fetch=FetchType.LAZY)
    private List<RtmpSource> listRtmpSources;
    
    // country no se usa de momento
    @Column(name = "country", nullable = true, length = 50)
    private String country;
    
    // Consultar aquí: http://www.javacodegeeks.com/2012/05/load-or-save-image-using-hibernate.html
    // Además lo hace con Hibernate
    @Lob
    @Column(name = "imgIcon", nullable = true)
    private byte[] imgIcon;
    
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
    
    
    public Channel(String nameIdCh) {
    	this.idChBusiness = nameIdCh;
    }
    
	public Long getIdChPersistence() {
		return idChPersistence;
	}

	public List<Schedule> getListSchedules() {
		return listSchedules;
	}

	public void setListSchedules(List<Schedule> listSchedules) {
		this.listSchedules = listSchedules;
	}
	
	public void setIdChPersistence(Long idChPersistence) {
		this.idChPersistence = idChPersistence;
	}

	public String getIdChBusiness() {
		return idChBusiness;
	}

	public void setIdChBusiness(String idChBusiness) {
		this.idChBusiness = idChBusiness;
	}

	public String getNameCh() {
		return nameCh;
	}

	public void setNameCh(String nameCh) {
		this.nameCh = nameCh;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public byte[] getImgIcon() {
		return imgIcon;
	}

	public void setImgIcon(byte[] imgIcon) {
		this.imgIcon = imgIcon;
	}
	
	
	public void computeHashIdChBusiness() {
		hashIdChBusiness = Hashing.murmur3_32().hashString(idChBusiness, StandardCharsets.UTF_8).toString();
	}
	
	public String getHashIdChBusiness() {
		return hashIdChBusiness;
	}

	public void addListSchedules(List<Schedule> lSched) {
		this.listSchedules.addAll(lSched);
	}
	
	public void addSchedule(Schedule sched) {
		this.listSchedules.add(sched);
	}
	
	public void setListProgrammes(List<Programme> lProg) {
		listProgrammes = lProg;
	}
	
	public List<Programme> getListProgrammes() {
		return listProgrammes;
	}
	
	public void computeListProgrammesFromListSchedules() {
		listProgrammes = new ArrayList<Programme>();
		HashMap<String, Integer> hmProg = new HashMap<String, Integer>();
		for (Schedule sched: listSchedules) {
			Programme prog = sched.getProgramme();
			if (hmProg.putIfAbsent(prog.getHashNameProg(), 1) == null) {
				listProgrammes.add(prog);
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idChBusiness == null) ? 0 : idChBusiness.hashCode());
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
		Channel other = (Channel) obj;
		if (idChBusiness == null) {
			if (other.idChBusiness != null)
				return false;
		} else if (!idChBusiness.equals(other.idChBusiness))
			return false;
		return true;
	}

	public String toString() {
		return "Channel {"+
				"nomIdCh="+this.getIdChBusiness()+", "+
				"country="+this.getCountry()+"}";
	}
	


}
