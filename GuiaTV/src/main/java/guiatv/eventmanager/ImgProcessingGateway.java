package guiatv.eventmanager;

import guiatv.common.datatypes.Frame;
import guiatv.persistence.domain.Event;
import guiatv.persistence.domain.RTEvent;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.messaging.handler.annotation.Payload;

public interface ImgProcessingGateway {


	Future<Frame> sendFrame(Frame frame);

}