package guiatv.persistence.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Queue;

import guiatv.catalog.serializers.ScheduleChannelSerializer;
import guiatv.common.CommonUtility;
import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.RtSchedule.InstantState;
import guiatv.persistence.domain.Schedule.CustomSchedule;
import guiatv.persistence.domain.helper.ArffHelper;
import guiatv.schedule.publisher.SchedulePublisher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.log4j.Logger;
import org.opencv.core.Mat;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import weka.classifiers.Classifier;
import weka.classifiers.UpdateableClassifier;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

import org.opencv.highgui.Highgui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Entity(name = "mlchannel")
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"channel_fk", "streamSource_fk"})})
public class MLChannel {
	
	private static final Logger logger = Logger.getLogger("debugLog");
	
	@Id
    @Column(name = "idMlChPersistence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMlChPersistence;
	
	@JsonView({SchedulePublisher.PublisherRtScheduleView.class})
	@OneToOne(targetEntity=Channel.class, fetch=FetchType.LAZY)
	@JoinColumn(name="channel_fk", referencedColumnName="idChPersistence")
	private Channel channel;
	
	@Column(name="trainedClassifierUri", nullable=true)
	private String trainedClassifierUri;
	
	@Column(name="dataSetUri", nullable=true)
	private String dataSetUri;
	
	@Column(name="fullDataSetUri", nullable=true)
	private String fullDataSetUri;
	
	@Column(name="imgCols", nullable=false)
	private int imgCols;
	
	@Column(name="imgRows", nullable=false)
	private int imgRows;
	
	@Column(name="topLeft", nullable=false)
	private int[] topLeft;
	
	@Column(name="botRight", nullable=false)
	private int[] botRight;
	
	@OneToOne(targetEntity=StreamSource.class, fetch=FetchType.LAZY)
	@JoinColumn(name="streamSource_fk", referencedColumnName="idStreamSourcePersistence")
	private StreamSource streamSource;
	
	@OneToMany(targetEntity=Blob.class, mappedBy="mlChannel", fetch=FetchType.LAZY, orphanRemoval=true)
	private List<StreamSource> listRoiBlobs;
	
	@Transient
	private NaiveBayesUpdateable trainedClassifier;
	
	@Transient
	private Instances dataSet;
	
	@Transient
	private Instances fullDataSet;
	
	@Transient
	private InstantState currentState;
	
	@Transient
	private final static int MIN_FIFO_QUEUE_SIZE = 2;
	
	private int numSamplesToSwitchState = MIN_FIFO_QUEUE_SIZE;
	
	@Transient
	private Queue<InstantState> fifoRtSched = new CircularFifoQueue<InstantState>(numSamplesToSwitchState);
	
	public MLChannel() {
	}
	
	public MLChannel(Channel channel, StreamSource streamSource,
			int imgCols, int imgRows, int[] topLeft, int[] botRight,
			int numSamplesToSwitchState) {
		
		this.channel = channel;
		this.streamSource = streamSource;
		this.imgCols = imgCols;
		this.imgRows = imgRows;
		this.topLeft = topLeft;
		this.botRight = botRight;
		this.numSamplesToSwitchState = numSamplesToSwitchState;
	}
	
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public int[] getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(int[] topLeft) {
		this.topLeft = topLeft;
	}

	public int[] getBotRight() {
		return botRight;
	}

	public void setBotRight(int[] botRight) {
		this.botRight = botRight;
	}

	public int getImgCols() {
		return imgCols;
	}

	public void setImgCols(int imgCols) {
		this.imgCols = imgCols;
	}

	public int getImgRows() {
		return imgRows;
	}

	public void setImgRows(int imgRows) {
		this.imgRows = imgRows;
	}

	public StreamSource getStreamSource() {
		return streamSource;
	}

	public void setStreamSource(StreamSource streamSource) {
		this.streamSource = streamSource;
	}

	public List<StreamSource> getListRoiBlobs() {
		return listRoiBlobs;
	}

	public void setListRoiBlobs(List<StreamSource> listRoiBlobs) {
		this.listRoiBlobs = listRoiBlobs;
	}

	public Long getIdMlChPersistence() {
		return idMlChPersistence;
	}
	
	public boolean loadDataSet() {
		try { 
			ArffLoader loader = new ArffLoader();
			loader.setFile(CommonUtility.getFileFromRelativeUri(dataSetUri));
			dataSet = loader.getDataSet();
			dataSet.setClassIndex(dataSet.numAttributes()-1);
			return true;
		} catch (IOException e) {
//			e.printStackTrace();
			return false;
		}
	}
	
	public boolean loadFullDataSet() {
		try { 
			ArffLoader loader = new ArffLoader();
			loader.setFile(CommonUtility.getFileFromRelativeUri(fullDataSetUri));
			fullDataSet = loader.getDataSet();
			fullDataSet.setClassIndex(fullDataSet.numAttributes()-1);
			return true;
		} catch (IOException e) {
//			e.printStackTrace();
			return false;
		}
	}
	
	public boolean loadTrainedClassifier() {
		try {
			InputStream is = new FileInputStream(CommonUtility.getFileFromRelativeUri(trainedClassifierUri));
			ObjectInputStream objectInputStream = new ObjectInputStream(is);
			trainedClassifier = (NaiveBayesUpdateable) objectInputStream.readObject();
			objectInputStream.close();
			return true;
		} catch (IOException e) {
//			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
			return false;
		}

	}
	
	public void saveDataSet() {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		try {
			CommonUtility.createFileFromClassPathUriIfDoesNotExists(dataSetUri);
			saver.setFile(CommonUtility.getFileFromRelativeUri(dataSetUri));
			saver.writeBatch();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveFullDataSet() {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(fullDataSet);
		try {
			CommonUtility.createFileFromClassPathUriIfDoesNotExists(fullDataSetUri);
			saver.setFile(CommonUtility.getFileFromRelativeUri(fullDataSetUri));
			saver.writeBatch();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveTrainedClassifier() {
		OutputStream os;
		try {
			CommonUtility.createFileFromClassPathUriIfDoesNotExists(trainedClassifierUri);
			os = new FileOutputStream(CommonUtility.getFileFromRelativeUri(trainedClassifierUri));
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
			objectOutputStream.writeObject(trainedClassifier);
			objectOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getNumSamplesToSwitchState() {
		return numSamplesToSwitchState;
	}

	public void setNumSamplesToSwitchState(int numSamplesToSwitchState) {
		this.numSamplesToSwitchState = numSamplesToSwitchState;
	}

	public void releaseDataSet() {
		dataSet = null; // Dejar que el recolector haga su trabajo
	}
	
	public void releaseFullDataSet() {
		fullDataSet = null; // Dejar que el recolector haga su trabajo
	}
	
	public void releaseTrainedClassifier() {
		trainedClassifier = null; // Dejar que el recolector haga su trabajo
	}
	
	public void saveAndReleaseDataSet() {
		saveDataSet();
		dataSet = null; // Dejar que el recolector haga su trabajo
	}
	
	public void saveAndReleaseFullDataSet() {
		saveDataSet();
		fullDataSet = null; // Dejar que el recolector haga su trabajo
	}
	
	public void saveAndReleaseTrainedClassifier() {
		saveTrainedClassifier();
		trainedClassifier = null; // Dejar que el recolector haga su trabajo
	}

	public NaiveBayesUpdateable getTrainedClassifier() {
		return trainedClassifier;
	}

	public Instances getDataSet() {
		return dataSet;
	}
	
	public Instances getFullDataSet() {
		return fullDataSet;
	}
	
	public void createDataSetUri() {
		dataSetUri = "META-INF/MLChannels_DBFiles/fileArff/"
				+channel.getIdChBusiness()+"/dataSet.arff";
	}
	
	public void createFullDataSetUri() {
		fullDataSetUri = "META-INF/MLChannels_DBFiles/fileArff/"
				+channel.getIdChBusiness()+"/fullDataSet.arff";
	}
	
	public void createTrainedClassifierUri() {
		trainedClassifierUri = "META-INF/MLChannels_DBFiles/trainedClassifier/"
				+channel.getIdChBusiness()+"/trainedClassifier.model";
	}
	
	public void addSample(Blob blob, boolean truth) 
	{
		if (dataSet == null) {
			// TODO: Asumo que si dataSet == null, entonces trainedClassifier == null. NO SÉ SI SE CUMPLE SIEMPRE O NO
			dataSet = ArffHelper.createInstancesObject(blob);
			fullDataSet = ArffHelper.createInstancesObject(blob);
			trainedClassifier = ArffHelper.createClassifierNaiveBayesUpdateable(dataSet, blob);
		}
		Instance newInstance = ArffHelper.getLabeledInstance(dataSet, blob, truth);
		if (dataSet.checkInstance(newInstance)) {
			/** IMPORTANTE */
//			dataSet.add(newInstance); // TODO: No sé si hace falta (Ni se si hace falta conservar todas las muestras en data)
			newInstance.setDataset(dataSet);
			fullDataSet.add(newInstance); 
			newInstance.setDataset(fullDataSet); 
		}
		else {
			throw new IllegalArgumentException("newInstance IS NOT compatible with DataSet data");
		}
		ArffHelper.updateClassifier(trainedClassifier, newInstance);
	}
	
	
	public InstantState getCurrentState() {
		return currentState;
	}
	
	public void switchCurrentState() {
		switch(currentState) {
		case ON_PROGRAMME:
			currentState = InstantState.ON_ADVERTS;
			break;
		case ON_ADVERTS:
			currentState = InstantState.ON_PROGRAMME;
			break;
		default:
			logger.error("ERROR ON switchCurrentState()");
			break;
		}
	}
	
	/**
	 * Añade el estado del RtSchedule actual a la cola circular
	 * Devuelve TRUE si el estado currentState ha cambiado y por lo tanto el cambio
	 * debe ser notificado y FALSE en otro caso.
	 * 
	 * En principio NO hace falta la keyword synchronized, ya que cada instancia
	 * de MLChannel debe se accedida por un solo thread
	 */
	public synchronized boolean addRtSched(RtSchedule rtSched) {
		if (fifoRtSched.size() < MIN_FIFO_QUEUE_SIZE) { // Si la cola NO está llena aun
			fifoRtSched.add(rtSched.getState());
			return false;
		}
		else { // Si la cola está llena
			if (currentState == null) { // Si todavía NO se ha inicializado currentState
				int numOnProgramme = 0, numOnAdverts = 0;
				// Quedarse con el instante mayoritario
				for (InstantState instant: fifoRtSched) {
					switch(instant) {
					case ON_PROGRAMME:
						numOnProgramme++;
						break;
					case ON_ADVERTS:
						numOnAdverts++;
						break;
					default:
						break;
					}
				}
				currentState = InstantState.ON_PROGRAMME;
				if (numOnAdverts > numOnProgramme) {
					currentState = InstantState.ON_ADVERTS;
				}
				Channel ch = rtSched.getMlChannel().getChannel();
				logger.debug("Initialized channel ("+ch.getIdChBusiness()+") -> "+currentState);
				return false;
			}
			else { // Si currentState ya estaba inicializado
				fifoRtSched.add(rtSched.getState());
				for (InstantState instant: fifoRtSched) {
					// Si alguno de los estados de la cola coincide con el estado actual -> Salir
					if (instant.equals(currentState)) {
						return false;
					}
				}
				/*
				 * Post: Todos los estados de la cola son distintos al estado actual currentState
				 * Por lo tanto rtSched.getState(), al pertenecer a la cola, también 
				 * es distinto al estado actual 
				 */
				// Cambiar el estado actual
				switchCurrentState();
				return true;
			}
		}
	}
	
	
}
