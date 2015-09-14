package guiatv.realtime.servicegateway;


import guiatv.persistence.domain.blobFrame;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.messaging.handler.annotation.Payload;

public interface CapturedBlobsGateway {
	Future<blobFrame> sendBlob(blobFrame blob);
}