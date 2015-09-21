package guiatv.computervision.classificator;

import guiatv.common.CommonUtility;
import guiatv.persistence.domain.helper.ArffHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jsat.classifiers.UpdateableClassifier;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.NBTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Debug;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.unsupervised.instance.SparseToNonSparse;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesSimple;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.LWL;
import weka.classifiers.meta.GridSearch;
import weka.classifiers.mi.CitationKNN;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.functions.LibLINEAR;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;

public class NBBenchmark {

	public static void main(String[] args) throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Cargar libreria de
														// OpenCV
		List<ChannelContainer> chList = new ArrayList<ChannelContainer>();
		chList.add(new ChannelContainer("00_a24", 1113, 562, 1203, 659));
		chList.add(new ChannelContainer("01_a1", 1118, 605, 1197, 666));
		chList.add(new ChannelContainer("02_a2", 1120, 614, 1194, 675));
		chList.add(new ChannelContainer("03_a3", 1103, 606, 1165, 661));
		chList.add(new ChannelContainer("05_a5", 1123, 606, 1176, 663));
		chList.add(new ChannelContainer("06_a6", 1103, 615, 1153, 665));
		chList.add(new ChannelContainer("07_aNeox", 1064, 629, 1177, 662));
		chList.add(new ChannelContainer("08_aNova", 1024, 614, 1151, 649));
		chList.add(new ChannelContainer("09_aTDP", 1145, 36, 1254, 94));
		chList.add(new ChannelContainer("10_aAragon", 1110, 50, 1164, 107));
		
		String[] features_header  = {"FEATURES: VALOR DE LOS PÍXELES", "FEATURES: MOMENTOS DE IMAGEN"};
		String[] folder = {"pixels", "moments"};
		/**
		 * MODELOS
		 */
//		List<Classifier> lC = new ArrayList<Classifier>();
//		List<String> lNameC = new ArrayList<String>(); 
//		lC.add(new NaiveBayesUpdateable()); lNameC.add("NaiveBayesUpdateable");
//		lC.add(new SMO()); lNameC.add("SMO"); 
//		lC.add(new IBk()); lNameC.add("IBk"); 
//		lC.add(new J48()); lNameC.add("J48"); 
//		lC.add(new RandomForest()); lNameC.add("RandomForest"); 
		NaiveBayesUpdateable nbClassif = new NaiveBayesUpdateable();

		List<Double> lTrainTime = new ArrayList<Double>();
		List<Double> lClassifTime = new ArrayList<Double>();
		for (ChannelContainer ch : chList) {
			System.out
				.println("=====================================================\n"
						+ "==================== "
						+ ch.nameCh
						+ "====================\n"
						+ "=====================================================\n");
			/************************************
			 * Para cada modelo
			 ************************************/
			Arff arff;
			arff = new ArffInstance(ch.nameCh, ch.topLeft,
					ch.botRight);
		
			File[] chListFiles = new File(ch.goodSamplesDir).listFiles();

			for (File imgFile : chListFiles) {
				arff.addSample(imgFile.getAbsolutePath(), true);
			}
			File[] adsListFiles = new File(ch.badSamplesDir).listFiles();
			for (File imgFile : adsListFiles) {
				arff.addSample(imgFile.getAbsolutePath(), false);
			}
			// Guardar el fichero de DATOS en disco
			ArffSaver saver = new ArffSaver();
			saver.setInstances(arff.getData());
			File dataFile = new File("results/"+folder[0]+"/data/data_" + ch.nameCh + ".arff");
			saver.setFile(dataFile);
			saver.writeBatch();

			// Set class index
			arff.getData().setClassIndex(arff.getData().numAttributes() - 1);

//					Classifier cModel = (Classifier) new NaiveBayesUpdateable();
//				Classifier cModel = (Classifier) new IBk();
			long start, end;
			/** 
			 * TRAIN
			 */
			nbClassif.buildClassifier(arff.getData());
			start = System.currentTimeMillis();
			for (int i=0; i<arff.getData().numInstances(); i++) {
				nbClassif.updateClassifier(arff.getData().instance(i));
			}
			end = System.currentTimeMillis();
			lTrainTime.add(((double)(end-start))/arff.getData().numInstances());
			/**
			 * TEST
			 */
			Evaluation eTest = ArffHelper.doCrossValidation(nbClassif, arff.getData());
			Evaluation eTestTr = ArffHelper.evaluateTrainedClassifier(nbClassif, arff.getData());
			start = System.currentTimeMillis();
			for (int i=0; i<arff.getData().numInstances(); i++) {
				nbClassif.classifyInstance(arff.getData().instance(i));
			}
			end = System.currentTimeMillis();
			lClassifTime.add(((double)(end-start))/arff.getData().numInstances());
			// Evaluar con el modelo entrenado
//				Evaluation eTest = ArffHelper.doCrossValidation(cModel, arff.getData());
			
			// Guardar el fichero de MODELO en disco
			File modelFile = new File("results/"+folder[0]+"/models/model_"+ch.nameCh+".model");
			OutputStream os = null;
			try {
				os = new FileOutputStream(modelFile);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
				objectOutputStream.writeObject(nbClassif);
				objectOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			os.close();
			if (eTest != null) {
				double pctCorrectTr = eTestTr.pctCorrect();
				Double f_score = eTest.fMeasure(1);
				double pctCorrect = eTest.pctCorrect();
				double kappaCoeff = eTest.kappa();
				int numInstances = arff.getData().numInstances();
				int numAtts = arff.getData().numAttributes();
				double dataFileLengthMB = dataFile.length() / (1024.0 * 1024);
				double modelFileLengthMB = modelFile.length() / (1024.0 * 1024);
//						double elapsedSec = (end - start) / 1000.0;

				System.out.println("pctCorrectTr " + pctCorrectTr+" %");
				System.out.println("pctCorrect " + pctCorrect+" %");
				System.out.println("kappaCoeff " + kappaCoeff);
				System.out.println("F-Score "+f_score);
				System.out.println("numInstances " + numInstances);
//				System.out.println("numAtts = " + numAtts);
//				System.out
//						.println("dataFileLengthMB = " + dataFileLengthMB + " MB");
//				System.out.println("modelFileLengthMB = " + modelFileLengthMB
//						+ " MB");
//						System.out.println("elapsedSec = "+elapsedSec+" sec.");
			}
			else {
				System.out.println("ERROR: eTest es null");
			}
		} // FIN Para cada channel
		double avgTrainMs = 0;
		double avgClassMs = 0;
		for (int i=0; i<lTrainTime.size(); i++) {
			avgTrainMs += lTrainTime.get(i);
			avgClassMs += lClassifTime.get(i);
		}
		avgTrainMs /= lTrainTime.size();
		avgClassMs /= lClassifTime.size();
		System.out.println("avgTrainMs per instance = "+avgTrainMs+ " ms./instance");
		System.out.println("avgClassMs per instance = "+avgClassMs+ " ms./instance");
	}
}