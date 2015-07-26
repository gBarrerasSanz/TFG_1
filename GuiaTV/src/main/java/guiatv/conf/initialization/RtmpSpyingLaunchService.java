package guiatv.conf.initialization;

import guiatv.persistence.domain.ArffObject;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MLChannel;
import guiatv.persistence.domain.StreamSource;
import guiatv.persistence.repository.service.ArffObjectService;
import guiatv.persistence.repository.service.ChannelService;
import guiatv.persistence.repository.service.MLChannelService;
import guiatv.persistence.repository.service.StreamSourceService;
import guiatv.realtime.rtmpspying.MutexMonitor;
import guiatv.realtime.rtmpspying.RtmpSpyingService;
import guiatv.realtime.rtmpspying.serializable.ChannelData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RtmpSpyingLaunchService {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	ChannelService chServ;
	
	@Autowired
	ArffObjectService arffServ;
	
	@Autowired
	StreamSourceService streamSourceServ;
	
	@Autowired
	MLChannelService mlChServ;
	
	@Autowired
	RtmpSpyingService rtmpSpyingServ;
	
	@Autowired
	MutexMonitor monitor;
	
	public void loadAndSpyChannels() {
		ChannelData chData = null;
		while ((chData = monitor.acquireChannel()) != null) {
			// Crear MlChannel 
			StreamSource streamSource = new StreamSource(chData.getUrl());
			ArffObject arffObject = new ArffObject();
			Channel ch = chServ.findByIdChBusiness(chData.getChIdBusiness(), false);
			MLChannel mlChannel = new MLChannel(ch, streamSource, arffObject, 
					chData.getCols(), chData.getRows(), chData.getTopLeft(), chData.getBotRight());
			// Meterlo en la BD
			streamSourceServ.save(streamSource);
			arffServ.save(arffObject);
			mlChServ.save(mlChannel);
			if (ch != null) {
				// Espiar channel
				rtmpSpyingServ.doSpying(mlChannel);
			}
			else {
				logger.debug("ERROR: Cannot spy channel "+chData.getChIdBusiness()+ ": It is not included in xmltv conf !!!");
			}
		}
	}
}
