package guiatv.catalog.serializers;

import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;

import java.io.IOException;


import java.util.List;



import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;

public class ListSchedulesSerializer extends JsonSerializer<List<Schedule>> {

	@Override
	public void serialize(List<Schedule> lSched,
			com.fasterxml.jackson.core.JsonGenerator jgen,
			com.fasterxml.jackson.databind.SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
		jgen.writeStartArray();
		for (Schedule sched: lSched) {
			jgen.writeObject(sched);
		}
		jgen.writeEndArray();
		
	}

}