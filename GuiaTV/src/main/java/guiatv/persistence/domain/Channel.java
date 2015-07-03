package guiatv.persistence.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

//@Entity(name = "channel")

/*
 * El business-Id sobre el que se implementan los métodos de equals() y hashCode() 
 * debe ser único(@UniqueConstraint) y está compuesto de campos que deben ser inmutables:
 * business-Id: nameIdCh
 */
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"nameIdCh"})})
@Entity(name = "channel")
public class Channel extends ResourceSupport implements Serializable {
	private static final long serialVersionUID = 6291049572278425446L;
	
	@JsonCreator
	public Channel() {
	}
	@Id
    @Column(name = "idChPersistence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChPersistence;
    
	@JsonProperty
    @Column(name = "nameIdCh", nullable = false, length = 50)
    private String idChBusiness;
    
	@JsonProperty
    @Column(name = "nameCh", nullable = true, length = 50)
    private String nameCh;
    
	@JsonIgnore
    @OneToMany(mappedBy="channel", fetch=FetchType.LAZY)
    private List<Schedule> listSchedules;
    
    @OneToMany(mappedBy="channel", fetch=FetchType.LAZY)
    private List<Schedule> listRtmpSources;
    
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

	public void setSetSchedules(List<Schedule> listSchedules) {
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
