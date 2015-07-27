package guiatv.persistence.domain;

import java.util.List;

import guiatv.catalog.serializers.ScheduleChannelSerializer;
import guiatv.computervision.CvUtils;
import guiatv.persistence.domain.Schedule.CustomSchedule;
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
import javax.persistence.UniqueConstraint;

import org.opencv.core.Mat;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import weka.classifiers.Classifier;
import weka.classifiers.UpdateableClassifier;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

@Entity(name = "mlchannel")
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"channel_fk", "streamSource_fk"})})
public class MLChannel {
	
	@Id
    @Column(name = "idMlChPersistence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMlChPersistence;
	
	@OneToOne(targetEntity=Channel.class, fetch=FetchType.LAZY)
	@JoinColumn(name="channel_fk", referencedColumnName="idChPersistence")
	private Channel channel;
	
	@OneToOne(targetEntity=ArffObject.class, fetch=FetchType.LAZY)
	@JoinColumn(name="arffObject_fk", referencedColumnName="idArffPersistence")
	private ArffObject arffObject;
	
	@Column(name="trainedClassifier", nullable=true)
	private NaiveBayesUpdateable trainedClassifier;
	
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
	
	@Column(name="numTrainData", nullable=false)
	private int numTrainData;
	
	private static FastVector pixelAttVals;
	private static FastVector classAttVals;
	
	public MLChannel() {
	}
	
	public MLChannel(Channel channel, StreamSource streamSource, 
			int imgCols, int imgRows, int[] topLeft, int[] botRight) {
		
		this.channel = channel;
		this.streamSource = streamSource;
		this.imgCols = imgCols;
		this.imgRows = imgRows;
		this.topLeft = topLeft;
		this.botRight = botRight;
		this.numTrainData = 0;
		
		// Miembros estáticos
		pixelAttVals = new FastVector(2);
		pixelAttVals.addElement("b");
		pixelAttVals.addElement("w");
		
		classAttVals = new FastVector(2);
		classAttVals.addElement("true");
		classAttVals.addElement("false");
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

	public ArffObject getArffObject() {
		return arffObject;
	}

	public void setArffObject(ArffObject arffObject) {
		this.arffObject = arffObject;
	}

	public NaiveBayesUpdateable getTrainedClassifier() {
		return trainedClassifier;
	}

	public void setTrainedClassifier(NaiveBayesUpdateable trainedClassifier) {
		this.trainedClassifier = trainedClassifier;
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
	
	public int getNumTrainData() {
		return numTrainData;
	}

	public void setNumTrainData(int numTrainData) {
		this.numTrainData = numTrainData;
	}

	public void updateClassifier(List<Instance> lInstances) {
		for (Instance instance: lInstances) {
			try {
				trainedClassifier.updateClassifier(instance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateClassifier(Instance instance) {
		try {
			trainedClassifier.updateClassifier(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setupClassifierNaiveBayesUpdateable(Blob blob) {
		trainedClassifier = new NaiveBayesUpdateable();
		arffObject = new ArffObject();
		arffObject.setupAtts(blob);
		Instances instances = arffObject.getData();
		try {
			trainedClassifier.buildClassifier(instances);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addSample(Blob blob, boolean truth) {
		// Se saca el objecto mat del blob
		Mat img = CvUtils.getMatFromByteArray(blob.getBlob(), blob.getBlobCols(), blob.getBlobRows());
		// Se binariza la imagen 
		Mat binImg = CvUtils.thresholdImg(img);
		// Se obtiene el array de bytes de la imagen binarizada
		byte[] binBlob = CvUtils.getByteArrayFromMat(binImg);
		
		if (numTrainData == 0) {
			setupClassifierNaiveBayesUpdateable(blob);
		}
		
		int numAtts = arffObject.getData().numAttributes();
		double[] vals = new double[numAtts];
		for (int i=0; i < blob.getBlob().length; i++) {
			String val = (binBlob[i] == 0) ? "b" : "w";
			vals[i] = pixelAttVals.indexOf(val);
		}
		vals[numAtts-1] = classAttVals.indexOf(String.valueOf(truth));
		updateClassifier(new Instance(1.0, vals));
	}
}
