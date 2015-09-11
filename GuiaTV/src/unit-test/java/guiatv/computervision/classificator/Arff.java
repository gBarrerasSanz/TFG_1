package guiatv.computervision.classificator;

import weka.core.FastVector;
import weka.core.Instances;

public interface Arff {
	
	public void addSample(String imgPath, boolean truth);
	public FastVector getAtts();
	public Instances getData();
}
