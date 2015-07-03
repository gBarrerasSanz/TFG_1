package guiatv.catalog.datatypes;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Catalog extends ResourceSupport {
	
	private String country;
	
	public Catalog(){
	}
	
	@JsonCreator
    public Catalog(@JsonProperty("country") String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }
}
