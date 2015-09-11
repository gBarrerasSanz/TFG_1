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
import weka.classifiers.functions.LibLINEAR;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;

public class BenchmarkTrainAll {

	public static void main(String[] args) throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Cargar libreria de
														// OpenCV
		List<ChannelContainer> chList = new ArrayList<ChannelContainer>();
//		chList.add(new ChannelContainer("00_a24", 1113, 562, 1203, 659));
//		chList.add(new ChannelContainer("01_a1", 1118, 605, 1197, 666));
//		chList.add(new ChannelContainer("02_a2", 1120, 614, 1194, 675));
//		chList.add(new ChannelContainer("03_a3", 1103, 606, 1165, 661));
		chList.add(new ChannelContainer("05_a5", 1123, 606, 1176, 663));
//		chList.add(new ChannelContainer("06_a6", 1103, 615, 1153, 665));
		chList.add(new ChannelContainer("07_aNeox", 1064, 629, 1177, 662));
//		chList.add(new ChannelContainer("08_aNova", 1024, 614, 1151, 649));
//		chList.add(new ChannelContainer("09_aTDP", 1145, 36, 1254, 94));
//		chList.add(new ChannelContainer("10_aAragon", 1110, 50, 1164, 107));
		
		String[] features_header  = {"FEATURES: VALOR DE LOS PÍXELES", "FEATURES: MOMENTOS DE IMAGEN"};
		String[] folder = {"pixels", "moments"};
		/**
		 * MODELOS
		 */
		List<Classifier> lC = new ArrayList<Classifier>();
		List<String> lNameC = new ArrayList<String>(); 
		lC.add(new BayesNet()); lNameC.add("BayesNet");
		lC.add(new NaiveBayesUpdateable()); lNameC.add("NaiveBayesUpdateable");
		lC.add(new SMO()); lNameC.add("SMO"); 
		lC.add(new IBk()); lNameC.add("IBk"); 
		lC.add(new LWL()); lNameC.add("LWL"); 
		lC.add(new J48()); lNameC.add("J48"); 
		lC.add(new RandomForest()); lNameC.add("RandomForest"); 
		lC.add(new RandomTree()); lNameC.add("RandomTree"); 
		/************************************
		 * Para cada conjunto de features
		 ************************************/
		for (int i=1; i>=0; i--) {
			System.out
			.println("***********************************************************\n"
					+ "****************** "
					+ features_header[i]
					+ " ********************\n"
					+ "***********************************************************\n");
			/************************************
			 * Para cada channel
			 ************************************/
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
				for (int m=0; m<lC.size(); m++) {
					long start = System.currentTimeMillis();
					System.out
					.println("***********************************************************\n"
							+ "****************** "
							+ lNameC.get(m)
							+ " ********************\n"
							+ "***********************************************************\n");
					Classifier classifier = lC.get(m);
					Arff arff;
					if (i==0) {
						arff = new ArffInstance(ch.nameCh, ch.topLeft,
								ch.botRight);
					}
					else {
						arff = new ArffInstanceMoments(ch.nameCh, ch.topLeft,
								ch.botRight);
					}
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
					File dataFile = new File("results/"+folder[i]+"/data/data_" + ch.nameCh + ".arff");
					saver.setFile(dataFile);
					saver.writeBatch();
		
					// Set class index
					arff.getData().setClassIndex(arff.getData().numAttributes() - 1);
		
	//					Classifier cModel = (Classifier) new NaiveBayesUpdateable();
	//				Classifier cModel = (Classifier) new IBk();
	//					cModel.buildClassifier(arff.getData());
					// Test the model
					Evaluation eTest = ArffHelper.doCrossValidation(classifier, arff.getData());
	//				Evaluation eTest = ArffHelper.doCrossValidation(new IBk(), arff.getData());
					// Evaluar con el modelo entrenado
	//				Evaluation eTest = ArffHelper.doCrossValidation(cModel, arff.getData());
					
					// boolean successSave =
					// Debug.saveToFile("/results/models/model"+ch.nameCh+".model",
					// cModel);
					// if ( ! successSave) {
					// System.err.println("Error on save model");
					// }
					
					// Guardar el fichero de MODELO en disco
					File modelFile = new File("results/"+folder[i]+"/models/model_"+ch.nameCh+".model");
					OutputStream os = null;
					try {
						os = new FileOutputStream(modelFile);
						ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
						objectOutputStream.writeObject(classifier);
						objectOutputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					os.close();
					if (eTest != null) {
						long end = System.currentTimeMillis();
						double pctCorrect = eTest.pctCorrect();
						double kappaCoeff = eTest.kappa();
						int numInstances = arff.getData().numInstances();
						int numAtts = arff.getData().numAttributes();
						double dataFileLengthMB = dataFile.length() / (1024.0 * 1024);
						double modelFileLengthMB = modelFile.length() / (1024.0 * 1024);
						double elapsedSec = (end - start) / 1000.0;
						System.out.println("pctCorrect = " + pctCorrect);
						System.out.println("kappaCoeff = " + kappaCoeff);
						System.out.println("numInstances = " + numInstances);
						System.out.println("numAtts = " + numAtts);
						System.out
								.println("dataFileLengthMB = " + dataFileLengthMB + " MB");
						System.out.println("modelFileLengthMB = " + modelFileLengthMB
								+ " MB");
						System.out.println("elapsedSec = "+elapsedSec+" sec.");
					}
					else {
						System.out.println("ERROR: eTest es null");
					}
				} // FIN Para cada modelo
			} // FIN Para cada channel
		} // FIN Para cada conjunto de features
	}
}