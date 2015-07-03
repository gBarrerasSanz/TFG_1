package guiatv.catalog.serializers;

import guiatv.persistence.domain.Programme;

import java.io.IOException;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;

public class ScheduleSerializer extends JsonSerializer<Programme> {

	@Override
	public void serialize(Programme prog,
			com.fasterxml.jackson.core.JsonGenerator jgen,
			com.fasterxml.jackson.databind.SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
		jgen.writeString(prog.getNameProg());
	}

}