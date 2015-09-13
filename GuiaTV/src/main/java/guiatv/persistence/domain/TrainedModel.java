package guiatv.persistence.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import guiatv.common.CommonUtility;
import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.helper.ArffHelper;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

//@Entity(name = "trainedmodel")
public class TrainedModel {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	private static final String FILES_BASE_DIR = "META-INF/MLChannels_DBFiles/";
	
//	@Id
//    @Column(name = "idTrainedModelPersistence", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTrainedModelPersistence;
	
//	@OneToOne(targetEntity=MyCh.class, fetch=FetchType.LAZY)
//	@JoinColumn(name="mych_fk", referencedColumnName="idMyChPersistence")
//	@Transient
	private MyCh myCh;
	
//	@Column(name = "dataSetUri", nullable = false)
	private String dataSetUri;
	
//	@Column(name = "fullDataSetUri", nullable = false)
	private String fullDataSetUri;
	
//	@Column(name = "trainedClassifierUri", nullable = false)
	private String trainedClassifierUri;
	
//	@Column(name = "goodSamplesLearned", nullable = false)
	private int goodSamplesLearned;
	
//	@Column(name = "badSamplesLearned", nullable = false)
	private int badSamplesLearned;
	
//	@Column(name = "batchGoodSamplesUri", nullable = false)
	private String batchGoodSamplesUri;
	
//	@Column(name = "batchBadSamplesUri", nullable = false)
	private String batchBadSamplesUri;
	
//	@Column(name = "trained", nullable = false)
	private boolean trained;
	
//	@Transient
	private Instances dataSet;
	
//	@Transient
	private Instances fullDataSet;
	
//	@Transient
	private NaiveBayesUpdateable trainedClassifier;
	
	public TrainedModel() {
	}
	
	/*
	 * CONSTRUCTOR PREFERIDO
	 */
	public TrainedModel(String idChBusiness, String batchGoodSamplesUri, String batchBadSamplesUri) {
		this.batchGoodSamplesUri = batchGoodSamplesUri;
		this.batchBadSamplesUri = batchBadSamplesUri;
		
		this.dataSetUri = FILES_BASE_DIR+"fileArff/"+idChBusiness+"/dataSet.arff";
		this.fullDataSetUri = FILES_BASE_DIR+"fileArff/"+idChBusiness+"/fullDataSet.arff";
		this.trainedClassifierUri = FILES_BASE_DIR+"trainedClassifier/"+idChBusiness+"/trainedClassifier.model";
		
		this.goodSamplesLearned = 0;
		this.badSamplesLearned = 0;
	}
	
	public void setParent(MyCh myCh) {
		this.myCh = myCh;
	}
	
	public boolean learnSample(Blob blob, boolean truth) {
		// Cargar o crear el modelo
		loadOrCreateDataSet(blob);
		loadOrCreateFullDataSet(blob);
		loadOrCreateTrainedClassifier(blob);
		// Aprender la muestra 
		Instance instance = ArffHelper.getLabeledInstance(dataSet, blob, truth);
		ArffHelper.addInstanceToModel(instance, dataSet, 
				fullDataSet, trainedClassifier);
		// Contabilizar si la muestra es buena o mala		
		if (truth) { goodSamplesLearned++; }
		else { badSamplesLearned++; }
//		if ( ! trained) { trained = true; }
		return true;
	}
	
	public boolean trainWithBatchSamples() {
		if(loadClassifiedDataFromChannel()) {
//			trained = true;
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean loadOrCreateDataSet(Blob blob) {
		if (dataSet == null) { // Si dataSet no está cargado
			// Cargarlo del fichero en el disco duro
			dataSet = ArffHelper.loadDataSet(dataSetUri);
			if (dataSet == null) { // Si no se ha podido cargar el dataSet
				// Entonces crearlo
				dataSet = ArffHelper.createInstancesObject(blob);
			}
			if (dataSet != null){
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return true;
		}
	}
	
	/*
	 * El acceso a fullDataSet se hace en exclusión mutua
	 */
	public synchronized boolean loadOrCreateFullDataSet(Blob blob) {
		if (fullDataSet == null) { // Si fullDataSet no está cargado
			// Cargarlo del fichero en el disco duro
			fullDataSet = ArffHelper.loadDataSet(fullDataSetUri);
			if (fullDataSet == null) { // Si no se ha podido cargar el fullDataSet
				// Entonces crearlo
				fullDataSet = ArffHelper.createInstancesObject(blob);
			}
			if (fullDataSet != null){
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return true;
		}
	}
	
	/*
	 * El acceso a fullDataSet se hace en exclusión mutua
	 */
	public synchronized void freeFullDataSetMemory() {
		fullDataSet = null;
	}
	
	public synchronized void clearTrainedModel() {
		String PATH = "src/main/resources/";
		File dataSetFile = new File(PATH+dataSetUri);
		dataSetFile.setWritable(true);
		
		File fullDataSetFile = new File(PATH+fullDataSetUri);
		fullDataSetFile.setWritable(true);
		
		File trainedClassifierFile = new File(PATH+trainedClassifierUri);
		trainedClassifierFile.setWritable(true);
		
		boolean dataSetDeleted = dataSetFile.delete() || ! dataSetFile.exists();
		boolean fullDataSetDeleted = fullDataSetFile.delete() || ! fullDataSetFile.exists();
		boolean trainedClassifierDeleted = trainedClassifierFile.delete() || ! trainedClassifierFile.exists();
		
		if( dataSetDeleted && fullDataSetDeleted && trainedClassifierDeleted) {
			dataSet = null;
			fullDataSet = null;
			trainedClassifier = null;
			
			goodSamplesLearned = 0;
			badSamplesLearned = 0;
			trained = false;
		}
	}
	
	public boolean loadOrCreateTrainedClassifier(Blob blob) {
		if (dataSet == null) {
			return false;
		}
		if (trainedClassifier == null) { // Si trainedClassifier no está cargado
			// Cargarlo del fichero en el disco duro
			trainedClassifier = ArffHelper.loadTrainedClassifier(trainedClassifierUri);
			if (trainedClassifier == null) { // Si no se ha podido cargar el trainedClassifier
				// Entonces crearlo
				trainedClassifier = ArffHelper.createClassifierNaiveBayesUpdateable(dataSet, blob);
			}
			if (trainedClassifier != null){
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return true;
		}
	}
	
	public MyCh getMyCh() {
		return myCh;
	}

	public void setMyCh(MyCh myCh) {
		this.myCh = myCh;
	}

	public String getDataSetUri() {
		return dataSetUri;
	}

	public void setDataSetUri(String dataSetUri) {
		this.dataSetUri = dataSetUri;
	}

	public String getFullDataSetUri() {
		return fullDataSetUri;
	}

	public void setFullDataSetUri(String fullDataSetUri) {
		this.fullDataSetUri = fullDataSetUri;
	}

	public String getTrainedClassifierUri() {
		return trainedClassifierUri;
	}

	public void setTrainedClassifierUri(String trainedClassifierUri) {
		this.trainedClassifierUri = trainedClassifierUri;
	}

	public int getGoodSamplesLearned() {
		return goodSamplesLearned;
	}

	public void setGoodSamplesLearned(int goodSamplesLearned) {
		this.goodSamplesLearned = goodSamplesLearned;
	}

	public int getBadSamplesLearned() {
		return badSamplesLearned;
	}

	public void setBadSamplesLearned(int badSamplesLearned) {
		this.badSamplesLearned = badSamplesLearned;
	}

	public String getBatchGoodSamplesUri() {
		return batchGoodSamplesUri;
	}

	public void setBatchGoodSamplesUri(String batchGoodSamplesUri) {
		this.batchGoodSamplesUri = batchGoodSamplesUri;
	}

	public String getBatchBadSamplesUri() {
		return batchBadSamplesUri;
	}

	public void setBatchBadSamplesUri(String batchBadSamplesUri) {
		this.batchBadSamplesUri = batchBadSamplesUri;
	}

	public boolean isTrained() {
		return trained;
	}

	public void setTrained(boolean trained) {
		this.trained = trained;
	}

	public Instances getDataSet() {
		return dataSet;
	}

	public void setDataSet(Instances dataSet) {
		this.dataSet = dataSet;
	}

	public Instances getFullDataSet() {
		return fullDataSet;
	}

	public void setFullDataSet(Instances fullDataSet) {
		this.fullDataSet = fullDataSet;
	}

	public NaiveBayesUpdateable getTrainedClassifier() {
		return trainedClassifier;
	}

	public void setTrainedClassifier(NaiveBayesUpdateable trainedClassifier) {
		this.trainedClassifier = trainedClassifier;
	}

	public Long getIdTrainedModelPersistence() {
		return idTrainedModelPersistence;
	}
	
	public boolean isBatchTrainable() {
		return 	(batchGoodSamplesUri.length() > 0) &&
				(batchBadSamplesUri.length() > 0);
	}
	
	public boolean isAbleToCV() {
		/**
		 * Devuelve True si el número de muestras de entrenamiento es mayor
		 * que el número k de folds que hará el k-fold-cross-validation
		 */
		return (goodSamplesLearned + badSamplesLearned >= 10 
				&& goodSamplesLearned >= 1
				&& badSamplesLearned >= 1);
	}
	
	public boolean hasLearnedAnySample() {
		return (goodSamplesLearned + badSamplesLearned > 0);
	}
	
	private boolean loadClassifiedDataFromChannel() {
		try {
			// Cargar muestras good
			File goodDirFile = new File(batchGoodSamplesUri);
			File[] goodListFiles = goodDirFile.listFiles();
			List<Blob> lGoodBlob = CvUtils.loadFileListGroup(goodListFiles, myCh);
	    	
			// Crear el modelo con un blob cualquiera
			loadOrCreateDataSet(lGoodBlob.get(0));
			loadOrCreateFullDataSet(lGoodBlob.get(0));
			loadOrCreateTrainedClassifier(lGoodBlob.get(0));
			// Entrenar con las muestras de lGoodBlob 
			for (Blob blob: lGoodBlob) {
				Instance instance = ArffHelper.getLabeledInstance(dataSet, blob, true);
				ArffHelper.addInstanceToModel(instance, dataSet, 
						fullDataSet, trainedClassifier);
				goodSamplesLearned++;
			}
			lGoodBlob = null; // Liberar memoria de lGoodBlob
			// Cargar muestras bad
	    	File badDirFile = new File(batchBadSamplesUri);
	    	File[] badListFiles = badDirFile.listFiles();
	    	List<Blob> lBadBlob = CvUtils.loadFileListGroup(badListFiles, myCh);
	    	// Entrenar con las muestras de lGoodBlob 
	    	for (Blob blob: lBadBlob) {
				Instance instance = ArffHelper.getLabeledInstance(dataSet, blob, false);
				ArffHelper.addInstanceToModel(instance, dataSet, 
						fullDataSet, trainedClassifier);
				badSamplesLearned++;
			}
	    	lBadBlob = null; // Liberar memoria de lGoodBlob
//	    	freeFullDataSetMemory();
	    	return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (getClass() != obj.getClass()) {
	        return false;
	    }
	    final TrainedModel other = (TrainedModel) obj;
	    if ((this.dataSetUri == null) ? (other.dataSetUri != null) : !this.dataSetUri.equals(other.dataSetUri)) {
	        return false;
	    }
	    if ((this.fullDataSetUri == null) ? (other.fullDataSetUri != null) : !this.fullDataSetUri.equals(other.fullDataSetUri)) {
	        return false;
	    }
	    if ((this.trainedClassifierUri == null) ? (other.trainedClassifierUri != null) : !this.trainedClassifierUri.equals(other.trainedClassifierUri)) {
	        return false;
	    }
	    if ((this.batchGoodSamplesUri == null) ? (other.batchGoodSamplesUri != null) : !this.batchGoodSamplesUri.equals(other.batchGoodSamplesUri)) {
	        return false;
	    }
	    if ((this.batchBadSamplesUri == null) ? (other.batchBadSamplesUri != null) : !this.batchBadSamplesUri.equals(other.batchBadSamplesUri)) {
	        return false;
	    }
	    if (this.badSamplesLearned != other.badSamplesLearned || 
	    		this.goodSamplesLearned != other.goodSamplesLearned ||
	    		this.trained != other.trained) {
	        return false;
	    }
	    return true;
	}
	
}
