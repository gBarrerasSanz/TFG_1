package guiatv.persistence.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Version;

@Entity(name = "channel")
public class Channel implements Serializable {
	private static final long serialVersionUID = 6291049572278425446L;

	@Id
    @Column(name = "idCh", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCh;
    
    @Column(name = "nomIdCh", nullable = true, length = 50)
    private String nomIdCh;
    
    @Column(name = "nomCh", nullable = true, length = 50)
    private String nomCh;
    
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
    
	public Long getIdCh() {
		return idCh;
	}

	public void setIdCh(Long idCh) {
		this.idCh = idCh;
	}

	public String getNomIdCh() {
		return nomIdCh;
	}

	public void setNomIdCh(String nomIdCh) {
		this.nomIdCh = nomIdCh;
	}

	public String getNomCh() {
		return nomCh;
	}

	public void setNomCh(String nomCh) {
		this.nomCh = nomCh;
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
		result = prime * result + ((nomIdCh == null) ? 0 : nomIdCh.hashCode());
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
		if (nomIdCh == null) {
			if (other.nomIdCh != null)
				return false;
		} else if (!nomIdCh.equals(other.nomIdCh))
			return false;
		return true;
	}

	public String toString() {
		return "Channel {"+
				"nomIdCh="+this.getNomIdCh()+", "+
				"country="+this.getCountry()+"}";
	}
	


}
