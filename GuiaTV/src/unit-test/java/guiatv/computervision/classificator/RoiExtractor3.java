package guiatv.computervision.classificator;

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
import weka.core.converters.ArffSaver;
import weka.filters.unsupervised.instance.SparseToNonSparse;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;

public class RoiExtractor3
{

    public static void main(String[] args) throws Exception
    {
    	int[] a3_topLeft = {1102, 604};
    	int[] a3_botRight = {1167, 662};
    	
    	int[] aNeox_topLeft = {1064, 629};
    	int[] aNeox_botRight = {1177, 662};
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Cargar libreria de OpenCV
    	
    	String adsDirPath = "captures/publicidad/";
    	String chDirPath = "captures/03_a3/goodSamples/";
    	File chDirFile = new File(chDirPath);
    	File adsDirFile = new File(adsDirPath);
    	int idxImgCh = 0;
    	ArffInstance arff = new ArffInstance("a3", a3_topLeft, a3_botRight);
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
		arff.getData().setClassIndex(0);
		 
		// Create a na�ve bayes classifier
		Classifier cModel = (Classifier)new SMO();
//		Classifier cModel = (Classifier)new NaiveBayes();
		cModel.buildClassifier(arff.getData());
		
		// Test the model
		 Evaluation eTest = new Evaluation(arff.getData());
		 eTest.evaluateModel(cModel, arff.getData());
		 
		 // Print the result � la Weka explorer:
		 String strSummary = eTest.toSummaryString();
		 System.out.println(strSummary);
		 
		 // Get the confusion matrix
//		 double[][] cmMatrix = eTest.confusionMatrix();

    }
}