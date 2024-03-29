package guiatv.catalog.serializers;

import guiatv.catalog.restcontroller.CatalogRestController;
import guiatv.persistence.domain.Programme;

import java.io.IOException;




import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;

public class ScheduleProgrammeSerializer extends JsonSerializer<Programme> {

	@Override
	public void serialize(Programme prog,
			com.fasterxml.jackson.core.JsonGenerator jgen,
			com.fasterxml.jackson.databind.SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
		jgen.writeString(prog.getNameProg());
	}

}