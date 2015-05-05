package guiatv.computervision;

public interface Classif {
	
	public static enum ClassifResult {
		ADVERTISEMENT, PROGRAM
	}
	
	public boolean learn();
	
	public ClassifResult classify();  
	
}
