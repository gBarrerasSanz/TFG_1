package guiatv.schedule.poller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import guiatv.common.CommonUtility;
import guiatv.persistence.domain.Schedule;
import guiatv.realtime.servicegateway.CapturedBlobsGateway;

public class SchedulePollerOutDummy {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	public void receiveDummy(Message<List<Schedule>> listScheduleMsg) {
		// No hacer nada
	}
}
