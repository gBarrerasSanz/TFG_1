package guiatv.cv.classificator;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import guiatv.cv.classificator.Classif_old;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;


public class TemplateMatchingClassif implements Classif_old{

	private final int MATCH_METHOD = Imgproc.TM_SQDIFF_NORMED;
	///
	private boolean trained = false;
	private Mat tpt;
	
	public TemplateMatchingClassif() {
		pseudoTrain();
	}

	private void pseudoTrain() {
		URL tptUrl = this.getClass().getClassLoader().getResource("guiatv.opencv/template_samples/laSexta/tpt1.png");
		File tptFile = null;
		try {
			tptFile = new File(tptUrl.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Mat tpt = Highgui.imread(tptFile.getAbsolutePath());
		trained = true;
	}
	
	@Override
	public boolean train(Mat tpt) {
		this.tpt = tpt;
		trained = true;
		return true;
	}

	@Override
	public ClassifResult classify(Mat img) {
		if ( ! trained) { throw new IllegalStateException("Not trained"); }
		int result_cols = img.cols() - tpt.cols() + 1;
        int result_rows = img.rows() - tpt.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
		Imgproc.matchTemplate(img, tpt, result, MATCH_METHOD);
		MinMaxLocResult mmr = Core.minMaxLoc(result);
		Point matchLoc;
        matchLoc = mmr.minLoc;
        
//        showTemplate(img,  matchLoc);

        if (mmr.maxVal == 1) { 	return ClassifResult.PROGRAM; }
        else {					return ClassifResult.ADVERTISEMENT; }
	}
	
	
	private void showTemplate(Mat img, Point matchLoc) {
		 Core.rectangle(img, matchLoc, new Point(matchLoc.x + tpt.cols(),
	                matchLoc.y + tpt.rows()), new Scalar(0, 0, 255));
	        
	        Imshow im = new Imshow("Img");
	        im.showImage(img);
	        
	        // Save Image
	        // Save the visualized detection.
//	        System.out.println("Writing "+ outFile);
//	        Highgui.imwrite(outFile, img);
	}

}
