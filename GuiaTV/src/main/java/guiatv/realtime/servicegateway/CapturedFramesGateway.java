package guiatv.realtime.servicegateway;

import guiatv.common.datatypes.Frame;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.messaging.handler.annotation.Payload;

public interface CapturedFramesGateway {


	Future<Frame> sendFrame(Frame frame);

}