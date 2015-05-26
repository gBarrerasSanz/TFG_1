package guiatv.persistence.domain;

import java.io.Serializable;
import java.sql.Date;
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

/*
 * El business-Id sobre el que se implementan los m�todos de equals() y hashCode() 
 * debe ser �nico(@UniqueConstraint) y est� compuesto de campos que deben ser inmutables:
 * business-Id: nomProg
 */
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"nomProg"})})
@Entity(name = "programme")
public class Programme implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7540103864705903738L;

	@Id
    @Column(name = "idProg", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProg;
    
    @Column(name = "nomProg", nullable = true, length = 50)
    private String nomProg;
    
    @OneToMany(mappedBy="programme", fetch=FetchType.LAZY)
    private Set<Schedule> setSchedules;
    

    public Set<Schedule> getSetSchedules() {
		return setSchedules;
	}

	public void setSetSchedules(Set<Schedule> setSchedules) {
		this.setSchedules = setSchedules;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomProg == null) ? 0 : nomProg.hashCode());
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
		Programme other = (Programme) obj;
		if (nomProg == null) {
			if (other.nomProg != null)
				return false;
		} else if (!nomProg.equals(other.nomProg))
			return false;
		return true;
	}

	
}
