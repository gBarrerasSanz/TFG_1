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

@Entity(name = "CHANNEL")
public class Channel implements Serializable {
	private static final long serialVersionUID = 6291049572278425446L;

	@Id
    @Column(name = "IDCH", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCh;
    
    @Column(name = "NOMIDCH", nullable = true, length = 50)
    private String nomIdCh;
    
    @Column(name = "NOMCH", nullable = true, length = 50)
    private String nomCh;
    
    @Column(name = "COUNTRY", nullable = true, length = 50)
    private String country;
    
    // Consultar aquí: http://www.javacodegeeks.com/2012/05/load-or-save-image-using-hibernate.html
    // Además lo hace con Hibernate
    @Lob
    @Column(name = "IMGSMB", nullable = true)
    private byte[] imgSmb;
    
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

	public byte[] getImgSmb() {
		return imgSmb;
	}

	public void setImgSmb(byte[] imgSmb) {
		this.imgSmb = imgSmb;
	}


}
