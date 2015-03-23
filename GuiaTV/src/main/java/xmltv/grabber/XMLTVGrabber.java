package xmltv.grabber;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class XMLTVGrabber {
	
	private enum Platform {
		WINDOWS
	}
	
	Platform platform;
	String[] xmltvSources = {
			"tv_grab_es_laguiatv"
	};
	
	public XMLTVGrabber() {
		// TODO: Detectar plataforma e inicializar platform
		platform = Platform.WINDOWS;
	}
	
	public File doGrabbing() {
		File resFile = null;
		String outFileName = "xmltvDumpTest1.xml";
		resFile = new File(outFileName);
		String errFileName = "errLog.txt";
		switch(platform) {
		case WINDOWS:
			URL url = ClassLoader.getSystemResource("src/main/resources/xmltv/grabber/windows_bin");
			try {
				File binDir = new File(url.toURI());
				String cmd = "xmltv.exe " + xmltvSources[0] + " > xmltvDumpTest1.xml";
				Process p = null;
				ProcessBuilder pb = new ProcessBuilder(cmd);
				pb.directory(binDir);
				pb.redirectOutput(resFile);
				pb.redirectError(new File(errFileName));
				p = pb.start();
		        p.waitFor();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		return resFile;
	}
}
