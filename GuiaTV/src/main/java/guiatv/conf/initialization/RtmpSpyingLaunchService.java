package guiatv.conf.initialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.ArffObject;
import guiatv.persistence.domain.Blob;
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

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
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
			// Cargar datos clasificados
			loadClassifiedDataFromChannel(chData, mlChannel);
			// Meterlo en la BD
			streamSourceServ.save(mlChannel.getStreamSource());
			arffServ.save(mlChannel.getArffObject());
			mlChServ.save(mlChannel);
			if (ch != null) {
				// Añadir a los MlChannel en curso
				
				// Espiar channel
				rtmpSpyingServ.doSpying(mlChannel);
			}
			else {
				logger.debug("ERROR: Cannot spy channel "+chData.getChIdBusiness()+ ": It is not included in xmltv conf !!!");
			}
		}
	}
	
	private void loadClassifiedDataFromChannel(ChannelData chData, MLChannel mlChannel) {
		// Good Samples de mlChannel
		File chDirFile = new File(chData.getBatchDataUri());
		File[] chListFiles = chDirFile.listFiles();
		loadFileListGroup(chListFiles, mlChannel, true);
    	
		// Avertisements de mlChannel
    	final String ADS_BATCH_DATA_URI = "captures/publicidad";
    	File adsDirFile = new File(ADS_BATCH_DATA_URI);
    	File[] adsListFiles = adsDirFile.listFiles();
    	loadFileListGroup(adsListFiles, mlChannel, false);
	}
	
	private void loadFileListGroup(File[] fList, MLChannel mlChannel, boolean truth) {
		for (File imgFile: fList) {
//    		InputStream imgFileInputStream;
			try {
//				imgFileInputStream = new FileInputStream(imgFile);
//				byte[] imgData = IOUtils.toByteArray(imgFileInputStream);
				Mat imgMat = Highgui.imread(imgFile.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
				byte[] imgData = CvUtils.getByteArrayFromMat(imgMat);
//				Highgui.imwrite("imgMat1.jpeg", imgMat);
//				Highgui.imwrite("imgMat2.jpeg", CvUtils.getGrayScaleMatFromByteArray(
//						imgData, imgMat.cols(), imgMat.rows()));
				Blob blob = new Blob(imgData, mlChannel);
				// DEBUG
//				Highgui.imwrite("imOut1.jpeg", imgMat);
//				Highgui.imwrite("imOut2.jpeg", CvUtils.getGrayScaleMatFromByteArray(
//						imgData, blob.getBlobCols(), blob.getBlobRows()));
	    		mlChannel.getArffObject().addSample(blob, truth);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
	}
}
