package guiatv.catalog.serializers;

import guiatv.catalog.restcontroller.CatalogRestController;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.Schedule;

import java.io.IOException;


import java.util.List;





import org.springframework.hateoas.mvc.ControllerLinkBuilder;

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
			sched.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
					CatalogRestController.class).getChannelByHashIdChBusiness(
							sched.getChannel().getHashIdChBusiness())).withRel("channel"));
			sched.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
					CatalogRestController.class).getProgrammeByHashNameProg(
							sched.getProgramme().getHashNameProg())).withRel("programme"));
			jgen.writeObject(sched);
		}
		jgen.writeEndArray();
		
	}

}