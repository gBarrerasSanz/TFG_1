package guiatv.computervision.classificator;

import guiatv.persistence.domain.helper.ArffHelper;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;








import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.unsupervised.instance.SparseToNonSparse;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.functions.SMO;

public class TrainIncr
{

    public static void main(String[] args) throws Exception
    {
    	int[] a3_topLeft = {1102, 604};
    	int[] a3_botRight = {1167, 662};
    	
    	int[] aNeox_topLeft = {1064, 629};
    	int[] aNeox_botRight = {1177, 662};
    	
    	int[] a5_topLeft = {1125, 607};
    	int[] a5_botRight = {1175, 661};
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Cargar libreria de OpenCV
    	
    	String adsDirPath = "captures/publicidad/";
    	String chDirPath = "captures/07_aNeox/goodSamples/";
    	File chDirFile = new File(chDirPath);
    	File adsDirFile = new File(adsDirPath);
    	int idxImgCh = 0;
    	ArffInstance arff = new ArffInstance("a3", aNeox_topLeft, aNeox_botRight);
    	File[] chListFiles = chDirFile.listFiles();
    	for (File imgFile: chListFiles) {
    		arff.addSample(imgFile.getAbsolutePath(), true);
    		if (idxImgCh >= 2000) { break; }
    		idxImgCh++;
    	}
    	int idxImgAds = 0;
    	File[] adsListFiles = adsDirFile.listFiles();
    	for (File imgFile: adsListFiles) {
    		arff.addSample(imgFile.getAbsolutePath(), false);
    		if (idxImgAds >= 2000) { break; }
    		idxImgAds++;
    	}
    	// Guardar el fichero en disco
    	ArffSaver saver = new ArffSaver();
		saver.setInstances(arff.getData());
		File fileOut = new File("data.arff");
		saver.setFile(fileOut);
		saver.writeBatch();
		
		 // Set class index
		arff.getData().setClassIndex(arff.getData().numAttributes()-1);
		 
		// Create a naïve bayes classifier
//		Classifier cModel = (Classifier)new SMO();
		// Todas las muestras a la vez
//		Classifier cModel = (Classifier)new NaiveBayesUpdateable();
//		cModel.buildClassifier(arff.getData());
		
		// TODO: Las muestras de una en una
		NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
		Instances trainSet = new Instances("Rel", arff.getAtts(), 1);
		trainSet.setClassIndex(trainSet.numAttributes()-1);
		Instance firstInstance = arff.getData().firstInstance();
		trainSet.add(firstInstance);
		nb.buildClassifier(trainSet);
		int onlineHits = 0;
		for (int i=1; i<arff.getData().numInstances(); i++) {
			double actualValue = arff.getData().instance(i).classValue();
			double retValue = nb.classifyInstance(arff.getData().instance(i));
			String res = i+": actualValue="+actualValue+", retValue="+retValue;
			if (actualValue == retValue) {
				System.out.println(res + " -> HIT");
				onlineHits++;
			}
			else {
				System.out.println(res + " -> FAILURE");
			}
			nb.updateClassifier(arff.getData().instance(i));
		}
		double onlineSuccRate = (double)onlineHits / (arff.getData().numInstances() - 1);
		System.out.println("onlineSuccRate = "+onlineSuccRate);
		
		// Test the model
		 Evaluation eTest = new Evaluation(arff.getData());
		 eTest.evaluateModel(nb, arff.getData());
		 
		 // Print the result à la Weka explorer:
		 String strSummary = eTest.toSummaryString();
		 System.out.println(strSummary);
		 
		 // Get the confusion matrix
//		 double[][] cmMatrix = eTest.confusionMatrix();

    }
}