package guiatv.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Version;

@Entity(name = "PROGRAMME")
public class Programme implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7540103864705903738L;

	@Id
    @Column(name = "IDPROG", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProg;
    
    @Column(name = "NOMPROG", nullable = true, length = 50)
    private String nomProg;


    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
    
	public Long getIdProg() {
		return idProg;
	}

	public void setIdProg(Long idProg) {
		this.idProg = idProg;
	}

	public String getNomProg() {
		return nomProg;
	}

	public void setNomProg(String nomProg) {
		this.nomProg = nomProg;
	}

}
