package guiatv.persistence.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;
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

//@Entity(name = "channel")

/*
 * El business-Id sobre el que se implementan los métodos de equals() y hashCode() 
 * debe ser único(@UniqueConstraint) y está compuesto de campos que deben ser inmutables:
 * business-Id: nameIdCh
 */
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"nameIdCh"})})
@Entity(name = "channel")
public class Channel implements Serializable {
	private static final long serialVersionUID = 6291049572278425446L;

	@Id
    @Column(name = "idCh", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCh;
    
    @Column(name = "nameIdCh", nullable = false, length = 50)
    private String nameIdCh;
    
    @Column(name = "nomCh", nullable = true, length = 50)
    private String nameCh;
    
    @OneToMany(mappedBy="channel", fetch=FetchType.LAZY)
    private Set<Schedule> setSchedules;
    
    @OneToMany(mappedBy="channel", fetch=FetchType.LAZY)
    private Set<Schedule> setRtmpSources;
    
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
    public Channel() {
    }
    
    public Channel(String nameIdCh) {
    	this.nameIdCh = nameIdCh;
    }
    
	public Long getIdCh() {
		return idCh;
	}

	public Set<Schedule> getSetSchedules() {
		return setSchedules;
	}

	public void setSetSchedules(Set<Schedule> setSchedules) {
		this.setSchedules = setSchedules;
	}

	public void setIdCh(Long idCh) {
		this.idCh = idCh;
	}

	public String getNameIdCh() {
		return nameIdCh;
	}

	public void setNameIdCh(String nameIdCh) {
		this.nameIdCh = nameIdCh;
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
		result = prime * result + ((nameIdCh == null) ? 0 : nameIdCh.hashCode());
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
		if (nameIdCh == null) {
			if (other.nameIdCh != null)
				return false;
		} else if (!nameIdCh.equals(other.nameIdCh))
			return false;
		return true;
	}

	public String toString() {
		return "Channel {"+
				"nomIdCh="+this.getNameIdCh()+", "+
				"country="+this.getCountry()+"}";
	}
	


}
