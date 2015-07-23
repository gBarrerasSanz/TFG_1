//package guiatv.computervision.classificator;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.OutputStream;
//
//import jsat.ARFFLoader;
//import jsat.DataSet;
//import jsat.classifiers.CategoricalResults;
//import jsat.classifiers.ClassificationDataSet;
//import jsat.classifiers.ClassificationModelEvaluation;
//import jsat.classifiers.Classifier;
//import jsat.classifiers.DataPoint;
//import jsat.classifiers.bayesian.MultivariateNormals;
//import jsat.classifiers.bayesian.NaiveBayes;
//import jsat.classifiers.bayesian.NaiveBayesUpdateable;
//import jsat.classifiers.knn.NearestNeighbour;
//import jsat.classifiers.linear.ALMA2;
//import jsat.classifiers.linear.AROW;
//import jsat.classifiers.linear.LinearSGD;
//import jsat.classifiers.svm.DCD;
//import jsat.classifiers.svm.DCDs;
//import guiatv.cv.classificator.Imshow;
//import guiatv.persistence.domain_NOT_USED.ArffObject;
//
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.highgui.Highgui;
//import org.opencv.imgproc.Imgproc;
//
//import weka.classifiers.trees.RandomForest;
//import weka.core.converters.ArffSaver;
//import weka.filters.unsupervised.instance.SparseToNonSparse;
//
//
//public class RoiExtractor2
//{
//
//    public static void main(String[] args) throws IOException
//    {
//    	int[] a3_topLeft = {1102, 604};
//    	int[] a3_botRight = {1167, 662};
//    	
//    	int[] aNeox_topLeft = {1064, 629};
//    	int[] aNeox_botRight = {1177, 662};
//    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Cargar libreria de OpenCV
//    	
//    	String adsDirPath = "captures/publicidad/";
//    	String chDirPath = "captures/03_a3/goodSamples/";
//    	File chDirFile = new File(chDirPath);
//    	File adsDirFile = new File(adsDirPath);
//    	int idxImgCh = 0;
//    	ArffObject arff = new ArffObject("a3", a3_topLeft, a3_botRight);
//    	File[] chListFiles = chDirFile.listFiles();
//    	for (File imgFile: chListFiles) {
//    		arff.addSample(imgFile.getAbsolutePath(), true);
//    		if (idxImgCh >= 2000) { break; }
//    		idxImgCh++;
//    	}
//    	int idxImgAds = 0;
//    	File[] adsListFiles = adsDirFile.listFiles();
//    	for (File imgFile: adsListFiles) {
//    		arff.addSample(imgFile.getAbsolutePath(), false);
//    		if (idxImgAds >= 2000) { break; }
//    		idxImgAds++;
//    	}
//    	ArffSaver saver = new ArffSaver();
//		saver.setInstances(arff.getData());
//		File fileOut = new File("data.arff");
//		saver.setFile(fileOut);
//		saver.writeBatch();
//		
//		DataSet dataSet = ARFFLoader.loadArffFile(fileOut);
//		
//		//We specify '0' as the class we would like to make the target class. 
//        ClassificationDataSet cDataSet = new ClassificationDataSet(dataSet, 0);
//
//        int errors = 0;
//        Classifier classifier = new NaiveBayesUpdateable();
////      Classifier classifier = new NaiveBayes();
////        Classifier classifier = new DCD();
////        Classifier classifier = new DCDs();
//        classifier.trainC(cDataSet);
//
//        for(int i = 0; i < dataSet.getSampleSize(); i++)
//        {
//            DataPoint dataPoint = cDataSet.getDataPoint(i);//It is important not to mix these up, the class has been removed from data points in 'cDataSet' 
//            int truth = cDataSet.getDataPointCategory(i);//We can grab the true category from the data set
//
//            //Categorical Results contains the probability estimates for each possible target class value. 
//            //Classifiers that do not support probability estimates will mark its prediction with total confidence. 
//            CategoricalResults predictionResults = classifier.classify(dataPoint);
//            int predicted = predictionResults.mostLikely();
//            String failed = "OK";
//            String filePath = "";
//            if(predicted != truth) {
//                errors++;
//                failed = "FAILED";
//                
//                if (i < idxImgCh) {
//                	filePath = chListFiles[i].getPath();
//                }
//                else {
//                	filePath = adsListFiles[i - idxImgCh].getPath();
//                }
//            }
//            
//            
//            System.out.println(i + "| "+failed+ ": " + "Confidence: " + predictionResults.getProb(predicted) + " -> "+filePath);
////            System.out.println( i + "| True Class: " + truth + ", Predicted: " + predicted + ", Confidence: " + predictionResults.getProb(predicted) +" ["+failed+"]");
//        }
//
//        System.out.println(errors + " errors were made, " + 100.0*errors/dataSet.getSampleSize() + "% error rate" );
//    }
//}