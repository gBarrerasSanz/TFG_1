package guiatv.computervision.classificator;

public class ChannelContainer {
	public String nameCh;
	public String goodSamplesDir;
	public String badSamplesDir;
	public int[] topLeft = new int[2];
	public int[] botRight = new int[2];
	
	public ChannelContainer(String nameCh, int topLeft_0, int topLeft_1, int botRight_0, int botRight_1) {
		this.nameCh = nameCh;
		this.goodSamplesDir = "captures/"+nameCh+"/goodSamples/";
		this.badSamplesDir = "captures/publicidad/";
		this.topLeft[0] = topLeft_0;
		this.topLeft[1] = topLeft_1;
		this.botRight[0] = botRight_0;
		this.botRight[1] = botRight_1;
	}
}
