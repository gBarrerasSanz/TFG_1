package guiatv.schedule.publisher;

import guiatv.catalog.restcontroller.PublisherRestController;
import guiatv.common.CommonUtility;
import guiatv.persistence.domain.Programme;
import guiatv.persistence.domain.RtSchedule;
import guiatv.persistence.domain.Schedule;
import guiatv.persistence.repository.service.ProgrammeService;
import guiatv.persistence.repository.service.ScheduleService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourcesLinksVisible;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SchedulePublisher {
	
//	@Autowired
//	RabbitAdmin rabbitAdmin;
//	@Autowired
//	TopicExchange topicExch;
	
	private static Logger logger = Logger.getLogger("debugLog");
	
	public interface PublisherScheduleView {};
	
	public interface PublisherRtScheduleView {};
	
	@Autowired
	AmqpTemplate amqpTmp;
	
	@Autowired
	ScheduleService schedServ;
	
	@Autowired
	ProgrammeService progServ;
	
	public void publishListSchedules(Message<List<Schedule>> listSchedMsg ) {
		List<Schedule> nonPublishedSched = new ArrayList<Schedule>();
		String routKey = null;
		for (Schedule sched: listSchedMsg.getPayload()) {
			try {
				assert(sched != null);
				routKey = sched.getChannel().getHashIdChBusiness()+"."+sched.getProgramme().getHashNameProg();
				String schedJson = sched.toStringPublisher();
				schedJson = StringEscapeUtils.unescapeJava(schedJson);
				amqpTmp.convertAndSend(routKey, schedJson);
				
//				logger.debug("Published Schedule: "+schedJson);
				logger.debug("Published Sched ("+sched.getIdSched()+"): "+sched.getProgramme().getNameProg()
						+ " ["+sched.getChannel().getIdChBusiness()+"]"
						+" -> "+CommonUtility.dateToStr(sched.getStart())+
						" --- "+CommonUtility.dateToStr(sched.getEnd())+
						" ==> Published: "+sched.isPublished());
			
			} catch (AmqpException e) {
				nonPublishedSched.add(sched);
				logger.error("ERROR: Could NOT connect to RabbitMQ");
			} catch(Exception e) {
				nonPublishedSched.add(sched);
				e.printStackTrace();
//				logger.error("ERROR: Unknown error");
			}
			for (Schedule schedule: nonPublishedSched) {
				schedServ.setFalsePublished(schedule);
			}
		}
		/**
		 * Actualizar el campo published de los schedules publicados a True.
		 */
//		for (Schedule sched: publishedSched) {
//			schedServ.setTruePublishedWhereIdSched(sched.getIdSched());
////			Schedule updatedSched = schedServ.findOneByIdSched(sched.getIdSched(), false);
////			logger.debug(updatedSched);
//		}
//		schedServ.delete(publishedSched);
	}
	
	public void publishRtSchedule(Message<RtSchedule> rtScheduleMsg) {
		RtSchedule rtSched = rtScheduleMsg.getPayload();
		// Determinar a qu� programa afecta
		Programme prog = progServ.findOneByChannelAndInstant(rtSched.getMyCh().getChannel(), 
				rtSched.getInstant());
		if (prog != null) {
			rtSched.setProgramme(prog);
			String routKey = null;
			try {
				assert(rtSched != null);
				routKey = rtSched.getMyCh().getChannel().getHashIdChBusiness()+"."+rtSched.getProgramme().getHashNameProg();
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
		        mapper.setConfig(mapper.getSerializationConfig()
		                .withView(PublisherRtScheduleView.class));
				String rtSchedJsonString = mapper.writeValueAsString(rtSched);
	//			schedJson = StringEscapeUtils.unescapeJava(schedJson);
				amqpTmp.convertAndSend(routKey, rtSchedJsonString);
	//			logger.debug("Published RtSchedule: "+rtSchedJsonString);
				logger.debug("Published RtSchedule ("+rtSched.getMyCh().getChannel().getIdChBusiness()+"): "
						+rtSched.getProgramme().getNameProg()
						+" -> "+rtSched.getState());
				} catch (AmqpException e) {
					logger.error("ERROR: Could NOT connect to RabbitMQ");
				} catch(Exception e) {
					e.printStackTrace();
	//				logger.error("ERROR: Unknown error");
				}
		}
		else {
			logger.error("Channel ["+rtSched.getMyCh().getChannel().getIdChBusiness()+"]:RtSchedule DOES NOT contain any programme");
		}
	}
}
