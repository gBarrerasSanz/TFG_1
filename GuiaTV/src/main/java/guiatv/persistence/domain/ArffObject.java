package guiatv.persistence.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import guiatv.computervision.CvUtils;
import guiatv.computervision.Imshow;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

@Entity(name = "arffobject")
public class ArffObject implements Serializable {
	
	@Id
    @Column(name = "idArffPersistence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArffPersistence;
	
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "atts", nullable = true, columnDefinition="BINARY(16777215)")
	private FastVector atts; // Atributos
	
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "data", nullable = true, columnDefinition="BINARY(16777215)")
	private Instances data; // Muestras
	
	@Basic(fetch = FetchType.LAZY)
	@Column(name="trainedClassifier", nullable=true, columnDefinition="BINARY(16777215)")
	private NaiveBayesUpdateable trainedClassifier;
	
	private static FastVector pixelAttVals;
	private static FastVector classAttVals;
	
	public ArffObject() {
		pixelAttVals = new FastVector(2);
		pixelAttVals.addElement("b");
		pixelAttVals.addElement("w");
		
		classAttVals = new FastVector(2);
		classAttVals.addElement("true");
		classAttVals.addElement("false");
	}
	
//	/**
//	 * blob: imagen de 3 canales correspondiente al Roi
//	 * truth: Clasificación humana del blob ( true => Emisión; false => Anuncios)
//	 */
//	public void addSample(Blob blob, boolean truth) {
//		// Se saca el objecto mat del blob
//		Mat img = CvUtils.getMatFromByteArray(blob.getBlob(), blob.getBlobCols(), blob.getBlobRows());
//		// Se binariza la imagen 
//		Mat binImg = CvUtils.thresholdImg(img);
//		// Se obtiene el array de bytes de la imagen binarizada
//		byte[] binBlob = CvUtils.getByteArrayFromMat(binImg);
//		
//		if (data.numInstances() == 0) {
//			setupAtts(img);
//		}
//		
//		double[] vals = new double[data.numAttributes()];
//		for (int i=0; i < blob.getBlob().length; i++) {
//			String val = (binBlob[i] == 0) ? "b" : "w";
//			vals[i] = pixelAttVals.indexOf(val);
//		}
//		vals[atts.size()-1] = classAttVals.indexOf(String.valueOf(truth));
//		data.add(new Instance(1.0, vals));
//	}
	
	public Instances getData() {
		return data;
	}
	
	public void setupAtts(Blob blob) {
		// Se saca el objecto mat del blob
		Mat img = CvUtils.getGrayMatFromByteArray(blob.getBlob(), blob.getBlobCols(), blob.getBlobRows());
//		// Se binariza la imagen 
//		Mat binImg = CvUtils.thresholdImg(img);
//		// Se obtiene el array de bytes de la imagen binarizada
//		byte[] binBlob = CvUtils.getByteArrayFromMat(binImg);
		int numAtts = img.cols() * img.rows() + 1;
		atts = new FastVector(numAtts);
		for (int c=0; c<img.cols(); c++) {
			for (int r=0; r<img.rows(); r++) {
				atts.addElement(new Attribute(c+"_"+r, pixelAttVals));
			}
		}
		// Atributo de clase		
		Attribute classAtt = new Attribute("class", classAttVals);
		atts.addElement(classAtt);
		data = new Instances("MyRel", atts, 0);
		data.setClass(classAtt); // Establece el atributo classAtt como el atributo que indica la clase
	}
	
    /**********************************************************
     * 					GETTERS / SETTERS
     *********************************************************/
	
	public FastVector getAtts() {
		return atts;
	}

	public void setAtts(FastVector atts) {
		this.atts = atts;
	}

	public static FastVector getPixelAttVals() {
		return pixelAttVals;
	}

	public static void setPixelAttVals(FastVector pixelAttVals) {
		ArffObject.pixelAttVals = pixelAttVals;
	}

	public static FastVector getClassAttVals() {
		return classAttVals;
	}

	public static void setClassAttVals(FastVector classAttVals) {
		ArffObject.classAttVals = classAttVals;
	}

	public Long getIdArffPersistence() {
		return idArffPersistence;
	}

	public void setData(Instances data) {
		this.data = data;
	}
	
	public NaiveBayesUpdateable getTrainedClassifier() {
		return trainedClassifier;
	}

	public void setTrainedClassifier(NaiveBayesUpdateable trainedClassifier) {
		this.trainedClassifier = trainedClassifier;
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
		try {
			trainedClassifier.buildClassifier(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addSample(Blob blob, boolean truth) {

		if (data == null) {
			setupAtts(blob);
			setupClassifierNaiveBayesUpdateable(blob);
		}
		
		Instance newInstance = getLabeledInstance(blob, truth);
		if (data.checkInstance(newInstance)) {
//			data.add(newInstance); // TODO: No sé si hace falta (Ni se si hace falta conservar todas las muestras en data)
			newInstance.setDataset(data);
		}
		else {
			throw new IllegalArgumentException("newInstance IS NOT compatible with DataSet data");
		}
		
//		Instances newDataSet = new Instances("sameRel", atts, 1);
//		newDataSet.setClassIndex(newDataSet.numAttributes()-1);
//		Instance newInstance = new Instance(1.0, vals);
//		newDataSet.add(newInstance);
		
		
		updateClassifier(newInstance);
	}
	
	private Instance getLabeledInstance(Blob blob, boolean truth) {
		byte[] binBlob = getBinBlobFromBlob(blob);
//		byte[] binBlob = blob.getBlob();
		int numAtts = data.numAttributes();
		double[] vals = new double[numAtts];
		for (int i=0; i < blob.getBlob().length; i++) {
			String val = (binBlob[i] == 0) ? "b" : "w";
			vals[i] = pixelAttVals.indexOf(val);
		}
		vals[numAtts-1] = classAttVals.indexOf(String.valueOf(truth));
		return new Instance(1.0, vals);
	}
	
	public Instance getUnlabeledInstance(Blob blob) {
		byte[] binBlob = getBinBlobFromBlob(blob);
//		byte[] binBlob = blob.getBlob();
		int numAtts = data.numAttributes();
		double[] vals = new double[numAtts];
		for (int i=0; i < blob.getBlob().length; i++) {
			String val = (binBlob[i] == 0) ? "b" : "w";
			vals[i] = pixelAttVals.indexOf(val);
		}
//		vals[numAtts-1] = classAttVals.indexOf(String.valueOf(truth));
		Instance newInstance = new Instance(1.0, vals);
		newInstance.setDataset(data);
		return newInstance;
	}
	
	private byte[] getBinBlobFromBlob(Blob blob) {
		// Se saca el objeto mat del blob
		Mat img = CvUtils.getGrayMatFromByteArray(blob.getBlob(), blob.getBlobCols(), blob.getBlobRows());
		// Se binariza la imagen 
		CvUtils.threshold(img);
		// Se obtiene el array de bytes de la imagen binarizada
		byte[] binBlob = CvUtils.getByteArrayFromMat(img);
		return binBlob;
	}
	
}
