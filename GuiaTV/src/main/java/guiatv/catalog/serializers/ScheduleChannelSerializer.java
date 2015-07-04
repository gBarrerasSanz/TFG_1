package guiatv.catalog.serializers;

import guiatv.catalog.restcontroller.CatalogRestController;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.Schedule;

import java.io.IOException;




import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;

public class ScheduleChannelSerializer extends JsonSerializer<Channel> {


	@Override
	public void serialize(Channel ch,
			com.fasterxml.jackson.core.JsonGenerator jgen,
			com.fasterxml.jackson.databind.SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
		jgen.writeString(ch.getIdChBusiness());
		
//		sched.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(
//		CatalogRestController.class).getChannelByIdChBusiness(
//				sched.getChannel().getIdChBusiness())).withRel("channels"));
       
	}

}