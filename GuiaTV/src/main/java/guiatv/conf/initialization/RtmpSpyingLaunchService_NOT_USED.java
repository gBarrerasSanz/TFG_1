//package guiatv.conf.initialization;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//
//import guiatv.common.CommonUtility;
//import guiatv.computervision.CvUtils;
//import guiatv.persistence.domain.Blob;
//import guiatv.persistence.domain.Channel;
//import guiatv.persistence.domain.MLChannel;
//import guiatv.persistence.domain.StreamSource;
//import guiatv.persistence.domain.helper.ArffHelper;
//import guiatv.persistence.repository.service.ChannelService;
//import guiatv.persistence.repository.service.MLChannelService;
//import guiatv.persistence.repository.service.StreamSourceService;
//import guiatv.realtime.rtmpspying.MutexMonitor;
//import guiatv.realtime.rtmpspying.RtmpSpyingService;
//import guiatv.realtime.rtmpspying.serializable.ChannelData;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.log4j.Logger;
//import org.opencv.core.Mat;
//import org.opencv.highgui.Highgui;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Service;
//
//import weka.classifiers.bayes.NaiveBayesUpdateable;
//import weka.core.Instances;
//
////@DependsOn("MutexMonitor")
//@Service
//@Scope("singleton")
//public class RtmpSpyingLaunchService_NOT_USED {
//	
//	private static final Logger logger = Logger.getLogger("debugLog");
//	
//	@Autowired
//	ChannelService chServ;
//	
//	@Autowired
//	StreamSourceService streamSourceServ;
//	
//	@Autowired
//	MLChannelService mlChServ;
//	
//	@Autowired
//	RtmpSpyingService rtmpSpyingServ;
//	
//	@Autowired
//	MutexMonitor monitor;
//	
////	@PostConstruct
////	public void onPostConstruct() {
////		this.loadAndSpyChannels();
////	}
//	
////	@PostConstruct
//	public boolean loadMLChannel(ChannelData chData) {
//		try {
//			if ( ! CommonUtility.validateURL(chData.getUrl())) {
//				logger.debug("ERROR: channel: idChBusiness = "+chData.getIdChBusiness()+" CANNOT be trained without a stream url");
//			}
//			// Crear MlChannel 
//			StreamSource streamSource = new StreamSource(chData.getUrl());
//			Channel ch = chServ.findByIdChBusiness(chData.getIdChBusiness(), false);
//			if (ch == null) {
//				return false;
//			}
//			else { // Channel distinto de null
//				MLChannel mlChannel = new MLChannel(ch, streamSource,
//						chData.getCols(), chData.getRows(), chData.getTopLeft(), chData.getBotRight(),
//						chData.getNumSamplesToSwitchState());
//				mlChannel.createDataSetUri();
//				mlChannel.createFullDataSetUri();
//				mlChannel.createTrainedClassifierUri();
//				boolean dataSetAndClassifierLoaded = mlChannel.loadDataSet() && 
//						mlChannel.loadFullDataSet() && mlChannel.loadTrainedClassifier();
//				
//				// Si el dataSet y el Classifier SÍ estaban guardados
//				boolean trained = false;
//				if (dataSetAndClassifierLoaded) {
//					// Entonces marcar el MLChannel como que ya está entrenado
//					trained = true;
//				}
//				else { // Si el dataSet y el Classifier NO estaban guardados
//					// Cargar datos clasificados del directorio goodSamples
//					// SI todo va bien, habrá entrenado y por lo tanto trained == true
//					trained = loadClassifiedDataFromChannel(chData, mlChannel);
//				}
//				// Meter el StreamSource en la BD (Esté entrenado o no, si quiere espiar necesita una url de stream)
//				streamSourceServ.save(mlChannel.getStreamSource());
//				if (trained) { // Si el clasificador está entrenado
//					mlChannel.setTrained(true);
//					if (dataSetAndClassifierLoaded) { // Si el dataSet y el Classifier ya estaban guardados
//						// Entonces guardar mlChannel en la base de datos
//						mlChServ.save(mlChannel);
//					}
//					else { // Si el dataSet y el Classifier NO estaban guardados
//						// Entonces guardar mlChannel en la base de datos y los ficheros de dataSet y Classifier
//						mlChServ.saveAndSaveFiles(mlChannel);
//					}
//				}
//				else { // Si el clasificador NO está entrenado
//					mlChannel.setTrained(false);
//					mlChServ.save(mlChannel);
//				}
//				chData.setMyCh(mlChannel);
//				return true;
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//			logger.debug("ERROR: channel: idChBusiness = "+chData.getIdChBusiness()+" DOES NOT HAVE a trained classifier yet.");
//			return false;
//		}
//	}
//	
//	public boolean launchChannelSpying(ChannelData chData) {
//		if (chData == null || ! CommonUtility.validateURL(chData.getUrl()) || ! chData.isActive() || chData.isSpied()) {
//			return false;
//		}
//		
//		MLChannel mlCh = chData.getMyCh();
//		if (mlCh == null) {
//			return false;
//		}
//		mlCh.resetFifoAndCurrentStateRtSched(); // Limpiar fifo
//		rtmpSpyingServ.doSpying(mlCh);
//		return true;
//	}
//	
//	
//	private boolean loadClassifiedDataFromChannel(ChannelData chData, MLChannel mlChannel) {
//		try {
//			// Good Samples de mlChannel
//			File chDirFile = new File(chData.getBatchDataUri());
//			File[] chListFiles = chDirFile.listFiles();
//			loadFileListGroup(chListFiles, mlChannel, true);
//	    	
//			// Avertisements de mlChannel
//	    	final String ADS_BATCH_DATA_URI = "captures/publicidad";
//	    	File adsDirFile = new File(ADS_BATCH_DATA_URI);
//	    	File[] adsListFiles = adsDirFile.listFiles();
//	    	loadFileListGroup(adsListFiles, mlChannel, false);
//	    	return true;
//		} catch(Exception e) {
//			return false;
//		}
//	}
//	
//	private boolean loadFileListGroup(File[] fList, MLChannel mlChannel, boolean truth) {
//		for (File imgFile: fList) {
//			try {
//				Mat imgMat = Highgui.imread(imgFile.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
//				byte[] imgData = CvUtils.getByteArrayFromMat(imgMat);
//				Blob blob = new Blob(imgData, mlChannel);
//				monitor.addSample(blob, truth);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//				return false;
//			} catch (IOException e) {
//				e.printStackTrace();
//				return false;
//			} catch (Exception e) {
//				e.printStackTrace();
//				return false;
//			}
//    	}
//		return true;
//	}
//}
