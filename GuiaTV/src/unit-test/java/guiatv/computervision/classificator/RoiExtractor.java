package guiatv.computervision.classificator;

import guiatv.computervision.Imshow;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class RoiExtractor
{

    public static void main(String[] args)
    {
    	int[] a3_topLeft = {1102, 604};
    	int[] a3_botRight = {1167, 662};
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Cargar libreria de OpenCV
    	String pathToFile = "captures/03_a3/goodSamples/a3_0001 (2).jpg";
    	Mat src = Highgui.imread(pathToFile, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    	Mat dst = src.submat(a3_topLeft[1], a3_botRight[1], a3_topLeft[0], a3_botRight[0]);
//    	Mat dst =  new Mat(src.rows(),src.cols(),src.type());
    	
    	Imgproc.adaptiveThreshold(dst, dst, 255,
    	         Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 11, 2);
    	Imshow im = new Imshow("Image");
    	im.showImage(dst);
    	System.out.println("dst.rows()="+dst.rows()+"; dst.cols()="+dst.cols());
		byte buff[] = new byte[(int) (dst.total() * dst.channels())];
		dst.get(0, 0, buff);
		for (int i=0; i < buff.length; i++) {
			int val = buff[i];
			if (val==-1) { val = 8; }
			System.out.print(val);
			if(i%(a3_botRight[0]-a3_topLeft[0]) == 0) {
				System.out.println();
			}
		}
		System.out.println();
    	System.out.println(dst);
    	Highgui.imwrite("res.jpg", dst);
    }
}