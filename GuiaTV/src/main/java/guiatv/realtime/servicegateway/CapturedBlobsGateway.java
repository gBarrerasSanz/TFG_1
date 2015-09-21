package guiatv.realtime.servicegateway;


import guiatv.persistence.domain.BlobFrame;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.messaging.handler.annotation.Payload;

public interface CapturedBlobsGateway {
	Future<BlobFrame> sendBlob(BlobFrame blob);
}