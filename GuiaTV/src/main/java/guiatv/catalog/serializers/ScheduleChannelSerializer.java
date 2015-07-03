package guiatv.catalog.serializers;

import guiatv.persistence.domain.Channel;

import java.io.IOException;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;

public class ScheduleChannelSerializer extends JsonSerializer<Channel> {


	@Override
	public void serialize(Channel value,
			com.fasterxml.jackson.core.JsonGenerator jgen,
			com.fasterxml.jackson.databind.SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
		jgen.writeString("");
       
	}

}