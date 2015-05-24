package guiatv.persistence.domain;

import java.io.Serializable;
import java.sql.Date;

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

	


}
