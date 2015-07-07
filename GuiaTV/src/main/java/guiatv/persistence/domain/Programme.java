package guiatv.persistence.domain;



import guiatv.catalog.restcontroller.CatalogRestController;
import guiatv.catalog.serializers.ListSchedulesSerializer;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import jdk.nashorn.internal.objects.annotations.Getter;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.ResourcesLinksVisible;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/*
 * El business-Id sobre el que se implementan los métodos de equals() y hashCode() 
 * debe ser único(@UniqueConstraint) y está compuesto de campos que deben ser inmutables:
 * business-Id: nameProg
 */

//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(Include.NON_NULL)
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"nameProg"})})
@Entity(name = "programme")
public class Programme extends ResourceSupport implements Serializable {
	
	public interface MultipleProgrammes extends ResourcesLinksVisible {}
	public interface SingleProgramme extends ResourcesLinksVisible {}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7540103864705903738L;
	
	@JsonCreator
	public Programme() {
    	listSchedules = new ArrayList<Schedule>();
    }
	
	@Id
    @Column(name = "idProgPersistence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProgPersistence;
    
//	@JsonView({MultipleProgrammes.class, SingleProgramme.class})
	@JsonProperty
	@Column(name = "nameProg", nullable = false, length = 70)
    private String nameProg;
	
//	@JsonSerialize(using=ListSchedulesSerializer.class)
//	@JsonView({SingleProgramme.class})
	@JsonProperty
	@OneToMany(targetEntity=Schedule.class, cascade=CascadeType.ALL, mappedBy="programme", fetch=FetchType.LAZY)
	private List<Schedule> listSchedules;
    
    
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/

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
	
	public List<Schedule> getListSchedules() {
		return listSchedules;
	}

	public void setListSchedules(List<Schedule> listSchedules) {
		this.listSchedules = listSchedules;
	}
	
	
	public void addListSchedules(List<Schedule> lSched) {
		this.listSchedules.addAll(lSched);
	}
	
	public void addSchedule(Schedule sched) {
		this.listSchedules.add(sched);
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
