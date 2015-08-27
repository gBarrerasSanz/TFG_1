package guiatv.conf.initialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.Blob;
import guiatv.persistence.domain.Channel;
import guiatv.persistence.domain.MLChannel;
import guiatv.persistence.domain.StreamSource;
import guiatv.persistence.domain.helper.ArffHelper;
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
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instances;

//@DependsOn("MutexMonitor")
@Service
public class RtmpSpyingLaunchService {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Autowired
	ChannelService chServ;
	
	@Autowired
	StreamSourceService streamSourceServ;
	
	@Autowired
	MLChannelService mlChServ;
	
	@Autowired
	RtmpSpyingService rtmpSpyingServ;
	
	@Autowired
	MutexMonitor monitor;
	
//	@PostConstruct
//	public void onPostConstruct() {
//		this.loadAndSpyChannels();
//	}
	
//	@PostConstruct
	public void loadAndSpyChannels() {
		ChannelData chData = null;
		
		while ((chData = monitor.acquireNonBusyChannel()) != null) {
			
			if (chData != null && chData.getUrl() != null) {
				// Crear MlChannel 
				StreamSource streamSource = new StreamSource(chData.getUrl());
				Channel ch = chServ.findByIdChBusiness(chData.getIdChBusiness(), false);
				if (ch != null) {
//					// TODO: Decidir donde poner esto
//					// Cambiar nombre del channel si se indica en el xml de ChannelData
//					if (chData.getNameProg() != null) { 
//						ch.setNameCh(chData.getNameProg());
//						chServ.updateNameProgWhereIdChPersistence(chData.getNameProg(), ch);
//					}
					
					MLChannel mlChannel = new MLChannel(ch, streamSource,
							chData.getCols(), chData.getRows(), chData.getTopLeft(), chData.getBotRight(),
							chData.getNumSamplesToSwitchState());
					mlChannel.createDataSetUri();
					mlChannel.createFullDataSetUri();
					mlChannel.createTrainedClassifierUri();
					boolean dataSetAndClassifierLoaded = mlChannel.loadDataSet() && 
							mlChannel.loadFullDataSet() && mlChannel.loadTrainedClassifier();
					if ( ! dataSetAndClassifierLoaded ) { // Si el dataSet y el Classifier NO estaban guardados
						// Cargar datos clasificados en el directorio goodSamples
						loadClassifiedDataFromChannel(chData, mlChannel);
					}
					// Meterlo en la BD
					streamSourceServ.save(mlChannel.getStreamSource());
					if (dataSetAndClassifierLoaded) { // Si el dataSet y el Classifier ya estaban guardados
						// Entonces guardar mlChannel en la base de datos
						mlChServ.save(mlChannel);
					}
					else { // Si el dataSet y el Classifier NO estaban guardados
						// Entonces guardar mlChannel en la base de datos y los ficheros de dataSet y Classifier
						mlChServ.saveAndSaveFiles(mlChannel);
					}
					// Hacer cross-validation del modelo entrenado
	//				String cvResults = ArffHelper.doCrossValidation(mlChannel.getTrainedClassifier(), mlChannel.getFullDataSet());
	//				logger.debug("Channel "+ch.getIdChBusiness()+": "+cvResults);
					// Liberar la memoria que ocupa fullDataSet
					mlChannel.releaseFullDataSet();
					// Espiar channel
					logger.debug("Spying channel "+ch.getNameCh());
					rtmpSpyingServ.doSpying(mlChannel);
				}
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
			try {
				Mat imgMat = Highgui.imread(imgFile.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
				byte[] imgData = CvUtils.getByteArrayFromMat(imgMat);
				Blob blob = new Blob(imgData, mlChannel);
				mlChannel.addSample(blob, truth);
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
