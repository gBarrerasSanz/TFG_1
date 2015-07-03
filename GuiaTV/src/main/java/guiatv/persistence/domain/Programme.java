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

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/*
 * El business-Id sobre el que se implementan los métodos de equals() y hashCode() 
 * debe ser único(@UniqueConstraint) y está compuesto de campos que deben ser inmutables:
 * business-Id: nameProg
 */
@JsonInclude(Include.NON_NULL)
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"nameProg"})})
@Entity(name = "programme")
public class Programme extends ResourceSupport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7540103864705903738L;
	
	@JsonIgnore
	@Id
    @Column(name = "idProgPersistence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProgPersistence;
    
    @Column(name = "nameProg", nullable = false, length = 70)
    private String nameProg;
    
    @JsonIgnore
    @OneToMany(mappedBy="programme", fetch=FetchType.LAZY)
    private Set<Schedule> setSchedules;
    
    
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
    public Programme() {
    }

    public Programme(String nameProg) {
    	this.nameProg = nameProg;
    }
	public Long getIdProgPersistence() {
		return idProgPersistence;
	}

	public void setIdProgPersistence(Long idProgPersistence) {
		this.idProgPersistence = idProgPersistence;
	}

	public String getNameProg() {
		return nameProg;
	}

	public void setNameProg(String nameProg) {
		this.nameProg = nameProg;
	}

	public Set<Schedule> getSetSchedules() {
		return setSchedules;
	}

	public void setSetSchedules(Set<Schedule> setSchedules) {
		this.setSchedules = setSchedules;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nameProg == null) ? 0 : nameProg.hashCode());
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
		if (nameProg == null) {
			if (other.nameProg != null)
				return false;
		} else if (!nameProg.equals(other.nameProg))
			return false;
		return true;
	}

	
}
