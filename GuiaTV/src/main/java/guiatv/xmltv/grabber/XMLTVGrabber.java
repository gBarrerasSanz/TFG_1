package guiatv.xmltv.grabber;

import guiatv.common.CommonUtility;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;


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
//		Resource capDirRes = ctx.getResource("META-INF/xmltv/cap");
		URL capDirUrl = this.getClass().getClassLoader().getResource("META-INF/xmltv/cap");
		File capDir = null;
		try {
			capDir = new File(capDirUrl.toURI());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		File tmpDir = null;
		tmpDir = new File(capDir.getAbsolutePath()+
				File.separator+"tmp");
		if (tmpDir.exists() == false) { tmpDir.mkdirs(); }
//		Resource binDirRes = ctx.getResource("META-INF/xmltv/grabber/windows_bin/xmltv-0.5.66-win32");
		URL binDirUrl = this.getClass().getClassLoader()
				.getResource("META-INF/xmltv/grabber/windows_bin/xmltv-0.5.66-win32");
		
		File resFile = null, errFile = null;
		/**
		 * Asignar nombre en función de la fecha de obtención
		 */
		resFile = new File(tmpDir.getAbsolutePath()+
				File.separator+"xmltvDump_"+CommonUtility.getDateString()+".xml");
		/**
		 * Asignar nombre (el mismo para todos, con lo que se reemplazará)
		 */
//		resFile = new File(tmpDir.getAbsolutePath()+
//				File.separator+"xmltvDump.xml");
		errFile = new File(tmpDir.getAbsolutePath()+
				File.separator+"errorLog_"+CommonUtility.getDateString()+".txt");
		switch(platform) {
		case WINDOWS:
			try {
				File binDir = new File(binDirUrl.toURI());
				String[] cmd = { 
					binDir.getAbsolutePath()+File.separator+"xmltv.exe",
					xmltvSources[0]		
				};
				Process p = null;
				ProcessBuilder pb = new ProcessBuilder(cmd);
				pb.directory(binDir);
				pb.redirectOutput(resFile);
				pb.redirectError(errFile);
				p = pb.start();
		        p.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("Execution interrumpted");
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} finally {

			}
			break;
		default:
			break;
		}
		return resFile;
	}
}
