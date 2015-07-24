package guiatv.realtime.servicegateway;


import guiatv.persistence.domain.Blob;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.messaging.handler.annotation.Payload;

public interface CapturedFramesGateway {
	Future<Blob> sendBlob(Blob blob);
}