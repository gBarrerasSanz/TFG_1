package guiatv.persistence.domain.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import guiatv.common.CommonUtility;
import guiatv.computervision.CvUtils;
import guiatv.computervision.Imshow;
import guiatv.persistence.domain.Blob;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class ArffHelper implements Serializable {
	
//	private FastVector atts; // Atributos
//	
//	private Instances data; // Muestras
	
//	private NaiveBayesUpdateable trainedClassifier;
	
//	private static FastVector pixelAttVals;
//	private static FastVector classAttVals;
	
	public ArffHelper() {
		
	}
	
	private static FastVector getPixelAttVals() {
		FastVector pixelAttVals = new FastVector(2);
		pixelAttVals.addElement("b");
		pixelAttVals.addElement("w");
		return pixelAttVals;
	}
	
	private static FastVector getClassAttVals() {
		FastVector classAttVals = new FastVector(2);
		classAttVals.addElement("true");
		classAttVals.addElement("false");
		return classAttVals;
	}
	
	public static Instances createInstancesObject(Blob blob) {
		FastVector pixelAttVals = getPixelAttVals();
		FastVector classAttVals = getClassAttVals();
		// Se saca el objecto mat del blob
		Mat img = CvUtils.getGrayMatFromByteArray(blob.getBlob(), blob.getBlobCols(), blob.getBlobRows());

		int numAtts = img.cols() * img.rows() + 1;
		FastVector atts = new FastVector(numAtts);
		for (int c=0; c<img.cols(); c++) {
			for (int r=0; r<img.rows(); r++) {
				atts.addElement(new Attribute(c+"_"+r, pixelAttVals));
			}
		}
		// Atributo de clase		
		Attribute classAtt = new Attribute("class", classAttVals);
		atts.addElement(classAtt);
		// Crear objeto Instances
		String nameInstances = "Rel "+blob.getMyCh().getChannel().getIdChBusiness();
		Instances dataSet = new Instances(nameInstances, atts, 0);
		// Añadir atributos al objeto Instances
		dataSet.setClass(classAtt); // Establece el atributo classAtt como el atributo que indica la clase
		return dataSet;
	}
	/**
	 * 
	 * @param trainedClassifier: IN/OUT
	 * @param lInstances: IN
	 */
	public static void updateClassifier(NaiveBayesUpdateable trainedClassifier, List<Instance> lInstances) {
		for (Instance instance: lInstances) {
			try {
				trainedClassifier.updateClassifier(instance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void updateClassifier(NaiveBayesUpdateable trainedClassifier, Instance instance) {
		try {
			trainedClassifier.updateClassifier(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param dataSet: IN/OUT
	 * @param blob: IN
	 */
	public static NaiveBayesUpdateable createClassifierNaiveBayesUpdateable(Instances dataSet, Blob blob) {
		NaiveBayesUpdateable trainedClassifier = new NaiveBayesUpdateable();
		try {
			trainedClassifier.buildClassifier(dataSet);
			return trainedClassifier;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Instance getLabeledInstance(Instances dataSet, Blob blob, boolean truth) {
		FastVector pixelAttVals = getPixelAttVals();
		FastVector classAttVals = getClassAttVals();
		byte[] binBlob = getBinBlobFromBlob(blob);
		int numAtts = dataSet.numAttributes();
		double[] vals = new double[numAtts];
		for (int i=0; i < blob.getBlob().length; i++) {
			String val = (binBlob[i] == 0) ? "b" : "w";
			vals[i] = pixelAttVals.indexOf(val);
		}
		vals[numAtts-1] = classAttVals.indexOf(String.valueOf(truth));
		return new Instance(1.0, vals);
	}
	
	public static Instance getUnlabeledInstance(Instances dataSet, Blob blob) {
		FastVector pixelAttVals = getPixelAttVals();
		byte[] binBlob = getBinBlobFromBlob(blob);
		int numAtts = dataSet.numAttributes();
		double[] vals = new double[numAtts];
		for (int i=0; i < blob.getBlob().length; i++) {
			String val = (binBlob[i] == 0) ? "b" : "w";
			vals[i] = pixelAttVals.indexOf(val);
		}
		// Se pone la clase a FALSE por poner algo y evitar ArrayIndexOutOfBoundsException
//		vals[numAtts-1] = classAttVals.indexOf(String.valueOf(false));
		Instance newInstance = new Instance(1.0, vals);
		newInstance.setDataset(dataSet);
		return newInstance;
	}
	
	private static byte[] getBinBlobFromBlob(Blob blob) {
		// Se saca el objeto mat del blob
		Mat img = CvUtils.getGrayMatFromByteArray(blob.getBlob(), blob.getBlobCols(), blob.getBlobRows());
		// Se binariza la imagen 
		CvUtils.threshold(img);
		// Se obtiene el array de bytes de la imagen binarizada
		byte[] binBlob = CvUtils.getByteArrayFromMat(img);
		return binBlob;
	}
	
	public static String doCrossValidation(NaiveBayesUpdateable trainedClassifier, Instances dataSet) {
		Evaluation eTest;
		try {
			eTest = new Evaluation(dataSet);
			eTest.crossValidateModel(trainedClassifier, dataSet, 10, new Random(1));
			return eTest.toSummaryString();
		} catch (Exception e) {
			return "ERROR: "+e.getMessage();
		}
	}
	
	public static boolean addInstanceToModel(Instance instance, Instances dataSet, Instances fullDataSet, NaiveBayesUpdateable trainedClassifier) {
		if (dataSet.checkInstance(instance)) { // Si la instancia concuerda con el modelo creado
			/** IMPORTANTE */
//			dataSet.add(newInstance); // TODO: No sé si hace falta (Ni se si hace falta conservar todas las muestras en data)
			instance.setDataset(dataSet);
			instance.setDataset(fullDataSet); 
			fullDataSet.add(instance);
			ArffHelper.updateClassifier(trainedClassifier, instance);
			return true;
		}
		else {
			return false;
		}
	}
	
	public static Instances loadDataSet(String uri) {
		try { 
			ArffLoader loader = new ArffLoader();
			loader.setFile(CommonUtility.getFileFromRelativeUri(uri));
			Instances dataSet = loader.getDataSet();
			dataSet.setClassIndex(dataSet.numAttributes()-1);
			return dataSet;
		} catch (IOException e) {
			return null;
		}
	}
	
	public static Instances loadFullDataSet(String uri) {
		try { 
			ArffLoader loader = new ArffLoader();
			loader.setFile(CommonUtility.getFileFromRelativeUri(uri));
			Instances fullDataSet = loader.getDataSet();
			fullDataSet.setClassIndex(fullDataSet.numAttributes()-1);
			return fullDataSet;
		} catch (IOException e) {
//			e.printStackTrace();
			return null;
		}
	}
	
	public static NaiveBayesUpdateable loadTrainedClassifier(String uri) {
		try {
			InputStream is = new FileInputStream(CommonUtility.getFileFromRelativeUri(uri));
			ObjectInputStream objectInputStream = new ObjectInputStream(is);
			NaiveBayesUpdateable trainedClassifier = (NaiveBayesUpdateable) objectInputStream.readObject();
			objectInputStream.close();
			return trainedClassifier;
		} catch (IOException e) {
//			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
			return null;
		}

	}
	
}
